package fart.dungeoncrawler.actor;

import fart.dungeoncrawler.enums.Heading;
import fart.dungeoncrawler.enums.NPCType;

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
	
	public NPCDescription(String spritePath,
			int type,
			ActorDescription aDesc) {
		super(spritePath,
					aDesc.getLevel(),
					aDesc.getElement().ordinal(),
					aDesc.stats,
					aDesc.heading);
		this.type = NPCType.values()[type];
	}
	
	public NPCType getType() { return type; }
}
