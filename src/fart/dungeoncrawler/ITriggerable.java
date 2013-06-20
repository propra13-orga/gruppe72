package fart.dungeoncrawler;

import fart.dungeoncrawler.npc.BaseNPC;

public interface ITriggerable
{
	//public Rectangle getTriggerArea();
	public void trigger(Player player);
	public void trigger(BaseNPC npc);
}
