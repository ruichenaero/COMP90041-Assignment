//This is player class
//to record information and behavior of a player
import java.util.Scanner;
public class NimPlayer {
	
	//records name of player
	private String player;
	
	//constructor method
	//to ensure that the player instance is created when enter his/her name
	public NimPlayer(String player) {
		this.player=player;
	}
	
	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	//records the behavior that a player remove stones
	public int removeStone(Scanner kbd) {
		
		System.out.println(player+"'s turn - remove how many?");
		int removalNum = kbd.nextInt();
		return removalNum; //return the number of stones that the player removes
	}
}
