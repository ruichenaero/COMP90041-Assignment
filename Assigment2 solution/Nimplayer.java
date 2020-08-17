//This is player class
//to record information and actions of a player.
import java.util.Scanner;
public class Nimplayer {
	
	//store the information of players
	private String username;
	private String familyName;
	private String givenName;
	
	private int gameNumber;
	private int gameWon;
	private int winRatio;
	
	//constructor for initializing a player
	public Nimplayer() {
		gameNumber = 0;
		gameWon = 0;
	}

	//get and set 
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	//cannot be set outside this class
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
	
	//action that a player remove stones
	public int removeStone(Scanner kbd) {
			
		System.out.println(givenName+"'s turn - remove how many?");
		int removalNum = kbd.nextInt();
		return removalNum; //return the number of stones that the player removes
			
	}
		

}
