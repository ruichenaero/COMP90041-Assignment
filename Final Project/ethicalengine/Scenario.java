package ethicalengine;
/**
 * This is the Scenario Class
 * @author Rui Chen
 *
 */
public class Scenario {
	
	private Character[] passengers;
	private Character[] pedestrians;
	private boolean isLegalCrossing;
	private boolean hasYouInCar;
	private boolean hasYouInLane;
	private int passengerCount;
	private int pedestrianCount;
	
	/*
	public Scenario() {
		
	}
	*/
	
	/**
	 * generate a new scenario
	 * 
	 * @param passengers passengers in the scenario
	 * @param pedestrians pedestrians in the scenario
	 * @param isLegalCrossing whether the pedestrians across lane legally
	 */
	public Scenario(Character[] passengers, Character[] pedestrians, boolean isLegalCrossing) {
	
		this.passengers = passengers;
		this.pedestrians = pedestrians;
		this.isLegalCrossing = isLegalCrossing;
		this.passengerCount = getPassengerCount();
		this.pedestrianCount = getPedestrianCount();
		this.hasYouInCar = hasYouInCar();
		this.hasYouInLane = hasYouInLane();
		
	}
	

	public Character[] getPassengers() {
		return passengers;
	}

	public void setPassengers(Character[] passengers) {
		this.passengers = passengers;
	}

	public Character[] getPedestrians() {
		return pedestrians;
	}

	public void setPedestrians(Character[] pedstrians) {
		this.pedestrians = pedstrians;
	}

	public boolean isLegalCrossing() {
		return isLegalCrossing;
	}

	public void setLegalCrossing(boolean isLegalCrossing) {
		this.isLegalCrossing = isLegalCrossing;
	}

	public boolean hasYouInCar() {
		for(int i = 0; i < passengers.length; i++ ) {
			if(passengers[i] instanceof Person) {
				Person tempPerson = (Person) passengers[i];
				if (tempPerson.isYou()) {
					hasYouInCar = true;
					break;
				}
				//hasYouInCar = tempPerson.isYou();
			}
		}
		return hasYouInCar;
	}

	public boolean hasYouInLane() {
		hasYouInLane = false;
		for(int i = 0; i < pedestrians.length; i++ ) {
			if(pedestrians[i] instanceof Person) {
				Person tempPerson = (Person) pedestrians[i];
				if (tempPerson.isYou()) {
					hasYouInLane = true;
					break;
				}
				//hasYouInLane = tempPerson.isYou();
			}
		}
		return hasYouInLane;
	}

	public int getPassengerCount() {
		if (passengers != null) {
			passengerCount  = passengers.length;
		}
		return passengerCount;
	}

	public void setPassengerCount(int passengerCount) {
		this.passengerCount = passengerCount;
	}

	public int getPedestrianCount() {
		if (pedestrians != null) {
			pedestrianCount = pedestrians.length;
		}
		return pedestrianCount;
	}

	public void setPedestrianCount(int pedestrianCount) {
		this.pedestrianCount = pedestrianCount;
	}
	
	public String toString () {
		
		String legalCrossing = this.isLegalCrossing? "yes" : "no";
		StringBuffer buf=new StringBuffer();
		buf.append("======================================\n"+
			   "# Scenario\n"+
			   "======================================\n"+
			   "Legal Crossing: "+legalCrossing+"\n");
		
		buf.append("Passengers ("+ getPassengerCount() +")\n");
		if(passengers.length != 0) {
			for(Character character : passengers) {
				buf.append("- "+character.toString()+"\n");
			}
		}
		
		buf.append("Pedestrians ("+getPedestrianCount()+")");
		if (pedestrians.length != 0) {
			for(Character character : pedestrians) {
				buf.append("\n- "+character.toString());
			}
		}
						

		String scenarioInfo = buf.toString();
		return scenarioInfo;
	}
	
}
