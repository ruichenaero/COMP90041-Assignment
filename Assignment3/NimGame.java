/*
* This is a Nim game 
*/
public abstract class NimGame {
	
	//instance variable
	private int initialStone;
	private int upperBound;
	
	private NimPlayer player1;
	private NimPlayer player2;
	
	//constructor for initializing Nim game
	public NimGame(NimPlayer player1, NimPlayer player2, int initialStone, int upperBound) {
		this.player1 = player1;
		this.player2 = player2;
		this.initialStone = initialStone;
		this.upperBound = upperBound;
	}
	
	public NimGame(NimPlayer player1, NimPlayer player2, int initialStone) {
		this.player1 = player1;
		this.player2 = player2;
		this.initialStone = initialStone;
		this.upperBound = 2;
	}
	
	public int getInitialStone() {
		return initialStone;
	}

	public void setInitialStone(int initialStone) {
		this.initialStone = initialStone;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

	public NimPlayer getPlayer1() {
		return player1;
	}

	public void setPlayer1(NimPlayer player1) {
		this.player1 = player1;
	}

	public NimPlayer getPlayer2() {
		return player2;
	}

	public void setPlayer2(NimPlayer player2) {
		this.player2 = player2;
	}

	//how to play game
	public abstract void gameStart();
}
