package fart.dungeoncrawler.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import javax.xml.XMLConstants;

import nu.xom.*;

@SuppressWarnings("serial")
public class MEPanel extends JPanel implements MouseInputListener
{
	private MapEditor me;
	private ImageManager imgmgr;
	
	private char walls[][];
	private Point highlight = new Point(0,0);
	
	public MEPanel(MapEditor mapeditor, ImageManager imgmgr)
	{
		super();
		if((this.me = mapeditor) == null)
		{
			System.err.println("MapEditor Object in MEPanel is null");
			System.exit(1);
		}
		
		if((this.imgmgr = imgmgr) == null)
		{
			System.err.println("ImageManager Object in MEPanel is null");
			System.exit(1);
		}
		
		this.setPreferredSize(new Dimension(me.WIDTH*me.TILE_SIZE, me.HEIGHT*me.TILE_SIZE));

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		init();
	}
	
	public void init()
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
					g2d.drawImage(imgmgr.getImage("wall"), null, i*me.TILE_SIZE, j*me.TILE_SIZE);
				else
					g2d.drawImage(imgmgr.getImage("grass"), null, i*me.TILE_SIZE, j*me.TILE_SIZE);
			}
		}
		
		g2d.setColor(Color.GRAY);
		
		// Draw vertical lines
		for(int i=1; i<me.WIDTH; i++)
			g2d.drawLine(i*me.TILE_SIZE-1, 0, i*me.TILE_SIZE-1, me.HEIGHT*me.TILE_SIZE);
		
		// Draw horizontal lines
		for(int i=1; i<me.HEIGHT; i++)
			g2d.drawLine(0, i*me.TILE_SIZE-1, me.WIDTH*me.TILE_SIZE, i*me.TILE_SIZE-1);
	
		// Draw highlighting rectangle
		g2d.setColor(Color.RED);
		g2d.drawRect((int)highlight.getX()-1, (int)highlight.getY()-1, me.TILE_SIZE, me.TILE_SIZE);
	}
	
	public void saveMap()
	{
		String mapString = "\n";
		for(int j=0; j<walls[j].length; j++)
		{
			mapString += "\t\t";
			for(int i=0; i<walls.length; i++)
			{
				mapString += walls[i][j];
			}
			mapString += "\n";
		}
		mapString += "\t";
		
		// Root Element level
		Element map = new Element("map");
		{
			// Elements to append to map
			Element map_name = new Element("name");
			map_name.appendChild("MapEditorSave");
			map.appendChild(map_name);
			
			Element map_width = new Element("width");
			map_width.appendChild("32");
			map.appendChild(map_width);
			
			Element map_height = new Element("height");
			map_height.appendChild("20");
			map.appendChild(map_height);
			
			Element map_tiles = new Element("tiles");
			Attribute map_tiles_space = new Attribute("xml:space", XMLConstants.XML_NS_URI, "preserve");
			map_tiles.addAttribute(map_tiles_space);			
			map_tiles.appendChild(mapString);
			map.appendChild(map_tiles);
			
			Element descriptions = new Element("descriptions");
			{
				// Elements to append to descriptions
				
				Element platzhalter = new Element("bla");
				platzhalter.appendChild("das hier loeschen");
				descriptions.appendChild(platzhalter);
			}
			map.appendChild(descriptions);
			
			Element gameobjects = new Element("gameobjects");
			{
				// Elements to append to gameobjects
				
				Element platzhalter = new Element("bla");
				platzhalter.appendChild("das hier loeschen");
				gameobjects.appendChild(platzhalter);
			}
			map.appendChild(gameobjects);
		}
		Document doc = new Document(map);
		
		try
		{
			FileOutputStream fop = new FileOutputStream("res/maps/L0R0ME.xml");
			Serializer serializer = new Serializer(fop, "ISO-8859-1");
			//Serializer serializer = new Serializer(System.out, "ISO-8859-1"); // DEBUG
			serializer.setIndent(8);
			serializer.setMaxLength(64);
			serializer.write(doc);
		} catch (IOException e)
		{
			System.err.println("Could not save map");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		int tileX = (int)e.getX()/me.TILE_SIZE;
		int tileY = (int)e.getY()/me.TILE_SIZE;
		
		if(tileX<0)
			tileX = 0;
		else if(tileX>=me.WIDTH)
			tileX = me.WIDTH-1;
		
		if(tileY<0)
			tileY = 0;
		else if(tileY>=me.HEIGHT)
			tileY = me.HEIGHT-1;
				
		highlight.setLocation(tileX*me.TILE_SIZE, tileY*me.TILE_SIZE);
		
		if(me.getMEToolbar().getCurrentID() == "grass")
			walls[tileX][tileY] = ' ';
		else if(me.getMEToolbar().getCurrentID() == "wall")
			walls[tileX][tileY] = '#';
		
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseClicked(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		int tileX = (int)e.getX()/me.TILE_SIZE;
		int tileY = (int)e.getY()/me.TILE_SIZE;
		
		if(tileY<0)
			tileY = 0;
		else if(tileY>=me.HEIGHT)
			tileY = me.HEIGHT-1;
		
		highlight.setLocation(tileX*me.TILE_SIZE, tileY*me.TILE_SIZE);
		
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }
}
