/*
 * This is a advanced Nim game system.
 * This system is used to manages and query information of players.
 * It is also an access to the Nim game.
 * The system is executed by related command.
 * @author Rui Chen 
 * student ID :1100500 
 * username: RCCHEN1
 * final version
 */
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
public class Nimsys {

	//when an element does not exist in an array, its index points to -1
	private static final int NOMATCH = -1;
	//to invoke scanner in multiple methods in this class and outside this class
	public static Scanner kbd = new Scanner(System.in);
	//store players
	private Nimplayer[] playerList = new Nimplayer[0];

	public static void main(String[] args) {
		
		//to invoke methods and attributes in "Nimsys class"
		Nimsys system = new Nimsys();
		System.out.println("Welcome to Nim");
		
		boolean notExit = true;
		
		//Nimsys executes until exit
		while(notExit) {
			System.out.println();
			System.out.print("$");
			
			//read the command imputed here
			String commandText = kbd.nextLine();
			//store parameters split from command
			String parameter[] = system.splitCommand(commandText);
			//execute functions expected
			system.methodList(parameter);
		}	
	}
	
	
	//split the argument command, from which getting method name and parameters
	//assume the argument command input is valid
	public String[] splitCommand(String commandText){
		
		//the number of space in the command
		//the number of words that from command at most
		//the number of parameters received from command at most
		int numSpace = 1, numCommand = 5, numPara = 4;
		
		//for storing split contexts partitioned by space in command
		String[] spaceSplit = new String[numSpace+1];
		//for storing split contexts partitioned by comma in command
		String[] commaSplit = new String[numPara];
		//for storing the split command
		String[] command = new String[numCommand];
		
		//split command by space
		StringTokenizer tokenizerSpace = new StringTokenizer(commandText);
		for(int i = 0; i < 2 && tokenizerSpace.hasMoreTokens(); i++) {
			spaceSplit[i] = tokenizerSpace.nextToken();
		}
		
		//split command by comma
		if(spaceSplit[1]!=null) {
			StringTokenizer tokenizerComma = new StringTokenizer(spaceSplit[1],",");
			for(int i=0; i<4 && tokenizerComma.hasMoreTokens(); i++) {
				commaSplit[i] = tokenizerComma.nextToken();
			}
		}
		
		//store the split command
		command[0] = spaceSplit[0];
		for(int i = 1; i < numCommand; i++) {
			command[i] = commaSplit[i-1];
		}
		
		return command;
	}
	
	//determine which method requested to be executed according the command
	public void methodList(String[] parameter){
		
		//get method name and parameters from split command
		String method = parameter[0];
		String para1 = parameter[1];
		String para2 = parameter[2];
		String para3 = parameter[3];
		String para4 = parameter[4];
		
		switch(method) {
			case "addplayer":
				addPlayer(para1, para2, para3);
				break;
			case "removeplayer":
				if(para1 == null) {
					removePlayer();
				}else {
					removePlayer(para1);
				}
				break;
			case "editplayer":
				editPlayer(para1,para2, para3);
				break;
			case "resetstats":
				if(para1 == null) {
					resetStats();
				}else {
					resetStats(para1);
				}
				break;
			case "displayplayer":
				if(para1 == null) {
					displayPlayer();
				}else {
					displayPlayer(para1);
				}
				break;
			case "startgame":
				int initialStone = Integer.parseInt(para1);
				int upperBound = Integer.parseInt(para2);
				startGame(para3, para4, initialStone, upperBound);
				break;
			case "rankings":
				if(para1 == null || para1.equals("desc")) {
					rankingDesc();
				}else if(para1.equals("asc")) {
					rankingAsc();
				}
				break;
			case "exit":
				exit();
				break;
		}
		
	}
	
	//add new players into Nimsys
	//assume the user name, family name, given name input are valid
	public void addPlayer(String username, String familyName, String givenName) {
		
		//search the player existed
		int index = matchPlayer(username);
	
		
		if(index == NOMATCH) { //if the player doesn't exist
			
			//create a new player
			Nimplayer newPlayer = new Nimplayer();
			newPlayer.setUsername(username);
			newPlayer.setFamilyName(familyName);
			newPlayer.setGivenName(givenName);
			
			int newLeng = playerList.length;
			newLeng++;//when add a new player, the length of PlayerList +1
			
			//create a temporary array to duplicate array playerList
			Nimplayer temp[] = new Nimplayer[playerList.length];
			for(int i = 0; i < playerList.length; i++) {
				temp[i] = playerList[i];
			 }
			
			//extend the array length to newLeng
			playerList = new Nimplayer[newLeng];
			
			for(int j = 0; j < newLeng-1; j++) {
				playerList[j] = temp[j];
			}
			
			//add player to playerList
			playerList[newLeng-1] = newPlayer;
			
		}else {  //the player exist
			System.out.println("The player already exists.");
			return;
		}
	}
	
	//remove all players
	public void removePlayer() {
		System.out.println("Are you sure you want to remove all players? (y/n)");
		String request = kbd.nextLine();
		
		if(request.equals("y")) {
			playerList = new Nimplayer[0];
		}
		if(request.equals("n")) {
			return;
		}
		
	}
	
