package fart.dungeoncrawler.mapeditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class represents one category of the toolbar.
 * @author Timo
 *
 */
@SuppressWarnings("serial")
public class METoolpage extends JPanel implements MouseListener
{
	private MapEditor me;
	private METoolbar toolbar;
	
	private ArrayList<JLabel> tools;
	private int curSelection;
	private Point highlight;
	
	/**
	 * Creates an empty toolpage. 
	 * @param mapeditor instance of the editor
	 * @param toolbar instance of the toolbar
	 */
	public METoolpage(MapEditor mapeditor, METoolbar toolbar)
	{
		super();
		if((this.me = mapeditor) == null)
		{
			System.err.println("MapEditor Object in METoolpage is null");
			System.exit(1);
		}
		
		if((this.toolbar = toolbar) == null)
		{
			System.err.println("METoolbar Object in METoolpage is null");
			System.exit(1);
		}
		
		this.setVisible(false);
		this.setLocation(0, 0);
		this.setPreferredSize(new Dimension(toolbar.getWidth(), toolbar.getHeight()));
		this.setLayout(null);
		
		init();
	}
	
	/**
	 * Initializes an empty page. 
	 */
	public void init()
	{
		tools = new ArrayList<JLabel>();
		curSelection = 1;
		highlight = new Point(0,0);
	}
	
	/**
	 * Adds a tool to the list of all tools. 
	 * @param tool
	 */
	public void addTool(JLabel tool)
	{
		tool.setLocation(toolbar.getWidth()/2-me.TILE_SIZE/2, (me.TILE_SIZE+16)*tools.size()+16);
		tool.addMouseListener(this);
		tools.add(tool);
		this.add(tool);
		
		if(tools.size() == 2)
			highlight.setLocation(tool.getLocation());
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.RED);
		g2d.drawRect((int)highlight.getX()-1, (int)highlight.getY()-1, me.TILE_SIZE+1, me.TILE_SIZE+1);
		g2d.drawRect((int)highlight.getX()-2, (int)highlight.getY()-2, me.TILE_SIZE+3, me.TILE_SIZE+3);
	}
	
	/**
	 * Returns the ID of the current selection.
	 * @return
	 */
	public String getCurrentID()
	{
		return tools.get(curSelection).getName();
	}
	
	/**
	 * Sets the current selection.
	 * @param select index of the selected tool
	 */
	public void setCurrentSelection(int select)
	{
		curSelection = select;
		highlight.setLocation(tools.get(curSelection).getLocation());
		repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		String name = ((JLabel) e.getSource()).getName();

		if((name.length() > 2) && (name.substring(0, 2).equals("cV")))
		{
			toolbar.changeView(Integer.valueOf(name.substring(2, 3)));
		}
		else
		{
			curSelection = tools.indexOf(e.getSource());
			highlight.setLocation(tools.get(curSelection).getLocation());
		}
		
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
