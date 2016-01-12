import org.json.JSONObject;
import org.json.*;
import java.util.*;
import java.io.*;

public class beFuddledStats {

	public static void main(String[] args){

		String filename;
		Scanner scanner = new Scanner(System.in);
		JSONObject current;
		JSONObject action;
		ArrayList<Integer> trackPoints = new ArrayList<Integer>();
		ArrayList<Integer> trackMoves = new ArrayList<Integer>();
		Hashtable<Integer, Integer> histMoves = new Hashtable<Integer, Integer>();
		Hashtable<String, user> users = new Hashtable<String, user>();

		int board[][] = new int[21][21];
		int specialMoves[] = new int[4];

		int startedGames = 0;
		int completedGames = 0;
		int totalPoints = 0;
		int winPoints = 0;
		int losePoints = 0;
		int totalMoves = 0;
		int winMoves = 0;
		int loseMoves = 0;
		int usersStarted = 0;
		int usersCompleted = 0;
		int largestStarted = 0;
		int largestCompleted = 0;
		int largestWins = 0;
		int largestLoss = 0;

		String userCompleted;
		String userWins;
		String userLoss;
		String actionType;

		System.out.println("Enter filename: ");
		filename = scanner.next();

		try {
	  // JSONTokener is the org.json wrapper around any Reader object 
			JSONTokener t = new JSONTokener(new FileReader(new File(filename)));

	  // Everything from the p.json file is converted into a JSON array
	  // in one fell swoop
			JSONArray a = new JSONArray(t);
			System.out.println("Array size: " + a.length());

			for(int i = 0; i < a.length(); i++) {
				current = a.getJSONObject(i);
				action = current.getJSONObject("action");
				actionType = action.getString("actionType");
				if(actionType.equals("gameStart")) {
					startedGames++;
				}
				else if(actionType.equals("Move")) {
					int x;
					int y;
					JSONObject location = action.getJSONObject("location");
					x = location.getInt("x");
					y = location.getInt("y");

					board[x][y]++;
				}
				else if(actionType.equals("specialMove")) {

				}
				//GameEnd
				else {
					completedGames++;
					int moves = action.getInt("actionNumber");
					trackPoints.add(action.getInt("points"));
					trackMoves.add(moves);
					if(action.get("gameStatus").equals("LOSS")) {
						losePoints += action.getInt("points");
						loseMoves += moves;
					}
					else {
						winPoints += action.getInt("points");
						loseMoves += moves;
					}
					if(moves < 15) {
						histMoves.put(0, moves);
					}
					else if (moves < 30) {
						histMoves.put(15, moves);
					}
					else if (moves < 40) {
						histMoves.put(30, moves);
					}
					else if (moves < 50) {
						histMoves.put(40, moves);
					}
					else if (moves < 60) {
						histMoves.put(50, moves);
					}
					else if (moves < 80) {
						histMoves.put(60, moves);
					}
					else {
						histMoves.put(80, moves);
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Ouch!");
			System.out.println(e);
		}

		System.out.println();
	}

	private int calcMean(ArrayList<Integer> values) {
		return 0;
	}

	private int calcStDev(int mean, ArrayList<Integer> values) {
		return 0;
	}
}