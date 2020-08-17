/**
 * This is the Audit Class
 * To store information of scenarios and survivors
 * Calculate the statistic information
 * @author Rui Chen
 */
import ethicalengine.Character;
import ethicalengine.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class Audit {

	private String auditType = "Unspecified";
	private Scenario[] scenarios = new Scenario[0]; 
	private String[] decisions = new String[0];
	private int redSurvivorCount;
	private int redCharacterCount;
	private int greenSurvivorCount;
	private int greenCharacterCount;
	private float averageAge;
	private Character[] survivors;
	private Character[] characters;
	
	private String[][] survivalChar = {{"BABY","0"},{"CHILD","0"},{"ADULT","0"},{"SENIOR","0"}, 																		
										{"MALE","0"},{"FEMALE","0"},							  																		
										{"AVERAGE","0"},{"ATHLETIC","0"},{"OVERWEIGHT","0"},							 												
										{"DOCTOR","0"},{"CEO","0"},{"CRIMINAL","0"},{"HOMELESS","0"},{"UNEMPLOYED","0"},{"UNKNOWN","0"},{"NURSE","0"},{"TEACHER","0"},	
										{"PREGNANT","0"},																												
										{"PERSON","0"},{"ANIMAL","0"},																									
										{"DOG","0"},{"CAT","0"},{"BIRD","0"},{"FERRET", "0"},																							
										{"PET","0"},																													
										{"YOU","0"},																													
										{"RED","0"},{"GREEN","0"}};																			
															
	private String[][] statisticChar = {{"BABY","0"},{"CHILD","0"},{"ADULT","0"},{"SENIOR","0"}, 																		
										{"MALE","0"},{"FEMALE","0"},							  																		
										{"AVERAGE","0"},{"ATHLETIC","0"},{"OVERWEIGHT","0"},							 												
										{"DOCTOR","0"},{"CEO","0"},{"CRIMINAL","0"},{"HOMELESS","0"},{"UNEMPLOYED","0"},{"UNKNOWN","0"},{"NURSE","0"},{"TEACHER","0"},	
										{"PREGNANT","0"},																												
										{"PERSON","0"},{"ANIMAL","0"},																									
										{"DOG","0"},{"CAT","0"},{"BIRD","0"},{"FERRET","0"},																							
										{"PET","0"},																													
										{"YOU","0"},																													
										{"RED","0"},{"GREEN","0"}};	
	
	private String[][] statisticResult = {{"BABY","0"},{"CHILD","0"},{"ADULT","0"},{"SENIOR","0"}, 																		
											{"MALE","0"},{"FEMALE","0"},							  																		
											{"AVERAGE","0"},{"ATHLETIC","0"},{"OVERWEIGHT","0"},							 												
											{"DOCTOR","0"},{"CEO","0"},{"CRIMINAL","0"},{"HOMELESS","0"},{"UNEMPLOYED","0"},{"UNKNOWN","0"},{"NURSE","0"},{"TEACHER","0"},	
											{"PREGNANT","0"},																												
											{"PERSON","0"},{"ANIMAL","0"},																									
											{"DOG","0"},{"CAT","0"},{"BIRD","0"},{"FERRET","0"},																						
											{"PET","0"},																													
											{"YOU","0"},																													
											{"RED","0"},{"GREEN","0"}};	
		
	public Audit() {
		
	}
	
	/**
	 * Audit the information from scenarios
	 * 
	 * @param scenarios multiple scenarios
	 */
	public Audit(Scenario[] scenarios) {
		this.scenarios = scenarios;
	}
	
	public void setAuditType (String name) {
		auditType = name;
	}
	
	public String getAuditType () {
		return this.auditType;
	}

	public void userRun() {
		this.setGreenScheme();
		this.setRedScheme();
		this.setSurvivors();
		this.setCharacters();
		this.setSurvivalChar();
		this.setStatisticChar();
		this.setAverageAge();
		this.setStatisticResult();
	}
	
	public void run () {
		// set decision
		if(scenarios.length != 0) {
			decisions = new String[scenarios.length];
			for(int i=0; i<scenarios.length; i++) {
				decisions[i] = EthicalEngine.decide(scenarios[i]).toString();
			}
			this.setGreenScheme();
			this.setRedScheme();
			this.setSurvivors();
			this.setCharacters();
			this.setSurvivalChar();
			this.setStatisticChar();
			this.setAverageAge();
			this.setStatisticResult();
		}
	}
	
	/**
	 * Generate multiple scenarios
	 * and simulate the reality, decide who live
	 * 
	 * @param runs execute how many times 
	 */
	public void run (int runs) { 
		
		ScenarioGenerator generator = new ScenarioGenerator();
		
		for (int i = 0; i < runs; i++) { // add each scenario generated to scenarios[] one by one
			int newLength = scenarios.length;
			newLength++;
			
			// add each scenario to scenarios[]
			Scenario[] tempScenario = scenarios;
			
			scenarios = new Scenario[newLength];
			for (int j = 0; j < tempScenario.length; j++) {
				scenarios[j] = tempScenario[j];
			}
			scenarios[newLength-1] = generator.generate();
			
			// set decisions[]
			String[] tempDecision = decisions;
			decisions = new String[newLength];
			for (int j = 0; j < tempDecision.length; j++) {
				decisions[j] = tempDecision[j];
			}
			decisions[newLength-1] = EthicalEngine.decide(scenarios[newLength-1]).toString();
		}
		
		this.setGreenScheme();
		this.setRedScheme();
		this.setSurvivors();
		this.setCharacters();
		this.setSurvivalChar();
		this.setStatisticChar();
		this.setAverageAge();
		this.setStatisticResult();
	}
	
	public Scenario[] getScenario() {
		return this.scenarios;
	}
	
	public String[] getDecisions() {
		return this.decisions;
	}
	
	public void setDecisions(String[] decisions) {
		this.decisions = decisions;
	}

	
	public void setAverageAge() {
		averageAge = 0;
		if(survivors.length != 0) {
			int ageCount = 0;
			int personCount = 0;
			
			for(int i = 0; i < survivors.length; i++) {
				if(survivors[i] instanceof Person) {
					ageCount+= survivors[i].getAge();					
					personCount++;
				}
			}
			if(personCount != 0) {
				averageAge = (float)Math.floor(10*(float)ageCount/(float)personCount)/10;
			}
		}
	}
	
	public float getAverageAge() {
		return this.averageAge;
	}
	
	public void setRedScheme () {
		redCharacterCount = 0;
		redSurvivorCount = 0;
		if(scenarios.length != 0 && decisions.length != 0) {
			for (int i = 0; i < scenarios.length; i++) {	
				if (!scenarios[i].isLegalCrossing()) { // red
					redCharacterCount += scenarios[i].getPassengerCount() + scenarios[i].getPedestrianCount();
				
					if ("PASSENGERS" == decisions[i]) {
						redSurvivorCount += scenarios[i].getPassengerCount();
					} else if ("PEDESTRIANS" == decisions[i]) {
						redSurvivorCount += scenarios[i].getPedestrianCount();
					}
				}
			}
		}
	}
	
	public int getRedCharacterCount() {
		return this.redCharacterCount;
	}
	
	public int getRedSurvivorCount() {
		return this.redSurvivorCount;
	}
	
	public void setGreenScheme () {
		greenCharacterCount = 0;
		greenSurvivorCount = 0;
		if(scenarios.length != 0 && decisions.length != 0) {
			for (int i = 0; i < scenarios.length; i++) {//green
				if (scenarios[i].isLegalCrossing()) { 
					greenCharacterCount += scenarios[i].getPassengerCount() + scenarios[i].getPedestrianCount();
					if ("PASSENGERS" == decisions[i]) {
						greenSurvivorCount += scenarios[i].getPassengerCount();
					} else if ("PEDESTRIANS" == decisions[i]) {
						greenSurvivorCount += scenarios[i].getPedestrianCount();
					}
				}
			}	
		}
	}
	
	
	public int getGreenCharacterCount() {
		return this.greenCharacterCount;
	}
	
	public int getGreenSurvivorCount() {
		return this.greenSurvivorCount;
	}
	
	public void setSurvivors () { // scan scenarios and all store survivors in a Charcter[]     
		if(scenarios.length != 0 && decisions.length != 0) {
			survivors = new Character[0];
			for (int i = 0; i < scenarios.length; i++) {
				if ("PASSENGERS" == decisions[i]) {
					int newLength = survivors.length;
					newLength += scenarios[i].getPassengers().length;
					Character[] temp = survivors;
					
					survivors = new Character[newLength];
					for (int j = 0; j < temp.length; j++) {
						survivors[j] = temp[j];
					}
					
					for (int j = temp.length; j < newLength; j++) {
						survivors[j] = scenarios[i].getPassengers()[j-temp.length];
					}
				} else if ("PEDESTRIANS" == decisions[i]) {
					int newLength = survivors.length;
					newLength += scenarios[i].getPedestrians().length;
					Character[] temp = survivors;
					
					survivors = new Character[newLength];
					for (int j = 0; j < temp.length; j++) {
						survivors[j] = temp[j];
					}
					
					for (int j = temp.length; j < newLength; j++) {
						survivors[j] = scenarios[i].getPedestrians()[j-temp.length];
					}
				}
			}
		}
	}
	
	public Character[] getSurvivors() {
		return this.survivors;
	}
	
	public void setCharacters () { // scan scenarios and store all characters
		
		if(scenarios.length != 0) {
			characters = new Character[0];
			for (int i = 0; i < scenarios.length; i++) {
				int newLength = characters.length;
				newLength += scenarios[i].getPassengerCount() + scenarios[i].getPedestrianCount();
				Character[] temp = characters;
				
				characters = new Character[newLength];
				for (int j = 0; j < temp.length; j++) {
					characters[j] = temp[j];
				}
				// add passengers
				for (int j = temp.length; j < temp.length + scenarios[i].getPassengerCount(); j++) {
					characters[j] = scenarios[i].getPassengers()[j-temp.length];
				}
				// add pedestrians 
				for (int j = temp.length + scenarios[i].getPassengerCount(); j < newLength; j++ ) {
					characters[j] = scenarios[i].getPedestrians()[j-temp.length-scenarios[i].getPassengerCount()];
				}
			}
		}
	}
	
	public Character[] getCharacters() {
		return this.characters;
	}
	
	public void setStatisticChar () { //scan characters, statistic information of all characters

		if(characters.length != 0) {
			for(String[] str:statisticChar) {
				str[1] = "0";
			}
			for (int i =0; i < characters.length; i++) {
				if (characters[i] instanceof Person) {
					
					int classTypeIndex = match("PERSON",statisticChar);
					statisticChar[classTypeIndex][1] 
					= String.valueOf(Integer.parseInt(statisticChar[classTypeIndex][1])+1);
					
					int bodyTypeIndex = match(characters[i].getBodyType().toString(), statisticChar);
					if (bodyTypeIndex != -1) {
						statisticChar[bodyTypeIndex][1] 
						= String.valueOf(Integer.parseInt(statisticChar[bodyTypeIndex][1])+1);
					}
					int genderIndex = match(characters[i].getGender().toString(), statisticChar);
					if (genderIndex != -1 && !characters[i].getGender().toString().equals("UNKNOWN")) {
						statisticChar[genderIndex][1] 
						= String.valueOf(Integer.parseInt(statisticChar[genderIndex][1])+1);
					}
					
					Person tempPerson = (Person) characters[i];
					int ageCategoryIndex = match(tempPerson.getAgeCategory().toString(), statisticChar);
					if (ageCategoryIndex != -1) {
						statisticChar[ageCategoryIndex][1] 
						= String.valueOf(Integer.parseInt(statisticChar[ageCategoryIndex][1])+1);
					}
					int professionIndex = match(tempPerson.getProfession().toString(), statisticChar);
					if (professionIndex != -1) {
						statisticChar[professionIndex][1] 
						= String.valueOf(Integer.parseInt(statisticChar[professionIndex][1])+1);
					}
					int pregnantIndex = match("PREGNANT", statisticChar);
					if (tempPerson.isPregnant()) {
						statisticChar[pregnantIndex][1] 
						= String.valueOf(Integer.parseInt(statisticChar[pregnantIndex][1])+1);
					}
					int youIndex = match("YOU", statisticChar);
					if (tempPerson.isYou()) {
						statisticChar[youIndex][1] 
						= String.valueOf(Integer.parseInt(statisticChar[youIndex][1])+1);
					}
					
				} else if (characters[i] instanceof Animal) {
					int classTypeIndex = match("ANIMAL",statisticChar);
					statisticChar[classTypeIndex][1] 
					= String.valueOf(Integer.parseInt(statisticChar[classTypeIndex][1])+1);
					
					Animal tempAnimal = (Animal) characters[i];
					int speciesIndex = match(tempAnimal.getSpecies().toUpperCase(), statisticChar);
					//tempAnimal.getSpecies().toUpperCase().toString()
					if (speciesIndex != -1) {
						statisticChar[speciesIndex][1] 
						= String.valueOf(Integer.parseInt(statisticChar[speciesIndex][1])+1);
					}
					int petIndex = match("PET", statisticChar);
					if (tempAnimal.isPet()) {
						statisticChar[petIndex][1] 
						= String.valueOf(Integer.parseInt(statisticChar[petIndex][1])+1);
					}
				}	
			}
		}
		
		statisticChar[match("RED",statisticChar)][1] = String.valueOf(redCharacterCount);
		statisticChar[match("GREEN",statisticChar)][1] = String.valueOf(greenCharacterCount);
		
	}
	
	public String[][] getStatisticChar(){
		return this.statisticChar;
	}
	
	public void setSurvivalChar () { // scan scenarios, store statistic information of survivors 
		if(survivors.length != 0) {
			for(String[] str:survivalChar) { // clear 
				str[1] = "0";
			}
			for (int i = 0; i < survivors.length; i++) {
				if (survivors[i] instanceof Person) {
					
					int classTypeIndex = match("PERSON",survivalChar);
					survivalChar[classTypeIndex][1] 
					= String.valueOf(Integer.parseInt(survivalChar[classTypeIndex][1])+1);
					
					int bodyTypeIndex = match(survivors[i].getBodyType().toString(), survivalChar);
					if (bodyTypeIndex != -1) {
						survivalChar[bodyTypeIndex][1] 
						= String.valueOf(Integer.parseInt(survivalChar[bodyTypeIndex][1])+1);
					}
					int genderIndex = match(survivors[i].getGender().toString(), survivalChar);
					if (genderIndex != -1 && !survivors[i].getGender().toString().equals("UNKNOWN")) {
						survivalChar[genderIndex][1] 
						= String.valueOf(Integer.parseInt(survivalChar[genderIndex][1])+1);
					}
					
					Person tempPerson = (Person) survivors[i];
					int ageCategoryIndex = match(tempPerson.getAgeCategory().toString(), survivalChar);
					if (ageCategoryIndex != -1) {
						survivalChar[ageCategoryIndex][1] 
						= String.valueOf(Integer.parseInt(survivalChar[ageCategoryIndex][1])+1);
					}
					int professionIndex = match(tempPerson.getProfession().toString(), survivalChar);
					if (professionIndex != -1) {
						survivalChar[professionIndex][1] 
						= String.valueOf(Integer.parseInt(survivalChar[professionIndex][1])+1);
					}
					int pregnantIndex = match("PREGNANT", survivalChar);
					if (tempPerson.isPregnant()) {
						survivalChar[pregnantIndex][1] 
						= String.valueOf(Integer.parseInt(survivalChar[pregnantIndex][1])+1);
					}
					int youIndex = match("YOU", survivalChar);
					if (tempPerson.isYou()) {
						survivalChar[youIndex][1] 
						= String.valueOf(Integer.parseInt(survivalChar[youIndex][1])+1);
					}
					
				} else if (survivors[i] instanceof Animal) {
					int classTypeIndex = match("ANIMAL",survivalChar);
					survivalChar[classTypeIndex][1] 
					= String.valueOf(Integer.parseInt(survivalChar[classTypeIndex][1])+1);
					
					Animal tempAnimal = (Animal) survivors[i];
					int speciesIndex = match(tempAnimal.getSpecies().toUpperCase(), survivalChar);
					//tempAnimal.getSpecies().toString(), survivalChar
					if (speciesIndex != -1) {
						survivalChar[speciesIndex][1] 
						= String.valueOf(Integer.parseInt(survivalChar[speciesIndex][1])+1);
					}
					int petIndex = match("PET", survivalChar);
					if (tempAnimal.isPet()) {
						survivalChar[petIndex][1] 
						= String.valueOf(Integer.parseInt(survivalChar[petIndex][1])+1);
					}
				}	
			}
		}
		
		survivalChar[match("RED",survivalChar)][1] = String.valueOf(redSurvivorCount);
		survivalChar[match("GREEN",survivalChar)][1] = String.valueOf(greenSurvivorCount);
	
	}
	
	public String[][] getSurvivalChar(){
		return this.survivalChar;
	}
	
	public void setStatisticResult () {
		
		if(survivalChar.length != 0 && statisticChar.length != 0) {
			for(String[] str:statisticResult) {
				str[1] = "0";
			}
			for (int i = 0; i < statisticResult.length; i++) {
				if (!"0".equals(statisticChar[i][1])) {
					
					float survivalRatio = (float) Math.floor(10*Float.parseFloat(survivalChar[i][1])/Float.parseFloat(statisticChar[i][1]))/10;
					statisticResult[i][1] = String.valueOf(survivalRatio);
				}
			}
			
			//sort the array on win ratio in descending order
			//ties is resolved by sorting the array on username alphabetically
			
			Arrays.sort(statisticResult, new Comparator<Object>() {  
				public int compare(Object ObjectA, Object ObjectB) {  
				String[] arTempOne = (String[])ObjectA;  
				String[] arTempTwo = (String[])ObjectB;  
				
				//sort on ratio first
				if(Float.parseFloat(arTempOne[1])-Float.parseFloat(arTempTwo[1])>0) {
				    return -1;  
				}else if(Float.parseFloat(arTempOne[1])-Float.parseFloat(arTempTwo[1])<0) {  
				    return 1;  
				}else {  
					//if it is ties, then order alphabetically
					if(arTempOne[0].compareTo(arTempTwo[0])>0) {
						return 1;
					}else if(arTempOne[0].compareTo(arTempTwo[0])<0) {
						return -1;
					}
				}  
				return 0;  
				}  
			});
			   
		}	
	}
	
	public String[][] getStatisticResult(){
		return this.statisticResult;
	}
	
	/**
	 * return a element's index in a array 
	 * 
	 * @param characteristic the element
	 * @param array the array
	 * @return return the index of the element
	 */
	public int match (String characteristic, String[][] array) {
		int index = -1;
		
		for (int i = 0; i < array.length; i++) {
			if (characteristic.equals(array[i][0])) {
				index = i;
			}
		}
		return index;
	}
	
	public String toString () {
		String summary = "no audit available";
		if (statisticResult.length != 0) {
			StringBuffer buf=new StringBuffer();
			buf.append("======================================\n"+
				   "# "+ auditType + " Audit\n"+
				   "======================================\n"+
				   "- % SAVED AFTER "+scenarios.length+" RUNS\n"
				   );
			
			for (String[] str :statisticResult) {
				if(!"0".equals(str[1])) {
					buf.append(str[0].toLowerCase()+": "+str[1]+"\n");
				}
			}
			
			buf.append("--\n");
			buf.append("average age: "+averageAge);
			
			summary = buf.toString();
		}
		return summary;
	}
	
	/**
	 * @param filePath the file path which store the file
	 */
	public void printToFile(String filePath) { 
		
		FileWriter writer = null;
		try {
			
			System.out.println(this.toString());
			//System.out.print(this.toString());
			writer = new FileWriter(filePath,true);
			writer.write("======================================\r\n"+
				     "# "+ auditType + " Audit\r\n"+
				     "======================================\r\n"+
				     "- % SAVED AFTER "+scenarios.length+" RUNS\r\n"
				     );
			for (String[] str : statisticResult) {
				if(!"0".equals(str[1])) {
					writer.write(str[0].toLowerCase()+": "+str[1]+"\r\n");
				}
			}
			
			writer.write("--\r\n");
			writer.write("average age:"+ averageAge+"\r\n");
			writer.flush();
			
		}catch (FileNotFoundException e) {
			System.out.println("ERROR: could not print results. Target directory does not exist.");
		}catch (IOException e)	{
			e.printStackTrace();
		} finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
