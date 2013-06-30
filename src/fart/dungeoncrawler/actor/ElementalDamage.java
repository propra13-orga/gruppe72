package fart.dungeoncrawler.actor;

public class ElementalDamage {
	private int fireDamage;
	private int waterDamage;
	private int earthDamage;
	
	public ElementalDamage(int fire, int water, int earth) {
		fireDamage = fire;
		waterDamage = water;
		earthDamage = earth;
	}
	
	public ElementalDamage(ElementalDamage e) {
		fireDamage = e.fireDamage;
		waterDamage = e.waterDamage;
		earthDamage = e.earthDamage;
	}

	public void add(ElementalDamage e) {
		fireDamage += e.fireDamage;
		waterDamage += e.waterDamage;
		earthDamage += e.earthDamage;
	}
	
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
