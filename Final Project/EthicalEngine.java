/**
 * This is the EthicalEngine Class
 * To execute the program
 * There are interactive model and non-interactive model
 * Generate scenarios randomly either by config file
 * Show the statistic and determine whether to record it to file  
 * @author Rui Chen, 1100500
 */
import ethicalengine.Character;
import ethicalengine.Animal;
import ethicalengine.Character.BodyType;
import ethicalengine.Character.Gender;
import ethicalengine.Person;
import ethicalengine.Scenario;
import ethicalengine.ScenarioGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class EthicalEngine {

	public enum Decision { PEDESTRIANS, PASSENGERS }
	private Scenario[] scenarios = new Scenario[0];
	private String configFilePath = ""; // path of config file
	private String resultFilePath = ""; // path of result file
	private boolean interactiveLaunch = false; // whether start interactive model
	private boolean ifSave = false; //user consent
	private Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
	
		EthicalEngine sys = new EthicalEngine();
		
		sys.cmdLineParser(args); // parsing the command
		sys.printWelcom(); // print welcome
		sys.modeLaunch(); // choose which model to launch
		
		
	}

	/**
	 * This method makes a decision that which group live
	 * 
	 * @param scenario a scenario generate randomly or loaded from config file
	 * @return passenger or pedestrian live?
	 */
	public static Decision decide(Scenario scenario) { 
		
		Decision live = Decision.PASSENGERS;
		int passengersvalue = 0;
		int pedestriansvalue = 0;
		for(int i=0;i<scenario.getPassengers().length;i++) {
			if(scenario.getPassengers()[i].getAge() > 60) { // age is considered
				passengersvalue += 1;
			} else {
				passengersvalue += 0.5;
			}
			
			if(scenario.getPassengers()[i].getBodyType() == BodyType.AVERAGE) { // body type is considered
				passengersvalue += 1;
			} else {
				passengersvalue += 0.8;
			}
			
			if(scenario.getPassengers()[i].getGender() == Gender.FEMALE) { // gender is considered
				passengersvalue += 0.6;
			} else {
				passengersvalue += 0.4;
			}
			
			if(scenario.getPassengers()[i] instanceof Person ) {
				passengersvalue += 5;
				Person p = (Person) scenario.getPassengers()[i];
				if(p.isPregnant()) { // "if pregnant" is considered
					passengersvalue += 5;
				}
				
				if(p.isYou()) { // "isYou" is considered
					passengersvalue += 8;
				}
				
			} else {
				passengersvalue += 0.2;
				
				Animal p = (Animal)scenario.getPassengers()[i];
				if(p.isPregnant()) {
					passengersvalue += 1;
				}
				
				if(p.isPet()) {
					passengersvalue += 1;
				}
			}						
		}
		
		for(int i=0;i<scenario.getPedestrians().length;i++) {
			if(scenario.getPedestrians()[i].getAge() > 40) {
				pedestriansvalue += 1;
			} else {
				pedestriansvalue += 0.5;
			}
			
			if(scenario.getPedestrians()[i].getBodyType() == BodyType.AVERAGE) {
				pedestriansvalue += 1;
			} else {
				pedestriansvalue += 0.8;
			}
			
			if(scenario.getPedestrians()[i].getGender() == Gender.FEMALE) {
				pedestriansvalue += 0.6;
			} else {
				pedestriansvalue += 0.4;
			}
			
			if (scenario.getPedestrians()[i] instanceof Person ) {
				pedestriansvalue += 5;
				Person p = (Person) scenario.getPedestrians()[i];
				if(p.isPregnant()) {
					pedestriansvalue += 5;
				}
				
				if(p.isYou()) {
					pedestriansvalue += 8;
				}
				
			} else {
				pedestriansvalue += 0.2;
				
				Animal p = (Animal)scenario.getPedestrians()[i];
				if(p.isPregnant()) {
					pedestriansvalue += 1;
				}
				
				if(p.isPet()) {
					pedestriansvalue += 1;
				}
			}		
		}
		
		if(scenario.isLegalCrossing()) {
			pedestriansvalue += 5;
		}
		live = pedestriansvalue>passengersvalue?Decision.PASSENGERS:Decision.PEDESTRIANS;
		return live;
	}
	
	public void printWelcom() { // print welcome
		
		//read data file
		FileInputStream reader = null;
		//store the contents of the file into a String-type variable
		String fileToStr = null;
		
		try {
			
			reader = new FileInputStream("./welcome.ascii");
			
			//read file using a byte-type array
			byte[] bytes = new byte[reader.available()];
			reader.read(bytes);
			
			fileToStr = new String(bytes);
			
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: could not find config file.");
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
		
		System.out.println(fileToStr);
	}
	
	/**
	 * This method focus on splitting and paring command line
	 * 
	 * @param args command line 
	 */
	public void cmdLineParser(String[] args) {
		try {
			
			// default result file path
			resultFilePath = "result.log";
			
			
			// if command contains -h or --help
			if (Arrays.asList(args).contains("-h") || Arrays.asList(args).contains("--help")) {  
			   throw new Exception();
			}  
		  		
			// if command contains -c or --config
			if (Arrays.asList(args).contains("-c") || Arrays.asList(args).contains("--config") ) {  
				int index = 0;
				if(Arrays.asList(args).contains("-c")) {
					index = Arrays.asList(args).indexOf("-c");
				}else if(Arrays.asList(args).contains("--config")) {
					index = Arrays.asList(args).indexOf("--config");
				}
				  
				if (index+1<args.length) {
					if (args[index+1].contains("-")) {
						throw new Exception();
					} else {
						configFilePath = args[index+1];
					}
				} else { // if file path followed by -c/--config is empty 
					throw new Exception();
				}
				  
				File file = new File(configFilePath);
				if(!file.exists()) {
					System.out.println("ERROR: could not find config file.");
					System.exit(0); 
				}
				 
				loadCfgFile();
			}  
			
			// if command contains -i or --iteractive
			if (Arrays.asList(args).contains("-i") || Arrays.asList(args).contains("--interactive")) {  
				interactiveLaunch = true;
			}  
			    
			if(interactiveLaunch) {
				resultFilePath = "user.log";
			}
			
			// if command contains -r or --result
			// set the result file path
			if( Arrays.asList(args).contains("-r") || Arrays.asList(args).contains("--results")) {    
				int index = 0;
				if (Arrays.asList(args).contains("-r")) {
					index = Arrays.asList(args).indexOf("-r");
				} else if(Arrays.asList(args).contains("--results")) {
					index = Arrays.asList(args).indexOf("--results");
				}
				  
				if(index+1<args.length) {
					if (args[index+1].contains("-")) {
						throw new Exception();
					} else { // interactive model, statistic must be stored in file user.log
						if(interactiveLaunch) {
							if(args[index+1].contains("result.log")) {
								int pathIndex = args[index+1].indexOf("result.log");
								resultFilePath = args[index+1].substring(0,pathIndex) + "user.log";
							}
							if(!args[index+1].contains(".log")){
								resultFilePath = args[index+1] + "\\user.log";
							}
							   
							if(args[index+1].contains("user.log")){
								resultFilePath = args[index+1];
							}
						} else { //  non-interactive model, statistic must be stored in file result.log
							if(args[index+1].contains("user.log")) {
								int pathIndex = args[index+1].indexOf("user.log");
								resultFilePath = args[index+1].substring(0,pathIndex) + "result.log";
							}
							   
							if(!args[index+1].contains(".log")){
								resultFilePath = args[index+1] + "\\result.log";
							}
							   
							if(args[index+1].contains("result.log")){
								resultFilePath = args[index+1];
							}
						}
					} 
				}else {
					throw new Exception();
				}
			}
		} catch (Exception e) {
			System.out.print( 
						"EthicalEngine - COMP90041 - Final Project\n" + 
						"Usage: java EthicalEngine [arguments]\n" + 
				   		"Arguments: -c or --config Optional: path to config file\n" +
				   		"           -h or --help Print Help (this message) and exit\n" +
				   		"           -r or --results Optional: path to results log file\n" +
				   		"           -i or --interactive Optional: launches interactive mode"
				        );  
			System.exit(0);
		}
	}
	
	
	// load configfile
	public void loadCfgFile() {
		if(configFilePath == "") {
			return;
		}
		
		// load file
		File file = new File(configFilePath);
		BufferedReader reader = null;
		List<String> lineList = new ArrayList<>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempStr;
			
			while((tempStr = reader.readLine()) != null) {
				lineList.add(tempStr);
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: could not find config file.");	
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//phrasing config file
		int len = lineList.size();
		List<Character> passengersList = new ArrayList<>();
		List<Character> pedestriansList = new ArrayList<>();
		List<Scenario> scenarioList = new ArrayList<>(); //arraylist to store scenario information
		
		/**
		 * This part focus on check the legality of information form config file
		 *  
		 * @throws InvalidDataFormatException Invalid number of data fields per row
		 * @throws IllegalArgumentExceptio Invalid field values
		 * @throws NumberFormatException Invalid data type
		 */
		for (int i =1; i<len; i++) {
			try {
				
				if(lineList.get(i).contains("passenger") || lineList.get(i).contains("pedestrian")) {
					String line = lineList.get(i);
					String[] items = line.split(",");
					if(items.length != 10 ) { 
						throw new InvalidDataFormatException("WARNING: invalid data format in config file in line "+(i+1));
						//throw new IllegalArgumentException("WARNING: invalid data format in config file in line "+(i+1));
					}
					 
					if(items[0].contains("animal")) { // class, [0]
						 
						 // animal gender, [1]
						if(!(items[1].contains("female") || items[1].contains("male") || items[1].equals("unknown"))) {
							throw new IllegalArgumentException("WARNING: invalid data format in config file in line "+(i+1));
						}
						
						// animal age, [2]
						Integer.parseInt(items[2]);// throw NumberFormatException e
						 
						// animal bodytype, [3]
						// animal profession, [4]
						// animal pregnant, [5]
						// animal isYou, [6]
						// animal spieces, [7]
						 
						// animal isPet, [8]
						if(!(items[8].contains("true") || items[8].contains("false"))) {
							throw new IllegalArgumentException("WARNING: invalid data format in config file in line "+(i+1));
						}
						 
						// animal role, [9]
						if(!(items[9].contains("passenger") || items[9].contains("pedestrian"))) {
							 throw new IllegalArgumentException("WARNING: invalid data format in config file in line "+(i+1));
						}
				
					} else if (items[0].contains("person")) { // class, [0]
						 
						// person gender, [1]
						if(!(items[1].contains("female") || items[1].contains("male") || items[1].equals("unknown"))) {
							throw new IllegalArgumentException("WARNING: invalid data format in config file in line "+(i+1));
						}
					     
						// person age, [2]
						Integer.parseInt(items[2]);// may throw NumberFormatException e
					     
						// person body type, [3]
						if(!(items[3].contains("athletic") || items[3].contains("average") ||
							items[3].contains("overweight") || items[3].equals("unspecified"))) {
							throw new IllegalArgumentException("WARNING: invalid data format in config file in line "+(i+1));
						}
						 
						// person profession, [4]
						if(!(items[4].contains("doctor") || items[4].contains("ceo") ||
							items[4].contains("criminal") || items[4].contains("unknown") ||
							items[4].contains("nurse") || items[4].contains("homeless") ||
							items[4].contains("unemployed") || items[4].contains("teacher") ||
							items[4].contains("none") || "".equals(items[4]))) {
							throw new IllegalArgumentException("WARNING: invalid data format in config file in line "+(i+1));
						}
						 
						// person isPregnant, [5]
						if (!(items[5].contains("true") || items[5].contains("false"))) {
							throw new IllegalArgumentException("WARNING: invalid data format in config file in line "+(i+1));
						}
						 
						// person isYou, [6]
						if (!(items[6].contains("true") || items[6].contains("false"))) {
							throw new IllegalArgumentException("WARNING: invalid data format in config file in line "+(i+1));
						}
						 
						// person species, [7]
						// person isPet, [8]
						 
						// person role, [9]
						if(!(items[9].contains("passenger") || items[9].contains("pedestrian"))) {
							throw new IllegalArgumentException("WARNING: invalid data format in config file in line "+(i+1));
						}

					} else { // not a person or animal, class is invalid
						 throw new IllegalArgumentException("WARNING: invalid data format in config file in line "+(i+1));
					} 
				} 
				
			} catch (NumberFormatException e) {
				 System.out.println("WARNING: invalid data format in config file in line "+(i+1));
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			} catch (InvalidDataFormatException e) {
				System.out.println(e.getMessage());
			}
		}
		
		// assign value
		for (int i =len-1; i>0; i--) { // scan config file from bottom to up
			
			if(lineList.get(i).contains("scenario")) {
				
				String line = lineList.get(i);
				String[] items = line.split(":");
				boolean isLegalCrossing = false; 
				if(items[1].contains("green")) {
					isLegalCrossing = true;
				}
				
				Collections.reverse(passengersList);
				Collections.reverse(pedestriansList);
				
				// create a new scenario 
				Scenario newScenario = new Scenario(passengersList.toArray(new Character[passengersList.size()] ),
								    pedestriansList.toArray(new Character[pedestriansList.size()]),
								    isLegalCrossing);
				
				// add scenario to the list
				scenarioList.add(newScenario);
   			 	passengersList.clear();
   			 	pedestriansList.clear();
			}
			
			if(lineList.get(i).contains("passenger") || lineList.get(i).contains("pedestrian")) {
				String line = lineList.get(i);
				String[] items = line.split(",");
				if(items.length != 10 ) {
					continue;
				}
				 
				if(items[0].contains("animal")) {
					 
					Character.Gender gender; // = Character.Gender.UNKNOWN;
					if(items[1].contains("female")) {
						gender = Character.Gender.FEMALE;
					} else if (items[1].contains("male")) {
						 gender = Character.Gender.MALE;
					} else if (items[1].equals("unknown")) {
						 gender = Character.Gender.UNKNOWN;
					} else {
						continue;
					}
					
					int age = 0;
					try {
						age = Integer.parseInt(items[2]);// throw NumberFormatException e
					} catch (NumberFormatException e) {
						 age = 0; //default age
					}
						
					Character.BodyType bodyType = Character.BodyType.UNSPECIFIED;
					 
					boolean isPregnant = false;
					
					boolean isYou = false;
					String species = items[7];
					 
					boolean isPet;	    			
					if(items[8].contains("true")) {
						isPet = true;
					} else if(items[8].contains("false")) {
						isPet = false;
					} else {
						continue;
					}
					 
					//generate a animal
					Animal animal =new Animal(species, age, gender, bodyType, isPregnant, isPet); 
					animal.setYou(isYou);
			
					if(items[9].contains("passenger")) {
						 passengersList.add(animal);
					} else if(items[9].contains("pedestrian")) {
						 pedestriansList.add(animal);
					} else {
						 continue;
					}
			
				} else if (items[0].contains("person")) {
					 
					Character.Gender gender;
					if(items[1].contains("female")) {
						gender = Character.Gender.FEMALE;
					} else if (items[1].contains("male")) {
						gender = Character.Gender.MALE;
					} else if (items[1].equals("unknown")) {
						gender = Character.Gender.UNKNOWN;
					} else {
						continue;
					}
					 
					int age = 0;
					try {
						age = Integer.parseInt(items[2]);// throw NumberFormatException e
					} catch (NumberFormatException e) {
						age = 0;
					}
					
					Character.BodyType bodyType;
					if(items[3].contains("athletic")) {
						bodyType = Character.BodyType.ATHLETIC;
					} else if (items[3].contains("average")) {
						bodyType = Character.BodyType.AVERAGE;
					} else if (items[3].contains("overweight")) {
						bodyType = Character.BodyType.OVERWEIGHT;
					 } else if (items[3].equals("unspecified")) {
						bodyType = Character.BodyType.UNSPECIFIED;
					} else {
						continue;
					}
					 
					Person.Profession profession;
					if(items[4].contains("doctor")) {
						profession = Person.Profession.DOCTOR;
					} else if (items[4].contains("ceo")) {
						profession = Person.Profession.CEO;
					} else if (items[4].contains("criminal")) {
						profession = Person.Profession.CRIMINAL;
					} else if (items[4].contains("unknown")) {
						profession = Person.Profession.UNKNOWN;
					} else if (items[4].contains("nurse")) {
						profession = Person.Profession.NURSE;
					} else if (items[4].contains("homeless")) {
						profession = Person.Profession.HOMELESS;
					} else if (items[4].contains("unemployed")) {
						profession = Person.Profession.UNEMPLOYED;
					} else if (items[4].contains("teacher")) {
						profession = Person.Profession.TEACHER;
					} else if(items[4].equals("none")) {
						profession = Person.Profession.NONE;
					} else if(items[4].equals("")) {
						profession = Person.Profession.NONE;
					} else { // profession is invalid
						continue;
					}
					 
					boolean isPregnant;
					if (items[5].contains("true")) {
						isPregnant = true;
					} else if (items[5].contains("false")) {
						isPregnant = false;
					} else { // pregnant is invalid
						continue;
					}
					 
					boolean isYou;
					if (items[6].contains("true")) {
						isYou = true;
					} else if (items[6].contains("false")) {
						isYou = false;
					} else { //isYou is invalid
						continue;
					}
					 
					//generate a person
					Person person = new Person(age, profession, gender, bodyType, isPregnant);
					person.setAsYou(isYou);
					 
					if(items[9].contains("passenger")) {
						passengersList.add(person);
					} else if(items[9].contains("pedestrian")) {
						pedestriansList.add(person);
					} else {
						continue;
					}
					 
					 
				} else { // not a person or animal, class is invalid
					continue;
				} 
			} 	
		}
		
		len = scenarioList.size();
		scenarios = new Scenario[len];
		for(int i=0; i<len; i++) {
			scenarios[i] = scenarioList.get(len-i-1);
		}
	}
	
	
	// determine which model launch
	public void modeLaunch () {
	
		if(interactiveLaunch) { //interactive model, without config file
			if (scenarios.length == 0) {
				this.userMode();
			} else { //interactive model, WITH config file
				this.userConfigMode();
			}
			
		} else {
			if (scenarios.length == 0) { // ai model, wIthout config file
				this.aiModel();
			} else {  // ai model, WHITH config file
				this.aiConfigMode();
			}
		}
	}
	
	// AI model
	public void aiModel() {
		Audit aiAudit = new Audit();
		aiAudit.run(10);
		aiAudit.run(50);
		aiAudit.setAuditType("Algorithm");
		aiAudit.printToFile(resultFilePath);
	}
	
	// AI model with config file
	public void aiConfigMode() {
		Audit aiCfgAudit = new Audit(scenarios);
		aiCfgAudit.run();
		aiCfgAudit.setAuditType("Algorithm");
		aiCfgAudit.printToFile(resultFilePath);
	}
	
	//User model without config file
	public void userMode() {
	
		// collect user's consent
		try {
			System.out.println("Do you consent to have your decisions saved to a file? (yes/no)");
			String ans = scan.nextLine();
			if("yes".equals(ans)) {
				ifSave = true;
			} else if ("no".equals(ans)) {
				ifSave = false;
			} else {
				throw new IllegalArgumentException(
								   "Invalid response. Do you consent to have your decisions saved to a file? (yes/no)"
								   );
			}
				
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			String ans = scan.nextLine();
			if("yes".equals(ans)) {
				ifSave = true;
			} else if ("no".equals(ans)) {
				ifSave = false;
			} 
		}
		
		// present scenarios
		List<Scenario> scenarioList = new ArrayList<>();
		List<String> decisionList = new ArrayList<>();
		ScenarioGenerator generator = new ScenarioGenerator();
		do {
			for(int i = 0; i<3; i++) {
				Scenario newRandomSce = generator.generate();
				scenarioList.add(newRandomSce);
				//System.out.print(newRandomSce.toString());
				System.out.println(newRandomSce.toString());
				// user decides
				System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2])");
				String decision = scan.nextLine();
				if("passenger".equals(decision) || "passengers".equals(decision) || "1".equals(decision)) {
					decisionList.add(Decision.PASSENGERS.toString());
				} else if("pedestrian".equals(decision) || "pedestrians".equals(decision) || "2".equals(decision)) {
					decisionList.add(Decision.PEDESTRIANS.toString());
				}
			}
			
			// statistic 
			Audit userAudit = new Audit(scenarioList.toArray(new Scenario[scenarioList.size()]));
			userAudit.setDecisions(decisionList.toArray(new String[decisionList.size()]));
			userAudit.setAuditType("User");
			userAudit.userRun();
			
			// print & save statistic
			if(ifSave) {
				userAudit.printToFile(resultFilePath);
			} else {
				//System.out.print(userAudit.toString());
				System.out.println(userAudit.toString());
			}
		} while (ifContinue());
		
		this.exit();
	}
	
	//User model WITH config file
	public void userConfigMode() {
		// collect user's consent
		try {
			System.out.println("Do you consent to have your decisions saved to a file? (yes/no)");
			String ans = scan.nextLine();
			if("yes".equals(ans)) {
				ifSave = true;
			} else if ("no".equals(ans)) {
				ifSave = false;
			} else {
				throw new IllegalArgumentException(
								   "Invalid response. Do you consent to have your decisions saved to a file? (yes/no)"
								   );
			}
				
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			String ans = scan.nextLine();
			if("yes".equals(ans)) {
				ifSave = true;
			} else if ("no".equals(ans)) {
				ifSave = false;
			} 
		}
		
		// present scenarios and make decision by user
		int round = 0;
		int sceLen = scenarios.length;
		List<Scenario> scenarioList = new ArrayList<>();
		List<String> decisionList = new ArrayList<>();
		do {
			
			int repeatTimes = 0;
			repeatTimes = sceLen > 3 ? 3 : sceLen;
			
			// present scenarios
			for(int i = 0; i<repeatTimes; i++) {
				scenarioList.add(scenarios[i+3*round]);
				//System.out.print(scenarios[i+3*round].toString());
				System.out.println(scenarios[i+3*round].toString());
				// user decides
				System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2])");
				String decision = scan.nextLine();
				if("passenger".equals(decision) || "passengers".equals(decision) || "1".equals(decision)) {
					decisionList.add(Decision.PASSENGERS.toString());
				} else if("pedestrian".equals(decision) || "pedestrians".equals(decision) || "2".equals(decision)) {
					decisionList.add(Decision.PEDESTRIANS.toString());
				}
			}
					
			// statistic 
			Audit userAudit = new Audit(scenarioList.toArray(new Scenario[scenarioList.size()]));
			userAudit.setDecisions(decisionList.toArray(new String[decisionList.size()]));
			userAudit.setAuditType("User");
			userAudit.userRun();
			
			// print & save statistic
			if(ifSave) {
				userAudit.printToFile(resultFilePath);
			} else {
				//System.out.print(userAudit.toString());
				System.out.println(userAudit.toString());
			}
			
			//
			sceLen -= 3;	
			round++;
			// check the scenarios
			if(sceLen <= 0) {
				System.out.println("That's all. Press Enter to quit.");
				scan.nextLine();
				this.exit();
			}
			
		} while (ifContinue());
		
		this.exit();
	}
	
	/**
	 * whether user continue
	 * 
	 * @return boolean ifContinue: true continue; false exit 
	 */
	public boolean ifContinue () {
		boolean ifContinue = false;
		System.out.println("Would you like to continue? (yes/no)");
		String keyboard = scan.nextLine();
		if ("yes".equals(keyboard)) {
			ifContinue = true;
		} else if ("no".equals(keyboard)) {
			ifContinue = false;
		}
		return ifContinue;
	}
	
	public void exit () {
		System.exit(0);  
	}
	
}
