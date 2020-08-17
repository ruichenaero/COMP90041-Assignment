/*
 * This is the Human player class
 */
public class HumanPlayer extends NimPlayer {

	int age;
	
	public HumanPlayer() {
		super();
	}
	
	//Override
	public int removeStone(int bound, int remaining) throws NumberFormatException {
		System.out.println(getGivenName()+"'s turn - remove how many?");
		int removalNum = Integer.parseInt(Nimsys.kbd.nextLine());
		return removalNum; //return the number of stones that the player removes
	}
	
	// advanced move override
	public String advancedMove(boolean[] available, String lastMove) {
		System.out.println(getGivenName()+"'s turn - which to remove?");
		String move = Nimsys.kbd.nextLine();
		return move;
	}
}
