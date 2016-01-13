import org.json.JSONObject;
import org.json.*;
import java.util.*;
import java.io.*;

public class beFuddledStats {

	public static void main(String[] args){

		beFuddledStats calc = new beFuddledStats();
		String filename;
		Scanner scanner = new Scanner(System.in);
		JSONObject current;
		JSONObject action;
		ArrayList<Integer> trackPoints = new ArrayList<Integer>();
		ArrayList<Integer> trackPointsWon = new ArrayList<Integer>();
		ArrayList<Integer> trackPointsLoss = new ArrayList<Integer>();
		ArrayList<Integer> trackMoves = new ArrayList<Integer>();
		ArrayList<Integer> trackMovesWon = new ArrayList<Integer>();
		ArrayList<Integer> trackMovesLoss = new ArrayList<Integer>();
		ArrayList<String> usersMostStarted = new ArrayList<String>();
		ArrayList<String> usersMostCompleted = new ArrayList<String>();
		ArrayList<String> usersMostWins = new ArrayList<String>();
		ArrayList<String> usersMostLoss = new ArrayList<String>();
		ArrayList<String> usersMostMoves = new ArrayList<String>();
		Hashtable<Integer, Integer> histMoves = new Hashtable<Integer, Integer>();
		Hashtable<String, User> users = new Hashtable<String, User>();

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
		int usersWon = 0;
		int usersLoss = 0;
		int largestStarted = 0;
		int largestCompleted = 0;
		int largestWins = 0;
		int largestLoss = 0;
		int largestMoves = 0;

		double averagePointsTotal;
		double averagePointsWon;
		double averagePointsLoss;
		double averageMovesTotal;
		double averageMovesWon;
		double averageMovesLoss;

		double stdDevPointsTotal;
		double stdDevPointsWon;
		double stdDevPointsLoss;
		double stdDevMovesTotal;
		double stdDevMovesWon;
		double stdDevMovesLoss;

		String actionType;

		histMoves.put(0, 0);
		histMoves.put(15, 0);
		histMoves.put(30, 0);
		histMoves.put(40, 0);
		histMoves.put(50, 0);
		histMoves.put(60, 0);
		histMoves.put(80, 0);


		System.out.println("Enter filename: ");
		filename = scanner.next();

		try {
	  // JSONTokener is the org.json wrapper around any Reader object 
			JSONTokener t = new JSONTokener(new FileReader(new File(filename)));

	  // Everything from the p.json file is converted into a JSON array
	  // in one fell swoop
			JSONArray a = new JSONArray(t);

			for(int i = 0; i < a.length(); i++) {
				current = a.getJSONObject(i);
				action = current.getJSONObject("action");
				actionType = action.getString("actionType");
				if(actionType.equals("gameStart")) {
					User user;
					String userId = current.getString("user");
					startedGames++;
					if(users.containsKey(userId)) {
						user = users.get(userId);
					}
					else {
						user = new User();
						users.put(current.getString("user"), user);
					}
					user.incrementStart();
					if(user.getStarted() > largestStarted) {
						usersMostStarted.clear();
						usersMostStarted.add(current.getString("user"));
						largestStarted = user.getStarted();
					}
					else if(user.getStarted() == largestStarted) {
						usersMostStarted.add(current.getString("user"));
					}
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
					String sMove = action.getString("move");
					if(sMove == "Shuffle") {
						specialMoves[0]++;
					}
					else if(sMove == "Clear") {
						specialMoves[1]++;
					}
					else if(sMove == "Invert") {
						specialMoves[2]++;
					}
					else if(sMove == "Rotate") {
						specialMoves[3]++;
					}
				}
				//GameEnd
				else {
					completedGames++;
					int moves = action.getInt("actionNumber");
					String userId = current.getString("user");
					trackPoints.add(action.getInt("points"));
					trackMoves.add(moves);
					if(action.get("gameStatus").equals("LOSS")) {
						User user;
						losePoints += action.getInt("points");
						loseMoves += moves;
						usersLoss++;
						if(users.containsKey(userId)) {
							user = users.get(userId);
						}
						else {
							user = new User();
							users.put(userId, user);
						}
						user.setMoves(moves);
						user.incrementLoss();
						user.incrementComplete();
						trackPointsLoss.add(action.getInt("points"));
						trackMovesLoss.add(moves);
						//Check for most losses
						if(user.getLoss() > largestLoss) {
							largestLoss = user.getLoss();
							usersMostLoss.clear();
							usersMostLoss.add(userId);
						}
						else if(user.getLoss() == largestLoss) {
							usersMostLoss.add(userId);
						}
					}
					else {
						User user;
						winPoints += action.getInt("points");
						winMoves += moves;
						usersWon++;
						if(users.containsKey(userId)) {
							user = users.get(userId);
						}
						else {
							user = new User();
							users.put(userId, user);
						}
						user.setMoves(moves);
						user.incrementWins();
						user.incrementComplete();
						trackPointsWon.add(action.getInt("points"));
						trackMovesWon.add(moves);
						//Check for most wins
						if(user.getWins() > largestWins) {
							largestWins = user.getWins();
							usersMostWins.clear();
							usersMostWins.add(userId);
						}
						else if(user.getWins() == largestWins) {
							usersMostWins.add(userId);
						}
					}
					User user = users.get(userId);
					//Check for most completed games
					if(user.getCompleted() > largestCompleted) {
						largestCompleted = user.getCompleted();
						usersMostCompleted.clear();
						usersMostCompleted.add(userId);
					}
					else if(user.getCompleted() == largestCompleted) {
						usersMostCompleted.add(userId);
					}
					//Check for most moves
					if(user.getMoves() > largestMoves) {
						largestMoves = user.getMoves();
						usersMostMoves.clear();
						usersMostMoves.add(userId);
					}
					else if(user.getMoves() == largestMoves) {
						usersMostMoves.add(userId);
					}
					if(moves < 15) {
						histMoves.put(0, histMoves.get(0)+1);
					}
					else if (moves < 30) {
						histMoves.put(15, histMoves.get(15)+1);
					}
					else if (moves < 40) {
						histMoves.put(30, histMoves.get(30)+1);
					}
					else if (moves < 50) {
						histMoves.put(40, histMoves.get(40)+1);
					}
					else if (moves < 60) {
						histMoves.put(50, histMoves.get(50)+1);
					}
					else if (moves < 80) {
						histMoves.put(60, histMoves.get(60)+1);
					}
					else {
						histMoves.put(80, histMoves.get(80)+1);
					}
				}
			}

		} catch (Exception e) {
			System.out.println("Ouch!");
			System.out.println(e);
		}

		averagePointsTotal = calc.calcMean(trackPoints);
		averagePointsWon = calc.calcMean(trackPointsWon);
		averagePointsLoss = calc.calcMean(trackPointsLoss);
		averageMovesTotal = calc.calcMean(trackMoves);
		averageMovesWon = calc.calcMean(trackMovesWon);
		averageMovesLoss = calc.calcMean(trackMovesLoss);

		stdDevPointsTotal = calc.calcStdDev(averagePointsTotal, trackPoints);
		stdDevPointsWon = calc.calcStdDev(averagePointsWon, trackPointsWon);
		stdDevPointsLoss = calc.calcStdDev(averagePointsLoss, trackPointsLoss);
		stdDevMovesTotal = calc.calcStdDev(averageMovesTotal, trackMoves);
		stdDevMovesWon = calc.calcStdDev(averageMovesWon, trackMovesWon);
		stdDevMovesLoss = calc.calcStdDev(averageMovesLoss, trackMovesLoss);

		System.out.println("\n----- Stats for BeFuddled Games -----\n");
		System.out.println("\n-- Game Stats --\n");
		System.out.println("Total Number of Games: " + startedGames);
		System.out.println("Total Number of Completed Games: " + completedGames);
		System.out.println("Total Number of Wins: " + usersWon);
		System.out.println("Total Number of Losses: " + usersLoss);

		System.out.println("\n-- Points --\n");
		System.out.println("Total point average: " + averagePointsTotal);
		System.out.println("Total standard deviation: " + stdDevPointsTotal);
		System.out.println("Win point average: " + averagePointsWon);
		System.out.println("Win standard deviation: " + stdDevPointsWon);
		System.out.println("Loss point average: " + averagePointsLoss);
		System.out.println("Loss standard deviation: " + stdDevPointsLoss);

		System.out.println("\n-- Moves --\n");
		System.out.println("Total moves average: " + averageMovesTotal);
		System.out.println("Total standard deviation " + stdDevMovesTotal);
		System.out.println("Win moves average: " + averageMovesWon);
		System.out.println("Win standard deviation " + stdDevMovesWon);
		System.out.println("Loss moves average: " + averageMovesLoss);
		System.out.println("Loss standard deviation " + stdDevMovesLoss);

		System.out.println("\n-- Move Count Histogram --\n");
		System.out.println("[0, 15): " + histMoves.get(0));
		System.out.println("[15, 30): " + histMoves.get(15));
		System.out.println("[30, 40): " + histMoves.get(30));
		System.out.println("[40, 50): " + histMoves.get(40));
		System.out.println("[50, 60): " + histMoves.get(50));
		System.out.println("[60, 80): " + histMoves.get(60));
		System.out.println("[80, ~): " + histMoves.get(80));

		System.out.println("\n-- Users --\n");
		System.out.print("Total users who started at least one game: " + startedGames);
		System.out.println("Total users who completed at least one game: " + completedGames);
		System.out.print("Largest number of games a user started: " + largestStarted + ": ");
		printUsers(usersMostStarted);
		System.out.print("Largest number of games a user completed: " + largestCompleted + ": ");
		printUsers(usersMostCompleted);
		System.out.print("Largest number of wins a user had: " + largestWins + ": ");
		printUsers(usersMostWins);
		System.out.print("Largest number of losses a user had: " + largestLoss + ": ");
		printUsers(usersMostLoss);
		System.out.print("Longest game a user had: " + largestMoves);
		printUsers(usersMostMoves);

		System.out.println();
	}

	private double calcMean(ArrayList<Integer> values) {
		int total = 0;
		if(values.isEmpty()) {
			return 0;
		}
		for(int i = 0; i < values.size(); i++) {
			total += values.get(i);
		}

		return total / values.size();
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

	private int[] calcTopTenSpots(int[][] board) {
		int[] ten = new int[10];

		return ten;
	}
	private void printUsers(ArrayList<String> list) {
		for(int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i) + " ");
		}
		System.out.println();
	}
}