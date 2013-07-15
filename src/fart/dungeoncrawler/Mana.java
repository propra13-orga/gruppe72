package fart.dungeoncrawler;

/**
 * Represents the mana of an actor.
 * @author Roman
 *
 */
public class Mana {
    private float maxMana;
    private float curMana;
   
    /**
     * Creates a new instance. Current mana is set to maximum mana. 
     * @param maxMana
     */
    public Mana(float maxMana) {
        this.maxMana = maxMana;
        curMana = maxMana;
    }
   
    /**
     * Creates a new instance. 
     * @param maxMana maximum mana
     * @param curMana current mana
     */
    public Mana(float maxMana, float curMana) {
        this.maxMana = maxMana;
        this.curMana = curMana;
    }
    
    /**
     * Copies an instance. 
     * @param mana
     */
    public Mana(Mana mana) {
    	maxMana = mana.maxMana;
    	curMana = mana.curMana;
	}

    /**
     * Returns the current mana. 
     * @return
     */
	public float getCurrentMana() {
    	return curMana;
    }
   
	/**
	 * Reduces the mana. 
	 * @param amount
	 */
    public void reduceMana(float amount) {
        this.curMana -= amount;
        if(curMana < 0)
            curMana = 0;
    }
   
    /**
     * Adds mana. 
     * @param amount
     */
    public void addMana(float amount) {
        curMana += amount;
        if(curMana > maxMana)
            curMana = maxMana;
    }
   
    /**
     * Adds to maximum mana.
     * @param amount
     */
    public void addMaxMana(float amount) {
        maxMana += amount;
    }
   
    /**
     * Reduces maximum mana. 
     * @param amount
     */
    public void reduceMaxMana(float amount) {
        maxMana -= amount;
        if(curMana > maxMana)
            curMana = maxMana;
    }

    /**
     * Returns the maximum mana. 
     * @return
     */
	public float getMaxMana() {
		return maxMana;
	}
	
	/**
	 * Sets current mana to maximum mana. 
	 */
	public void fillMana() {
		curMana = maxMana;
	}
	
	/**
	 * Sets the maximum mana. 
	 * @param max
	 */
	public void setMaxMana(float max) {
		maxMana = max;
		addMana(0);
	}
}
