package fart.dungeoncrawler.actor;

import java.awt.image.BufferedImage;

import Utils.Vector2;
import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.ITriggerable;
import fart.dungeoncrawler.enums.GameState;

public class NPCTrigger extends BaseNPC implements ITriggerable {

	public NPCTrigger(Game game, ActorDescription actDesc, Vector2 position, NPCDescription npcDesc) {
		super(game, actDesc, position, npcDesc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void trigger(Actor player) {
		manager.getGame().setGameState(GameState.InShop);
	}

	@Override
	public void update(float elapsed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected BufferedImage getTexture() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void terminate() {}
	
}
