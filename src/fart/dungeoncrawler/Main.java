package fart.dungeoncrawler;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * The class containing the main()-method. It creates a window and adds the game (extending JPanel) to it. 
 * @author Timo
 *
 */
public class Main
{
	/**
	 * Creates the window and game-instance. 
	 */
	public Main()
	{
		JFrame frame = new JFrame();
		frame.setTitle("Dungeon Crawler");
		
		Game mainpanel = new Game((byte)0, 1, false);
		mainpanel.setPreferredSize(new Dimension(32 * 32 - 10, 21 * 32 + 16));
		frame.add(mainpanel);
		
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		frame.setVisible(true);
		
		mainpanel.startGameLoop();
	}
	
	/**
	 * The entrypoint. 
	 * @param args
	 */
	public static void main(String[] args)
	{
		new Main();
	}
}
