package fart.dungeoncrawler;

import java.util.ArrayList;

import fart.dungeoncrawler.enums.DynamicObjectState;
import fart.dungeoncrawler.enums.Heading;
import fart.dungeoncrawler.npc.BaseEnemy;
import fart.dungeoncrawler.npc.BaseNPC;
import fart.dungeoncrawler.npc.EnemyDescription;
import fart.dungeoncrawler.npc.MeleeEnemy;
import fart.dungeoncrawler.npc.NPCDescription;
import Utils.Vector2;

public class CheckPoint {
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
		
		public GameObjectInfo(Player player) {
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
	
	public void saveCheckPoint(String map, Player player, ArrayList<BaseNPC> npcList) {
		this.map = map;
		this.savedPlayer = new GameObjectInfo(player);
		npcs.clear();
		
		for(BaseNPC npc : npcList) {
			npcs.add(new GameObjectInfo(npc));
		}
	}
	
	public void loadCheckPoint(Tilemap map, Player player, DynamicObjectManager manager, Collision collision) {
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
				npc = new BaseNPC(npcDesc);
				npc.setPosition(info.getPosition());
				npc.activateState(info.getState());
				npc.setHeading(info.getHeading());
			} else {
				npcDesc = info.getEnemyDesc();
				boolean isRanged = ((EnemyDescription)npcDesc).getIsRanged();
				if(isRanged) {
					//ranged einbauen...
				} else {
					npc = new MeleeEnemy((EnemyDescription)npcDesc, collision);
					npc.setPosition(info.getPosition());
					npc.activateState(info.getState());
					npc.setHeading(info.getHeading());
				}
			}
			
		}
	}
}
