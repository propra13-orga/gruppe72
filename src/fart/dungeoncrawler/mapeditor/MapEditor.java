package fart.dungeoncrawler.mapeditor;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MapEditor
{
	public final int TILE_SIZE = 32;
	public final int WIDTH = 32;
	public final int HEIGHT = 20;
	
	private MEPanel mepanel;
	private METoolbar metoolbar;
	
	public MapEditor()
	{
		JFrame frame = new JFrame();
		frame.setTitle("Dungeoncrawler MapEditor");
		
		//TODO: Add setting bar to BorderLayout.NORTH
		
		metoolbar = new METoolbar(this);
		frame.add(metoolbar, BorderLayout.EAST);
		
		mepanel = new MEPanel(this);
		frame.add(mepanel, BorderLayout.CENTER);
		
		frame.pack();
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		frame.setVisible(true);
	}
	
	public MEPanel getMEPanel()
	{
		return mepanel;
	}
	
	public METoolbar getMEToolbar()
	{
		return metoolbar;
	}
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new MapEditor();
			}
		});
	}
}
