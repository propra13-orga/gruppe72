package fart.dungeoncrawler.actor;

import java.awt.Graphics2D;

import fart.dungeoncrawler.IDrawable;

public class Level implements IDrawable {
	private Actor owner;
	private int level;
	private int experience;
	private int experienceNext;
	
	public Level(Actor owner, int startLevel) {
		this.owner = owner;
		this.level = startLevel;
		this.experience = 0;
		this.experienceNext = getExperienceForLevel(startLevel);
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getCurrentExperience() {
		return experience;
	}
	
	public int getExperienceForLevelUp() {
		return experienceNext;
	}
	
	public void addExperince(int amount) {
		experience += amount;
		if(experience >= experienceNext) {
			level += 1;
			experience -= experienceNext;
			experienceNext = getExperienceForLevel(level);
			owner.levelUp();
		}
	}
	
	public float getExpPercent() {
		return (float)experience/experienceNext;
	}
	
	public static int getExperienceForLevel(int level) {
		return 10 * level * level + 180 * level;
	}
	
	public static int getMobExperienceForLevel(int level) {
		return (int)(level * 3f + 25);
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		
	}

}