	//remove a selected player
	public void removePlayer(String username) {
		
		//create a temporary array to duplicate array playerList
		Nimplayer temp[] = new Nimplayer[playerList.length];
		for(int i = 0; i < playerList.length; i++) {
			temp[i] = playerList[i];
 		}
		
		//search the index of the player to remove in playerList
		int index = matchPlayer(username);
		
		if(index == NOMATCH) { //player does not exist
			System.out.println("The player does not exist.");
			return;
		}else {
			//create a new playerList to store the remaining players 
			playerList = new Nimplayer[playerList.length-1];
			for(int j = 0; j < index;j++) {
				//retrieve the players No.0 to No.index-1 
				playerList[j] = temp[j];
			}
			
			for(int j = index; j < playerList.length; j++) {
				//retrieve the players No.index to the last 
				playerList[j] = temp[j+1];
			}
		}
	}
	
	//display all players sorting on username alphabetically
	public void displayPlayer() {
		
		//create a new array to store players' information and their game statistics
		String[][] userInfo = new String[playerList.length][5];
		for(int i = 0; i < playerList.length; i++) {
			userInfo[i][0] = playerList[i].getUsername();
			userInfo[i][1] = playerList[i].getGivenName();
			userInfo[i][2] = playerList[i].getFamilyName();
			userInfo[i][3] = String.valueOf(playerList[i].getGameNumber());
			userInfo[i][4] = String.valueOf(playerList[i].getGameWon());
		}
		
		//sort the array on username alphabetically
		Arrays.sort(userInfo, new Comparator<Object>() {  
			public int compare(Object ObjectA, Object ObjectB) {  
				String[] arTempOne = (String[])ObjectA;  
				String[] arTempTwo = (String[])ObjectB;  
			     
				if(arTempOne[0].compareTo(arTempTwo[0])>0) {
					return 1;
				}else if(arTempOne[0].compareTo(arTempTwo[0])<0) {
					return -1;
				}
				return 0;  
			}  
		});
        
		//print to display
		for(int i=0; i<playerList.length; i++) {
			System.out.println(userInfo[i][0]+","
					  +userInfo[i][1]+","
					  +userInfo[i][2]+","
					  +userInfo[i][3]+" games,"
					  +userInfo[i][4]+" wins");
		}
	}
	
	//displayer a selected player
	public void displayPlayer(String username) {
		//search the player needed to be displayed
		int index = matchPlayer(username);
		
		if(index == NOMATCH) {
			System.out.println("The player does not exist.");
			return;
		}else {
			System.out.println(playerList[index].getUsername()+","
					  +playerList[index].getGivenName()+","
					  +playerList[index].getFamilyName()+","
					  +playerList[index].getGameNumber()+" games,"
					  +playerList[index].getGameWon()+" wins");
		}
	}
	
	//edit a selected player 
	public void editPlayer(String username, String familyName, String givenName) {
		//search the player who need to be edited
		int index = matchPlayer(username);
		
		if(index == NOMATCH) {
			System.out.println("The player does not exist.");
			return;
		}else {
			playerList[index].setUsername(username);
			playerList[index].setFamilyName(familyName);
			playerList[index].setGivenName(givenName);
		}
	}
	
	//reset all player statistics
	public void resetStats() {
		System.out.println("Are you sure you want to reset all player statistics? (y/n)");
		String request = kbd.nextLine();
		
		if(request.equals("y")) {
			for(int i=0; i<playerList.length;i++) {
				playerList[i].setGameNumber(0); 
				playerList[i].setGameWon(0);
			}
		}
		if(request.equals("n")) {
			return;
		}
	}
	
	//reset a certain player's statistics
	public void resetStats(String username) {
		
		int index = matchPlayer(username);
		
		if(index == NOMATCH) {
			System.out.println("The player does not exist.");
			return;
		}else {
			playerList[index].setGameNumber(0);
			playerList[index].setGameWon(0);
		}
	}
	
	//access of starting Nim game
	//assume numbers of initial stones and upper bound of stones removal are valid and correct 
	public void startGame(String username1, String username2, int initialStone, int upperBound ) {
		
		int index1 = matchPlayer(username1);
		int index2 = matchPlayer(username2);
		
		//check if one of players does not exist
		if(index1 == -1 || index2 == -1) { //one or both players do not exist
			System.out.println("One of the players does not exist.");
		}else {
			
			NimGame newGame = new NimGame(playerList[index1], playerList[index2], 
								   initialStone, upperBound);
			newGame.gameStart(kbd);
		}
		
	}
	
