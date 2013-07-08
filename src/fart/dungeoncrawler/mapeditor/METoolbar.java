package fart.dungeoncrawler.mapeditor;

import java.awt.Dimension;
import java.awt.Font;
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
		// MISC
		imgmgr.add("grass", ObjectCategory.misc, "res/grass.png");
		imgmgr.add("wall", ObjectCategory.misc, "res/wall.png");
		imgmgr.add("0", ObjectCategory.misc, "res/tp.png");
		imgmgr.add("1", ObjectCategory.misc, "res/goal.png");
		imgmgr.add("2", ObjectCategory.misc, "res/trap.png");
		imgmgr.add("7", ObjectCategory.misc, "res/tp2.png");
		
		// ENEMIES
		imgmgr.add("3", ObjectCategory.enemies, "res/enemy1.png");
		imgmgr.add("4", ObjectCategory.enemies, "res/enemy1.png");
		
		// NPCs
		imgmgr.add("5", ObjectCategory.npcs, "res/shop.png");
		imgmgr.add("6", ObjectCategory.npcs, "res/shop.png");
		
		// Items
		imgmgr.add("100", ObjectCategory.items, "res/icons/smallhealpotion.png");
		imgmgr.add("101", ObjectCategory.items, "res/icons/smallmanapotion.png");
		imgmgr.add("102", ObjectCategory.items, "res/icons/leatherarmor.png");
		imgmgr.add("103", ObjectCategory.items, "res/icons/leathergloves.png");
		imgmgr.add("104", ObjectCategory.items, "res/icons/leatherboots.png");
		imgmgr.add("105", ObjectCategory.items, "res/icons/rustyhelmet.png");
		imgmgr.add("106", ObjectCategory.items, "res/icons/ring1.png");
		imgmgr.add("107", ObjectCategory.items, "res/icons/sword.png");
		imgmgr.add("108", ObjectCategory.items, "res/icons/staff.png");
		
		
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

		
		JLabel tmp;
		// Create category page
		// MISC Page
		tmp = new JLabel("MISC");
		tmp.setSize(me.TILE_SIZE, me.TILE_SIZE);
		tmp.setFont(new Font(tmp.getFont().getName(), tmp.getFont().getStyle(), 11));
		tmp.setName("cV1");
		toolpages.get(0).addTool(tmp);
		
		// ENEMIES Page
		tmp = new JLabel("ENEMIES");
		tmp.setSize(me.TILE_SIZE, me.TILE_SIZE);
		tmp.setFont(new Font(tmp.getFont().getName(), tmp.getFont().getStyle(), 6));
		tmp.setName("cV2");
		toolpages.get(0).addTool(tmp);
		
		// NPCs Page
		tmp = new JLabel("NPCs");
		tmp.setSize(me.TILE_SIZE, me.TILE_SIZE);
		tmp.setFont(new Font(tmp.getFont().getName(), tmp.getFont().getStyle(), 10));
		tmp.setName("cV3");
		toolpages.get(0).addTool(tmp);
		
		// ITEMS Page
		tmp = new JLabel("ITEMS");
		tmp.setSize(me.TILE_SIZE, me.TILE_SIZE);
		tmp.setFont(new Font(tmp.getFont().getName(), tmp.getFont().getStyle(), 10));
		tmp.setName("cV4");
		toolpages.get(0).addTool(tmp);
		
		toolpages.get(0).setCurrentSelection(0);
		
		// Put a BACK Button on every tool page
		for(int i=1; i<5; i++)
		{
			tmp = new JLabel("BACK");
			tmp.setSize(me.TILE_SIZE, me.TILE_SIZE);
			tmp.setFont(new Font(tmp.getFont().getName(), tmp.getFont().getStyle(), 10));
			tmp.setName("cV0");
			toolpages.get(i).addTool(tmp);
		}
		
		// Put tools on toolpages
		for(int i=0; i<imgmgr.getSize(); i++)
		{
			tmp = new JLabel(new ImageIcon(imgmgr.getImage(i)));
			tmp.setSize(me.TILE_SIZE, me.TILE_SIZE);
			
			String id = imgmgr.getID(i);
			tmp.setName(id);
			
			toolpages.get(imgmgr.getCategory(id)+1).addTool(tmp);
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
	
	public void changeView(int pageNr)
	{
		if((pageNr >= 0) && (pageNr < toolpages.size()))
		{
			for(int i=0; i<toolpages.size(); i++)
			{
				if(i!=pageNr)
				{
					toolpages.get(i).setVisible(false);
				}
				else
				{
					toolpages.get(i).setVisible(true);
					
					if(i != 0)
						curPage = i;
				}
			}
		}
		else
			System.err.println("toolpage "+pageNr+" does not exist!");
	}
}
