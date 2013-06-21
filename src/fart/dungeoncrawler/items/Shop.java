package fart.dungeoncrawler.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Utils.Vector2;

import fart.dungeoncrawler.Controller;
import fart.dungeoncrawler.IDrawable;
import fart.dungeoncrawler.IUpdateable;
import fart.dungeoncrawler.Tilemap;

public class Shop implements IDrawable, IUpdateable {
	private static Vector2 startPosition = new Vector2(50, 50);
	private static int borderSize = 8;
	private static int columns = 5;
	private static int rows = 6;
	private static int maxItems = columns * rows;
	
	private ArrayList<BaseItem> items;
	private Controller controller;
	private IconController iconController;
	
	//DEBUG
	private int iCount = 26;
	
	public Shop(Controller controller) {
		this.controller = controller;
		iconController = new IconController(controller, startPosition, borderSize, columns, rows);
	}

	@Override
	public void update(float elapsed) {
		iconController.update(elapsed);
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(new Color(1.0f, 0.0f, 1.0f));
		
		for(int i = 0; i < iCount; i++) {
			int x = i % columns;
			int xPos = x * Tilemap.TILE_SIZE;
			xPos += (x * borderSize);
			xPos += startPosition.x;
			
			int y = i / columns;
			int yPos = y * Tilemap.TILE_SIZE;
			yPos += (y * borderSize);
			yPos += startPosition.y;
			
			graphics.fillRect(xPos, yPos, Tilemap.TILE_SIZE, Tilemap.TILE_SIZE);
		}
		
		iconController.draw(graphics);
	}
}
