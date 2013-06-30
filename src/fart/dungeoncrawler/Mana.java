package fart.dungeoncrawler;

public class Mana {
    private float maxMana;
    private float curMana;
   
    public Mana(float maxMana) {
        this.maxMana = maxMana;
        curMana = maxMana;
    }
   
    public Mana(float maxMana, float curMana) {
        this.maxMana = maxMana;
        this.curMana = curMana;
    }
        
    public Mana(Mana mana) {
    	maxMana = mana.maxMana;
    	curMana = mana.curMana;
	}

	public float getCurrentMana() {
    	return curMana;
    }
   
    public void reduceMana(float amount) {
        this.curMana -= amount;
        if(curMana < 0)
            curMana = 0;
    }
   
    public void addMana(float amount) {
        curMana += amount;
        if(curMana > maxMana)
            curMana = maxMana;
    }
   
    public void addMaxMana(float amount) {
        maxMana += amount;
    }
   
    public void reduceMaxMana(float amount) {
        maxMana -= amount;
        if(curMana > maxMana)
            curMana = maxMana;
    }
    
    public boolean lowerThan(float percent) {
    	float perc = (float)curMana / (float)maxMana;
    
    	return perc < percent;
    }

	public float getMaxMana() {
		return maxMana;
	}
	
	public void fillMana() {
		curMana = maxMana;
	}
	
	public void setMaxMana(float max) {
		maxMana = max;
		addMana(0);
	}
}
