package fart.dungeoncrawler;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Menu implements IDrawable, IUpdateable{
	private BufferedImage titleScreen;
	private BufferedImage selectionTexture;
	private Game game;
	private Controller controller;
	private int cursorPosition;
	private int optionCount;
	private HashMap<Integer, Point> positions;
	
	public Menu(Game game, Controller controller) {
		this.game = game;
		this.controller = controller;
		positions = new HashMap<Integer, Point>();
		positions.put(0, new Point(124, 175));
		positions.put(1, new Point(124, 252));
		
		cursorPosition = 0;
		optionCount = 1; //number of options in menu - 1
		
		try {
			titleScreen = ImageIO.read(new File("res/titlescreen.png"));
			selectionTexture = ImageIO.read(new File("res/selection.png"));
		} catch (IOException e) {
			System.err.println("Couldn't load images.");
		} catch (IllegalArgumentException e) {
			System.err.println("Couldn't load images.");
		}
	}
	
	@Override
	public void update(float elapsed) {
		if(controller.isDownPressed())
			cursorPosition += 1;
		if(controller.isUpPressed())
			cursorPosition -= 1;
		
		if(cursorPosition < 0)
			cursorPosition = optionCount;
		else if(cursorPosition > optionCount)
			cursorPosition = 0;
		
		if(controller.isReturnPressed()) {
			if(cursorPosition == 0) {
				//hier muss ein neues spiel gestartet werden
			} else if(cursorPosition == 1){
				//hier muss beendet werden
			}
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.drawImage(titleScreen, 0, 0, null);
		
		Point drawPosition = positions.get(cursorPosition);
		int xPos = drawPosition.x;
		int yPos = drawPosition.y;
		graphics.drawImage(selectionTexture, xPos, yPos, null);
	}
	
}
