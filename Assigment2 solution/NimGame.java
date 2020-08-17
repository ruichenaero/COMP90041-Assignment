/*
* This is a Nim game by person VS person.
* The player who does not remove the last stone is the winner.
*/
import java.util.Scanner;
public class NimGame {

	//Constants
	
	//when there is no stone
	private static final int NONSTONE = 0;
	//the minimum number that a player can remove
	private static final int MINNUMREMOVE = 1;
	
	//instance variable
	private int initialStone;
	private int upperBound;
	
	private Nimplayer player1;
	private Nimplayer player2;
	
	//constructor for initializing Nim game
	public NimGame(Nimplayer player1, Nimplayer player2, int initialStone, int upperBound) {
		this.player1 = player1;
		this.player2 = player2;
		this.initialStone = initialStone;
		this.upperBound = upperBound;
	}
	
	//instance method
	//how to play game
	public void gameStart(Scanner kbd) {
		
		int round = 1;//determine whose turn
		int remainingStone= initialStone;
		
		int gameNo1 = player1.getGameNumber();
		int gameNo2 = player2.getGameNumber();
		int gameWon1 = player1.getGameWon();
		int gameWon2 = player2.getGameWon();
		
		System.out.println();
		System.out.println("Initial stone count: "+ initialStone);
		System.out.println("Maximum stone removal: " + upperBound);
		System.out.println("Player 1: " + player1.getGivenName()+" "+player1.getFamilyName());
		System.out.println("Player 2: " + player2.getGivenName()+" "+player2.getFamilyName());
		
		while(remainingStone > NONSTONE) { //players remove the stone until there is no stone left
			
			System.out.println();
			System.out.print(remainingStone+" stones left:");
			for(int i=0; i < remainingStone; i++) { //print out the remaining stones represented by "*"
				System.out.print(" *");
				
			}
			
			System.out.println();
			int removeStone=0; //before the game starting, no one remove stones
			switch(round%2) {
				case 1: //it is player1's turn to remove stones
					removeStone = player1.removeStone(kbd);
					break;
					
				case 0: //it is player2's turn to remove stones
					removeStone = player2.removeStone(kbd);
					break;
			}
			
			//determine the upper-bound move
			int moveBound = upperBound > remainingStone ? remainingStone : upperBound;
			
			//check valid move
			if(removeStone >= MINNUMREMOVE && removeStone <= moveBound) { //move valid
				remainingStone -= removeStone; //stones have been removed
				round++; //the next turn
			}else { //move invalid
				System.out.println();
				System.out.println("Invalid move. You must remove between "+ MINNUMREMOVE +" and "
											  + moveBound +" stones.");
			}

		}
		
		//count the game numbers of players
		player1.setGameNumber(++gameNo1);
		player2.setGameNumber(++gameNo2);
		
		//there is no stone
		//judge who is the next turn, and he/she is the winner
		System.out.println();
		System.out.println("Game Over");
		switch(round%2) {
			case 1: //player1 wins
				System.out.println(player1.getGivenName()+" "+player1.getFamilyName()+" wins!");
				player1.setGameWon(++gameWon1);
				break;
			case 0: //player2 wins
				System.out.println(player2.getGivenName()+" "+player2.getFamilyName()+" wins!");
				player2.setGameWon(++gameWon2);
				break;
		}
		
		kbd.nextLine(); //throw away next line
	}
}
