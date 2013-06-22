package fart.dungeoncrawler.items;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Utils.Vector2;

import fart.dungeoncrawler.Controller;
import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.Tilemap;

public class IconController implements IDrawable, IUpdateable {
	private Controller controller;
	private Vector2 startPosition;
	private int border;
	private int rows;
	private int columns;
	private int curX;
	private int curY;
	private BufferedImage frame;
	
	public IconController(Controller controller, Vector2 startPos, int border, int columns, int rows) {
		this.controller = controller;
		this.startPosition = startPos;
		this.border = border;
		this.columns = columns;
		this.rows = rows;
		
		try {
			frame = ImageIO.read(new File("res/iconFrame.png"));
		} catch (IOException e) {
			System.out.println("Could not load image.");
			e.printStackTrace();
		}
		
		curX = 0;
		curY = 0;
	}
	
	public int getCurrentIndex() {
		return curX + curY * rows;
	}

	@Override
	public void update(float elapsed) {
		if(controller.justPressed(KeyEvent.VK_RIGHT))
			curX += 1;
		else if(controller.justPressed(KeyEvent.VK_LEFT))
			curX -= 1;
		else if(controller.justPressed(KeyEvent.VK_DOWN))
			curY += 1;
		else if(controller.justPressed(KeyEvent.VK_UP))
			curY -= 1;
		
		if(curX < 0)
			curX = columns - 1;
		else if(curX >= columns)
			curX = 0;
		else if(curY < 0)
			curY = rows - 1;
		else if(curY >= rows)
			curY = 0;
	}

	@Override
	public void draw(Graphics2D graphics) {
		int xPos = curX * Tilemap.TILE_SIZE;
		xPos += (curX * border);
		xPos += startPosition.x;
		xPos -= 2;
		
		int yPos = curY * Tilemap.TILE_SIZE;
		yPos += (curY * border);
		yPos += startPosition.y;
		yPos -= 2;
		
		graphics.drawImage(frame, xPos, yPos, null);
	}

}
