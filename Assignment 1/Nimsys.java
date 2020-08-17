/*
 * This is a simple Nim game by person VS person.
 * The player who does not remove the last stone is the winner.
 * @author Rui Chen 
 * student ID :1100500 
 * username: RCCHEN1
 * final version
 */
import java.util.Scanner;
public class Nimsys {
	
	//Constant
	static final int NONSTONE = 0;//when there is no stone
	static final int MINNUMREMOVE = 1;//the minimum number that a player can remove
	
	public static void main(String[] args) {
		
		boolean playAgain; //to determine whether players want to play again
		
		Scanner kbd = new Scanner(System.in);
		
		System.out.println("Welcome to Nim");
		System.out.println();
		
		//create player1 and player2, two instance of NimPlayer class
		//player 1
		System.out.println("Please enter Player 1's name:");
		//a player instance could be created when his/her name is entered
		NimPlayer player1 = new NimPlayer(kbd.nextLine());
		System.out.println();
		
		//player 2
		System.out.println("Please enter Player 2's name:");
		NimPlayer player2 = new NimPlayer(kbd.nextLine());
		
		/**
		 * think about the case when there are 3 players
		 * just increase an instance of the additional player 
		 * and divide "round" by 3 in the "gameStart" method to determine whose turn it is
		 * case "round%3 = 0" is player3's turn
		 * so they can play game successfully
		 */
		
		//the game starts
		do {
			//play the game
			gameStart(kbd, player1, player2);
		
			//determine the value of playAgain
			//true, play the game again
			//false, terminate the game
			playAgain = ifPlayAgain(kbd);
			
		}while(playAgain);
	}
	
	//players make a choice that whether play a game again 
	public static boolean ifPlayAgain(Scanner kbd) {
		
		System.out.println();
		System.out.print("Do you want to play again (Y/N):");
		kbd.nextLine();//throw away  rest of line
		String again = kbd.nextLine();

		if (again.equals("Y")) {
			return true;
		}else {
			System.out.println();
			return false;
		}
	}
	
	//how to play game
	public static void gameStart(Scanner kbd, NimPlayer player1, NimPlayer player2) {
		
		int round = 1;//determine whose turn
		
		//set upper bound of stone removal
		System.out.println();
		System.out.println("Please enter upper bound of stone removal:");
		int upperBound = kbd.nextInt();
		
		//set initial number of stones 
		System.out.println();
		System.out.println("Please enter initial number of stones:");
		int remainingStone=kbd.nextInt();
		
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
			
			if(removeStone >= MINNUMREMOVE && removeStone <= upperBound && removeStone <= remainingStone) {
				remainingStone -= removeStone; //stones have been removed
				round++; //the next turn
			}
		}
		
		//there is no stone
		//judge who is the next turn, and he/she is the winner
		System.out.println();
		System.out.println("Game Over");
		switch(round%2) {
			case 1: //player1 wins
				System.out.println(player1.getPlayer()+" wins!");
				break;
			case 0: //player2 wins
				System.out.println(player2.getPlayer()+" wins!");
		}
	}
}
