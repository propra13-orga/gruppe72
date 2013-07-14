package fart.dungeoncrawler.actor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

import Utils.Vector2;
import fart.dungeoncrawler.*;
import fart.dungeoncrawler.enums.*;

public class BossEnemy extends BaseEnemy {
	private DynamicObjectState curState;
	
	private String mapToName;
	private Vector2 mapToPos;
	
	public BossEnemy(Game game, Vector2 position, EnemyDescription enemyDesc,
						String mapToName, Vector2 mapToPos) {
		super(game, position, enemyDesc);
		curState = DynamicObjectState.Idle;
		heading = Heading.Down;
		
		this.mapToName = mapToName;
		this.mapToPos = mapToPos;
		
		buildAttacks();
		buildSpell();
		setCurrentAnimation(curState);
	}
	
	public BossEnemy(Game game, CheckPointInfo info) {
		super(game, info);
		
		buildAttacks();
		buildSpell();
		setCurrentAnimation(curState);
	}
	
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
		int frameDur = 44;
		animations.put(DynamicObjectState.Attacking, simpleAttackAnim);
		simpleAttack = new Attack(15, simpleAttackAnim, /*atRects, */frameDur, this);
	}
	
	private void buildSpell() {
		spTex = new BufferedImage(32, 32, ColorSpace.TYPE_RGB);
		Graphics2D g2d = (Graphics2D)spTex.getGraphics();
		g2d.setColor(new Color(0.6f, 0.1f, 0.8f));
		g2d.fillOval(8, 8, 16, 16);
		
		simpleSpell = new SpellFireBold(null, new SpellProjectile(this, spTex, collision));;//new Spell(10, 120, ElementType.Water, null, new SpellProjectile(this, spTex, 25, collision), 25, 2.5f);
		spellManager.addSpell(simpleSpell);
	}
	
	@Override
	public void terminate() {
		velocity = Vector2.Zero;
		machine.setState(DynamicObjectState.Terminated);
		collision.removeDynamicObject(this);
		manager.removeObject(this);
		
		Random r = new Random();
		if(r.nextFloat() > 0.5f) {
			int amount = r.nextInt(12);
			new GoldItem(game, screenPosition, amount);
		}
		
		String mapTo = new String(game.getMapPath());
		if (mapTo.equals("res/maps/L2R2.xml")) {
			Goal g = new Goal(new Vector2(screenPosition.x / Tilemap.TILE_SIZE, screenPosition.y / Tilemap.TILE_SIZE), game);
			game.getStaticManager().addObject(g);
			collision.addTriggerOnKey(g);
		} else {
			StringBuilder sb = new StringBuilder(mapTo);
			sb.replace(mapTo.length() - 5, mapTo.length() - 4, "0");
			String l = "";
			l += sb.charAt(10);
			Integer level = Integer.parseInt(l);
			level += 1;
			sb.replace(10, 11, level.toString());
			String result = sb.toString();
			
			PortalOnKey p = new PortalOnKey(game, result, new Vector2(screenPosition.x / Tilemap.TILE_SIZE, screenPosition.y / Tilemap.TILE_SIZE), new Vector2(1 * Tilemap.TILE_SIZE, 13 * Tilemap.TILE_SIZE));
			collision.addTriggerOnKey(p);
			game.getStaticManager().addObject(p);
		}
		
	}
}
