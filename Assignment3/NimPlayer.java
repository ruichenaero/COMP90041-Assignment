//This is player class
//to record information and actions of a player.
public abstract class NimPlayer implements Testable{
	
	//store the information of players
	private String userName;
	private String familyName;
	private String givenName;
	
	private int gameNumber;
	private int gameWon;
	private int winRatio;
	
	//constructor for initializing a player
	public NimPlayer() {
		gameNumber = 0;
		gameWon = 0;
	}

	//get and set 
	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public int getGameNumber() {
		return gameNumber;
	}

	public void setGameNumber(int gameNumber) {
		this.gameNumber = gameNumber;
	}

	public int getGameWon() {
		return gameWon;
	}

	public void setGameWon(int gameWon) {
		this.gameWon = gameWon;
	}
	
	//the ratio of win
	//which is determined by numbers of games and wins
	public int getWinRatio( ) {
		
		if(gameNumber == 0) {
			return 0;
		}else {
			
			//round the winRatio to the nearest integer
			int tempWinRatio = 0;
			double refRatio = 0.0;
			
			tempWinRatio = 100*gameWon/gameNumber;
			refRatio = 100*(double)gameWon/(double)gameNumber;
		
			if(refRatio-tempWinRatio >= 0.5) {
				winRatio = tempWinRatio+1;
				return winRatio;
			}else {
				winRatio = tempWinRatio;
				return winRatio;
			}
		}
	}
	
	public void setWinRatio(int winRatio) {
		this.winRatio = winRatio;
	}
	
	//action that a player remove stones
	public abstract int removeStone(int bound, int reamining) throws Exception; 
}
