package fart.dungeoncrawler.actor;

import java.awt.Graphics2D;

import fart.dungeoncrawler.IDrawable;

/**
 * Represents the level of an actor. Addtionally the experiance of players is handled in this class.
 * @author Felix
 *
 */
public class Level implements IDrawable {
	private Actor owner;
	private int level;
	private int experience;
	private int experienceNext;
	
	/**
	 * Creates an instance of the class with the appropriate level.
	 * @param owner owner of the instance
	 * @param startLevel the level to be applied
	 */
	public Level(Actor owner, int startLevel) {
		this.owner = owner;
		this.level = startLevel;
		this.experience = 0;
		this.experienceNext = getExperienceForLevel(startLevel);
	}
	
	/**
	 * Returns the current level.
	 * @return
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Returns the experiance gained on this level.
	 * @return
	 */
	public int getCurrentExperience() {
		return experience;
	}
	
	/**
	 * Returns the experiance needed for the next level.
	 * @return
	 */
	public int getExperienceForLevelUp() {
		return experienceNext;
	}
	
	/**
	 * Adds experiance and checks if the owner leveled up. 
	 * @param amount amount if experiance to add
	 */
	public void addExperince(int amount) {
		experience += amount;
		if(experience >= experienceNext) {
			level += 1;
			experience -= experienceNext;
			experienceNext = getExperienceForLevel(level);
			owner.levelUp();
		}
	}
	
	/**
	 * Returns the amount of experiance already gained in comparison to the needed experiance for the next level.
	 * @return
	 */
	public float getExpPercent() {
		return (float)experience/experienceNext;
	}
	
	/**
	 * Returns the amount of experiance needed for a given level. 
	 * @param level
	 * @return
	 */
	public static int getExperienceForLevel(int level) {
		return 10 * level * level + 180 * level;
	}
	
	/**
	 * Returns the amount of experiance a player gains when killing an enemy of a specific level. 
	 * @param level
	 * @return
	 */
	public static int getMobExperienceForLevel(int level) {
		return (int)(level * 3f + 25);
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		
	}

}
