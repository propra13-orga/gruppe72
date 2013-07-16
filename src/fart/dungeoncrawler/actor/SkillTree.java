package fart.dungeoncrawler.actor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fart.dungeoncrawler.CollisionDetector;
import fart.dungeoncrawler.Controller;

public class SkillTree {
	private Actor owner;
	private CollisionDetector collision;
	
	private ArrayList<Skill> fireSkills;
	private ArrayList<Skill> waterSkills;
	private ArrayList<Skill> earthSkills;
	
	public SkillTree(Actor owner, Controller controller, CollisionDetector collision) {
		this.owner = owner;
		this.collision = collision;
		
		fireSkills = new ArrayList<Skill>();
		waterSkills = new ArrayList<Skill>();
		earthSkills = new ArrayList<Skill>();
		
		initSkills();
	}
	
	private void initSkills() {
		//FIRE
		BufferedImage fireTex = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)fireTex.getGraphics();
		g2d.setColor(new Color(0, 0, 0, 0));
		g2d.fillRect(0, 0, 32, 32);
		g2d.setColor(Color.red);
		g2d.fillOval(8, 8, 16, 16);
		
		BufferedImage fireIcon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		g2d = (Graphics2D)fireIcon.getGraphics();
		g2d.setColor(Color.red);
		g2d.fillRect(0, 0, 32, 32);
		
		Spell fireBold = new SpellFireBold(fireIcon, new SpellProjectile(owner, fireTex, collision));
		Skill fireBoldSkill = new Skill(fireBold, 1);
		fireSkills.add(fireBoldSkill);
		
		//WATER
		BufferedImage waterTex = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		g2d = (Graphics2D)waterTex.getGraphics();
		g2d.setColor(new Color(0, 0, 0, 0));
		g2d.fillRect(0, 0, 32, 32);
		g2d.setColor(Color.blue);
		g2d.fillOval(8, 8, 16, 16);
		
		BufferedImage waterIcon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		g2d = (Graphics2D)waterIcon.getGraphics();
		g2d.setColor(Color.blue);
		g2d.fillRect(0, 0, 32, 32);
		
		Spell waterBold = new SpellWaterBold(waterIcon, new SpellProjectile(owner, waterTex, collision));
		Skill waterBoldSkill = new Skill(waterBold, 1);
		waterSkills.add(waterBoldSkill);
		
		//EARTH
		BufferedImage earthTex = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		g2d = (Graphics2D)earthTex.getGraphics();
		g2d.setColor(new Color(0, 0, 0, 0));
		g2d.fillRect(0, 0, 32, 32);
		g2d.setColor(new Color(0.8f, 0.64f, 0.05f));
		g2d.fillOval(8, 8, 16, 16);
		
		BufferedImage earthIcon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		g2d = (Graphics2D)earthIcon.getGraphics();
		g2d.setColor(new Color(0.8f, 0.64f, 0.05f));
		g2d.fillRect(0, 0, 32, 32);
			
		Spell earthBold = new SpellEarthBold(earthIcon, new SpellProjectile(owner, earthTex, collision));
		Skill earthBoldSkill = new Skill(earthBold, 1);
		earthSkills.add(earthBoldSkill);
	}
	
	public ArrayList<Skill> getFireSkills() { return fireSkills; }
	public ArrayList<Skill> getWaterSkills() { return waterSkills; }
	public ArrayList<Skill> getEarthSkills() { return earthSkills; }
	
	class Skill {
		private Spell spell;
		private int levelReq;
		private int skillLevel;
		private boolean prevLearned;
		
		public Skill(Spell spell, int levelReq) {
			this.spell = spell;
			this.levelReq = levelReq;
			
			prevLearned = (skillLevel == 1);
			
			skillLevel = 1;
		}
		
		public boolean skillable(int level) {
			return level >= levelReq && prevLearned;
		}
		
		public Spell getSpell() { return spell; }
	}
}

