package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

import fart.dungeoncrawler.actor.Player;

public class GameShieldMessage extends GameMessage implements Serializable {
	private static final long serialVersionUID = 8276018548122752704L;
	public byte shieldID;

	public GameShieldMessage(Player player) {
		super((byte)player.getActorID(), GameMessage.GAME_SHIELD_MESSAGE);
		
		shieldID = (byte)player.getElementType().ordinal();
	}

}
