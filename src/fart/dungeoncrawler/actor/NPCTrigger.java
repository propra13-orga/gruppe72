package fart.dungeoncrawler.actor;


import java.awt.Graphics2D;

import Utils.Vector2;
import fart.dungeoncrawler.Game;
import fart.dungeoncrawler.ITriggerableOnKey;

public abstract class NPCTrigger extends BaseNPC implements ITriggerableOnKey {

	public NPCTrigger(Game game, Vector2 position, NPCDescription npcDesc) {
		super(game, position, npcDesc);
		
		this.texture = npcDesc.getSpriteSheet();
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		graphics.drawImage(texture, (int)screenPosition.x, (int)screenPosition.y, null);
	}
}
