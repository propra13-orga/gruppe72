package fart.dungeoncrawler.mapeditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class MEMenuBar extends JMenuBar implements ActionListener
{
	MapEditor me;
	
	public MEMenuBar(MapEditor mapeditor)
	{
		super();
		this.me = mapeditor;
		
		init();
	}
	
	public void init()
	{
		JMenu file = new JMenu("Datei");
		file.setMnemonic(KeyEvent.VK_D);
		
		JMenuItem newmap = new JMenuItem("Neue Map", KeyEvent.VK_N);
		newmap.setName("newmap");
		newmap.addActionListener(this);
		file.add(newmap);
		
		JMenuItem load = new JMenuItem("Laden", KeyEvent.VK_L);
		load.setName("load");
		load.addActionListener(this);
		file.add(load);
		
		JMenuItem save = new JMenuItem("Speichern", KeyEvent.VK_S);
		save.setName("save");
		save.addActionListener(this);
		file.add(save);
		
		JMenuItem close = new JMenuItem("Schließen", KeyEvent.VK_C);
		close.setName("close");
		close.addActionListener(this);
		file.add(close);
		
		this.add(file);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String action = ((JMenuItem)e.getSource()).getName();
		
		if(action.equals("newmap"))
		{
			me.getMEPanel().init();
		}
		else if(action.equals("load"))
		{
			
		}
		else if(action.equals("save"))
		{
			me.getMEPanel().saveMap();
		}
		else if(action.equals("close"))
		{
			System.exit(0);
		}
	}
}
