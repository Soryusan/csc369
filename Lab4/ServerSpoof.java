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
	public static void main(String[] args) {
		try {
			JSONTokener t = null;
			JSONObject config;
			MongoClient client;
			MongoDatabase db;
			MongoCollection<Document> dox;
			MongoCollection<Document> monitorColl;
			ArrayList<String> words = new ArrayList<String>();
			Scanner scanner;

			Logger logger = Logger.getLogger("org.mongodb.driver");
			logger.setLevel(Level.OFF);
			String mongoClient;
			String mongoDB;
			String monitor;
			int mongoPort;
			int delay;

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

			System.out.println((new Date()).toString());

			client = connect(mongoClient, mongoPort);

			db = database(mongoDB, client);
			//Get documents in collection
			dox = db.getCollection(config.getString("collection"));
			System.out.println("Number of Documents: " + dox.count());
			
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
			scanner = new Scanner(config.getString("words"));
			while(scanner.hasNextLine()) {
				words.add(scanner.nextLine());
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
}