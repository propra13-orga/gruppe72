package fart.dungeoncrawler;

public class Mana {
    private int maxMana;
    private int curMana;
   
    public Mana(int maxMana) {
        this.maxMana = maxMana;
        curMana = maxMana;
    }
   
    public Mana(int maxMana, int curMana) {
        this.maxMana = maxMana;
        this.curMana = curMana;
    }
        
    public Mana(Mana mana) {
    	maxMana = mana.maxMana;
    	curMana = mana.curMana;
	}

	public int getCurrentMana() {
    	return curMana;
    }
   
    public void reduceMana(int amount) {
        this.curMana -= amount;
        if(curMana < 0)
            curMana = 0;
    }
   
    public void addMana(int amount) {
        curMana += amount;
        if(curMana > maxMana)
            curMana = maxMana;
    }
   
    public void addMaxMana(int amount) {
        maxMana += amount;
    }
   
    public void reduceMaxMana(int amount) {
        maxMana -= amount;
        if(curMana > maxMana)
            curMana = maxMana;
    }
    
    public boolean lowerThan(float percent) {
    	float perc = (float)curMana / (float)maxMana;
    
    	return perc < percent;
    }

	public int getMaxMana() {
		return maxMana;
	}
	
	public void setMaxMana(int max) {
		maxMana = max;
		addMana(0);
	}
}
