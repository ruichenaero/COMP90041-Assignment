package ethicalengine;
/**
 * This is the Character class
 * @author Rui Chen
 *
 */
public abstract class Character {
	public static final Gender DEFAULT_GENDER = Gender.UNKNOWN;
	public static final BodyType DEFAULT_BODYTYPE = BodyType.UNSPECIFIED;
	private int age;
	private Gender gender;
	private BodyType bodyType;
	
	public enum Gender{ MALE, FEMALE, UNKNOWN }
	
	public enum BodyType{ AVERAGE, ATHLETIC, OVERWEIGHT, UNSPECIFIED }
	
	public Character() { //initialize
		age = 0;
		gender = DEFAULT_GENDER;
		bodyType = DEFAULT_BODYTYPE;
	}
	
	/**
	 * A character
	 * 
	 * @param age character'sm age
	 * @param gender character'sm age
	 * @param bodyType character'sm age
	 */
	public Character(int age, Gender gender, BodyType bodyType) {			
		if(age >= 0) {
			this.age = age;
		} else { 
			// if age < 0, age return default value 0;
			this.age = 0;
		}
		
		this.gender = gender;
		this.bodyType = bodyType;	
	}
	
	public Character(Character c) {
		
	}
	
	
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public BodyType getBodyType() {
		return bodyType;
	}

	public void setBodyType(BodyType bodyType) {
		this.bodyType = bodyType;
	}

}
