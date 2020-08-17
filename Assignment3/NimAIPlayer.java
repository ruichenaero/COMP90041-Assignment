import java.util.Random;
/*
	NimAIPlayer.java
	
	This class is provided as a skeleton code for the tasks of 
	Sections 2.4, 2.5 and 2.6 in Project C. Add code (do NOT delete any) to it
	to finish the tasks. 
*/
public class NimAIPlayer extends NimPlayer {
	// you may further extend a class or implement an interface
	// to accomplish the tasks.	

	public NimAIPlayer() {
		super();		
	}
	
	//Override
	public int removeStone(int bound, int remaining) {
		
		System.out.println(getGivenName()+"'s turn - remove how many?");
		
		int removalNum = 0;
		
		if((remaining-1)%(bound+1) == 0) { //victory condition cannot be hold
		    Random rand = new Random();
		    removalNum = rand.nextInt(bound) + 1;
		}else {
			
			/*
			 * to guarantee that the rival player is left with k(M+1)+1 stones, k = 0,1,2...
			 * X is the number of removal stone, M is "bound" here
			 * since X>=1 and X<=bound, reaming-X == k(M+1)+1
			 * k can be determined as the following
			 */ 
			double k = Math.floor((remaining-1)/(bound+1));
			removalNum = remaining-(int)k*(bound+1)-1;
		}
		return removalNum; //return the number of stones that the AIplayer removes
	}

	// advanced move override
	public String advancedMove (boolean[] available, String lastMove) {
		
		System.out.println(getGivenName()+"'s turn - which to remove?");
		
		// parameter obtained from available and lastMove
		String move = "0 0";
		String lastMoveInfo[];
		int lastMovePosition; 
		int lastMoveNum;
		int movePosition = 0;
		int moveNumber = 0;
		int length = available.length;
		String[] availableInfo = consistent(available).split(" ");
	
		// check whether the AIPlayer plays first
		boolean playFirst = false;
		for (int i = 0; i < available.length; i++){
			if (available[i]){
				playFirst = true;
			} else {
				playFirst = false;
				break;
			}
		}
		
		if (playFirst) { // AIPlayer plays firstly
			
			// AIPlayer takes the stones in the middle
			// takes 2 stones when the amount of stones is even
			// takes 1 stone when the amount of stones is odd
			moveNumber = 2 - length%2;
			movePosition = length/2 + length%2;
			move = movePosition + " " + moveNumber;
			
		} else { // AIplayer starts round 2 or plays secondly
			
			lastMoveInfo = lastMove.split(" ");
			lastMovePosition = Integer.parseInt(lastMoveInfo[0]);
			lastMoveNum = Integer.parseInt(lastMoveInfo[1]);
			
			// if plays secondly, check whether there are "true" in available[]
			// that are not split by "false".
			// just one condition of guaranteeing victory when playing secondly
			// sorry, cannot figure out it well
			boolean consistent = Boolean.getBoolean(availableInfo[0]);
			
			if (consistent) {
				moveNumber = 2 - Integer.parseInt(availableInfo[1])%2;
				movePosition = Integer.parseInt(availableInfo[2])+1;
			} else {
				// starts round 2
				// AIPlayer take the stones symmetrically with the human player
				moveNumber = lastMoveNum;
				movePosition = length+1-lastMovePosition-lastMoveNum+1;
			}
			
			if (available[movePosition-1] && available[movePosition+moveNumber-2]) {
				move = movePosition + " " + moveNumber;
			} else {
				move = randMove(available);
			}
		}
		
		return move;
	}
	
	public static String consistent (boolean[] available) {
		String str = "false 0 0";
		boolean consistency = false;
		int range = 0;
		int index = -1;
		
		int count = 0;
		for(int i=0; i<available.length-1; i++) {
			if(available[i] != available[i+1]) {
				count += 1;
			}
		}
		
		if (available[0] && (count == 0 || count ==1)) {
			int lo = 0;
			int hi = available.length-1;
			consistency = true;
			for(int i=0; i<available.length; i++) {
				if(!available[i]) {
					hi = i-1;
					break;
				}
			}
			
			range = hi-lo+1;
			index = (lo+hi)/2;
		}
		
		if((!available[0]) && (count == 2 || count == 1)){
			int lo = 0;
			int hi = available.length-1;
			consistency = true;
			for(int i=0; i<available.length; i++) {
				if(available[i]) {
					lo = i;
					break;
				}
			}
			
			for(int j=available.length-1; j>0; j--) {
				if(available[j]) {
					hi = j;
					break;
				}
			}
			
			range = hi-lo+1;
			index = (lo+hi)/2;
		}
		
		str = consistency+" "+range+" "+index;
		
		return str;
	}
	
	public String randMove (boolean[] available) {
		Random rand = new Random();
		int length = available.length;
		int moveNumber = 0;
		int movePosition =0;
		String move = "0 0";
		do {
			moveNumber = rand.nextInt(2)+1;
			movePosition = rand.nextInt(length)+1;
		} while(!(available[movePosition-1] && available[movePosition+moveNumber-2]));
		move = movePosition + " " + moveNumber;
		return move;
	}
}
