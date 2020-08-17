/*
* This is a original Nim game 
* The player who does not remove the last stone is the winner.
*/
public class OriginalGame extends NimGame{
		
	//constructor for initializing Nim game
	public OriginalGame(NimPlayer player1, NimPlayer player2, int initialStone, int upperBound) {
		super(player1, player2, initialStone, upperBound);
	}
	
	//how to play game
	public void gameStart() {
		
		int round = 1;//determine whose turn
		int nonStone = 0;//when there is no stone
		int minNumRemove = 1;//the minimum number that a player can remove
		int remainingStone= getInitialStone();
		
		int gameNo1 = getPlayer1().getGameNumber();
		int gameNo2 = getPlayer2().getGameNumber();
		int gameWon1 = getPlayer1().getGameWon();
		int gameWon2 = getPlayer2().getGameWon();
		
		System.out.println();
		System.out.println("Initial stone count: "+ getInitialStone());
		System.out.println("Maximum stone removal: " + getUpperBound());
		System.out.println("Player 1: " + getPlayer1().getGivenName()+" "+getPlayer1().getFamilyName());
		System.out.println("Player 2: " + getPlayer2().getGivenName()+" "+getPlayer2().getFamilyName());
		
		while (remainingStone > nonStone) { //players remove the stone until there is no stone left
			
			System.out.println();
			System.out.print(remainingStone+" stones left:");
			for (int i=0; i < remainingStone; i++) { //print out the remaining stones represented by "*"
				System.out.print(" *");
			}
			
			System.out.println();
			//determine the upper-bound move
			int moveBound = getUpperBound() > remainingStone ? remainingStone : getUpperBound();

			int removeStone=0; //before the game starting, no one remove stones
			
			try {
				
				if (round%2 == 1) {//it is player1's turn to remove stones
					removeStone = getPlayer1().removeStone(moveBound, remainingStone);
				} else if (round%2 == 0) {//it is player2's turn to remove stones
					removeStone = getPlayer2().removeStone(moveBound, remainingStone);
				}
				
				//check valid move
				if(removeStone >= minNumRemove && removeStone <= moveBound) { //move valid
					remainingStone -= removeStone; //stones have been removed
					round++; //the next turn
				} else { //move invalid
					throw new Exception("Invalid move. You must remove between "+ 
							    minNumRemove +" and "+moveBound +" stones.");
				}
				
			} catch (NumberFormatException e) {
				//the exception that the input is not a integer
				System.out.println();
				System.out.println("Invalid move. You must remove between "+ 
						   minNumRemove +" and "+moveBound +" stones.");
			} catch (Exception e) {
				//the exception that the removed stones exceeds the bound or is less than 1
				System.out.println();
				System.out.println(e.getMessage());
			}
		}
		
		//count the game numbers of players
		getPlayer1().setGameNumber(++gameNo1);
		getPlayer2().setGameNumber(++gameNo2);
		
		//there is no stone
		//judge who is the next turn, and he/she is the winner
		System.out.println();
		System.out.println("Game Over");
		switch(round%2) {
			case 1: //player1 wins
				System.out.println(getPlayer1().getGivenName()+" "+getPlayer1().getFamilyName()+" wins!");
				getPlayer1().setGameWon(++gameWon1);
				break;
			case 0: //player2 wins
				System.out.println(getPlayer2().getGivenName()+" "+getPlayer2().getFamilyName()+" wins!");
				getPlayer2().setGameWon(++gameWon2);
				break;
		}
	}
}
