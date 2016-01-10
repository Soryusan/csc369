import org.json.*;
import java.util.*;
import java.io.*;

public class BeFuddledGen {

	private final int max_users = 10000;
	private Hashtable<Integer, GameInfo> gameRecord;
	private boolean[] userIds;
	private final int SHUFFLE = 0;
	private final int CLEAR = 1;
	private final int INVERT = 2;
	private final int ROTATE = 3;


    public static void main(String[] args) {

    	String outputFilename;
    	int size = 0;
       	int jsonCount = 0;
       	File outputFile;
       	BeFuddledGen game = new BeFuddledGen();
    	Scanner scan = new Scanner(System.in);
    	System.out.println("Input output file name: ");
    	outputFilename = scan.next();
    	System.out.println("Input number of JSON to generate: ");
    	size = scan.nextInt();
    	outputFile = new File(outputFilename);
    	game.createData(size, outputFile);
	}

	private void createData(int size, File outputFile) {
		int command;
		int userId;
		int gameCount = 1;
		Random randomNum = new Random();
		JSONObject jObj = null;
		userIds = new boolean[max_users + 1];
		gameRecord = new Hashtable<Integer, GameInfo>();

		for(int i = 0; i < size; i++) {
			if(gameRecord.isEmpty()) {
				jObj = createNewUser(gameCount++);
			}
			else {
				command = randomNum.nextInt(5);
				switch(command) {
					case 0:
						jObj = createNewUser(gameCount++);
						break;
					case 1:
					case 2:
						jObj = moveUser(gameCount);
						break;
					case 3:
						jObj = specialMove(gameCount);
						break;
					case 4:
						jObj = endGame(gameCount);
						break;
				}
			}
			printObj(jObj, outputFile);
		}
	}

	private JSONObject createNewUser(int gameId) {
		int userId;
		JSONObject jObj = new JSONObject();
		Random randomNum = new Random();
		userId = randomNum.nextInt(max_users) + 1;
		while(userIds[userId] == true) {
			userId = randomNum.nextInt(max_users) + 1;
		}
		userIds[userId] = true;
		try {
			jObj.append("user", "u" + userId);
			jObj.append("game", gameId);
			jObj.append("action", "GameStart");
		}
		catch (JSONException e) {
			System.out.println("could not append");
		}
		gameRecord.put(gameId, new GameInfo());
		return jObj;
	}

	private JSONObject moveUser(int gameCount) {
		Random randomNum = new Random();
		GameInfo gameInfo;
		JSONObject jObj = new JSONObject();
		JSONObject coords = new JSONObject();
		int game;
		int xCoord;
		int yCoord;
		int addPoints;
		//Gets random number to denote which game to use action on
		game = getRandomGame(gameCount);

		xCoord = randomNum.nextInt(20) + 1;
		yCoord = randomNum.nextInt(20) + 1;
		addPoints = randomNum.nextInt(41) - 20;
		gameInfo = gameRecord.get(game);
		gameInfo.addPoints(addPoints);
		gameInfo.incrementCount();
		try {
			coords.append("x", xCoord);
			coords.append("y", yCoord);

			jObj.append("actionNumber", gameInfo.getCount());
			jObj.append("actionType", "Move");
			jObj.append("location", coords);
			jObj.append("pointsAdded", addPoints);
			jObj.append("points", gameInfo.getPoints());
		}
		catch (JSONException e) {
			System.out.println("could not append");
		}
		return jObj;
	}

	private JSONObject specialMove(int gameCount) {
		Random randomNum = new Random();
		GameInfo gameInfo;
		JSONObject jObj = new JSONObject();
		int game;
		int addPoints;
		int special;
		//Gets random number to denote which game to use action on
		game = getRandomGame(gameCount);
		gameInfo = gameRecord.get(game);
		//Gets random number to denote which special move to use
		special = randomNum.nextInt(4);
		while(gameInfo.checkSpecialMove(special)) {
			special = randomNum.nextInt(4);
		}
		gameInfo.useSpecialMove(special);

		addPoints = randomNum.nextInt(41) - 20;
		gameInfo.addPoints(addPoints);
		gameInfo.incrementCount();

		try {
			jObj.append("actionNumber", gameInfo.getCount());
			jObj.append("actionType", "SpecialMove");
			jObj.append("pointsAdded", addPoints);
			jObj.append("points", gameInfo.getPoints());
		}
		catch (JSONException e) {
			System.out.println("could not append");
		}
		return jObj;	
	}

	private JSONObject endGame(int gameCount) {
		Random randomNum = new Random();
		GameInfo gameInfo;
		JSONObject jObj = new JSONObject();
		int game;
		String status;

		game = getRandomGame(gameCount);
		gameInfo = gameRecord.get(game);
		gameInfo.incrementCount();
		if(gameInfo.getPoints() > 150) {
			status = "WIN";
		}
		else {
			status = "LOSS";
		}
		try {
			jObj.append("actionNumber", gameInfo.getCount());
			jObj.append("actionType", "GameEnd");
			jObj.append("points", gameInfo.getPoints());
			jObj.append("gameStatus", status);
		}
		catch (JSONException e) {
			System.out.println("could not append");
		}
		return jObj;

	}

	private int getRandomGame(int num) {
		int ranGame;
		Random randomNum = new Random();
		ranGame = randomNum.nextInt(num) + 1;
		while(!gameRecord.containsKey(ranGame)) {
			ranGame = randomNum.nextInt(num) + 1;
		}
		return ranGame;
	}

	private void printObj(JSONObject jObj, File outputFile) {
		BufferedWriter output;
		try {
			output = new BufferedWriter(new FileWriter(outputFile));
			try {
				System.out.println(jObj.toString(3));
				output.write(jObj.toString(3));
			}
			catch (JSONException e) {
				System.out.println("could to to string json object");
			}
		}
		catch (IOException e) {
			System.out.println("Unable to open file " + outputFile.getName());
		}

	}
}

