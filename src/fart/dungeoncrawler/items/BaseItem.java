package fart.dungeoncrawler.items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.actor.Actor;

public abstract class BaseItem implements IDrawable {
	protected String name;
	protected BufferedImage icon;
	protected String tooltip;
	protected int priceOnBuy;
	protected int priceOnSell;
	protected int index;
	protected boolean consumed;
	
	private static int numItems = 0;

	public BaseItem(String name, String tooltip, String iconPath, int priceOnBuy, boolean consumed) {
		this.name = name;
		this.tooltip = tooltip;
		this.priceOnBuy = priceOnBuy;
		this.priceOnSell = priceOnBuy / 2;
		this.consumed = consumed;
		
		try {
			icon = ImageIO.read(new File(iconPath));
		} catch (IOException e) {
			System.out.println("Could not load image.");
			e.printStackTrace();
		}
		
		index = numItems;
		index++;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
		// TODO Auto-generated method stub
		
	}
	
	public abstract void use(Actor owner);

	public String getName() {
		return name;
	}

	public BufferedImage getIcon() {
		return icon;
	}

	public String getTooltip() {
		return tooltip;
	}

	public int getPrice() {
		return priceOnBuy;
	}

	public int getIndex() {
		return index;
	}
	
	public boolean isConsumed() {
		return consumed;
	}
}
