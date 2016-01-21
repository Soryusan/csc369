public class User {
	int gamesStarted;
	int gamesCompleted;
	int wins;
	int losses;
	int moves;

	public User() {
		gamesStarted = 0;
		gamesCompleted = 0;
		wins = 0;
		losses = 0;
	}

	public void incrementStart() {
		gamesStarted++;
	}

	public void incrementComplete() {
		gamesCompleted++;
	}

	public void incrementWins() {
		wins++;
	}

	public void incrementLoss() {
		losses++;
	}

	public void setMoves(int moves) {
		this.moves = moves;
	}

	public int getMoves() {
		return moves;
	}

	public int getStarted() {
		return gamesStarted;
	}

	public int getCompleted() {
		return gamesCompleted;
	}

	public int getWins() {
		return wins;
	}

	public int getLoss() {
		return losses;
	}
}