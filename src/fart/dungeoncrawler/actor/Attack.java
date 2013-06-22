package fart.dungeoncrawler.actor;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import fart.dungeoncrawler.GameObject;
import fart.dungeoncrawler.Tilemap;
import fart.dungeoncrawler.enums.Heading;

public class Attack {
	private int damage;
	private HashMap<Heading, Animation> anim;
	private HashMap<Heading, HashMap<Integer, Rectangle>> attackRects;
	private Animation curAnim;
	private Rectangle curRect;
	private int duration;
	private int curFrame;
	private GameObject owner;
	
	/**
	 * Represents a MeleeAttack. 
	 * 
	 * @param damage The damage done. 
	 * @param anim Animation for this attack.
	 * @param attackRects CollisionRects for Heading.Left. The Integer represents a framenumber, so that the
	 * 			CollisionRect can change over time. 
	 * @param frameDuration Duration in frames
	 * @param owner Owner of the attack.
	 */
	public Attack(int damage, HashMap<Heading, Animation> anim, HashMap<Integer, Rectangle> attackRects, int frameDuration, GameObject owner) {
		this.damage = damage;
		this.anim = anim;
		//this.attackRects = attackRects;
		this.owner = owner;
		duration = frameDuration;
		curFrame = 0;
		
		constructRectsFromList(attackRects);
	}
	
	/**
	 * Constructs attackRects for all Headings from a single Heading (LEFT)
	 * 
	 * @param list AttackRects for Heading.Left
	 */
	private void constructRectsFromList(HashMap<Integer, Rectangle> list) {
		HashMap<Heading, HashMap<Integer, Rectangle>> result = new HashMap<Heading, HashMap<Integer, Rectangle>>();
		result.put(Heading.Left, list);
		
		HashMap<Integer, Rectangle> right = new HashMap<Integer, Rectangle>();
		for(Map.Entry<Integer, Rectangle> entry : list.entrySet()) {
			int key = entry.getKey();
			int rx = Tilemap.TILE_SIZE;
			int rw = entry.getValue().width;
			int ry = 0;
			int rh = entry.getValue().height;
			right.put(key, new Rectangle(rx, ry, rw, rh));
		}
		HashMap<Integer, Rectangle> up = new HashMap<Integer, Rectangle>();
		for(Map.Entry<Integer, Rectangle> entry : list.entrySet()) {
			int key = entry.getKey();
			int rx = 0;
			int rw = Tilemap.TILE_SIZE;
			int ry = -entry.getValue().width;
			int rh = -ry;
			up.put(key, new Rectangle(rx, ry, rw, rh));
		}
		HashMap<Integer, Rectangle> down = new HashMap<Integer, Rectangle>();
		for(Map.Entry<Integer, Rectangle> entry : list.entrySet()) {
			int key = entry.getKey();
			int rx = 0;
			int rw = Tilemap.TILE_SIZE;
			int ry = Tilemap.TILE_SIZE;
			int rh = entry.getValue().width;
			down.put(key, new Rectangle(rx, ry, rw, rh));
		}
		
		result.put(Heading.Right, right);
		result.put(Heading.Up, up);
		result.put(Heading.Down, down);
		
		attackRects = result;
	}
	
	/**
	 * Gets the Animation for a given Heading
	 * @param heading Heading of the owner
	 * @return Desired animation
	 */
	public Animation getAnimation(Heading heading) {
		curAnim = anim.get(heading);
		return curAnim;
	}
	
	/**
	 * Returns the damage of the attack
	 * @return Damage done
	 */
	public int getDamage() {
		return damage;
	}
	
	/**
	 * Returns the CollisionRect for given heading in the current frame.
	 * @param heading Heading of the owner
	 * @return CollisionRect for heading and current frame
	 */
	public Rectangle getRect(Heading heading) {
		int frame = curAnim.getCurrentFrame();
		if(attackRects.get(heading).containsKey(frame)) 
			curRect = attackRects.get(heading).get(frame);
		
		Rectangle ret = new Rectangle(curRect);
		Rectangle pos = owner.getCollisionRect();
		ret.x += (int)pos.x;
		ret.y += (int)pos.y;
		
		return ret;
	}
	
	/**
	 * Updates the current attack. 
	 * @return Returns if the attack is over. 
	 */
	public boolean Update() {
		curFrame += 1;
		if(curFrame == duration) {
			curFrame = 0;
			return true;
		}
		
		return false;
	}
}
