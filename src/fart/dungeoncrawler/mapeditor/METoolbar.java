package fart.dungeoncrawler.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class METoolbar extends JPanel implements MouseListener
{
	private MapEditor me;
	private int width;
	private int height;
	
	private ArrayList<String> objIDs = new ArrayList<String>();
	private HashMap<String, JLabel> objects = new HashMap<String, JLabel>();
	private String currentID;
	
	private Point highlight = new Point(0,0);
	
	public METoolbar(MapEditor mapeditor)
	{
		super();
		if((this.me = mapeditor) == null)
		{
			System.err.println("MapEditor Object in METoolbar is null");
			System.exit(1);
		}
		
		width = 2*me.TILE_SIZE;
		height = me.HEIGHT*me.TILE_SIZE;
		this.setPreferredSize(new Dimension(width, height));
		this.setLayout(null);

		init();
	}
	
	public void init()
	{
		BufferedImage bi;
		JLabel label;
		
		try
		{
			// grass
			bi = ImageIO.read(new File("res/grass.png"));
			label = new JLabel(new ImageIcon(bi));
			objIDs.add("grass");
			objects.put("grass", label);
			
			// wall
			bi = ImageIO.read(new File("res/wall.png"));
			label = new JLabel(new ImageIcon(bi));
			objIDs.add("wall");
			objects.put("wall", label);
			
		} catch (IOException e)
		{
			System.err.println("addButtons(): Could not load all images.");
		}
		
		// Add Buttons with Icons to the Panel
		for(int i=0; i<objIDs.size(); i++)
		{
			JLabel tmp = objects.get(objIDs.get(i));
			tmp.setSize(me.TILE_SIZE, me.TILE_SIZE);
			tmp.setLocation(this.width/2-me.TILE_SIZE/2, i*(me.TILE_SIZE+10)+10);
			if(tmp.getToolTipText() == null)
				tmp.setToolTipText(objIDs.get(i));
			tmp.setName(objIDs.get(i));
			tmp.addMouseListener(this);
			this.add(tmp);
		}
		
		highlight.setLocation(objects.get(objIDs.get(0)).getLocation());
		currentID = objIDs.get(0);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.RED);
		g2d.drawRect((int)highlight.getX()-1, (int)highlight.getY()-1, me.TILE_SIZE+1, me.TILE_SIZE+1);
		g2d.drawRect((int)highlight.getX()-2, (int)highlight.getY()-2, me.TILE_SIZE+3, me.TILE_SIZE+3);
	}
	
	public String getCurrentID()
	{
		return currentID;
	}
	
	public ArrayList<String> getObjIDs()
	{
		return objIDs;
	}
	
	public BufferedImage getIcon(String objID)
	{
		BufferedImage bi = (BufferedImage)(((ImageIcon)objects.get(objID).getIcon()).getImage());
		return bi;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		currentID = ((JLabel)e.getSource()).getName();
		highlight.setLocation(objects.get(currentID).getLocation());
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
