package fart.dungeoncrawler;

import fart.dungeoncrawler.actor.BaseDescription;

/**
 * Describes the appereance of a portal. 
 * @author Erhan
 *
 */
public class PortalDescription extends BaseDescription {
	
	private String spritePath;
	
	/**
	 * Creates the description. 
	 * @param spritePath path to the texture
	 */
	public PortalDescription(String spritePath) {
		super(spritePath);
		this.spritePath = spritePath;
	}
	
	public String getSpritePath(){
		return spritePath;
	}
}