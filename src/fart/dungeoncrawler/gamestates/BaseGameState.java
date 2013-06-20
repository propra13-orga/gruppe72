package fart.dungeoncrawler.gamestates;

import fart.dungeoncrawler.*;

public abstract class BaseGameState implements IUpdateable, IDrawable {
	protected Game game;
	
	public BaseGameState(Game game) {
		this.game = game;
	}
	
	public abstract void activate();
	public abstract void exit();
}
