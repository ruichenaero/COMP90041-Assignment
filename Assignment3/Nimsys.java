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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
public class Nimsys {

	//when an element does not exist in an array, its index points to -1
	private static final int NOMATCH = -1;
	//to invoke scanner in multiple methods in this class and outside this class
	public static Scanner kbd = new Scanner(System.in);
	//store players
	private NimPlayer[] playerList = new NimPlayer[0];

	public static void main(String[] args) {
		
		//to invoke methods and attributes in "Nimsys class"
		Nimsys system = new Nimsys();
		System.out.println("Welcome to Nim");
		
		//load the file that records information of existed players
		system.load();
		
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
			try {
				system.methodList(parameter);
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	//load the player's information
	public void load() {
		
		//read data file
		FileInputStream reader = null;
		//store the contents of the file into a String-type variable
		String fileToStr = null;
		
		try {
			
			reader = new FileInputStream("player.dat");
			
			//read file using a byte-type array
			byte[] bytes = new byte[reader.available()];
			reader.read(bytes);
			
			fileToStr = new String(bytes);
			
		} catch (FileNotFoundException e) {
			//if file player.data does not exist
			//create a new file
			File file2 = new File("player.dat");
			try {
				file2.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//close the stream
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//split the text loaded to obtain players' information
		String[][] infoList = splitLoad (fileToStr);
		
		//upload players
		if (infoList != null) {
			for (int i = 0; i < infoList.length; i++) {
				if ("HumanPlayer".equals(infoList[i][0])) { //load information of HumanPlayer
					
					addHumanPlayer(infoList[i][1], infoList[i][2], infoList[i][3]);
					playerList[i].setGameNumber(Integer.parseInt(infoList[i][4]));
					playerList[i].setGameWon(Integer.parseInt(infoList[i][5]));
					
				} else if ("AIPlayer".equals(infoList[i][0])) { //load information of NimAIPlayer
					
					addAIPlayer(infoList[i][1], infoList[i][2], infoList[i][3]);
					playerList[i].setGameNumber(Integer.parseInt(infoList[i][4]));
					playerList[i].setGameWon(Integer.parseInt(infoList[i][5]));
					
				}
			}
		}
	}
	
	//the method that splits the loaded text to obtain information of players
	public String[][] splitLoad (String loadInfo){
		
		if(loadInfo != null) {
			
			int numOfPlayer = 0; //the number of players 
			int numOfInfo = 6; //the number of fields of a player's information
			
			//since information of different players is split by '\n' when archiving data
			//count the number of players by using statistic of '\n'
			for(int i = 0; i < loadInfo.length(); i++) {
				if(loadInfo.charAt(i) == '\n') {
					numOfPlayer++;
				}
			}
			
			//split loaded contents by whitespace('\n')
			String[] whiteSplit = new String[numOfPlayer];
			StringTokenizer tokenizerWhite = new StringTokenizer(loadInfo);
			for(int i = 0; i < numOfPlayer && tokenizerWhite.hasMoreTokens(); i++) {
				whiteSplit[i] = tokenizerWhite.nextToken();
			}
			
			//since the recording item of one player is split by comma when achieving data
			//split each item by comma to obtain information of each player
			String[][] infoList = new String[numOfPlayer][numOfInfo];
			for(int i = 0; i < numOfPlayer; i++) {
				StringTokenizer tokenizerComma = new StringTokenizer(whiteSplit[i],",");
				for(int j=0; j < numOfInfo && tokenizerComma.hasMoreTokens(); j++) {
					infoList[i][j] = tokenizerComma.nextToken();
				}
			}
			return infoList;
		} else {
			return null;
		}
	}
	
	//archive the players' information to disk
	public void archive() {
		
		FileOutputStream writer = null;
		
		try {

			writer = new FileOutputStream("player.dat");
			
			//achieve information of players
			//using ',' to split information of one player
			//using '\n' to split information of different players
			for (int i = 0; i < playerList.length; i++) {
				
				if (playerList[i] instanceof HumanPlayer) {
					//achieve information of HumanPlayer
					String playerType = "HumanPlayer";
					writer.write(playerType.getBytes());
					writer.write(',');
					writer.write(playerList[i].getUserName().getBytes());
					writer.write(',');
					writer.write(playerList[i].getFamilyName().getBytes());
					writer.write(',');
					writer.write(playerList[i].getGivenName().getBytes());
					writer.write(',');
					writer.write(String.valueOf(playerList[i].getGameNumber()).getBytes());
					writer.write(',');
					writer.write(String.valueOf(playerList[i].getGameWon()).getBytes());
					writer.write('\n');
					
				} else if (playerList[i] instanceof NimAIPlayer) {
					//achieve information of AIPlayer
					String playerType = "AIPlayer";
					writer.write(playerType.getBytes());
					writer.write(',');
					writer.write(playerList[i].getUserName().getBytes());
					writer.write(',');
					writer.write(playerList[i].getFamilyName().getBytes());
					writer.write(',');
					writer.write(playerList[i].getGivenName().getBytes());
					writer.write(',');
					writer.write(String.valueOf(playerList[i].getGameNumber()).getBytes());
					writer.write(',');
					writer.write(String.valueOf(playerList[i].getGameWon()).getBytes());
					writer.write('\n');
				}
			}
			
			writer.flush();
	
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//close  FileOutpuStream
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
	public void methodList(String[] parameter) throws IllegalArgumentException{
		
		// get method name and parameters from split command
		String method = parameter[0];
		String para1 = parameter[1];
		String para2 = parameter[2];
		String para3 = parameter[3];
		String para4 = parameter[4];
		
		if ("addplayer".equals(method)) {
			if (para1 != null && para2 != null && para3 != null) {
				addHumanPlayer(para1, para2, para3);
			} else {
				throw new IllegalArgumentException("Incorrect number of arguments supplied to command.");
			}
		} else if ("addaiplayer".equals(method)){
			if (para1 != null && para2 != null && para3 != null) {
				addAIPlayer(para1, para2, para3);
			} else {
				throw new IllegalArgumentException("Incorrect number of arguments supplied to command.");
			}
		} else if("removeplayer".equals(method)) {
			if (para1 == null) {
				removePlayer();
			} else {
				removePlayer(para1);
			}
		} else if ("editplayer".equals(method)) {
			if (para1 != null && para2 != null && para3 != null) {
				editPlayer(para1,para2, para3);
			} else {
				throw new IllegalArgumentException("Incorrect number of arguments supplied to command.");
			}
		} else if ("resetstats".equals(method)) {
			if (para1 == null) {
				resetStats();
			} else {
				resetStats(para1);
			}
		} else if ("displayplayer".equals(method)) {
			if (para1 == null) {
				displayPlayer();
			} else {
				displayPlayer(para1);
			}
		} else if ("startgame".equals(method)) {
			if (para1 != null && para2 != null && para3 != null && para4 != null) {
				int initialStone = Integer.parseInt(para1);
				int upperBound = Integer.parseInt(para2);
				startGame(para3, para4, initialStone, upperBound);
			} else {
				throw new IllegalArgumentException("Incorrect number of arguments supplied to command.");
			}
		} else if ("startadvancedgame".equals(method)) {
			if (para1 != null && para2 != null && para3 != null) {
				int initialStone = Integer.parseInt(para1);
				startAdvancedGame(para2, para3, initialStone);
			} else {
				throw new IllegalArgumentException("Incorrect number of arguments supplied to command.");
			}
		} else if ("rankings".equals(method)) {
			if (para1 == null || para1.equals("desc")) {
				rankingDesc();
			} else if (para1.equals("asc")) {
				rankingAsc();
			}
		} else if ("exit".equals(method)) {
			exit();
		} else {
			throw new IllegalArgumentException("\'" + method + "\'" + " is not a valid command.");
		}
	}
	
	//add new human players into Nimsys
	//assume that the user name, family name, given name input are valid
	public void addHumanPlayer(String username, String familyName, String givenName) {
		
		//search the player existed
		int index = matchPlayer(username);
	
		if(index == NOMATCH) { //if the player doesn't exist
			
			//create a new human player
			NimPlayer newHumanPlayer = new HumanPlayer();
			newHumanPlayer.setUserName(username);
			newHumanPlayer.setFamilyName(familyName);
			newHumanPlayer.setGivenName(givenName);
			
			int newLeng = playerList.length;
			newLeng++;//when add a new player, the length of PlayerList +1
			
			//create a temporary array to duplicate array playerList
			NimPlayer temp[] = new NimPlayer[playerList.length];
			temp = playerList;
			
			//extend the array length to newLeng
			playerList = new NimPlayer[newLeng];
			
			for(int i = 0; i < newLeng-1; i++) {
				playerList[i] = temp[i];
			}
			
			//add the player to playerList
			playerList[newLeng-1] = newHumanPlayer;
			// sort players according to the lexicographical order of the usernames.
			sortPlayer(playerList);
			
		}else {  //the player exist
			System.out.println("The player already exists.");
			return;
		}
	}
	
	// add AIplayer
	// all manipulation in this method are same as method addplayer()
	// excluding the type of players
	public void addAIPlayer(String username, String familyName, String givenName) {
		
		int index = matchPlayer(username);
	
		if(index == NOMATCH) { 
			
			// create a new AIPlayer
			// the method is like method addHumanPlayer
			NimPlayer newAIPlayer = new NimAIPlayer();
			newAIPlayer.setUserName(username);
			newAIPlayer.setFamilyName(familyName);
			newAIPlayer.setGivenName(givenName);
			
			int newLeng = playerList.length;
			newLeng++;
			
			NimPlayer temp[] = new NimPlayer[playerList.length];
			temp = playerList;
			
			playerList = new NimPlayer[newLeng];
			
			for(int i = 0; i < newLeng-1; i++) {
				playerList[i] = temp[i];
			}

			playerList[newLeng-1] = newAIPlayer;
			
			sortPlayer(playerList);
			
		}else { 
			System.out.println("The player already exists.");
			return;
		}
	}
	
	//remove all players
	public void removePlayer() {
		System.out.println("Are you sure you want to remove all players? (y/n)");
		String request = kbd.nextLine();
		
		if(request.equals("y")) {
			playerList = new NimPlayer[0];
		}
		if(request.equals("n")) {
			return;
		}
	}
	
	//remove a selected player
	public void removePlayer(String username) {
		
		//create a temporary array to duplicate array playerList
		NimPlayer temp[] = new NimPlayer[playerList.length];
		temp = playerList;
		
		//search the index of the player to remove in playerList
		int index = matchPlayer(username);
		
		if(index == NOMATCH) { //player does not exist
			System.out.println("The player does not exist.");
			return;
		}else {
			//create a new playerList to store the remaining players 
			playerList = new NimPlayer[playerList.length-1];
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
	
	//display all players sorted on username alphabetically
	public void displayPlayer() {
		
		//since players have been already sorted when adding players
		//print all players' information
		for(NimPlayer player : playerList) {
			System.out.println(player.getUserName()+","
					  +player.getGivenName()+","
					  +player.getFamilyName()+","
					  +player.getGameNumber()+" games,"
					  +player.getGameWon()+" wins");
		}
	}
	
	//display a selected player
	public void displayPlayer(String username) {
		//search the player needed to be displayed
		int index = matchPlayer(username);
		
		if(index == NOMATCH) {
			System.out.println("The player does not exist.");
			return;
		}else {
			System.out.println(playerList[index].getUserName()+","
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
			playerList[index].setUserName(username);
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
	
	//access of starting Nim Original game
	//assume that numbers of initial stones and upper bound of removal stones are valid and correct 
	public void startGame(String username1, String username2, int initialStone, int upperBound ) {
		
		int index1 = matchPlayer(username1);
		int index2 = matchPlayer(username2);
		
		//check if one of players does not exist
		if(index1 == NOMATCH || index2 == NOMATCH) {// one or both players do not exist
			System.out.println("One of the players does not exist.");
		} else {
			
			NimGame newGame = new OriginalGame(playerList[index1], playerList[index2], 
							   initialStone, upperBound);
			newGame.gameStart();
		}
	}
	
	//access of starting Nim advanced game
	public void startAdvancedGame(String username1, String username2, int initialStone ) {
		
		int index1 = matchPlayer(username1);
		int index2 = matchPlayer(username2);
		
		//check if one of players does not exist
		if(index1 == NOMATCH || index2 == NOMATCH) {// one or both players do not exist
			System.out.println("One of the players does not exist.");
		} else {
			
			NimGame newGame = new AdvancedGame(playerList[index1], playerList[index2], initialStone);
			newGame.gameStart();
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
			 gameInfo[i][4] = playerList[i].getUserName();
		}
		
		//sort the array on win ratio in descending order
		//ties is resolved by sorting the array on user name alphabetically
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
			gameInfo[i][4] = playerList[i].getUserName();
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
	
	//sort players on their username alphabetically
	public void sortPlayer (NimPlayer[] unsortedList){
	
		for(int i = 0; i < playerList.length; i++) {
			for(int j = i+1; j < playerList.length; j++) {
				if(playerList[j].getUserName().compareTo(playerList[i].getUserName()) < 0) {
					NimPlayer temp = playerList[i];
					playerList[i] = playerList[j];
					playerList[j] = temp;
				}
			}
		}
	}
	
	//match the player added/input to players in playerList
	//if the player matches, return his/her index of username in playerList 
	public int matchPlayer(String username) {
		
		//index points to the position that stores a player in playerList 
		//initialize it to point no where in the array
		int index = NOMATCH;
		
		//search the position of the player in playerList
		for(int i = 0; i < playerList.length; i++) {
			if(username.equals(playerList[i].getUserName())) {
				index = i;
			}	
		}
		return index;
	}
	
	//exit Nimsys
	public void exit() {
		archive();
		System.out.println();
		System.exit(0);
	}
}
