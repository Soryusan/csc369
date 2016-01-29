import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.sql.Timestamp;
import java.util.*;
import java.io.*;
import org.json.*;


public class ClientSpoof {

   private static ConfigFile configSettings;

   public static void main(String[] args) {
      if (args.length < 0 || args.length > 1) {
         System.out.println("Invalid number of arguments\nUsage: ClientSpoof configFileName");
      }
      else {
         String configFileName = args[0];
         
         configSettings = new ConfigFile();
         
         try {
            configSettings.readConfig(configFileName);
            
            Timestamp startTS = new Timestamp(System.currentTimeMillis());
            System.out.println("\nClient Started at " + startTS.toString() +"\nConnecting to " + 
                 configSettings.getMongoServer() + " on port " + configSettings.getMongoPort()+".\n");
            
            
            //turns off log messages from Mongo driver
            Logger logger = Logger.getLogger("org.mongodb.driver");
            logger.setLevel(Level.OFF);
            
            MongoClient mdbClient = new MongoClient(configSettings.getMongoServer(), 
                                                    configSettings.getMongoPort());
            MongoDatabase db = mdbClient.getDatabase(configSettings.getDBName());
            
            
            System.out.println("Successfully connected to database " + configSettings.getDBName());
            if (!collectionExists(db, configSettings.getCollectionName())) {
               db.createCollection(configSettings.getCollectionName());
            }
            
            
            MongoCollection<Document> dox = db.getCollection(configSettings.getCollectionName());
            //MongoCollection<Document> monitor = db.getCollection(configSettings.getMonitorCollName());
            
            
            
            
            System.out.println("Successfully connected to collection " + configSettings.getCollectionName() +
                                " in database " + configSettings.getDBName());
            System.out.println("The collection contains " + dox.count() + " documents\n");
            
            
            
            //to do here
            int startMsgId =0;
            if (dox.count() > 0) {
               //query for the message with the largest message id and add 1 to that id
               
               
               /*Document sortQuery = new Document();
               sortQuery.append("messageId", -1);
               
               FindIterable<Document> findResult = dox.find().sort(sortQuery).limit(1);
               
               MongoCursor<Document> findIterator = findResult.iterator();
               Document latestMsg = findIterator.next();
               JSONObject jsonMsg = new JSONObject(latestMsg.toJson());
               
               startMsgId = jsonMsg.getInt("messageId");*/
               
               startMsgId = (int)dox.count();
               
               //System.out.println();
            }
            
            //setup thghtShreGen object for main loop
            thghtShreGen msgGenerator = new thghtShreGen(startMsgId, configSettings.getWordFile());
            
            int numCycles =0;
            
            PrintWriter logWriter = new PrintWriter(new FileWriter(configSettings.getClientLogFile(), true));
            
            while(true) {
               int curDelay= RandomUtil.normal(configSettings.getDelayAmount(), configSettings.getDelayAmount()/2, 0, 90);
               try {
                  Thread.sleep(curDelay*1000);
               } catch(InterruptedException ex) {
                  Thread.currentThread().interrupt();
               }
               
               
               Document insertMsgDoc = new Document();
               JSONObject curMsg = msgGenerator.getNext();
               JSONArray msgFieldNames = curMsg.names();
               for (int i=0; i<msgFieldNames.length(); i++) {
                  insertMsgDoc.append(msgFieldNames.getString(i), curMsg.get(msgFieldNames.getString(i)));
               }
               dox.insertOne(insertMsgDoc);
               
               Timestamp insertTS = new Timestamp(System.currentTimeMillis());
               
               
               //System.out.println("The delay time was " + curDelay + " seconds");
               //writer.println("The delay time was " + curDelay + " seconds");
               if (numCycles == 40) {
                  System.out.println("The collection has " + dox.count() + " messages");
                  logWriter.println("The collection has " + dox.count() + " messages");
                  logWriter.flush();
                  
                  Document userQuery = new Document();
                  userQuery.append("user", curMsg.get("user"));
                  
                  FindIterable<Document> findResult = dox.find(userQuery);
                  MongoCursor<Document> findIterator = findResult.iterator();
                  int numMsgs = 0;
                  
                  while(findIterator.hasNext()) {
                     findIterator.next();
                     numMsgs++;
                  }
                  System.out.println("There are " + numMsgs + " messages written by user " + curMsg.get("user") + ".");
                  logWriter.println("There are " + numMsgs + " messages written by user " + curMsg.get("user") + ".");
                  logWriter.flush();
                  numCycles = 0;
               } 
               System.out.println(insertTS.toString() + "\n" + curMsg.toString(5) + "\n");
               logWriter.println(insertTS.toString() + "\n" + curMsg.toString(5) + "\n");
                logWriter.flush();
               numCycles++;
            }
            
            
            //FindIterable<Document> result = dox.find();
            
            
         }
         catch (Exception e) {
            System.out.println(e);
         }
         
         
         
         
      }
      
   }
   
   public static boolean collectionExists(MongoDatabase db, String name) {
      boolean result = false;
      
      MongoIterable<String> collectionNames = db.listCollectionNames();
      MongoCursor<String> iterator=collectionNames.iterator();
      
      while(iterator.hasNext() && !result) {
         String curName = iterator.next();
         if ( name.equals(curName)) {
            result = true;
         }
      }
      
      return result;
   }

}