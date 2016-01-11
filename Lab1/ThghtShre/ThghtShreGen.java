import org.json.*;
import java.util.*;
import java.io.*;

public class ThghtShreGen {

	private final int max_users = 10000;
	private final int max_words = 20;
	private final int min_words = 2;

	//message status
	private final int PUBLIC = 0;
	private final int PROTECTED = 1;
	private final int PRIVATE = 2;

	//recepient
	private final int ALL = 0;
	private final int SELF = 1;
	private final int SUBSCRIBERS = 2;

	private ArrayList<String> englishText;


	public static void main(String[] args) {
		int size = 0;
		String wordFile;
		String outputFilename;
		ThghtShreGen messageGen = new ThghtShreGen();
		Scanner scan = new Scanner(System.in);

		System.out.println("Input output file name: ");
    	outputFilename = scan.next();
    	System.out.println("Input number of JSON to generate: ");
    	size = scan.nextInt();
    	System.out.println("Input word file for message generation: ");
    	wordFile = scan.next();
    	scan.close();

		messageGen.generateText(wordFile);
		messageGen.createData(size, outputFilename);
	}

	private void createData(int size, String outputFilename) {
		int command = 0;
		int userId = 0;
		int numText = 0;
		Random randomNum = new Random();
		File outputFile = new File(outputFilename);
		PrintWriter writer = null;
		JSONObject jObj = null;

		try {
			writer = new PrintWriter(new FileWriter(outputFile));
			writer.print("[\n");

		}
		catch(IOException e) {
			System.out.println("Could not create file " + outputFilename);
		}

		for(int i = 0; i < size; i++) {
			command = randomNum.nextInt(10);
			userId = randomNum.nextInt(max_users) + 1;

			//public
			if(command >= 0 && command <= 7) {
				jObj = publicMessage(i, userId);
			}
			//protected
			else if(command == 8) {
				jObj = protectedMessage(i, userId);
			}
			//private
			else if (command == 9) {
				jObj = privateMessage(i, userId);
			}

			printObj(jObj, writer);

			if (i < size - 1) {
				writer.print(",\n");
			}
			else {
				writer.print("\n]");
			}
		}
		writer.close();
	}

	//subscribers, self, userId, all
	//mostly subsribers or all evenly
	//self or userId at most 20%
	private JSONObject publicMessage(int messageId, int userId) {
		int command = 0;
		Random randomNum = new Random();
		JSONObject jObj = null;

		command = randomNum.nextInt(9);

		//subscribers
		if(command >= 0 && command <= 3) {
			jObj = createObj(messageId, "u" + userId, "public", "subscribers");
		}
		//all
		else if(command >= 4 && command <= 7) {
			jObj = createObj(messageId, "u" + userId, "public", "all");

		}
		//userId
		else if(command == 8) {
			jObj = createObj(messageId, "u" + userId, "public", "u" + randomNum.nextInt(max_users) + 1);

		}

		return jObj;
	}

	//subscribers, self, userId
	//mainly to subscribers
	private JSONObject protectedMessage(int messageId, int userId) {
		Random randomNum = new Random();
		int command = randomNum.nextInt(5);
		JSONObject jObj = null;

		//subscribers
		if(command >= 0 && command <= 3) {
			jObj = createObj(messageId, "u" + userId, "protected", "subscribers");
		}
		else if (command == 4) {
			jObj = createObj(messageId, "u" + userId, "protected", "u" + randomNum.nextInt(max_users) + 1);
		}

		return jObj;
	}

	//self or userId
	//usually userId
	private JSONObject privateMessage(int messageId, int userId) {
		Random randomNum = new Random();
		
		return createObj(messageId, "u" + userId, "private", "u" + randomNum.nextInt(max_users) + 1);
	}

	private JSONObject createObj(int messageId, String userId, String status, String recepient) {
		JSONObject jObj = new JSONObject();
		try {
			jObj.put("messageId", messageId);
			jObj.put("user", userId);
			jObj.put("status", status);
			jObj.put("recipient", recepient);
			jObj.put("text", createTextMessage());

		}
		catch (JSONException e) {
			System.out.println("could not append");
		}
		return jObj;
	}

	private void generateText(String wordFile) {
		File file = new File(wordFile);
		Scanner scan;
		englishText = new ArrayList<String>();

		try {
			scan = new Scanner(file);

			while(scan.hasNext() == true) {
				englishText.add(scan.next());
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("Could not find file " + wordFile);
		}

	}

	private String createTextMessage() {
		Random randomNum = new Random();
		int numText = randomNum.nextInt(max_words - min_words) + min_words;
		int num = 0;
		String textMessage = "";
		for(int ndx = 0; ndx < numText; ndx++) {
			num = randomNum.nextInt(englishText.size());
			textMessage += englishText.get(num) + " ";
		}

		return textMessage;
	}

	private void printObj(JSONObject jObj, PrintWriter writer) {
		try {
			//System.out.println(jObj.toString(3));
			writer.print(jObj.toString(3));
		}
		catch (JSONException e) {
			System.out.println("could to to string json object");
		}

	}
}