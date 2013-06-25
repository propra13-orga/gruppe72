package fart.dungeoncrawler;

import java.awt.Rectangle;

import fart.dungeoncrawler.actor.Actor;

public interface ITriggerableOnKey {
	public void trigger(Actor actor);
	public Rectangle getTriggerArea();
}