package fart.dungeoncrawler;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import fart.dungeoncrawler.enums.GameState;

public class Menu implements IDrawable, IUpdateable{
	private BufferedImage titleScreen;
	private BufferedImage selectionTexture;
	private Game game;
	private Controller controller;
	private int cursorPosition;
	private int optionCount;
	private HashMap<Integer, Point> positions;
	private boolean isGameStarted;
	private Font font;
	private Color fontColor;
	
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
		
		isGameStarted = false;
		font = new Font("Arial", Font.BOLD, 16);
		fontColor = new Color(50, 15, 200);
	}
	
	@Override
	public void update(float elapsed) {
		if(controller.justPressed(KeyEvent.VK_DOWN))
			cursorPosition += 1;
		if(controller.justPressed(KeyEvent.VK_UP))
			cursorPosition -= 1;
		
		if(cursorPosition < 0)
			cursorPosition = optionCount;
		else if(cursorPosition > optionCount)
			cursorPosition = 0;
		
		if(controller.justPressed(KeyEvent.VK_ENTER)) {
			if(cursorPosition == 0) {
				game.setGameState(GameState.InGame);
				game.startGame(true);
				isGameStarted = true;
			} else if(cursorPosition == 1){
				System.exit(0);
			}
		}
		
		if(controller.justPressed(KeyEvent.VK_ESCAPE))
			game.setGameState(GameState.InGame);
	}
	
	public void setGameStarted(boolean started) {
		isGameStarted = started;
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.drawImage(titleScreen, 0, 0, null);
		
		Point drawPosition = positions.get(cursorPosition);
		int xPos = drawPosition.x;
		int yPos = drawPosition.y;
		graphics.drawImage(selectionTexture, xPos, yPos, null);
		
		if(isGameStarted) {
			graphics.setFont(font);
			graphics.setColor(fontColor);
			graphics.drawString("Press Escape to resume the current game!", 30, 460);
		}
	}
	
}
