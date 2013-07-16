package fart.dungeoncrawler.mapeditor;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * This is the mainclass for the editor. It creates the window and all of its components. 
 * @author Timo
 *
 */
public class MapEditor
{
	public final int TILE_SIZE = 32;
	public final int WIDTH = 32;
	public final int HEIGHT = 20;
	
	private ImageManager imgmgr;
	private MEMenuBar menubar;
	private MEPanel mepanel;
	private METoolbar metoolbar;
	
	/**
	 * Creates and initializes the editor. 
	 */
	public MapEditor(boolean isLoneWindow)
	{
		JFrame frame = new JFrame();
		frame.setTitle("Dungeoncrawler MapEditor");
		
		imgmgr = new ImageManager();
		
		menubar = new MEMenuBar(this);
		frame.setJMenuBar(menubar);
		
		metoolbar = new METoolbar(this, imgmgr);
		frame.add(metoolbar, BorderLayout.EAST);
		
		mepanel = new MEPanel(this, imgmgr);
		frame.add(mepanel, BorderLayout.CENTER);
		
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		
		if(isLoneWindow)
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		else
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.setResizable(false);
		
		frame.setVisible(true);
	}
	
	/**
	 * Returns the imagemanager. 
	 * @return
	 */
	public ImageManager getImageManager()
	{
		return imgmgr;
	}
	
	/**
	 * Returns the menubar. 
	 * @return
	 */
	public MEMenuBar getMEMenuBar()
	{
		return menubar;
	}
	
	/**
	 * Returns the mainpanel. 
	 * @return
	 */
	public MEPanel getMEPanel()
	{
		return mepanel;
	}
	
	/**
	 * Returns the toolbar.
	 * @return
	 */
	public METoolbar getMEToolbar()
	{
		return metoolbar;
	}
	
	/**
	 * Mainentry for the mapeditor. 
	 * @param args
	 */
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new MapEditor(true);
			}
		});
	}
}
