package fart.dungeoncrawler;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements Runnable
{
	private int room1[][];
	private BufferedImage wall, grass;
	
	public MainPanel()
	{
		super();
		initGame();
	}
	
	private void initGame()
	{
		room1 = new int[15][15];
		
		// 0 = space; 1 = wall
		// create a room with outside walls only
		for(int i=0; i<15; i++)
		{
			for(int j=0; j<15; j++)
			{
				if((i==0)||(i==14)||(j==0)||(j==14))
					room1[i][j] = 1;
				else
					room1[i][j] = 0;
			}
		}
		
		// output room1 in console (for checking)
		System.out.println("room1:");
		for(int j=0; j<15; j++)
		{
			for(int i=0; i<15; i++)
				System.out.print(room1[i][j]);
			System.out.println();
		}
		
		// Load images (put into its own class later)
		try
		{
			wall = ImageIO.read(new File("res/wall.png"));
			grass = ImageIO.read(new File("res/grass.png"));
		}
		catch (IOException e)
		{
			System.out.println("Couldn't load images");
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		for(int j=0; j<15; j++)
			for(int i=0; i<15; i++)
			{
				if(room1[i][j] == 1)
					g2d.drawImage(wall, null, i*32, j*32);
				else
					g2d.drawImage(grass, null, i*32, j*32);
			}
	}
	 
	public void run()
	{
		
	}
}
