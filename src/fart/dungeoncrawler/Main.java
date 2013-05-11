package fart.dungeoncrawler;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main
{
	public Main()
	{
		JFrame frame = new JFrame();
		frame.setTitle("Dungeon Crawler");
		
		MainPanel mainpanel = new MainPanel();
		mainpanel.setPreferredSize(new Dimension(480,480));
		frame.add(mainpanel);
		
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		frame.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				@SuppressWarnings("unused")
				Main dc = new Main();
			}
		});
	}
}
