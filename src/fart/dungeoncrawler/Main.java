package fart.dungeoncrawler;

import java.awt.Dimension;

import javax.swing.JFrame;
//import javax.swing.SwingUtilities;

public class Main
{
	public Main()
	{
		JFrame frame = new JFrame();
		frame.setTitle("Dungeon Crawler");
		
		Game mainpanel = new Game((byte)0, 1, false);
		mainpanel.setPreferredSize(new Dimension(32 * 32,20 * 32));
		frame.add(mainpanel);
		
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		frame.setVisible(true);
		
		mainpanel.startGameLoop();
	}
	
	public static void main(String[] args)
	{
		//SwingUtilities.invokeLater(new Runnable()
		//{
		//	public void run()
		//	{
		//		@SuppressWarnings("unused")
		//		Main dc = new Main();
		//	}
		//});
		new Main();
	}
}
