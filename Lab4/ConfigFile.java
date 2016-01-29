import java.io.*;
import org.json.*;

public class ConfigFile {

   private String MongoServer;
   private int MongoPort;
   private String DBName;
   private String CollectionName;
   private String MonitorCollName;
   
   private int delayAmount;
   
   private String wordFile;
   private String clientLogFile;
   private String serverLogFile;
   private String queryWordFile;


   public ConfigFile() {
      setToDefaults();
      
      //set other defaults
   }

   public ConfigFile(String configFileName) {
      setToDefaults();
      readConfig(configFileName);
   
      /*MongoServer = "localhost";
      MongoPort = 27017;
      DBName = "test"
      delayAmount = 10;*/
   }

   //sets all configuration info to the default states
   public void setToDefaults() {
      MongoServer = "localhost";
      MongoPort = 27017;
      DBName = "test";
      CollectionName = "";
      MonitorCollName = "";
      
      delayAmount = 10;
      
      wordFile = "";
      clientLogFile = "";
      serverLogFile = "";
      queryWordFile = "";
   }


   public void readConfig(String fileName) {
      try {
         JSONTokener tokenizer = new JSONTokener(new FileReader(new File(fileName)));
         JSONObject settings = (JSONObject)tokenizer.nextValue();
         
         if (settings.length() != 10) {
            //not all values present?
         }
         
         
         if (settings.has("mongo") && !(settings.isNull("mongo") || settings.getString("mongo").length() == 0)) {
            MongoServer = settings.getString("mongo");
         }
         
         if (settings.has("port") && !settings.isNull("port")) {
            MongoPort = settings.getInt("port");
         }
         
         if (settings.has("database") && !(settings.isNull("database") || settings.getString("database").length() == 0)) {
            DBName = settings.getString("database");
         }
         
         if (settings.has("collection") && !(settings.isNull("collection") || settings.getString("collection").length() == 0)) {
            CollectionName = settings.getString("collection");
         }
         
         if (settings.has("monitor") && !(settings.isNull("monitor") || settings.getString("monitor").length() == 0)) {
           
            if (!settings.getString("monitor").equals(CollectionName)) {
               MonitorCollName = settings.getString("monitor");
            } 
            else if (MonitorCollName.equals(CollectionName)) {
               MonitorCollName = CollectionName + "2";
            }
         }
         
         if (settings.has("delay") && !(settings.isNull("delay") || settings.getInt("delay") == 0)) {
            delayAmount = settings.getInt("delay"); 
         }
         
         if (settings.has("words") && !(settings.isNull("words") || settings.getString("words").length() == 0)) {
            wordFile = settings.getString("words");
         }
         
         if (settings.has("clientLog") && !(settings.isNull("clientLog") || settings.getString("clientLog").length() == 0)) {
            clientLogFile = settings.getString("clientLog");
         }
         
         if (settings.has("serverLog") && !(settings.isNull("serverLog") || settings.getString("serverLog").length() == 0)) {
            serverLogFile = settings.getString("serverLog");
         }
         
         if (settings.has("wordFilter") && !(settings.isNull("wordFilter") || settings.getString("wordFilter").length() == 0)) {
            queryWordFile = settings.getString("wordFilter");
         }
      }
      catch (Exception e) {
         e.printStackTrace();
      }
   }


   public String getMongoServer() {
      return MongoServer;
   }
   
   public int getMongoPort() {
      return MongoPort;
   }
   
   public String getDBName() {
      return DBName;
   }
   
   public String getCollectionName() {
      return CollectionName;
   }
   
   public String getMonitorCollName() {
      return MonitorCollName;
   }
   
   public int getDelayAmount() {
      return delayAmount;
   }
   
   public String getWordFile() {
      return wordFile;
   }
   
   public String getClientLogFile() {
      return clientLogFile;
   }
   
   public String getServerLogFile() {
      return serverLogFile;
   }
   
   public String getQueryWordFile() {
      return queryWordFile;
   }
   
   
   public void setMongoServer(String server) {
      MongoServer = server;
   }
   
   public void setMongoPort(int port) {
      MongoPort = port;
   }
   
   public void setDBName(String dbName) {
      DBName = dbName;
   }
   
   public void setCollectionName(String collection) {
      CollectionName = collection;
   }
   
   public void setMonitorCollName(String monitorColl) {
      MonitorCollName = monitorColl;
   }
   
   public void setDelayAmount(int delay) {
      delayAmount = delay;
   }
   
   public void setWordFile(String fileName) {
      wordFile = fileName;
   }
   
   public void setClientLogFile(String fileName) {
      clientLogFile = fileName;
   }
   
   public void setServerLogFile(String fileName) {
      serverLogFile = fileName;
   }
   
   public void setQueryWordFile(String fileName) {
      queryWordFile = fileName;
   }
}