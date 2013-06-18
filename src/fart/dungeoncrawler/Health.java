package fart.dungeoncrawler;

public class Health {
    private int maxHealth;
    private int curHealth;
    private boolean invulnerable;
   
    public Health(int maxHealth) {
        this.maxHealth = maxHealth;
        curHealth = maxHealth;
        invulnerable = false;
    }
   
    public Health(int maxHealth, int curHealth) {
        this.maxHealth = maxHealth;
        this.curHealth = curHealth;
        invulnerable = false;
    }
    
    public Health(int maxHealth, int curHealth, boolean invul) {
        this.maxHealth = maxHealth;
        this.curHealth = curHealth;
        invulnerable = invul;
    }
    
    public Health(Health health) {
    	curHealth = health.curHealth;
    	maxHealth = health.maxHealth;
    	invulnerable = health.invulnerable;
	}

	public int getCurrentHealth() {
    	return curHealth;
    }
    public int getMaxHealth() {
    	return maxHealth;
    }
   
    public void reduceHealth(int amount) {
    	if(invulnerable)
    		return;
    	
        this.curHealth -= amount;
        if(curHealth < 0)
            curHealth = 0;
    }
   
    public void addHealth(int amount) {
        curHealth += amount;
        if(curHealth > maxHealth)
            curHealth = maxHealth;
    }
   
    public void addMaxHealth(int amount) {
        maxHealth += amount;
    }
   
    public void reduceMaxHealth(int amount) {
        maxHealth -= amount;
        if(curHealth > maxHealth)
            curHealth = maxHealth;
    }
   
    public boolean isDead() {
        return curHealth == 0;
    }
    
    public boolean lowerThan(float percent) {
    	float perc = (float)curHealth / (float)maxHealth;
    	
    	return perc < percent;
    }
    
    public void setInvul(boolean invul) {
    	invulnerable = invul;
    }
}
