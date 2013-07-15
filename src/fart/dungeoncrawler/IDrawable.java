package fart.dungeoncrawler;

import java.awt.Graphics2D;

/**
 * Every element in game that has to be drawn implements this interface. 
 * @author Felix
 *
 */
public interface IDrawable {
	/**
	 * Draws the object to the screen. 
	 * @param graphics
	 */
	void draw(Graphics2D graphics);
}
