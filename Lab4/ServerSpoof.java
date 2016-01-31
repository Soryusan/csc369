import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.json.*;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import org.bson.*;

/*{
	"mongo": <MongoServer>,
	"port": <MongoPort>,
	"database": <DBName>,
	"collection": <CollectionName>,
	"monitor": <MonitorCollName>,
	"delay": <delayAmount>,
	"words": <wordFile>,
	"clientLog": <clientLogFile>,
	"serverLog": <serverLogFile>,
	"wordFilter": <queryWordFile>
}*/


public class ServerSpoof {
	static ThghtShreInfo monitorInfo;
	static Hashtable<String, ArrayList<String>> seenWords;
	static Hashtable<String, Boolean> words;
	static MongoCollection<Document> monitorColl;
	static Document monitorDoc;

	static long messages;
	static long newMessages;
	static int userCount;
	static BufferedWriter writer = null;
	static boolean firstPass = true;



	public static void main(String[] args) {
		try {
			JSONTokener t = null;
			JSONObject config;
			MongoClient client;
			MongoDatabase db;
			MongoCollection<Document> dox;
			Scanner scanner;
			Document totalRecord;

			Logger logger = Logger.getLogger("org.mongodb.driver");
			logger.setLevel(Level.OFF);
			String mongoClient;
			String mongoDB;
			String collection;
			String monitor;
			int mongoPort;
			int delay;
			int startNdx;

			long prevTotal;
			long self;
			long all;
			long subs;
			long userId;
			long numPublic;
			long numPrivate;
			long numProtected;

			monitorInfo = new ThghtShreInfo();
			words = new Hashtable<String, Boolean>();

			try {
				t = new JSONTokener(new FileReader(new File(args[0])));
			}
			catch (FileNotFoundException e) {
				System.out.println("File not found!");
			}
			//Grab necessary info from config file
			config = new JSONObject(t);
			mongoClient = config.getString("mongo");
			mongoDB = config.getString("database");
			monitor = config.getString("monitor");
			mongoPort = config.getInt("port");
			collection = config.getString("collection");
			try {
				writer = new BufferedWriter(new FileWriter(new File(config.getString("serverLog"))));
			}
			catch(IOException e) {
				System.out.println(e);
			}

			System.out.println((new Date()).toString());

			client = connect(mongoClient, mongoPort);

			db = database(mongoDB, client);
			//Get documents in collection
			dox = db.getCollection(collection);
			System.out.println("Number of Documents: " + dox.count());
			prevTotal = dox.count();
			/**
			 * Wipe contents of collection here
			 **/
			monitorColl = db.getCollection(monitor);
			monitorColl.drop();
			db.createCollection(monitor);
			monitorColl = db.getCollection(monitor);
			totalRecord = new Document();
			totalRecord.append("recordType", "monitor totals");
			totalRecord.append("msgTotals", new ArrayList<Long>());
			totalRecord.append("userTotals", new ArrayList<Long>());
			totalRecord.append("newMsgTotals", new ArrayList<Long>());
			monitorColl.insertOne(totalRecord);
			/* *************
			************** */

			//Read in words from specified file
			try {
				scanner = new Scanner(new File(config.getString("wordFilter")));
				while(scanner.hasNextLine()) {
					String word = scanner.nextLine().trim();
					words.put(word, true);
				}
			} catch(FileNotFoundException e) {
				System.out.println(e);
			}
			delay = checkDelay(config.getInt("delay"));
			System.out.println("Server Running...");
			
			while(true) {
				try {
					Thread.sleep(3 * delay * 1000);
				}
				catch(InterruptedException e) {
					System.out.println(e);
				}
				//Obtain necessary information for monitor file
				monitorDoc = new Document();
				messages = dox.count();
				newMessages = messages - prevTotal;

				JSONObject recObj = new JSONObject();
				JSONObject statusObj = new JSONObject();

				all = getQueryCount("recipient", "all", dox);
				self = getQueryCount("recipient", "self", dox);
				subs = getQueryCount("recipient", "subscribers", dox);
				recObj.put("all", all);
				recObj.put("self", self);
				recObj.put("subscribers", subs);

				seenWords = new Hashtable<String, ArrayList<String>>();
				collectInfo(dox);
				userCount = monitorInfo.getUsers();


				numPrivate = getQueryCount("status", "private", dox);
				numProtected = getQueryCount("status", "protected", dox);
				numPublic = getQueryCount("status", "public", dox);
				statusObj.put("public", numPublic);
				statusObj.put("protected", numProtected);
				statusObj.put("public", numPublic);

				monitorDoc.append("time", (new Date()).toString());
				monitorDoc.append("messages", messages);
				monitorDoc.append("users", userCount);
				monitorDoc.append("new", newMessages);
				monitorDoc.append("statusStats", statusObj.toString());

				monitorDoc.append("recipientStats", getUserRecCount(recObj).toString());
				monitorColl.insertOne(monitorDoc);

				if(!firstPass) {
					Set<String> keys = seenWords.keySet();
					try {
						for(String key : keys) {
							ArrayList<String> foundMessages = seenWords.get(key);
							System.out.println("Messages with word " + key);
							writer.write("Messages with word " + key + "\n");
							for(String message : foundMessages) {
								System.out.println(message);
								writer.write(message + "\n");
							}
						}
						writer.flush();
					}
					catch(IOException e) {
						System.out.println(e);
					}

				}
				else {
					firstPass = false;
				}

				try {
					writer.write("{" + "\n");
					writer.write("\ttime: " + monitorDoc.get("time") + "\n");
					writer.write("\tmessages: " + monitorDoc.get("messages") + "\n");
					writer.write("\tusers: " + monitorDoc.get("users") + "\n");
					writer.write("\tnew: " + monitorDoc.get("new") + "\n");
					writer.write("\tstatusStats: " + monitorDoc.get("statusStats") + "\n");
					writer.write("\trecipientStats: " + monitorDoc.get("recipientStats") + "\n");
					writer.write("}" + "\n");
					writer.flush();
				}
				catch(IOException e) {
					System.out.println(e);
				}
				System.out.println("{");
				System.out.println("\ttime: " + monitorDoc.get("time"));
				System.out.println("\tmessages: " + monitorDoc.get("messages"));
				System.out.println("\tusers: " + monitorDoc.get("users"));
				System.out.println("\tnew: " + monitorDoc.get("new"));
				System.out.println("\tstatusStats: " + monitorDoc.get("statusStats"));
				System.out.println("\trecipientStats: " + monitorDoc.get("recipientStats"));
				System.out.println("}");

				monitorDoc = new Document();
				monitorDoc.append("recordType", "monitor totals");
				FindIterable<Document> iter = monitorColl.find(monitorDoc);

				iter.forEach(new Block<Document>() {
					@Override
					public void apply(final Document document) {
						ArrayList<Long> totalMessages;
						ArrayList<Long> totalUsers;
						ArrayList<Long> totalNewMessages;

						totalMessages = (ArrayList<Long>) document.get("msgTotals");
						totalUsers = (ArrayList<Long>) document.get("userTotals");
						totalNewMessages = (ArrayList<Long>) document.get("newMsgTotals");

						totalMessages.add(new Long(messages));
						totalUsers.add(new Long(userCount));
						totalNewMessages.add(new Long(newMessages));

						monitorColl.deleteOne(monitorDoc);
						monitorDoc = new Document();
						monitorDoc.append("recordType", "monitor totals");
						monitorDoc.append("msgTotals", totalMessages);
						monitorDoc.append("userTotals", totalUsers);
						monitorDoc.append("newMsgTotals", totalNewMessages);

						System.out.println("{");
						System.out.println("\trecordType: " + monitorDoc.get("recordType"));
						System.out.println("\trecordType: " + monitorDoc.get("msgTotals"));
						System.out.println("\tuserTotals: " + monitorDoc.get("userTotals"));
						System.out.println("\tnewMsgTotals: " + monitorDoc.get("newMsgTotals"));
						System.out.println("}");

						try {
							writer.write("{" + "\n");
							writer.write("\trecordType: " + monitorDoc.get("recordType") + "\n");
							writer.write("\trecordType: " + monitorDoc.get("msgTotals") + "\n");
							writer.write("\tuserTotals: " + monitorDoc.get("userTotals") + "\n");
							writer.write("\tnewMsgTotals: " + monitorDoc.get("newMsgTotals") + "\n");
							writer.write("}" + "\n");
							writer.flush();
						}
						catch(IOException e) {
							System.out.println(e);
						}

						monitorColl.insertOne(monitorDoc);
					}
				});

				dox = db.getCollection(collection);
				prevTotal = messages;
				seenWords = new Hashtable<String, ArrayList<String>>();

			}

		}

		catch(JSONException e) {
			System.out.println(e);
		}
	}

