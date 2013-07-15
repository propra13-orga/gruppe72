package fart.dungeoncrawler.actor;

/**
 * This class represents elemental damage that can be done with a weapon. 
 * @author Felix
 *
 */
public class ElementalDamage {
	private int fireDamage;
	private int waterDamage;
	private int earthDamage;
	
	/**
	 * Creates an instance of the class. 
	 * @param fire fire damage
	 * @param water water damage
	 * @param earth earth damage
	 */
	public ElementalDamage(int fire, int water, int earth) {
		fireDamage = fire;
		waterDamage = water;
		earthDamage = earth;
	}
	
	/**
	 * Copies an instance of the ElementalDamage-class
	 * @param e
	 */
	public ElementalDamage(ElementalDamage e) {
		fireDamage = e.fireDamage;
		waterDamage = e.waterDamage;
		earthDamage = e.earthDamage;
	}

	/**
	 * Adds two ElementalDamage instances together.
	 * @param e
	 */
	public void add(ElementalDamage e) {
		fireDamage += e.fireDamage;
		waterDamage += e.waterDamage;
		earthDamage += e.earthDamage;
	}
	
	/**
	 * Subtracts two ElementalDamage instances. 
	 * @param e
	 */
	public void sub(ElementalDamage e) {
		fireDamage -= e.fireDamage;
		waterDamage -= e.waterDamage;
		earthDamage -= e.earthDamage;
	}

	/**
	 * @return the fireDamage
	 */
	public int getFireDamage() {
		return fireDamage;
	}

	/**
	 * @return the waterDamage
	 */
	public int getWaterDamage() {
		return waterDamage;
	}

	/**
	 * @return the earthDamage
	 */
	public int getEarthDamage() {
		return earthDamage;
	}
}
