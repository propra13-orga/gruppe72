package fart.dungeoncrawler;

public class PortalDescription extends BaseDescription {
	
	private String spritePath;
	
	public PortalDescription(String spritePath) {
		super(spritePath);
		this.spritePath = spritePath;
	}
	
	public String getSpritePath(){
		return spritePath;
	}
}
