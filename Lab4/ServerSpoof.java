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
	public static void main(String[] args) {
		try {
			JSONTokener t = null;
			JSONObject config;
			MongoClient client;
			MongoDatabase db;
			MongoCollection<Document> dox;
			MongoCollection<Document> monitorColl;
			BufferedWriter writer = null;
			Hashtable<String, Boolean> words = new Hashtable<String, Boolean>();
			monitorInfo = new ThghtShreInfo();
			Scanner scanner;

			Logger logger = Logger.getLogger("org.mongodb.driver");
			logger.setLevel(Level.OFF);
			String mongoClient;
			String mongoDB;
			String collection;
			String monitor;
			int mongoPort;
			int delay;
			int startNdx;

			int userCount;
			long prevTotal;
			long messages;
			long newMessages;
			long self;
			long all;
			long subs;
			long userId;
			long numPublic;
			long numPrivate;
			long numProtected;

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
			/* *************
			************** */

			//Read in words from specified file
			scanner = new Scanner(config.getString("wordFilter"));
			while(scanner.hasNextLine()) {
				words.put(scanner.nextLine(), true);
			}
			delay = checkDelay(config.getInt("delay"));
			System.out.println("Server Running...");
			
			while(true) {
				try {
					Thread.sleep(3 * delay);
				}
				catch(InterruptedException e) {
					System.out.println(e);
				}
				//Obtain necessary information for monitor file
				JSONObject monitorObj = new JSONObject();
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

				collectInfo(dox);
				userCount = monitorInfo.getUsers();


				numPrivate = getQueryCount("status", "private", dox);
				numProtected = getQueryCount("status", "protected", dox);
				numPublic = getQueryCount("status", "public", dox);
				statusObj.put("public", numPublic);
				statusObj.put("protected", numProtected);
				statusObj.put("public", numPublic);

				monitorObj.put("time", (new Date()).toString());
				monitorObj.put("messages", messages);
				monitorObj.put("users", userCount);
				monitorObj.put("new", newMessages);
				monitorObj.put("statusStats", statusObj);

				monitorObj.put("recipientStats", getUserRecCount());
				Document monitorDoc = new Document();
				monitorDoc.append("Monitor", monitorObj.toString());
				monitorColl.insertOne(monitorDoc);
				try {
					writer.write(monitorObj.toString());
					writer.flush();
				}
				catch(IOException e) {
					System.out.println(e);
				}
				System.out.println(monitorObj.toString(3));

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

	public static long getQueryCount(String key, String value, MongoCollection<Document> dox) {
		Document query = new Document();
		query.append(key, value);
		return dox.count(query);
	}

	public static void collectInfo(MongoCollection<Document> dox) {
		FindIterable<Document> iterable = dox.find();
		monitorInfo.set();
		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				if(monitorInfo.getStart() >= monitorInfo.getTotalMessages()) {
					monitorInfo.addUser(document.getString("user"));
					monitorInfo.addTotalMessages();
					monitorInfo.increStart();
					if(!document.get("recipient").equals("self") &&
						!document.get("recipient").equals("all") &&
						!document.get("recipient").equals("subscribers")) {

						monitorInfo.increRecipient(document.getString("recipient"));
					}
				}
			}
		});
	}

	public static JSONObject getUserRecCount() {
		ArrayList<String> userIds = monitorInfo.recList();
		String key;
		JSONObject recStateObj = new JSONObject();
		for(int i = 0; i < userIds.size(); i++) {
			key = userIds.get(i);
			try {
				recStateObj.put(key, monitorInfo.getRecMsgCount(key));
			}
			catch(JSONException e) {
				System.out.println(e);
			}
		}
		return recStateObj;
	}
}