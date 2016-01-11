public class GameInfo {
	int points;
	int actionCount;
	boolean[] specialMoves;
	String user;

	public GameInfo(String user) {
		points = 0;
		actionCount = 1;
		specialMoves = new boolean[4];
		this.user = user;
	}

	public int addPoints(int value) {
		return (points += value);
	}

	public int getPoints() {
		return points;
	}

	public int incrementCount() {
		return ++actionCount;
	}

	public int getCount() {
		return actionCount;
	}

	public void useSpecialMove(int move) {
		specialMoves[move] = true;
	}

	public boolean checkSpecialMove(int move) {
		return specialMoves[move];
	}

	public String getUser() {
		return user;
	}
}