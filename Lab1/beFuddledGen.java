import org.json.*;
import java.util.*;
import java.io.File;

public class beFuddledGen {

	private final int max_users = 10000;
	private Hasthtable<Integer, GameInfo> gameRecord;
	private File outputFile;
	private boolean[] userIds;
	private final int SHUFFLE = 0;
	private final int CLEAR = 1;
	private final int INVERT = 2;
	private final int ROTATE = 3;


    public static void main(String[] args) {

    	String outputFilename;
    	int size = 0;
       	int jsonCount = 0;

    	Scanner scan = new Scanner(System.in);
    	System.out.println("Input output file name: ");
    	outputFilename = scan.next();
    	System.out.println("Input number of JSON to generate: ");
    	size = scan.nextInt();
    	outputFile = new File(outputFilename);
	}

	private void createData(int size) {
		int command;
		int userId;
		int gameCount = 1;
		Random randomNum = new Random();
		userIds = new boolean[max_users + 1];
		gameRecord = new Hasthtable<Integer, GameInfo>();

		for(int i = 0; i < size; i++) {
			jObj = new JSONObject();
			if(i == 0) {
				createNewUser(gameCount++);
			}
			else {
				command = randomNum.nextInt(5);
				switch(command) {
					case 0:
						createNewUser(gameCount++);
						break;
					case 1:
					case 2:
						moveUser(gameCount);
						break;
					case 3:
						break;
					case 4:
						break;
				}
			}
		}
	}

	private void createNewUser(int gameId) {
		int userId;
		JSONObject jObj = new JSONObject();
		Random randomNum = new Random();
		userId = randomNum.nextInt(max_users) + 1;
		while(userIds[userId] == true) {
			userId = randomNum.nextInt(max_users) + 1;
		}
		userIds[userId] = true;
		jObj.append("user", "u" + userId);
		jObj.append("game", gameId);
		jObj.append("action", "GameStart");
		gameRecord.put(gameId, new GameInfo());
		printObj(jObj);
	}

	private void moveUser(int gameCount) {
		Random randomNum = new Random();
		GameInfo gameInfo;
		JSONObject jObj = new JSONObject();
		JSONObject coords = new JSONObject();
		int game;
		int xCoord;
		int yCoord;
		int addPoints;
		game = randomNum.nextInt(gameCount) + 1;
		while(gameRecord.containsKey(game) != true) {
			game = randomNum.nextInt(gameCount) + 1;
		}

		xCoord = randomNum.nextInt(20) + 1;
		yCoord = randomNum.nextInt(20) + 1;
		addPoints = randomNum.nextInt(41) - 20;
		gameInfo = gameRecord.get(game);
		gameInfo.addPoints(addPoints);
		gameInfo.incrementCount();
		coords.append("x", xCoord);
		coords.append("y", yCoord);

		jObj.append("actionNumber", gameInfo.getCount());
		jObj.append("actionType", "Move");
		jObj.append("location", coords);
		jObj.append("pointsAdded", addPoints);
		jObj.append("points", gameInfo.getPoints());
		printObj(jObj);
	}

	private void specialMove(int gameCount) {
		Random randomNum = new Random();
		GameInfo gameInfo;
		JSONObject jObj = new JSONObject();
		int game;
		int addPoints;
		int special;

		game = randomNum.nextInt(gameCount) + 1;
		while(gameRecord.containsKey(game) != true) {
			game = randomNum.nextInt(gameCount) + 1;
		}
		gameInfo = gameRecord.get(game);
		special = randomNum.nextInt(4);
		while(gameInfo.checkSpecialMove(special) = true) {
			special = randomNum.nextInt(4);
		}
		gameInfo.useSpecialMove(special);
		
		addPoints = randomNum.nextInt(41) - 20;
		gameInfo = gameRecord.get(game);
		gameInfo.addPoints(addPoints);
		gameInfo.incrementCount();

		jObj.append("actionNumber", gameInfo.getCount());
		jObj.append("actionType", "SpecialMove");
		jObj.append("pointsAdded", addPoints);
		jObj.append("points", gameInfo.getPoints());
		printObj(jObj);		
	}

	private void printObj(JSONObject jObj) {

	}
}

