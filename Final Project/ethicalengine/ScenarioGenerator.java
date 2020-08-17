package ethicalengine;
/**
 * This is a scenario generator
 * To generate scenario randomly
 * @author Rui Chen
 */
import java.util.Random;
import ethicalengine.Character.*;
import ethicalengine.Person.*;
import ethicalengine.Animal.*;

public class ScenarioGenerator {
	
	private Character[] passengers = new Character[0];
	private Character[] pedestrians = new Character[0];
	private int passengerCountMin = 1;
	private int passengerCountMax = 5;
	private int pedestrianCountMin = 1;
	private int pedestrianCountMax = 5;
	private Random randomIndex = new Random();
	private boolean isYouExist;
	private Scenario scenario;
	
	public ScenarioGenerator () {
	
	}
	
	public ScenarioGenerator (long seed) {
		randomIndex.setSeed(seed);
	}

	/**
	 * generate a set of scenarios randomly
	 * 
	 * @param seed for genereating a random value
	 * @param passengerCountMinimum minimum amount of passenger
	 * @param passengerCountMaximum maximum amount of passenger
	 * @param pedestrianCountMinimum minimum amount of passenger
	 * @param pedestrianCountMaximum maximum amount of passenger
	 */
	public ScenarioGenerator (long seed, int passengerCountMinimum, int passengerCountMaximum,
				  int pedestrianCountMinimum, int pedestrianCountMaximum) {
		randomIndex.setSeed(seed);
		if (passengerCountMaximum >= passengerCountMinimum) {
			this.passengerCountMax = passengerCountMaximum;
			this.passengerCountMin = passengerCountMinimum;
		} else {
			System.out.println("passengerCountMaximum < passengerCountMinimum");
		}
		
		if (pedestrianCountMaximum >= pedestrianCountMinimum) {
			this.pedestrianCountMax = pedestrianCountMaximum;
			this.pedestrianCountMin = pedestrianCountMinimum;
		} else {
			System.out.println(pedestrianCountMaximum < pedestrianCountMinimum);
		}
	}
	
	public void setPassengerCountMin (int min) {
		passengerCountMin = min;
	}
	
	public void setPassengerCountMax (int max) {
		passengerCountMax = max;
	}
	
	public void setPedestrianCountMin (int min) {
		pedestrianCountMin = min;
	}
	
	public void setPedestrianCountMax (int max) {
		pedestrianCountMax = max;
	}
	
	public Person getRandomPerson () { // generate a random person
		
		int randomAge = randomIndex.nextInt(100);
		Profession randomProfession = Profession.values()[randomIndex.nextInt(Profession.values().length)];
		Gender randomGender = Gender.values()[randomIndex.nextInt(Gender.values().length)];
		BodyType randomBodyType = BodyType.values()[randomIndex.nextInt(BodyType.values().length)];
		boolean randomPregnant = randomIndex.nextBoolean();
		
		Person randomPerson = new Person(randomAge, randomProfession, randomGender, 
										 randomBodyType, randomPregnant);
		return randomPerson;
	}
	
	public Animal getRandomAnimal() { // generate a random animal
		String randomSpiece = 
		AnimalSpecie.values()[randomIndex.nextInt(AnimalSpecie.values().length)].toString().toLowerCase();
		
		Animal randomAnimal = new Animal(randomSpiece);
		
		randomAnimal.setPet(randomIndex.nextBoolean());
		return randomAnimal;
	}
	
	public Scenario generate() {
		boolean randomLegalCross = randomIndex.nextBoolean();
		setPassenger();
		setPedestrians();
		scenario = new Scenario(passengers, pedestrians, randomLegalCross);
		return scenario;
	}
	
	private void setPassenger () { // generate a passenger list
		int passengerCount = passengerCountMin + 
				     randomIndex.nextInt(passengerCountMax-passengerCountMin+1);
		int personCount = randomIndex.nextInt(passengerCount+1);
		passengers = new Character[passengerCount];
		for (int i = 0; i < personCount; i++) {
			passengers[i] = getRandomPerson();
		}
		
		if (personCount > 0 && isYouExist == false) { // generate a "you"
			
			int potentialUser = randomIndex.nextInt(personCount);
			boolean isYou = randomIndex.nextBoolean();
			Character c = passengers[potentialUser];
			Person tempPerson = (Person) c;
			tempPerson.setAsYou(isYou);
			passengers[potentialUser] = tempPerson;

			this.isYouExist = isYou;
		}
	
		for (int i = personCount; i < passengerCount; i++) {
			passengers[i] = getRandomAnimal();
		}
	}
	
	private void setPedestrians () { // generate a pedestrian list
		
		int pedestrianCount = pedestrianCountMin + 
				      randomIndex.nextInt(pedestrianCountMax-pedestrianCountMin+1);
		
		int personCount = randomIndex.nextInt(pedestrianCount+1);
		pedestrians = new Character[pedestrianCount];
		for (int i = 0; i < personCount; i++) {
			pedestrians[i] = getRandomPerson();
		}
		
		if (personCount > 0 && isYouExist == false) { //generate a "you"
			
			int potentialUser = randomIndex.nextInt(personCount);
			boolean isYou = randomIndex.nextBoolean();
			Character c = pedestrians[potentialUser];
			Person tempPerson = (Person) c;
			tempPerson.setAsYou(isYou);
			pedestrians[potentialUser] = tempPerson;
			
			this.isYouExist = isYou;
		}
		
		for (int i = personCount; i < pedestrianCount; i++) {
			pedestrians[i] = getRandomAnimal();
		}
		
	}	
}
