package fart.dungeoncrawler.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.actor.Actor;
import fart.dungeoncrawler.enums.EquipSlot;

/**
 * This is the abstract base class for all items. Every item in the game has to extend it. 
 * It holds all common informations like name, icon, tooltip, prices and has an index that
 * is used primarily by the ItemCollection. It contains a list of all available items.
 * @author Svenja
 *
 */
public abstract class BaseItem implements IDrawable {
	protected static final Color ttBgColor = new Color(0.3f, 0.3f, 0.3f);
	protected static final Font fontBig = new Font("Arial", 0x1, 16);
	protected static final Font fontSmall = new Font("Arial", 0x0, 13);
	protected static final Color fontColor = new Color(1.0f, 1.0f, 1.0f);
	protected static final int border = 15;
	
	protected String name;
	protected BufferedImage icon;
	protected String tooltip;
	protected BufferedImage tooltipImage;
	protected int priceOnBuy;
	protected int priceOnSell;
	protected int index;
	protected boolean consumed;
	
	private static int numItems = 0;

	/**
	 * Creates the base for an items. 
	 * @param name name of the item
	 * @param tooltip tooltip that is shown in the shop and inventory
	 * @param iconPath path to the icon
	 * @param priceOnBuy price when buying the item
	 * @param consumed if it should disappear after using (potion etc)
	 */
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
		
		generateTooltipImage();
		
		index = numItems;
		numItems++;
	}
	
	@Override
	public void draw(Graphics2D graphics) {
	}
	
	/**
	 * This method is called from the constructor and generates an image containing the tooltip. 
	 */
	protected void generateTooltipImage() {
		tooltipImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D)tooltipImage.getGraphics();
		
		graphics.setFont(fontSmall);
		
		String tt = tooltip;
		ArrayList<String> tts = new ArrayList<String>();
		int maxw = 32 * 7 - 8;
		String[] split = tt.split(" ");
		FontMetrics met = graphics.getFontMetrics();
		String cur = "";
		String next = "";

		for(int j = 0; j < split.length; j++) {
			next += " " + split[j];
			if(met.stringWidth(next) < maxw) {
				cur += " " + split[j];
				if(j == split.length - 1)
					tts.add(new String(cur));
				continue;
			} else {
				tts.add(new String(cur));
				cur = new String(split[j]);
				next = "";
			}
		}
		
		int height = 42 + tts.size() * border;
		int width = 32 * 7;
		
		tooltipImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D)tooltipImage.getGraphics();
		
		graphics.setColor(ttBgColor);
		graphics.fillRect(0, 0, width, height);
		
		graphics.setColor(fontColor);
		graphics.setFont(fontBig);
		graphics.drawString(name, 4, 16);
		
		graphics.setFont(fontSmall);
		for(int j = 0; j < tts.size(); j++) {
			graphics.drawString(tts.get(j), 4, 42 + j * border);
		}
	}
	
	/**
	 * Used for drawing the toolltip. It takes care of drawing the tooltip in the right place (so that it does not
	 * lay partly or fully outside the window).
	 * @param graphics
	 * @param x the x coordinate of the icon
	 * @param y the y coordinate of the icon
	 */
	public void drawToolTip(Graphics2D graphics, int x, int y) {
		int xpos = x - tooltipImage.getWidth();
		int ypos = y - tooltipImage.getHeight();
		
		if(xpos <= 4)
			xpos += tooltipImage.getWidth() + 32;
		if(ypos <= 4)
			ypos += tooltipImage.getHeight() + 32;
		
		graphics.drawImage(tooltipImage, xpos, ypos, null);
	}
	
	/**
	 * Abstract method that is called when using the item. Every item has to override this.
	 * @param owner
	 */
	public abstract void use(Actor owner);
	/**
	 * Equipment-items have a specific slot. This function is responsible for returning the correct
	 * EquipSlot. 
	 * @return
	 */
	public abstract EquipSlot getSlot();

	/**
	 * Returns the item name.
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the icon.
	 * @return
	 */
	public BufferedImage getIcon() {
		return icon;
	}

	/**
	 * Returns the price for buying the item in a shop.
	 * @return
	 */
	public int getPrice() {
		return priceOnBuy;
	}

	/**
	 * Returns the item-index. 
	 * @return
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Returns if the item is consumed so that it disappears after using it. 
	 * @return
	 */
	public boolean isConsumed() {
		return consumed;
	}
}
