package fart.dungeoncrawler;

import fart.dungeoncrawler.actor.Actor;

public interface ITriggerable
{
	//public Rectangle getTriggerArea();
	public void trigger(Actor player);
	//public void trigger(BaseNPC npc);
}
