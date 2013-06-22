/*package fart.dungeoncrawler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fart.dungeoncrawler.actor.BaseEnemy;
import fart.dungeoncrawler.actor.BaseNPC;
import fart.dungeoncrawler.actor.EnemyDescription;
import fart.dungeoncrawler.actor.MeleeEnemy;
import fart.dungeoncrawler.actor.NPCDescription;
import fart.dungeoncrawler.actor.NewPlayer;
import fart.dungeoncrawler.actor.RangedEnemy;
import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.enums.Heading;
import Utils.Vector2;

public class CheckPoint extends GameObject {
	class GameObjectInfo {
		private Vector2 position;
		private int heading;
		private Health health;
		private int ID;
		private int state;
		private EnemyDescription enemyDesc;
		private NPCDescription npcDesc;
		
		public GameObjectInfo(BaseNPC npc) {
			position = new Vector2(npc.getPosition());
			heading = npc.getHeading().ordinal();
			health = new Health(npc.getHealth());
			ID = npc.getID();
			state = npc.getState().ordinal();
			npcDesc = npc.getDescription();
			enemyDesc = null;
		}
		
		public GameObjectInfo(NewPlayer player) {
			position = new Vector2(player.getPosition());
			heading = player.getHeading().ordinal();
			health = new Health(player.getHealth());
			ID = player.getID();
			state = player.getState().ordinal();
			npcDesc = null;
			enemyDesc = null;
		}
		
		public GameObjectInfo(BaseEnemy enemy) {
			position = new Vector2(enemy.getPosition());
			heading = enemy.getHeading().ordinal();
			health = new Health(enemy.getHealth());
			ID = enemy.getID();
			state = enemy.getState().ordinal();
			npcDesc = null;
			enemyDesc = enemy.getDescription();
		}

		public Vector2 getPosition() {
			return position;
		}

		public Heading getHeading() {
			return Heading.values()[heading];
		}

		public Health getHealth() {
			return health;
		}

		public int getID() {
			return ID;
		}
		
		public DynamicObjectState getState() {
			return DynamicObjectState.values()[state];
		}
		
		public NPCDescription getNPCDesc() {
			return npcDesc;
		}
		
		public EnemyDescription getEnemyDesc() {
			return enemyDesc;
		}
	}

	private String map;
	private GameObjectInfo savedPlayer;
	private ArrayList<GameObjectInfo> npcs = new ArrayList<GameObjectInfo>();
	private BufferedImage texture;
	private Rectangle collisionRect;
	
	public CheckPoint(CheckPointDescription desc, Vector2 position) {
		this.screenPosition = position;
		this.collisionRect = new Rectangle(desc.getCollisionRect());
		collisionRect.x = (int)position.x;
		collisionRect.y = (int)position.y;
		this.texture = desc.getTexture();
	}
	
	public void saveCheckPoint(String map, NewPlayer player, ArrayList<BaseNPC> npcList) {
		this.map = map;
		this.savedPlayer = new GameObjectInfo(player);
		npcs.clear();
		
		for(BaseNPC npc : npcList) {
			npcs.add(new GameObjectInfo(npc));
		}
	}
	
	public void loadCheckPoint(Tilemap map, NewPlayer player, DynamicObjectManager manager, Collision collision) {
		//Map aus datei laden...
		collision.changeMap(map);
		
		player.setScreenPosition(savedPlayer.getPosition());
		player.setState(savedPlayer.getState());
		player.setHealth(savedPlayer.getHealth());
		player.setHeading(savedPlayer.getHeading());

		manager.clearObjects();
		for(GameObjectInfo info : npcs) {
			NPCDescription npcDesc = info.getNPCDesc();
			BaseNPC npc;
			if(npcDesc != null) {
				npc = new BaseNPC(npcDesc, manager);
			} else {
				npcDesc = info.getEnemyDesc();
				boolean isRanged = ((EnemyDescription)npcDesc).getIsRanged();
				if(isRanged) {
					npc = new RangedEnemy((EnemyDescription)npcDesc, collision, manager);
				} else {
					npc = new MeleeEnemy((EnemyDescription)npcDesc, collision, manager);
				}
			}
			npc.setPosition(info.getPosition());
			npc.activateState(info.getState());
			npc.setHeading(info.getHeading());
		}
	}

	@Override
	protected BufferedImage getTexture() {
		return texture;
	}

	@Override
	public Rectangle getCollisionRect() {
		return collisionRect;
	}

	@Override
	public void terminate() { }
}*/
