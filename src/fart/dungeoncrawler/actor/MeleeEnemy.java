package fart.dungeoncrawler.actor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import Utils.Vector2;

import fart.dungeoncrawler.*;
import fart.dungeoncrawler.enums.*;

/**
 * This class represents a MeleeEnemy.
 * @author Felix
 *
 */
public class MeleeEnemy extends BaseEnemy {
	private DynamicObjectState curState;
	
	/**
	 * Creates a MeleeEnemy from an EnemyDescription.
	 * @param game instance of tha game running
	 * @param position the position in screenspace
	 * @param enemyDesc the EnemyDescription. 
	 */
	public MeleeEnemy(Game game, Vector2 position, EnemyDescription enemyDesc) {
		super(game, position, enemyDesc);
		curState = DynamicObjectState.Idle;
		heading = Heading.Down;
		
		buildAttacks();
		setCurrentAnimation(curState);
	}
	
	/**
	 * Creates a MeleeEnemy from a CheckPointInfo.
	 * @param game instance of the game running
	 * @param info the CheckPointInfo
	 */
	public MeleeEnemy(Game game, CheckPointInfo info) {
		super(game, info);
		
		buildAttacks();
		setCurrentAnimation(curState);
	}
	
	/**
	 * Builds the melee-attack.
	 */
	private void buildAttacks() {
		HashMap<Heading, Animation> simpleAttackAnim = new HashMap<Heading, Animation>();
		BufferedImage[] iSALeft = new BufferedImage[1];
		iSALeft[0] = new BufferedImage(32, 32, ColorSpace.TYPE_RGB);
		Graphics2D g2 = (Graphics2D)iSALeft[0].getGraphics();
		g2.setColor(new Color(0.0f, 0.0f, 1.0f));
		g2.fillRect(0, 0, 32, 32);
		simpleAttackAnim.put(Heading.Left, new Animation(iSALeft, 1));
		
		BufferedImage[] iSARight = new BufferedImage[1];
		iSARight[0] = new BufferedImage(32, 32, ColorSpace.TYPE_RGB);
		g2 = (Graphics2D)iSARight[0].getGraphics();
		g2.setColor(new Color(1.0f, 0.0f, 0.0f));
		g2.fillRect(0, 0, 32, 32);
		simpleAttackAnim.put(Heading.Right, new Animation(iSARight, 1));
		
		BufferedImage[] iSAUp = new BufferedImage[1];
		iSAUp[0] = new BufferedImage(32, 32, ColorSpace.TYPE_RGB);
		g2 = (Graphics2D)iSAUp[0].getGraphics();
		g2.setColor(new Color(0.0f, 1.0f, 0.0f));
		g2.fillRect(0, 0, 32, 32);
		simpleAttackAnim.put(Heading.Up, new Animation(iSAUp, 1));
		
		BufferedImage[] iSADown = new BufferedImage[1];
		iSADown[0] = new BufferedImage(32, 32, ColorSpace.TYPE_RGB);
		g2 = (Graphics2D)iSADown[0].getGraphics();
		g2.setColor(new Color(1.0f, 0.0f, 1.0f));
		g2.fillRect(0, 0, 32, 32);
		simpleAttackAnim.put(Heading.Down, new Animation(iSADown, 1));
		//HashMap<Integer, Rectangle> atRects = new HashMap<Integer, Rectangle>();
		//atRects.put(0, new Rectangle(-16, -16, 16, 16));
		int frameDur = 45;
		animations.put(DynamicObjectState.Attacking, simpleAttackAnim);
		simpleAttack = new Attack(8, simpleAttackAnim/*, atRects*/, frameDur, this);
	}
}
	
	
