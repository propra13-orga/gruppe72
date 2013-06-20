package fart.dungeoncrawler.npc;

import fart.dungeoncrawler.DynamicObjectManager;
import fart.dungeoncrawler.ITriggerable;
import fart.dungeoncrawler.Player;

public class NPCTrigger extends BaseNPC implements ITriggerable {

	public NPCTrigger(NPCDescription desc, DynamicObjectManager manager) {
		super(desc, manager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void trigger(Player player) {
		
	}

	@Override
	public void trigger(BaseNPC npc) {
		// TODO Auto-generated method stub
		
	}
	
}
