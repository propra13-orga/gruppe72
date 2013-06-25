package fart.dungeoncrawler.actor;

import java.awt.Rectangle;

import Utils.Vector2;
import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.enums.GameState;

public class NPCShop extends NPCTrigger {
	private Game game;
	private Rectangle triggerArea;

	public NPCShop(Game game, Vector2 position,
			NPCDescription npcDesc, Rectangle colRect) {
		super(game, position, npcDesc);
		this.game = game;
		this.triggerArea = colRect;
	}

	@Override
	public void trigger(Actor actor) {
		game.setGameState(GameState.InShop);
		game.setShopActor(actor);
	}

	@Override
	public Rectangle getTriggerArea() {
		return triggerArea;
	}

}
