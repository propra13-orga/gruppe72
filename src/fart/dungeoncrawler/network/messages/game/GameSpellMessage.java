package fart.dungeoncrawler.network.messages.game;

import java.io.Serializable;

import fart.dungeoncrawler.actor.NewPlayer;

public class GameSpellMessage extends GameMessage implements Serializable {
	private static final long serialVersionUID = -7226453598184590828L;
	
	public byte heading;
	public byte spellIndex;

	public GameSpellMessage(NewPlayer p, int spellIndex) {
		super((byte)p.getActorID(), GameMessage.GAME_SPELL_MESSAGE);
		
		heading = (byte)p.getHeading().ordinal();
		this.spellIndex = (byte)spellIndex;
	}
}