	//Check mongo connect information based on config file
	//Return a client based on given details
	public static MongoClient connect(String mongoClient, int mongoPort) {
		//Connect to mongo db specified in config
		if(mongoClient == null || mongoClient.equals("")) {
			
			System.out.print("Server: localhost\nPort: ");
			if(mongoPort < 0) {
				System.out.println("27017");
				return new MongoClient();
			}
			else {
				System.out.println(mongoPort);
				return new MongoClient("localhost", mongoPort);
			}

		}
		else {
			System.out.print("Server: " + mongoClient + "\nPort: ");
			if(mongoPort < 0) {
				System.out.println("27017");
				return new MongoClient(mongoClient, 27017);

			}
			else {
				System.out.println(mongoPort);
				return new MongoClient(mongoClient, mongoPort);
			}
			
		}
	}

	//Get database specified in config
	public static MongoDatabase database(String mongoDB, MongoClient client) {
		//If database isn't specified, default to test
		if(mongoDB == null || mongoDB.equals("")) {
			System.out.println("Database: test");
			return client.getDatabase("test");
		}
		else {
			System.out.println("Database: " + mongoDB);
			return client.getDatabase(mongoDB);
		}
	}

	//Check if a delay was specified
	public static int checkDelay(int delay) {
		if(delay == 0) 
			return 10;
		else
			return delay;
	}