	//rank all players by the win ratio in descending order
	//also is a rank in default order
	public void rankingDesc() {  
		
		//the number of fields to be displayed when ranking all players
		//"rankings" displays 10 players at most
		int numField = 5, mostPlayerRanked = 10;
		
		//create a array to store information to be display
		String[][] gameInfo = new String[playerList.length][numField];
		for(int i=0; i<gameInfo.length; i++) {
			 gameInfo[i][0] = String.valueOf(playerList[i].getWinRatio());
			 gameInfo[i][1] = String.valueOf(playerList[i].getGameNumber());
			 gameInfo[i][2] = playerList[i].getGivenName();
			 gameInfo[i][3] = playerList[i].getFamilyName();
			 gameInfo[i][4] = playerList[i].getUsername();
		}
		
		//sort the array on win ratio in descending order
		//ties is resolved by sorting the array on username alphabetically
		Arrays.sort(gameInfo, new Comparator<Object>() {  
			public int compare(Object ObjectA, Object ObjectB) {  
				String[] arTempOne = (String[])ObjectA;  
				String[] arTempTwo = (String[])ObjectB;  
				
				//sort on win ratio first
				if(Integer.parseInt(arTempOne[0])-Integer.parseInt(arTempTwo[0])>0) {
				    return -1;  
				}else if(Integer.parseInt(arTempOne[0])-Integer.parseInt(arTempTwo[0])<0) {  
				    return 1;  
				}else {  
					//if it is ties, then order by username alphabetically
					if(arTempOne[4].compareTo(arTempTwo[4])>0) {
						return 1;
					}else if(arTempOne[4].compareTo(arTempTwo[4])<0) {
						return -1;
					}
				}  
				return 0; 
			}  
		}); 
        
		//display ranking result
		//show information of 10 players at most
		for(int i = 0; i < gameInfo.length && i < mostPlayerRanked; i++) {
			if(gameInfo[i][0].length() == 3) {
				System.out.print(gameInfo[i][0]+"% |");
			}else if(gameInfo[i][0].length() == 2) {
				System.out.print(gameInfo[i][0]+"%  |");	
			}else if(gameInfo[i][0].length() == 1) {
				System.out.print(gameInfo[i][0]+"%   |");
			}
				
			if(gameInfo[i][1].length() == 1) {
				System.out.print(" 0"+gameInfo[i][1]+" games |");
			}else if(gameInfo[i][1].length() == 2) {
				System.out.print(" "+gameInfo[i][1]+" games |");
			}
			
			for(int j = 2; j < 4; j++) {
				System.out.print(" "+gameInfo[i][j]);
			}
			System.out.println();
		}
	}  
	
	//rank all players in ascending order
	public void rankingAsc() {
		
		//the number of fields to be displayed when ranking all players
		//"rankings" displays 10 players at most
		int numField = 5, mostPlayerRanked = 10;
		
		String[][] gameInfo = new String[playerList.length][numField];
		
		for(int i = 0; i < gameInfo.length; i++) {
			 gameInfo[i][0] = String.valueOf(playerList[i].getWinRatio());
			 gameInfo[i][1] = String.valueOf(playerList[i].getGameNumber());
			 gameInfo[i][2] = playerList[i].getGivenName();
			 gameInfo[i][3] = playerList[i].getFamilyName();
			 gameInfo[i][4] = playerList[i].getUsername();
		}
		
		//sort the array on win ratio in ascending order
		Arrays.sort(gameInfo, new Comparator<Object>() {  
			public int compare(Object ObjectA, Object ObjectB) {  
				String[] arTempOne = (String[])ObjectA;  
				String[] arTempTwo = (String[])ObjectB;  
				
				if(Integer.parseInt(arTempOne[0])-Integer.parseInt(arTempTwo[0])>0) {
				    return 1;  
				}else if(Integer.parseInt(arTempOne[0])-Integer.parseInt(arTempTwo[0])<0) {  
				    return -1;  
				}else {  
					if(arTempOne[4].compareTo(arTempTwo[4])>0) {
						return 1;
					}else if(arTempOne[4].compareTo(arTempTwo[4])<0) {
						return -1;
					}
				}  
				return 0;  
			}  
		}); 
        
		//display ranking result
		//show information of 10 players at most
		for(int i = 0; i < gameInfo.length && i < mostPlayerRanked; i++) {
			if(gameInfo[i][0].length() == 3) {
				System.out.print(gameInfo[i][0]+"% |");
			}else if(gameInfo[i][0].length() == 2) {
				System.out.print(gameInfo[i][0]+"%  |");	
			}else if(gameInfo[i][0].length() == 1) {
				System.out.print(gameInfo[i][0]+"%   |");
			}
				
			if(gameInfo[i][1].length() == 1) {
				System.out.print(" 0"+gameInfo[i][1]+" games |");
			}else if(gameInfo[i][1].length() == 2) {
				System.out.print(" "+gameInfo[i][1]+" games |");
			}
			
			for(int j = 2; j < 4; j++) {
				System.out.print(" "+gameInfo[i][j]);
			}
			System.out.println();
		}
	}
	
	//match the player input to players existed
	//if the player matches, return his/her index of username in playerList 
	//or return -1
	public int matchPlayer(String username) {
		
		//point to the position in playerList that stores a player
		//initialize it to point no where in the array
		int index = NOMATCH;
		
		//search the position of the player in playerList
		for(int i = 0; i < playerList.length; i++) {
			if(username.equals(playerList[i].getUsername())) {
				index = i;
			}	
		}
		
		return index;
	}
	
	//exit Nimsys
	public void exit() {
		System.out.println();
		System.exit(0);
	}
}
