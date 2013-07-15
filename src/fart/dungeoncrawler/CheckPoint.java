package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Utils.Vector2;

import fart.dungeoncrawler.actor.*;
import fart.dungeoncrawler.actor.states.EnemyStateMachine;
import fart.dungeoncrawler.enums.NPCType;

/**
 * This class represents a CheckPoint. The game is saved at this position. As soon as the player dies
 * this save is restored.
 * @author Erhan
 *
 */
public class CheckPoint extends GameObject implements ITriggerableOnKey {
	private static final int MAX_LOADS_PER_CP = 2;
	private static BufferedImage texture;
	
	private Game game;
	private DynamicObjectManager dManager;
	private StaticObjectManager sManager;
	private CollisionDetector collision;
	
	private Tilemap map;
	private String mapName;
	private ArrayList<CheckPointInfo> infos;
	private CheckPointInfo playerInfo;
	private Rectangle rect;
	private int loads = 0;
	
	/**
	 * Creates an instance of the Checkpoint. 
	 * @param game instance of the game currently running
	 * @param dManager the dynamicObjectManager
	 * @param collision the Collision-detector
	 * @param map the current map
	 * @param rect the trigger-rectangle for the checkpoint
	 */
	public CheckPoint(Game game, DynamicObjectManager dManager, CollisionDetector collision, Tilemap map, Rectangle rect) {
		this.game = game;
		this.dManager = game.getDynamicManager();
		this.sManager = game.getStaticManager();
		this.collision = game.getCollision();
		this.map = map;
		this.rect = rect;
		
		screenPosition = new Vector2(rect.x, rect.y);
		
		try {
			texture = ImageIO.read(new File("res/goal.png"));
		} catch (IOException e) {
			System.out.println("Could not load image.");
			e.printStackTrace();
		}
		
		infos = new ArrayList<CheckPointInfo>();
	}
	
	/**
	 * Saves the game.
	 * @param map the current map. 
	 */
	public void save(Tilemap map) {
		this.mapName = new String(map.getName());
		ArrayList<Actor> dynamics = dManager.getActors();
		infos.clear();
		
		for (Actor a : dynamics) {
			ActorDescription aDesc = a.getActorDesc();
			EnemyDescription eDesc = null;
			NPCDescription nDesc = null;
			boolean isBoss = false;
			if (a instanceof BossEnemy) {
				BossEnemy b = (BossEnemy)a;
				eDesc = b.getDescription();
				nDesc = b.getNPCDescription();
				isBoss = true;
			}
			//Save enemies
			else if(a instanceof BaseEnemy) {
				BaseEnemy e = (BaseEnemy)a;
				eDesc = e.getDescription();
				nDesc = e.getNPCDescription();
			}
			//Save all BaseNPCs
			else if (a instanceof BaseNPC) {
				BaseNPC n = (BaseNPC)a;
				nDesc = n.getNPCDescription();
			} 
			//Save player
			else if (a instanceof Player) {
				Player p = (Player)a;
				playerInfo = new CheckPointInfo(
									p.getHealth().getCurrentHealth(),
									p.getHealth().getMaxHealth(),
									p.getMana().getCurrentMana(),
									p.getMana().getMaxMana(),
									p.getHeading().ordinal(),
									p.getState().ordinal(),
									p.getStats(),
									p.getCollisionRect(),
									aDesc,
									nDesc,
									eDesc,
									isBoss);
				continue;
			} 
			//Save all other actors
			else {
				
			}
			
			CheckPointInfo info = new CheckPointInfo(
								a.getHealth().getCurrentHealth(),
								a.getHealth().getMaxHealth(),
								a.getMana().getCurrentMana(),
								a.getMana().getMaxMana(),
								a.getHeading().ordinal(),
								a.getState().ordinal(),
								a.getStats(),
								a.getCollisionRect(),
								aDesc,
								nDesc,
								eDesc,
								isBoss);
			
			infos.add(info);
		}
	}
	
	/**
	 * Loads the state that was previously saved. 
	 * @return
	 */
	public boolean load() {
		loads += 1;
		if(loads > MAX_LOADS_PER_CP)
			return false;
		
		map.loadMap(mapName);
		
		dManager.clearObjects();
		collision.clearDynamicObjects();
		
		for(CheckPointInfo i : infos) {
			EnemyDescription eDesc = i.getEnemyDesc();
			NPCDescription nDesc = i.getNpcDesc();
			//ActorDescription aDesc = i.getActDesc();
			if(eDesc != null) {
				if(!eDesc.getIsRanged()) {
					if(i.isBoss()) {
						BossEnemy b = new BossEnemy(game, i);
						b.setMachine(new EnemyStateMachine(b, game.getAllPlayers()));
						continue;
					}
					MeleeEnemy e = new MeleeEnemy(game, i);
					e.setMachine(new EnemyStateMachine(e, game.getAllPlayers()));
					continue;
				}
			} else if(nDesc != null) {
				NPCType type = nDesc.getType();
				if(type == NPCType.Talking) {
					new NPCTalking(game, i.getPosition(), nDesc, i.getRectangle());
				} else if (type == NPCType.Shop) {
					new NPCShop(game, i.getPosition(), nDesc, i.getRectangle());
				}
			}
		}
		
		game.getPlayer().resetCheckpoint(playerInfo);
		dManager.addObject(game.getPlayer());
		
		if(loads == MAX_LOADS_PER_CP)
			terminate();
		
		return true;
	}

	@Override
	public void trigger(Actor actor) {
		game.saveCheckPoint(this);
	}

	@Override
	protected BufferedImage getTexture() {
		return texture;
	}

	@Override
	public Rectangle getCollisionRect() {
		return rect;
	}

	@Override
	public void terminate() {
		collision.removeTriggerOnKey(this);
		sManager.removeObject(this);
		texture = null;
	}

	@Override
	public Rectangle getTriggerArea() {
		return rect;
	}

	/**
	 * Returns the name of the map. 
	 * @return
	 */
	public String getMapName() {
		return mapName;
	}
}
