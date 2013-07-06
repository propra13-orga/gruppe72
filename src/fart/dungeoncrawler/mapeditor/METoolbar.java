package fart.dungeoncrawler.mapeditor;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class METoolbar extends JPanel
{
	private MapEditor me;
	private ImageManager imgmgr;
	private int width;
	private int height;
	private ArrayList<METoolpage> toolpages;
	private int curPage;
	
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
		
		width = 3*me.TILE_SIZE;
		height = me.HEIGHT*me.TILE_SIZE;
		toolpages = new ArrayList<METoolpage>();
		this.setPreferredSize(new Dimension(width, height));

		init();
	}
	
	public void init()
	{
		// Add Objects to ImageManager
		imgmgr.add("grass", ObjectCategory.misc, "res/grass.png");
		imgmgr.add("wall", ObjectCategory.misc, "res/wall.png");
		
		// Create toolpages
		METoolpage newPage;
		for(int i=0; i<5; i++)
		{
			// 0 = Category List
			// 1-4 = Categories
			newPage = new METoolpage(me, this);
			toolpages.add(newPage);
			this.add(newPage);
		}
		toolpages.get(0).setVisible(true);
		curPage=0;

		for(int i=0; i<imgmgr.getSize(); i++)
		{
			JLabel tmp = new JLabel(new ImageIcon(imgmgr.getImage(i)));
			tmp.setSize(me.TILE_SIZE, me.TILE_SIZE);
			tmp.setName(imgmgr.getID(i));
			toolpages.get(0).addTool(tmp);
		}
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}

	public String getCurrentID()
	{
		return toolpages.get(curPage).getCurrentID();
	}
}
