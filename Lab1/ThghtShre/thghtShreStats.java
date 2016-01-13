import org.json.*;
import java.util.*;
import java.io.*;

public class thghtShreStats {
	
	public static void main(String[] args) {

		thghtShreStats tsStats = new thghtShreStats();

		int totalMessages = 0;
		int totalWords = 0;
		int totalChars = 0;
		ArrayList<Integer> trackWordLength = new ArrayList<Integer>();
		ArrayList<Integer> trackCharLength = new ArrayList<Integer>();
		
		String inputFilename = null;
		Scanner scan = new Scanner(System.in);

		//public, protected, private
		//num of status, word count, char count
		int[][] statuses = new int[3][3];

		// public, protected, private
		// all, subscribers, self
		//in-repsonse, not in-response
		int[][] histoStatuses = new int[3][5];

		ArrayList<Integer> trackPublicWords = new ArrayList<Integer>();
		ArrayList<Integer> trackPublicChars = new ArrayList<Integer>();
		ArrayList<Integer> trackProtectedWords = new ArrayList<Integer>();
		ArrayList<Integer> trackProtectedChars = new ArrayList<Integer>();
		ArrayList<Integer> trackPrivateWords = new ArrayList<Integer>();
		ArrayList<Integer> trackPrivateChars = new ArrayList<Integer>();

		//all, subscribers, self
		// num of recepient, word count, char count
		int[][] recipients = new int[3][3];

		//all, sub, self
		//in-response, not in-response
		int[][] histoRecipients = new int[3][2];

		ArrayList<Integer> trackAllWords = new ArrayList<Integer>();
		ArrayList<Integer> trackAllChars = new ArrayList<Integer>();
		ArrayList<Integer> trackSubWords = new ArrayList<Integer>();
		ArrayList<Integer> trackSubChars = new ArrayList<Integer>();
		ArrayList<Integer> trackSelfWords = new ArrayList<Integer>();
		ArrayList<Integer> trackSelfChars = new ArrayList<Integer>();

		//response, not response
		//num of response, word count, char count
		int[][] responses = new int[3][3];
		ArrayList<Integer> trackResponseWords = new ArrayList<Integer>();
		ArrayList<Integer> trackResponseChars = new ArrayList<Integer>();
		ArrayList<Integer> trackNoResponseWords = new ArrayList<Integer>();
		ArrayList<Integer> trackNoResponseChars = new ArrayList<Integer>();
		
		ArrayList<String> users = new ArrayList<String>();
		Hashtable<Integer, Integer> wordLength = new Hashtable<Integer, Integer>();


		PrintWriter writer = null;
		if(args.length >= 1) {
			try {
				writer = new PrintWriter(new FileWriter(new File(args[0])));
			}
			catch (IOException e) {
				System.out.println("could not create file");
			}
		}

		System.out.println("Input JSON file: ");
		if(scan.hasNext()) {
			inputFilename = scan.next();
		}
		else {
			System.out.println("Please input a JSON file name.");
		}


		try {
			JSONTokener token = new JSONTokener(new FileReader(new File (inputFilename)));
			JSONArray jsonArray = new JSONArray(token);
			JSONObject jObj = new JSONObject();
			JSONObject temp1;
			JSONObject temp2;
			
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
				trackWordLength.add(numWords);
				trackCharLength.add(numChars);

				//counts the length of message by in-response
				if(current.has("in-response") == true) {
					tsStats.trackMessage(responses[0], numWords, numChars, trackResponseChars, trackResponseWords);
				}
				else {
					tsStats.trackMessage(responses[1], numWords, numChars, trackNoResponseChars, trackNoResponseWords);

				}

				//keeps track of unique users who authored the message
				if(users.contains(user) == false) {
					users.add(user);
				}

				//keeps track of messages by status
				//counts the length of message (words and chars)
				if(status.equals("public")) {
					tsStats.trackMessage(statuses[0], numWords, numChars, trackPublicChars, trackPublicWords);
					tsStats.checkStatuses(histoStatuses[0], current.has("in-response"), recipient);
				} else if (status.equals("protected")) {
					tsStats.trackMessage(statuses[1], numWords, numChars, trackProtectedChars, trackProtectedWords);
					tsStats.checkStatuses(histoStatuses[1], current.has("in-response"), recipient);
				} else if (status.equals("private")) {
					tsStats.trackMessage(statuses[2], numWords, numChars, trackPrivateChars, trackPrivateWords);
					tsStats.checkStatuses(histoStatuses[2], current.has("in-response"), recipient);
				}

				//keeps track of messages by recipient
				//counts the length of message (words and chars)
				if(recipient.equals("all")) {
					tsStats.trackMessage(recipients[0], numWords, numChars, trackAllChars, trackAllWords);
					tsStats.checkResponse(histoRecipients[0], current.has("in-response"));

				} else if (recipient.equals("subscribers")) {
					tsStats.trackMessage(recipients[1], numWords, numChars, trackSubChars, trackSubWords);
					tsStats.checkResponse(histoRecipients[1], current.has("in-response"));

				} else if(recipient.equals("self")) {
					tsStats.trackMessage(recipients[2], numWords, numChars, trackSelfChars, trackSelfWords);
					tsStats.checkResponse(histoRecipients[2], current.has("in-response"));

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

			System.out.println("\n\n------------- Basic Stats -------------");
			System.out.println("Total Number of messages reported: " + totalMessages);
			System.out.println("Total number of unique users who authored the messages: " + users.size());
			System.out.println("Average length of characters of a message: " + (totalChars/totalMessages));
			System.out.println("Standard Deviation of average length of characters of a message: " + tsStats.calcStdDev((totalWords/totalMessages), trackWordLength));
			System.out.println("Average length of words of a message: " + (totalWords/totalMessages));
			System.out.println("Standard Deviation of average length of words of a message: " + tsStats.calcStdDev((totalWords/totalMessages), trackWordLength));
			
			temp1 = new JSONObject();
			temp1.put("Total Messages", totalMessages);
			temp1.put("Total Unique Author", users.size());
			temp1.put("Average Character Length", (totalChars/totalMessages));
			temp1.put("Standard Deviation of Character Length", tsStats.calcStdDev((totalWords/totalMessages), trackWordLength));
			temp1.put("Average Word Length", (totalWords/totalMessages));
			temp1.put("Standard Deviation of Word Length", tsStats.calcStdDev((totalWords/totalMessages), trackWordLength));
			jObj.put("Basic Stats", temp1);

			System.out.println("\n\n------------- Message Type Histogram -------------");
			System.out.println("Status:");
			System.out.println("Total public messages: " + statuses[0][0]);
			System.out.println("Total protected messages: " + statuses[1][0]);
			System.out.println("Total private messages: " + statuses[2][0]);

			System.out.println("\nRecipient:");
			System.out.println("Total messages to all: " + recipients[0][0]);
			System.out.println("Total messages to subscribers: " + recipients[1][0]);
			System.out.println("Total messages to self: " + recipients[2][0]);

			System.out.println("\nIn-Response:");
			System.out.println("Total in-response messages: " + responses[0][0]);
			System.out.println("Total not in-response messages: " + responses[1][0]);

			temp1 = new JSONObject();
			temp2 = new JSONObject();
			temp1.put("Total Public Messages", statuses[0][0]);
			temp1.put("Total Protected Messages", statuses[1][0]);
			temp1.put("Total Private Messages", statuses[2][0]);
			temp2.put("Status", temp1);

			temp1 = new JSONObject();
			temp1.put("Total All Messages", recipients[0][0]);
			temp1.put("Total Subscribers Messages", recipients[1][0]);
			temp1.put("Total Self Messages", recipients[2][0]);
			temp2.put("Recipient", temp1);

			temp1 = new JSONObject();
			temp1.put("Total In-Response Messages", responses[0][0]);
			temp1.put("Total Not In-Response Messages", responses[1][0]);
			temp2.put("In-Response", temp1);
			jObj.put("Message Type Histograms", temp2);

			System.out.println("\n\n------------- Stats for Subsets of Messages -------------");
			System.out.println("Status:");
			System.out.println("Average words of public messages: " + (statuses[0][1] / totalMessages));
			System.out.println("Average chars of public messages: " + (statuses[0][2] / totalMessages));
			System.out.println("Public Message Word Standard Deviation: " + tsStats.calcStdDev((statuses[0][1] / totalMessages), trackPublicWords));
			System.out.println("Public Message Char Standard Deviation: " + tsStats.calcStdDev((statuses[0][2] / totalMessages), trackPublicChars));

			temp1 = new JSONObject();
			temp2 = new JSONObject();
			temp1.put("Average Public Words", (statuses[0][1] / totalMessages));
			temp1.put("Average Public Chars", (statuses[0][2] / totalMessages));
			temp1.put("Standard Deviation Public Words", tsStats.calcStdDev((statuses[0][1] / totalMessages), trackPublicWords));
			temp1.put("Standard Deviation Public Chars", tsStats.calcStdDev((statuses[0][2] / totalMessages), trackPublicChars));

			System.out.println("\nAverage words of protected messages: " + (statuses[1][1] / totalMessages));
			System.out.println("Average words of protected messages: " + (statuses[1][2] / totalMessages));
			System.out.println("Protected Message Word Standard Deviation: " + tsStats.calcStdDev((statuses[1][1] / totalMessages), trackProtectedWords));
			System.out.println("Protected Message Word Standard Deviation: " + tsStats.calcStdDev((statuses[1][2] / totalMessages), trackProtectedChars));

			temp1.put("Average Protected Words", (statuses[1][1] / totalMessages));
			temp1.put("Average Protected Chars", (statuses[1][2] / totalMessages));
			temp1.put("Standard Deviation Protected Words", tsStats.calcStdDev((statuses[1][1] / totalMessages), trackProtectedWords));
			temp1.put("Standard Deviation Protected Chars", tsStats.calcStdDev((statuses[1][2] / totalMessages), trackProtectedChars));


			System.out.println("\nAverage words of private messages: " + (statuses[2][1] / totalMessages));
			System.out.println("Average chars of private messages: " + (statuses[2][2] / totalMessages));
			System.out.println("Private Message Word Standard Deviation: " + tsStats.calcStdDev((statuses[2][1] / totalMessages), trackPrivateWords));
			System.out.println("Private Message Word Standard Deviation: " + tsStats.calcStdDev((statuses[2][2] / totalMessages), trackPrivateChars));

			temp1.put("Average Private Words", (statuses[2][1] / totalMessages));
			temp1.put("Average Private Chars", (statuses[2][2] / totalMessages));
			temp1.put("Standard Deviation Private Words", tsStats.calcStdDev((statuses[2][1] / totalMessages), trackPrivateWords));
			temp1.put("Standard Deviation Private Chars", tsStats.calcStdDev((statuses[2][2] / totalMessages), trackPrivateChars));
			temp2.put("Status", temp1);


			System.out.println("\n\nRecipients:");
			System.out.println("Average words to all: " + (recipients[0][1] / totalMessages));
			System.out.println("Average chars to all: " + (recipients[0][2] / totalMessages));
			System.out.println("Message to All Word Standard Deviation: " + tsStats.calcStdDev((recipients[0][1] / totalMessages), trackAllWords));
			System.out.println("Message to All Char Standard Deviation: " + tsStats.calcStdDev((recipients[0][2] / totalMessages), trackAllChars));

			temp1 = new JSONObject();
			temp1.put("Average All Words", (recipients[0][1] / totalMessages));
			temp1.put("Average All Chars", (recipients[0][2] / totalMessages));
			temp1.put("Standard Deviation All Words", tsStats.calcStdDev((recipients[0][1] / totalMessages), trackAllWords));
			temp1.put("Standard Deviation All Chars", tsStats.calcStdDev((recipients[1][2] / totalMessages), trackAllChars));

			System.out.println("\nAverage words to subscribers: " + (recipients[1][1] / totalMessages));
			System.out.println("Average chars to subscribers: " + (recipients[1][2] / totalMessages));
			System.out.println("Message to Subscribers Word Standard Deviation: " + tsStats.calcStdDev((recipients[1][1] / totalMessages), trackSubWords));
			System.out.println("Message to Subscribers Char Standard Deviation: " + tsStats.calcStdDev((recipients[1][2] / totalMessages), trackSubChars));
			
			temp1.put("Average Subscribers Words", (recipients[1][1] / totalMessages));
			temp1.put("Average Subscribers Chars", (recipients[1][2] / totalMessages));
			temp1.put("Standard Deviation Subscribers Words", tsStats.calcStdDev((recipients[1][1] / totalMessages), trackSubWords));
			temp1.put("Standard Deviation Subscribers Chars", tsStats.calcStdDev((recipients[1][2] / totalMessages), trackSubChars));


			System.out.println("\nAverage words to self: " + (recipients[2][1] / totalMessages));
			System.out.println("Average char to self: " + (recipients[2][2] / totalMessages));
			System.out.println("Message to Self Word Standard Deviation: " + tsStats.calcStdDev((recipients[2][1] / totalMessages), trackSelfWords));
			System.out.println("Message to Self Char Standard Deviation: " + tsStats.calcStdDev((recipients[2][2] / totalMessages), trackSelfChars));

			temp1.put("Average Self Words", (recipients[1][1] / totalMessages));
			temp1.put("Average Self Chars", (recipients[1][2] / totalMessages));
			temp1.put("Standard Deviation Self Words", tsStats.calcStdDev((recipients[1][1] / totalMessages), trackSelfWords));
			temp1.put("Standard Deviation Self Chars", tsStats.calcStdDev((recipients[1][2] / totalMessages), trackSelfChars));
			temp2.put("Recipients", temp1);

			System.out.println("\n\nIn-Response: ");
			System.out.println("Average In-Response words: " + (responses[0][1] / totalMessages));
			System.out.println("Average In-Response chars: " + (responses[0][2] / totalMessages));
			System.out.println("Standard Deviation of In-Response words: " + tsStats.calcStdDev((responses[0][1] / totalMessages), trackResponseWords));
			System.out.println("Standard Deviation of In-Response chars: " + tsStats.calcStdDev((recipients[0][2] / totalMessages), trackResponseChars));

			temp1 = new JSONObject();
			temp1.put("Average In-Response Words", (responses[0][1] / totalMessages));
			temp1.put("Average In-Response Chars", (responses[0][2] / totalMessages));
			temp1.put("Standard Deviation In-Response Words", tsStats.calcStdDev((responses[0][1] / totalMessages), trackResponseWords));
			temp1.put("Standard Deviation In-Response Chars", tsStats.calcStdDev((responses[0][2] / totalMessages), trackResponseChars));

			System.out.println("\nAverage Not In-Response words: " + (responses[1][1] / totalMessages));
			System.out.println("Average Not In-Response chars: " + (responses[1][2] / totalMessages));
			System.out.println("Standard Deviation of Not In-Response words: " + tsStats.calcStdDev((responses[1][1] / totalMessages), trackNoResponseWords));
			System.out.println("Standard Deviation of Not In-Response chars: " + tsStats.calcStdDev((recipients[1][2] / totalMessages), trackNoResponseChars));

			temp1.put("Average Non In-Response Words", (responses[1][1] / totalMessages));
			temp1.put("Average Non In-Response Chars", (responses[1][2] / totalMessages));
			temp1.put("Standard Deviation Non In-Response Words", tsStats.calcStdDev((responses[1][1] / totalMessages), trackNoResponseWords));
			temp1.put("Standard Deviation Non In-Response Chars", tsStats.calcStdDev((responses[1][2] / totalMessages), trackNoResponseChars));
			temp2.put("In-Response", temp1);
			jObj.put("Stats for Subsets of Messages", temp2);

			System.out.println("\n\n------------- Conditional Histograms -------------");
			System.out.println("Status: ");
			System.out.println("Public messages to all: " + histoStatuses[0][0]);
			System.out.println("Public messages to subscribers: " + histoStatuses[0][1]);
			System.out.println("Public messages to self: " + histoStatuses[0][2]);
			System.out.println("Public messages that had in-response: " + histoStatuses[0][3]);
			System.out.println("Public messages that had NO in-response: " + histoStatuses[0][4]);

			temp1 = new JSONObject();
			temp2 = new JSONObject();
			temp1.put("Public Messages to All", histoStatuses[0][0]);
			temp1.put("Public Messages to Subscribers", histoStatuses[0][1]);
			temp1.put("Public Messages to Self", histoStatuses[0][2]);
			temp1.put("Public Messages to In-Response", histoStatuses[0][3]);
			temp1.put("Public Messages to Non In-Response", histoStatuses[0][4]);


			System.out.println("Protected messages to all: " + histoStatuses[1][0]);
			System.out.println("Protected messages to subscribers: " + histoStatuses[1][1]);
			System.out.println("Protected messages to self: " + histoStatuses[1][2]);
			System.out.println("Protected messages that had in-response: " + histoStatuses[1][3]);
			System.out.println("Protected messages that had NO in-response: " + histoStatuses[1][4]);

			temp1.put("Protected Messages to All", histoStatuses[1][0]);
			temp1.put("Protected Messages to Subscribers", histoStatuses[1][1]);
			temp1.put("Protected Messages to Self", histoStatuses[1][2]);
			temp1.put("Protected Messages to In-Response", histoStatuses[1][3]);
			temp1.put("Protected Messages to Non In-Response", histoStatuses[1][4]);

			System.out.println("Private messages to all: " + histoStatuses[2][0]);
			System.out.println("Private messages to subscribers: " + histoStatuses[2][1]);
			System.out.println("Private messages to self: " + histoStatuses[2][2]);
			System.out.println("Private messages that had in-response: " + histoStatuses[2][3]);
			System.out.println("Private messages that had NO in-response: " + histoStatuses[2][4]);

			temp1.put("Private Messages to All", histoStatuses[2][0]);
			temp1.put("Private Messages to Subscribers", histoStatuses[2][1]);
			temp1.put("Private Messages to Self", histoStatuses[2][2]);
			temp1.put("Private Messages to In-Response", histoStatuses[2][3]);
			temp1.put("Private Messages to Non In-Response", histoStatuses[2][4]);
			temp2.put("Status", temp1);

			System.out.println("\nRecipients: ");
			System.out.println("All messages that had in-response: " + histoRecipients[0][0]);
			System.out.println("All messages that had NO in-response: " + histoRecipients[0][1]);
			System.out.println("Subscribers messages that had in-response: " + histoRecipients[1][0]);
			System.out.println("Subscribers messages that had NO in-response: " + histoRecipients[1][1]);
			System.out.println("Self messages that had in-response: " + histoRecipients[2][0]);
			System.out.println("Self messages that had NO in-response: " + histoRecipients[2][1]);
			System.out.println("\n");

			temp1.put("All Messages In-Response", histoRecipients[0][0]);
			temp1.put("All Messages Non In-Response", histoRecipients[0][1]);
			temp1.put("Subscribers Messages In-Response", histoRecipients[1][0]);
			temp1.put("Subscribers Messages Non In-Response", histoRecipients[1][1]);
			temp1.put("Self Messages In-Response", histoRecipients[2][0]);
			temp1.put("Self Messages Non In-Response", histoRecipients[2][1]);
			temp2.put("Recipients", temp1);
			jObj.put("Conditional Histograms", temp2);

			if(writer != null) {
				writer.write(jObj.toString(3));
				writer.close();
			}

		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
		}
		catch (JSONException e) { 
			System.out.println("JSON Expection.");
		}
	}


	private double calcStdDev(double mean, ArrayList<Integer> values) {
		int total = 0;
		if(values.isEmpty()) {
			return 0;
		}
		for(int i = 0; i < values.size(); i++) {
			total += Math.pow(values.get(i) - mean, 2);
		}
		return Math.sqrt(total / values.size());
	}


	private void trackMessage(int[] array, int numWords, int numChars, ArrayList<Integer> charMessage, ArrayList<Integer> wordMessage) {
		array[0] += 1;
		array[1] += numWords;
		array[2] += numChars;
		charMessage.add(numChars);
		wordMessage.add(numWords);
	}

	private void checkResponse(int[] array, boolean response) {
		if(response == true) {
			array[0] += 1;
		}
		else {
			array[1] += 1;
		}
	}

	private void checkStatuses(int[] array, boolean response, String recipient) {
		if(recipient.equals("all")) {
			array[0] += 1;
		} else if(recipient.equals("subscribers")) {
			array[1] += 1;
		} else if(recipient.equals("self")) {
			array[2] += 1;
		}

		if(response == true) {
			array[3] += 1;
		} else {
			array[4] += 1;
		}
	}
}


