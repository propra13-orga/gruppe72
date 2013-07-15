package fart.dungeoncrawler.actor;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import Utils.Vector2;
import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.enums.GameState;

/**
 * This class represents an NPC with a shop. The player can buy items in it. When triggered the game changes
 * to the InShop-state.
 * @author Felix
 *
 */
public class NPCShop extends NPCTrigger {
	private Game game;
	private Rectangle triggerArea;

	/**
	 * Creates an NPCShop from an NPCDescription
	 * @param game instance of the game running
	 * @param position position in screenspace
	 * @param npcDesc the NPCDescription
	 * @param triggerArea the area in which the player can open the shop
	 */
	public NPCShop(Game game, Vector2 position,
			NPCDescription npcDesc, Rectangle triggerArea) {
		super(game, position, npcDesc);
		this.game = game;
		this.triggerArea = triggerArea;
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

	@Override
	public BufferedImage getTexture() {
		return this.texture;
	}
}
