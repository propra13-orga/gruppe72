package fart.dungeoncrawler;

public class Health {
    private float maxHealth;
    private float curHealth;
    private boolean invulnerable;
   
    public Health(float maxHealth) {
        this.maxHealth = maxHealth;
        curHealth = maxHealth;
        invulnerable = false;
    }
   
    public Health(float maxHealth, float curHealth) {
        this.maxHealth = maxHealth;
        this.curHealth = curHealth;
        invulnerable = false;
    }
    
    public Health(float maxHealth, float curHealth, boolean invul) {
        this.maxHealth = maxHealth;
        this.curHealth = curHealth;
        invulnerable = invul;
    }
    
    public Health(Health h) {
    	this.curHealth = h.curHealth;
    	this.maxHealth = h.maxHealth;
    	this.invulnerable = h.invulnerable;
    }
    
    public float getCurrentHealth() {
    	return curHealth;
    }
    
    public float getMaxHealth() {
    	return maxHealth;
    }
    
    public boolean isInvul() {
    	return invulnerable;
    }
   
    public void reduceHealth(float dmg) {
    	if(invulnerable)
    		return;
    	
        this.curHealth -= dmg;
        if(curHealth < 0)
            curHealth = 0;
    }
   
    public void addHealth(float amount) {
        curHealth += amount;
        if(curHealth > maxHealth)
            curHealth = maxHealth;
    }
   
    public void addMaxHealth(float amount) {
        maxHealth += amount;
    }
   
    public void reduceMaxHealth(float amount) {
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
    
    public void setMaxHealh(float max) {
    	maxHealth = max;
    	addHealth(0);
    }
    
    public void fillHealth() {
    	curHealth = maxHealth;
    }
    
    public void setInvul(boolean invul) {
    	invulnerable = invul;
    }
}
