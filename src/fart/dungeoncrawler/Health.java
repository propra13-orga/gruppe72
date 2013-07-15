package fart.dungeoncrawler;

/**
 * Represents the health of an actor. 
 * @author Erhan
 *
 */
public class Health {
    private float maxHealth;
    private float curHealth;
    private boolean invulnerable;
    
    /**
     * Creates a new instance. Current health is set to maxHealth.
     * @param maxHealth
     */
    public Health(float maxHealth) {
        this.maxHealth = maxHealth;
        curHealth = maxHealth;
        invulnerable = false;
    }
   
    /**
     * Creates a new instance. 
     * @param maxHealth maximum health
     * @param curHealth current health
     */
    public Health(float maxHealth, float curHealth) {
        this.maxHealth = maxHealth;
        this.curHealth = curHealth;
        invulnerable = false;
    }
    
    /**
     * Creates a new instance.
     * @param maxHealth maximum health
     * @param curHealth current health
     * @param invul if the actor should be invulnerable
     */
    public Health(float maxHealth, float curHealth, boolean invul) {
        this.maxHealth = maxHealth;
        this.curHealth = curHealth;
        invulnerable = invul;
    }
    
    /**
     * Copies the given health-instance.
     * @param h
     */
    public Health(Health h) {
    	this.curHealth = h.curHealth;
    	this.maxHealth = h.maxHealth;
    	this.invulnerable = h.invulnerable;
    }
    
    /**
     * Return the current health. 
     */
    public float getCurrentHealth() {
    	return curHealth;
    }
    
    /**
     * Return maximum health. 
     * @return
     */
    public float getMaxHealth() {
    	return maxHealth;
    }
    
    /**
     * Returns if the actor is invulnerable. 
     * @return
     */
    public boolean isInvul() {
    	return invulnerable;
    }
    
    /**
     * Sets the current health. 
     * @param health
     */
    public void setHealth(float health) {
    	this.curHealth = health;
    }
   
    /**
     * Reduces the current health. 
     * @param dmg
     */
    public void reduceHealth(float dmg) {
    	if(invulnerable)
    		return;
    	
        this.curHealth -= dmg;
        if(curHealth < 0)
            curHealth = 0;
    }
   
    /**
     * Adds to the current health. 
     * @param amount
     */
    public void addHealth(float amount) {
        curHealth += amount;
        if(curHealth > maxHealth)
            curHealth = maxHealth;
    }
   
    /**
     * Adds to maximum health. 
     * @param amount
     */
    public void addMaxHealth(float amount) {
        maxHealth += amount;
    }
   
    /**
     * Reduces maximum health. 
     * @param amount
     */
    public void reduceMaxHealth(float amount) {
        maxHealth -= amount;
        if(curHealth > maxHealth)
            curHealth = maxHealth;
    }
   
    /**
     * Returns if the actor is dead. 
     * @return
     */
    public boolean isDead() {
        return curHealth == 0;
    }
    
    /**
     * Returns if the current health is below a certain percentage value. 
     * @param percent
     * @return
     */
    public boolean lowerThan(float percent) {
    	float perc = (float)curHealth / (float)maxHealth;
    	
    	return perc < percent;
    }
    
    /**
     * Sets the maximum health. 
     * @param max
     */
    public void setMaxHealh(float max) {
    	maxHealth = max;
    	addHealth(0);
    }
    
    /**
     * Sets current health to aximum health.
     */
    public void fillHealth() {
    	curHealth = maxHealth;
    }
    
    /**
     * Sets the actor invulnerable. 
     * @param invul
     */
    public void setInvul(boolean invul) {
    	invulnerable = invul;
    }
}
