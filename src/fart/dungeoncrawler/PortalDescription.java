package fart.dungeoncrawler;

public class PortalDescription extends BaseDescription {
	private String mapTo;
	
	public PortalDescription(String spritePath, String map) {
		super(spritePath);
		mapTo = map;
	}
}
