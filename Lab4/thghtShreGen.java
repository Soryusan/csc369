import org.json.*;
import java.util.ArrayList;
import java.util.Random;

import java.io.*;


public class thghtShreGen {

   private String inputFileName = "sense.txt";
   private int curMsgId = 0;
   
   public thghtShreGen(String inFile) {
      if (inFile.length() > 0) {
          inputFileName = inFile;
      }
   }
   
   public thghtShreGen(int lastId, String inFile) {
      if (inFile.length() > 0) {
         inputFileName = inFile;
      }
      curMsgId = lastId;
   }
   
   public JSONObject getNext() throws JSONException {
      JSONObject curMsgObject = generateMsgObject(inputFileName, curMsgId);
      curMsgId++;
      return curMsgObject;
   }

   public void outputToFile(String outputFileName, int numJSONObjects, String inFile) {
   	  /*if (args.length < 2 || args.length > 3) {
   	     //show error msg & usage msg
   	  }
   	  else {*/
         //String outputFileName = args[0];
         //int numJSONObjects = Integer.parseInt(args[1]);
         
         if (inFile.length() > 0) {
            inputFileName = inFile;
         }
      
         //if (args.length == 3) {
         //   inputFileName = args[2];
            
         //}
      
         
      
         if (numJSONObjects <0) {
            //show error msg
         }
         else {
         
            PrintWriter printWriter = null;
            JSONWriter jsonWriter = null;
            try {
               printWriter = new PrintWriter(inFile);
               jsonWriter = new JSONWriter(printWriter);
            } catch(FileNotFoundException e) {
               System.out.println("Could not open " + inFile);
            }
            
            //ArrayList<JSONObject> userMsgs = new ArrayList<JSONObject>(); 
      
            try {
               jsonWriter.array();
               for (int i=0; i<numJSONObjects; i++) {
                  JSONObject curMsgObject = generateMsgObject(inputFileName, i);
                  jsonWriter.value(curMsgObject);
                  //updateUsrMsgs(userMsgs, curMsgObject);
                  //msgObjects.add(curMsgObject)
               }
               jsonWriter.endArray();
            }
            catch(JSONException e) {
               System.out.println("Failed to write to file.");
            } finally {
               printWriter.close();
            }
            //JSONArray
         }
      //}
   }
   
   private static JSONObject generateMsgObject(String inFile, int msgId) throws JSONException {
      JSONObject msg = new JSONObject();
      msg.put("messageId", msgId);
      msg.put("user", generateUserId());
      msg.put("status", generateStatus());
      msg.put("recipient", generateRecepient(msg.getString("status"), msg.getString("user")));
      
      int isInResponse = RandomUtil.normal(2, 1, 0, 4);
      if (msg.getInt("messageId") > 0 && isInResponse > 2) {
         msg.put("in-response", generateInResponse(msg.getInt("messageId"), msg.getString("recipient")));
      }
      msg.put("text", generateMsgTxt(inFile));
      return msg;
   }
   
   
   private static String generateUserId() {
      String userId = "u";
      int userIdNum = RandomUtil.normal(5000, 3000, 1, 10000);
      userId = userId+userIdNum;
      return userId;
   }
   
   private static String generateStatus() {
      //String status = "";
      ArrayList<Category> statusList = new ArrayList<Category>();
      statusList.add(new Category("public", 20));
      statusList.add(new Category("protected", 2));
      statusList.add(new Category("private", 2));
      
      Category chosenStatus = RandomUtil.pick(statusList);
      return chosenStatus.getName();
   }
   
   private static String generateRecepient(String status, String senderId) {
      ArrayList<Category> recepientList = new ArrayList<Category>();
      if (status.equals("public")) {
         recepientList.add(new Category("subscribers", 4));
         recepientList.add(new Category("all", 4));
         recepientList.add(new Category("self", 1));
         recepientList.add(new Category("individual", 1));
      }
      else if (status.equals("protected")) {
         recepientList.add(new Category("subscribers", 8));
         recepientList.add(new Category("self", 1));
         recepientList.add(new Category("individual", 1));
      }
      else if (status.equals("private")) {
         recepientList.add(new Category("individual", 6));
         recepientList.add(new Category("self", 1));
      }
      
      String recepient = RandomUtil.pick(recepientList).getName();
      
      if (recepient.equals("individual")) {
         String rUserId = senderId;
         while (rUserId.equals(senderId)) {
            rUserId= generateUserId(); 
         }
         recepient = rUserId;
      }
      
      return recepient;
   }
   
   private static String generateMsgTxt(String fileName) {
      String message = "";
      int numWords = RandomUtil.range(2, 20);
      ArrayList<String> wordList = new ArrayList<String>();
      try {
         BufferedReader reader = new BufferedReader(new FileReader(fileName));
         String word;
         while ((word = reader.readLine()) != null) {
         	wordList.add(word);
         } 
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      
      for (int i=0; i<numWords; i++) {
      	int wordIndex = RandomUtil.range(0, wordList.size());
      	String nextWord = wordList.get(wordIndex);
      	if (i != 0) {
      	   nextWord = " " + nextWord;
      	}
      	message = message + nextWord;
      }
      
      return message;
   }
   
   private static int generateInResponse(int curMsgId, String recepient) {
      int msgId = RandomUtil.range(0, curMsgId);
      return msgId;
   }

   /*private ArrayList<JSONObject> updateUsrMsgs(ArrayList<JSONObject> usrMsgs, JSONObject msg) {
      int userFound = -1;
      for (int i=0; i<usrMsgs.size() && userFound ==-1; i++) {
         if (usrMsgs.get(i).getString("user").equals(msg.getString("user")) {
         	userFound = i;
         }
      }
      
      if (userFound != -1) {
         JSONArray allUserMsgs = usrMsgs.get(userFound).getJSONArray("messages");
         allUserMsgs.put(allUserMsgs.length(), msg.getInt("messageId"));
         JSONObject userMsgObj = usrMsgs.get(userFound).put("messages", allUserMsgs);
         usrMsgs.set(userFound, userMsgObj);
      }
      else {
         JSONObject userMsgObj = new JSONObject();
         JSONArray allUserMsgs = new JSONArray();
         allUserMsgs.put(0, msg.getInt("messageId"));
         userMsgObj.put("user", msg.getString("user"));
         userMsgObj.put("messages", allUserMsgs);
         usrMsgs.add(userMsgObj);
      }
      
      return usrMsgs;
   }
   
   
   private int indexOfUsrMsgs(ArrayList<JSONObject> usrMsgs, String userId) {
      int userFound = -1;
      for (int i=0; i<usrMsgs.size() && userFound ==-1; i++) {
         if (usrMsgs.get(i).getString("user").equals(userId) {
         	userFound = i;
         }
      }
      return userFound;
   }*/
   
   

}