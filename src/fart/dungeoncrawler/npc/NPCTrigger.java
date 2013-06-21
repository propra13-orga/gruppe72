package fart.dungeoncrawler.npc;

import fart.dungeoncrawler.DynamicObjectManager;
import fart.dungeoncrawler.ITriggerable;
import fart.dungeoncrawler.Player;
import fart.dungeoncrawler.enums.GameState;

public class NPCTrigger extends BaseNPC implements ITriggerable {

	public NPCTrigger(NPCDescription desc, DynamicObjectManager manager) {
		super(desc, manager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void trigger(Player player) {
		manager.getGame().setGameState(GameState.InShop);
	}

	@Override
	public void trigger(BaseNPC npc) {
		// TODO Auto-generated method stub
		
	}
	
}
