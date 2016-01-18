import org.json.JSONObject;
import org.json.*;
import java.util.*;
import java.io.*;
import java.awt.*;

public class beFuddledStats {

	public static void main(String[] args){

		beFuddledStats calc = new beFuddledStats();
		String filename;
		Scanner scanner = new Scanner(System.in);
		JSONObject current;
		JSONObject action;
		JSONObject jObj = new JSONObject();
		JSONObject temp;
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
		Point topTenSpaces[];

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

		PrintWriter writer = null;
		if(args.length >= 1) {
			try {
				writer = new PrintWriter(new FileWriter(new File(args[0])));
			}
			catch (IOException e) {
				System.out.println("could not open file");
			}
		}

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
					if(sMove.equals("Shuffle")) {
						specialMoves[0]++;
					}
					else if(sMove.equals("Clear")) {
						specialMoves[1]++;
					}
					else if(sMove.equals("Invert")) {
						specialMoves[2]++;
					}
					else if(sMove.equals("Rotate")) {
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

		topTenSpaces = calc.calcTopTenSpots(board);

		try {
			System.out.println("\n----- Stats for BeFuddled Games -----\n");
			System.out.println("\n-- Game Stats --\n");
			System.out.println("Total Number of Games: " + startedGames);
			System.out.println("Total Number of Completed Games: " + completedGames);
			System.out.println("Total Number of Wins: " + usersWon);
			System.out.println("Total Number of Losses: " + usersLoss);
			temp = new JSONObject();
			temp.put("Total Games", startedGames);
			temp.put("Total Completed Games", completedGames);
			temp.put("Total Wins", usersWon);
			temp.put("Total Losses", usersLoss);
			jObj.put("Game Stats", temp);

			System.out.println("\n-- Points --\n");
			System.out.println("Total point average: " + averagePointsTotal);
			System.out.println("Total point standard deviation: " + stdDevPointsTotal);
			System.out.println("Win point average: " + averagePointsWon);
			System.out.println("Win point standard deviation: " + stdDevPointsWon);
			System.out.println("Loss point average: " + averagePointsLoss);
			System.out.println("Loss point standard deviation: " + stdDevPointsLoss);
			temp = new JSONObject();
			temp.put("Total point average", averagePointsTotal);
			temp.put("Total point standard deviation", stdDevPointsTotal);
			temp.put("Win point average", averagePointsWon);
			temp.put("Win point standard deviation", stdDevPointsWon);
			temp.put("Loss point average", averagePointsLoss);
			temp.put("Loss point standard deviation", stdDevPointsLoss);
			jObj.put("Points", temp);

			System.out.println("\n-- Moves --\n");
			System.out.println("Total moves average: " + averageMovesTotal);
			System.out.println("Total moves standard deviation " + stdDevMovesTotal);
			System.out.println("Win moves average: " + averageMovesWon);
			System.out.println("Win moves standard deviation " + stdDevMovesWon);
			System.out.println("Loss moves average: " + averageMovesLoss);
			System.out.println("Loss moves standard deviation " + stdDevMovesLoss);
			temp = new JSONObject();
			temp.put("Total moves average", averageMovesTotal);
			temp.put("Total moves standard deviation", stdDevMovesTotal);
			temp.put("Win move average", averageMovesWon);
			temp.put("Win moves standard deviation", stdDevMovesWon);
			temp.put("Loss moves average", averageMovesLoss);
			temp.put("Loss moves standard deviation", stdDevMovesLoss);
			jObj.put("Moves", temp);

			System.out.println("\n-- Move Count Histogram --\n");
			System.out.println("[0, 15): " + histMoves.get(0));
			System.out.println("[15, 30): " + histMoves.get(15));
			System.out.println("[30, 40): " + histMoves.get(30));
			System.out.println("[40, 50): " + histMoves.get(40));
			System.out.println("[50, 60): " + histMoves.get(50));
			System.out.println("[60, 80): " + histMoves.get(60));
			System.out.println("[80, ~): " + histMoves.get(80));
			temp = new JSONObject();
			temp.put("[0. 15)", histMoves.get(0));
			temp.put("[15, 30)", histMoves.get(15));
			temp.put("[30, 40)", histMoves.get(30));
			temp.put("[40, 50)", histMoves.get(40));
			temp.put("[50, 60)", histMoves.get(50));
			temp.put("[60, 80)", histMoves.get(60));
			temp.put("[80, ~)", histMoves.get(80));
			jObj.put("Move Count Histogram", temp);

			System.out.println("\n-- Users --\n");
			System.out.print("Total users who started at least one game: " + startedGames);
			System.out.println("Total users who completed at least one game: " + completedGames);
			System.out.print("Largest number of games a user started: " + largestStarted + ": ");
			calc.printUsers(usersMostStarted);
			System.out.print("Largest number of games a user completed: " + largestCompleted + ": ");
			calc.printUsers(usersMostCompleted);
			System.out.print("Largest number of wins a user had: " + largestWins + ": ");
			calc.printUsers(usersMostWins);
			System.out.print("Largest number of losses a user had: " + largestLoss + ": ");
			calc.printUsers(usersMostLoss);
			System.out.print("Longest game a user had: " + largestMoves + ": ");
			calc.printUsers(usersMostMoves);
			temp = new JSONObject();
			temp.put("Total users started at least one game", startedGames);
			temp.put("Total users completed at least one game", completedGames);
			temp.put("Largest number of games a user started", largestStarted);
			temp.put("Largest number of games a user completed", largestCompleted);
			temp.put("Largest number of wins a user had", largestWins);
			temp.put("Largest number of losses a user had", largestLoss);
			temp.put("Longest game a user had", largestMoves);
			jObj.put("Users", temp);

			System.out.println("\n-- Most Popular Board Positions --\n");
			for(int i = 0; i < 9; i++) {
				System.out.print("(" + topTenSpaces[i].getX() + ", " + topTenSpaces[i].getY() + "), ");
			}
			System.out.println("(" + topTenSpaces[9].getX() + ", " + topTenSpaces[9].getY() + ")");

			System.out.println("\n-- Special Moves --\n");
			System.out.println("Shuffle: " + specialMoves[0]);
			System.out.println("Clear: " + specialMoves[1]);
			System.out.println("Invert: " + specialMoves[2]);
			System.out.println("Rotate: " + specialMoves[3]);
			temp = new JSONObject();
			temp.put("Shuffle", specialMoves[0]);
			temp.put("Clear", specialMoves[1]);
			temp.put("Invert", specialMoves[2]);
			temp.put("Rotate", specialMoves[3]);
			jObj.put("Special Moves", temp);

			System.out.println();

			if(writer != null) {
				writer.write(jObj.toString(3));
				writer.close();
			}
		}
		catch (JSONException e) {
			System.out.println("bad");
		}
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

	private Point[] calcTopTenSpots(int[][] board) {
		int[] ten = new int[10];
		Point[] coords = new Point[10];
		int min = board[0][0];
		int minNdx = 0;
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				if(j < 10 && i == 0) {
					ten[j] = board[i][j];
					coords[j] = new Point(i, j);
					if(board[i][j] < min) {
						min = board[i][j];
						minNdx = j;
					}
				}
				else {
					if(board[i][j] > min) {
						ten[minNdx] = board[i][j];
						coords[minNdx] = new Point(i, j);
						min = ten[0];
						minNdx = 0;
						for(int k = 1; k < ten.length; k++) {
							if(ten[k] < min) {
								min = ten[k];
								minNdx = k;
							}
						}
					}
				}
			}
		}
		return coords;
	}
	private void printUsers(ArrayList<String> list) {
		for(int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i) + " ");
		}
		System.out.println();
	}
}