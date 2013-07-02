package fart.dungeoncrawler.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class METoolbar extends JPanel implements MouseListener
{
	private MapEditor me;
	private ImageManager imgmgr;
	private int width;
	private int height;
	private ArrayList<JLabel> tools;
	private int curSelection;
	
	private Point highlight = new Point(0,0);
	
	public METoolbar(MapEditor mapeditor, ImageManager imgmgr)
	{
		super();
		if((this.me = mapeditor) == null)
		{
			System.err.println("MapEditor Object in METoolbar is null");
			System.exit(1);
		}
		
		if((this.imgmgr = imgmgr) == null)
		{
			System.err.println("ImageManager Object in METoolbar is null");
			System.exit(1);
		}
		
		width = 2*me.TILE_SIZE;
		height = me.HEIGHT*me.TILE_SIZE;
		tools = new ArrayList<JLabel>();
		this.setPreferredSize(new Dimension(width, height));
		this.setLayout(null);

		init();
	}
	
	public void init()
	{
		// Add Objects to ImageManager
		imgmgr.add("grass", "res/grass.png");
		imgmgr.add("wall", "res/wall.png");

		for(int i=0; i<imgmgr.getSize(); i++)
		{
			JLabel tmp = new JLabel(new ImageIcon(imgmgr.getImage(i)));
			tmp.setLocation(this.width/2-me.TILE_SIZE/2, (me.TILE_SIZE+16)*i+16);
			tmp.setSize(me.TILE_SIZE, me.TILE_SIZE);
			tmp.setName(imgmgr.getID(i));
			tmp.addMouseListener(this);
			this.add(tmp);
			tools.add(tmp);
		}
		
		curSelection = 0;
		highlight.setLocation(tools.get(0).getLocation());
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
		return tools.get(curSelection).getName();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		curSelection = tools.indexOf(e.getSource());
		highlight.setLocation(tools.get(curSelection).getLocation());
		
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