	//Gets total number of documents for specific query Key
	public static long getQueryCount(String key, String value, MongoCollection<Document> dox) {
		Document query = new Document();
		query.append(key, value);
		return dox.count(query);
	}

	//Gets information from each new document in database
	public static void collectInfo(MongoCollection<Document> dox) {
		FindIterable<Document> iterable = dox.find();
		monitorInfo.set();
		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				if(monitorInfo.getStart() >= monitorInfo.getTotalMessages()) {
					monitorInfo.addUser(document.getString("user"));
					monitorInfo.addTotalMessages();
					if(!document.getString("recipient").equals("self") &&
						!document.getString("recipient").equals("all") &&
						!document.getString("recipient").equals("subscribers")) {
						monitorInfo.increRecipient(document.getString("recipient"));
					}
					findWords(document.getString("text"));

				}
				monitorInfo.increStart();
			}
		});
	}
	//Get users who received a message and how many messages they received
	public static JSONObject getUserRecCount(JSONObject recObj) {
		ArrayList<String> userIds = monitorInfo.recList();
		String key;
		for(int i = 0; i < userIds.size(); i++) {
			key = userIds.get(i);
			try {
				recObj.put(key, monitorInfo.getRecMsgCount(key));
			}
			catch(JSONException e) {
				System.out.println(e);
			}
		}
		return recObj;
	}

	//Finds words that appear message string and query word file
	public static void findWords(String message) {
		String[] splitMessage = message.split("\\s+");
		String curWord;
		ArrayList<String> messageList;
		for(int i = 0; i < splitMessage.length; i++) {
			curWord = splitMessage[i].trim();
			if(words.containsKey(curWord)) {
				if(seenWords.containsKey(curWord)) {
					messageList = seenWords.get(curWord);
					messageList.add(message);
					seenWords.put(curWord, messageList);
				}
				else {
					messageList = new ArrayList<String>();
					messageList.add(message);
					seenWords.put(curWord, messageList);
				}
			}
		}
	}
}