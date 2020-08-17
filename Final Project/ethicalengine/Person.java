package ethicalengine;
/**
 * This is the Person Class
 * @author Rui Chen
 *
 */

public class Person extends Character {
	
	public static final Profession DEFAULT_PROFESSION = Profession.NONE;
	private Profession profession;
	private boolean isPregnant;
	private AgeCategory ageCategory;
	private boolean isYou;

	public enum Profession { DOCTOR, CEO, CRIMINAL, HOMELESS, UNEMPLOYED, UNKNOWN, NURSE, TEACHER, NONE}
	public enum AgeCategory{ BABY, CHILD, ADULT, SENIOR }
	
	public Person () {
		super();
	}
	
	public Person(int age, Gender gender, BodyType bodytype) {
		super(age,gender,bodytype);
	}
	
	/**
	 * A person
	 * 
	 * @param age a person's age
	 * @param profession a person's profession
	 * @param gender a person's gender
	 * @param bodyType a person's body type
	 * @param isPregnant whether a person is pregnant
	 */
	public Person (int age, Profession profession, Gender gender, BodyType bodyType, boolean isPregnant) {
		super(age, gender, bodyType);
		ageCategory = getAgeCategory();
	
		if (haveProfession()) {
			if (profession != Profession.NONE) {
				this.profession = profession;
			} else {
				this.profession = Profession.UNKNOWN;
			}
		} else {
			if(profession != Profession.NONE) {
				//System.out.println("This person cannot have profession");
			}
			this.profession = DEFAULT_PROFESSION;
		}
		
		if (pregnancy()) {
			this.isPregnant = isPregnant;
		} else {
			if (isPregnant == true) {
				//System.out.println("This person cannot pregnant");
			}
		}
		
		isYou = false;
	}
	
	/**
	 * For coppying another person
	 * 
	 * @param otherPerson another person
	 */
	public Person (Person otherPerson) {
		setAge(otherPerson.getAge());
		setBodyType(otherPerson.getBodyType());
		setGender(otherPerson.getGender());
		ageCategory = otherPerson.ageCategory;
		isPregnant = otherPerson.isPregnant;
		isYou = otherPerson.isYou;
		profession = otherPerson.profession;
	}

	public Profession getProfession() { //only ADULTs have profession
		return profession;
	}

	public void setProfession(Profession profession) {
		if (haveProfession()) {
			this.profession = profession;
		} else {
			System.out.println("This person can not have profession");
		}
	}

	public boolean isPregnant() {
		return isPregnant;
	}

	public void setPregnant(boolean isPregnant) {
		
		if(pregnancy()) {
			this.isPregnant = isPregnant;
		} else {
			System.out.println("The person can not pregnant");
		}
	}

	public AgeCategory getAgeCategory() {
		if (getAge() > 68) {
			ageCategory = AgeCategory.SENIOR;
		} else if (getAge() >= 17) {
			ageCategory = AgeCategory.ADULT;
		} else if (getAge() >= 5) {
			ageCategory = AgeCategory.CHILD;
		} else if (getAge() >= 0) {
			ageCategory = AgeCategory.BABY;
		}
		return ageCategory;
	}

	public void setAgeCategory(AgeCategory ageCategory) {
		this.ageCategory = ageCategory;
	}
	
	public boolean isYou() {
		return isYou;
	}

	public void setAsYou(boolean isYou) {
		this.isYou = isYou;
	}
	
	public String toString() {
		
		String isYou = this.isYou? "you ":"";
		String bodyType = getBodyType().toString().toLowerCase()+" ";
		String ageCategory = this.ageCategory.toString().toLowerCase()+" ";
		String profession = haveProfession()? this.profession.toString().toLowerCase()+" ":"";
		String gender = getGender().toString().toLowerCase();
		String pregnant = this.isPregnant? " pregnant":"";
	
		String personInfo = isYou+bodyType+ageCategory+profession+gender+pregnant;
		
		return personInfo;
	}
	
	private boolean haveProfession () {
		if (ageCategory == AgeCategory.ADULT) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean pregnancy () {
		if (getGender() == Gender.FEMALE && ageCategory == AgeCategory.ADULT) {
			return true;
		} else {
			return false;
		}
	}

}
