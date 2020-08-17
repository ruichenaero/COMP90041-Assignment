/*
 * This is an advanced game
 */
public class AdvancedGame extends NimGame {

	public AdvancedGame(NimPlayer player1, NimPlayer player2, int initialStone) {
		super(player1, player2,initialStone);
	}

	//Override
	public void gameStart() {
		
		int round = 1;
		int nonStone = 0;
		int minNumRemove = 1;
		int remainingStone= getInitialStone();
		String lastMove = "";
		
		char[] stoneList = new char[getInitialStone()];
		boolean[] available = new boolean[getInitialStone()];
		for(int i=0; i<remainingStone; i++ ) {
			stoneList[i] = '*';
			available[i] = true;
		}
		
		int gameNo1 = getPlayer1().getGameNumber();
		int gameNo2 = getPlayer2().getGameNumber();
		int gameWon1 = getPlayer1().getGameWon();
		int gameWon2 = getPlayer2().getGameWon();
		
		System.out.println();
		System.out.println("Initial stone count: "+ getInitialStone());
		System.out.print("Stones display:");
		for(int i=0; i<stoneList.length; i++) {
			System.out.print(" <"+(i+1)+","+stoneList[i]+">");
		}
		System.out.println();
		System.out.println("Player 1: " + getPlayer1().getGivenName()+" "+getPlayer1().getFamilyName());
		System.out.println("Player 2: " + getPlayer2().getGivenName()+" "+getPlayer2().getFamilyName());
		
		while(remainingStone > nonStone) { 
			
			System.out.println();
			System.out.print(remainingStone+" stones left:");
			for(int i=0; i<stoneList.length; i++) {
				System.out.print(" <"+(i+1)+","+stoneList[i]+">");
			}
				
			System.out.println();
		
			int moveBound = getUpperBound() > remainingStone ? remainingStone : getUpperBound();
			
			String removeStone = "0 0";
			try {
				
				switch(round%2) {
					case 1: //it is player1's turn to remove stones
				
						removeStone = getPlayer1().advancedMove(available, lastMove);
				
						break;
							
					case 0: //it is player2's turn to remove stones
					
						removeStone = getPlayer2().advancedMove(available, lastMove);
					
						break;
						
				}
				
				//obtain the number and the position of stones removal
				String[] moveInfo = removeStone.split(" ");
				int removePosition = Integer.parseInt(moveInfo[0]);
				int removeNumber = Integer.parseInt(moveInfo[1]);
				
				//check valid move
				if(removeNumber >= minNumRemove && removeNumber <= moveBound 
				   && removePosition >= 1 && removePosition <= getInitialStone()
				   && available[removePosition-1]
				   && available[removePosition-1+removeNumber-1]) {
					
					lastMove = removeStone;
					available[removePosition-1] = false;
					available[removePosition-1+removeNumber-1] = false;
					stoneList[removePosition-1] = 'x';
					stoneList[removePosition-1+removeNumber-1] = 'x';
					remainingStone -= removeNumber; //stones have been removed
					round++; //the next turn
				
				}else { //move invalid
					throw new Exception("Invalid move.");
				}
				
			} catch (NumberFormatException e) {
				System.out.println();
				System.out.println("Invalid move.");
				
			} catch (Exception e) {
				System.out.println();
				System.out.println(e.getMessage());
			}
		}
		
		//count the game numbers of players
		getPlayer1().setGameNumber(++gameNo1);
		getPlayer2().setGameNumber(++gameNo2);
		
		//there is no stone
		//judge who wins the game
		System.out.println();
		System.out.println("Game Over");
		switch(round%2) {
			case 0: //player1 wins
				System.out.println(getPlayer1().getGivenName()+" "+getPlayer1().getFamilyName()+" wins!");
				getPlayer1().setGameWon(++gameWon1);
				break;
			case 1: //player2 wins
				System.out.println(getPlayer2().getGivenName()+" "+getPlayer2().getFamilyName()+" wins!");
				getPlayer2().setGameWon(++gameWon2);
				break;
		}
	}
}
