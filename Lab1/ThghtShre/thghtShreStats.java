import org.json.*;
import java.util.*;
import java.io.*;

public class thghtShreStats {
	
	public static void main(String[] args) {
		int totalMessages = 0;
		int totalWords = 0;
		int totalChars = 0;
		String inputFilename = null;
		Scanner scan = new Scanner(System.in);

		System.out.println("Input JSON file: ");
		if(scan.hasNext()) {
			inputFilename = scan.next();
		}
		else {
			System.out.println("Please input a JSON file name.");
		}

		//public, protected, private
		//num of status, word count, char count
		int[][] statuses = new int[3][3];

		//all, self, subscribers
		// num of recepient, word count, char count
		int[][] recipients = new int[3][3];

		//response, not response
		//num of response, word count, char count
		int[][] responses = new int[3][3];
		
		ArrayList<String> users = new ArrayList<String>();
		Hashtable<Integer, Integer> wordLength = new Hashtable<Integer, Integer>();

		try {
			JSONTokener token = new JSONTokener(new FileReader(new File (inputFilename)));
			JSONArray jsonArray = new JSONArray(token);
			
			//total number of messages reported
			totalMessages = jsonArray.length();

			for(int ndx = 0; ndx < jsonArray.length(); ndx++) {
				JSONObject current = jsonArray.getJSONObject(ndx);


				String user = current.getString("user");
				String status = current.getString("status");
				String recipient = current.getString("recipient");
				String text = current.getString("text");

				text = text.trim();
				int numWords = text.split("\\s+").length; 
				int numChars = (text.replace("\\s+", "")).length(); 

				totalWords += numWords;
				totalChars += numChars;

				//counts the length of message by in-response
				if(current.has("in-response") == true) {
					responses[0][0] += 1;
					responses[0][1] += numWords; 
					responses[0][2] += numChars;
				}
				else {
					responses[1][0] += 1;
					responses[1][1] += numWords; 
					responses[1][2] += numChars;
				}

				//keeps track of unique users who authored the message
				if(users.contains(user) == false) {
					users.add(user);
				}

				//keeps track of messages by status
				//counts the length of message (words and chars)
				if(status.equals("public")) {
					statuses[0][0] += 1;
					statuses[0][1] += numWords;
					statuses[0][2] += numChars;
				} else if (status.equals("protected")) {
					statuses[1][0] += 1;
					statuses[1][1] += numWords;
					statuses[1][2] += numChars;
				} else if (status.equals("private")) {
					statuses[2][0] += 1;
					statuses[2][1] += numWords;
					statuses[2][2] += numChars;
				}

				//keeps track of messages by recipient
				//counts the length of message (words and chars)
				if(recipient.equals("all")) {
					recipients[0][0] += 1;
					recipients[0][1] += numWords;
					recipients[0][2] += numChars;
				} else if (recipient.equals("subscribers")) {
					recipients[1][0] += 1;
					recipients[1][1] += numWords;
					recipients[1][2] += numChars;
				} else if(recipient.equals("self")) {
					recipients[1][0] += 1;
					recipients[1][1] += numWords;
					recipients[1][2] += numChars;
				}

				//keeps track of messages by recipient
				//counts the length of message (words and chars)
				if(recipient.equals("all")) {
					recipients[0][0] += 1;
					recipients[0][1] += numWords;
					recipients[0][2] += numChars;
				} else if (recipient.equals("subscribers")) {
					recipients[1][0] += 1;
					recipients[1][1] += numWords;
					recipients[1][2] += numChars;
				} else if(recipient.equals("self")) {
					recipients[1][0] += 1;
					recipients[1][1] += numWords;
					recipients[1][2] += numChars;
				}

				//track the number of words in a message
				String[] words = text.split("\\s+");
				for(int i = 0; i < words.length; i++) {
					if(wordLength.containsKey(words[i].length())) {
						wordLength.put(words[i].length(), wordLength.get(words[i].length()) + 1);
					}
					else {
						wordLength.put(words[i].length(), 1);
					}
				}

			}

			System.out.println("Basic Stats");
			System.out.println("Total Number of messages reported: " + totalMessages);
			System.out.println("Total number of unique users who authored the messages: " + users.size());
			System.out.println("Average length of characters of a message: " + (totalChars/totalMessages));
			System.out.println("Standard Deviation of average length of characters of a message: " + Math.sqrt((totalChars/totalMessages)));
			System.out.println("Average length of words of a message: " + (totalWords/totalMessages));
			System.out.println("Standard Deviation of average length of words of a message: " + Math.sqrt((totalWords/totalMessages)));

			System.out.println("\nMessage Type Histogram:");
		} catch (FileNotFoundException e) {

		}
		catch (JSONException e) {}

	}

}