package fart.dungeoncrawler.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MEPanel extends JPanel
{
	private MapEditor me;
	
	private char walls[][];
	private Point highlight = new Point(0,0);
	
	private BufferedImage wall, grass;
	
	
	public MEPanel(MapEditor mapeditor)
	{
		super();
		if((this.me = mapeditor) == null)
		{
			System.err.println("me (MapEditor Object) in MEPanel is null");
			System.exit(1);
		}
		
		this.setPreferredSize(new Dimension(me.WIDTH*me.TILE_SIZE, me.HEIGHT*me.TILE_SIZE));
		MEInputHandler ih = new MEInputHandler();
		this.addMouseListener(ih);
		this.addMouseMotionListener(ih);
		
		init();
	}
	
	private void init()
	{
		walls = new char[me.WIDTH][me.HEIGHT];
		for(int j=0; j<me.HEIGHT; j++)
		{
			for(int i=0; i<me.WIDTH; i++)
			{
				// Borders = '#' (wall)
				if(i==0 || j==0 || i == me.WIDTH-1 || j == me.HEIGHT-1)
					walls[i][j] = '#';
				else
					walls[i][j] = ' ';
			}
		}
		
		try
		{
			wall = ImageIO.read(new File("res/wall.png"));
			grass = ImageIO.read(new File("res/grass.png"));
		} catch (IOException e)
		{
			System.err.println("Could not load images wall.png and/or grass.png");
			System.exit(1);
		}
	}
	
	public void useMouseLocation(Point p, boolean clicked)
	{
		int tileX = (int)p.getX()/me.TILE_SIZE;
		int tileY = (int)p.getY()/me.TILE_SIZE;
		
		highlight.setLocation(tileX*me.TILE_SIZE, tileY*me.TILE_SIZE);
		
		if(clicked)
		{
			if(walls[tileX][tileY] == ' ')
				walls[tileX][tileY] = '#';
			else
				walls[tileX][tileY] = ' ';
		}
		
		repaint();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		// Draw walls and grass
		for(int j=0; j<me.HEIGHT; j++)
		{
			for(int i=0; i<me.WIDTH; i++)
			{
				if(walls[i][j] == '#')
					g2d.drawImage(wall, null, i*me.TILE_SIZE, j*me.TILE_SIZE);
				else
					g2d.drawImage(grass, null, i*me.TILE_SIZE, j*me.TILE_SIZE);
			}
		}
		
		g2d.setColor(Color.GRAY);
		
		// Draw vertical lines
		for(int i=1; i<me.WIDTH; i++)
			g2d.drawLine(i*me.TILE_SIZE-1, 0, i*me.TILE_SIZE-1, me.HEIGHT*me.TILE_SIZE);
		
		// Draw horizontal lines
		for(int i=1; i<me.HEIGHT; i++)
			g2d.drawLine(0, i*me.TILE_SIZE-1, me.WIDTH*me.TILE_SIZE, i*me.TILE_SIZE-1);
	
		g2d.setColor(Color.RED);
		g2d.drawRect((int)highlight.getX()-1, (int)highlight.getY()-1, me.TILE_SIZE, me.TILE_SIZE);
	}
}
