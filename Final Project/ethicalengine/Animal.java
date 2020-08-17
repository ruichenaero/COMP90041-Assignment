package ethicalengine;
/**
 * This is the Animal Class
 * @author Rui Chen
 *
 */
public class Animal  extends Character {

	enum AnimalSpecie {CAT, DOG, BIRD, FERRET} 
	private String species;
	private boolean isPet;
	private boolean isYou;
	private boolean isPregnant;
	
	public Animal () {
		super();
	}
	
	public Animal (String species) {
		super();
		this.species = species;
	}
	
	/**
	 * A animal
	 * 
	 * @param spiece animal's specie
	 * @param age animal's age
	 * @param gender animal's gender
	 * @param bodyType animal's body type
	 * @param isPregnant whether animal is pregnant 
	 * @param isPet animal's whether the animal is a pet
	 */
	public Animal (String spiece, int age, Gender gender, BodyType bodyType, boolean isPregnant, boolean isPet) {
		super(age,gender,bodyType);
		this.species = spiece;
		if (pregnancy()) {
			this.isPregnant = isPregnant;
		} else {
			if (isPregnant == true) {
				//System.out.println("This person cannot pregnant");
			}
		}
		this.isPet = isPet;
	}


	public Animal (Animal otherAnimal) {
		setAge(otherAnimal.getAge());
		setBodyType(otherAnimal.getBodyType());
		setGender(otherAnimal.getGender());
		species = otherAnimal.species;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public boolean isPet() {
		return isPet;
	}

	public void setPet(boolean isPet) {
		this.isPet = isPet;
	}
	
	
	public boolean isYou() {
		return isYou;
	}

	public void setYou(boolean isYou) {
		this.isYou = isYou;
	}

	public boolean isPregnant() {
		return isPregnant;
	}

	public void setPregnant(boolean isPregnant) {
		this.isPregnant = isPregnant;
	}
	
	private boolean pregnancy () {
		if (getGender() == Gender.FEMALE) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString () {
		//String isPet = this.isPet? " is pet":"";
		if (isPet) {
			return species + " is pet";
		} else {
			return species;
		}
	}
	
}
