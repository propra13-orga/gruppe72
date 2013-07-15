package fart.dungeoncrawler.actor;

import fart.dungeoncrawler.enums.Heading;
import fart.dungeoncrawler.enums.NPCType;

/**
 * Holds metadata for NPCs that can be created with this Description. 
 * @author Felix
 *
 */
public class NPCDescription extends ActorDescription {
	private NPCType type;
	
	public NPCDescription(String spritePath,
					int type,
					int level,
					int element,
					Stats stats,
					Heading heading) {
		super(spritePath,
					level,
					element,
					stats,
					heading);		
		this.type = NPCType.values()[type];
	}
	
	public NPCType getType() { return type; }
}
