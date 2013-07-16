package fart.dungeoncrawler.mapeditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This is the menubar-component. 
 * @author Timo
 *
 */
@SuppressWarnings("serial")
public class MEMenuBar extends JMenuBar implements ActionListener
{
	private MapEditor me;
	private JFileChooser fc;
	private JTextField tx_mapname;
	
	/**
	 * Creates the menubar. 
	 * @param mapeditor instance of the mapeditor
	 */
	public MEMenuBar(MapEditor mapeditor)
	{
		super();
		this.me = mapeditor;
		
		init();
	}
	
	/**
	 * Initializes the menubar and puts all elements in it. 
	 */
	public void init()
	{
		JMenu file = new JMenu("Datei");
		file.setMnemonic(KeyEvent.VK_D);
		
		fc = new JFileChooser("res/maps/");
		fc.setFileFilter(new FileNameExtensionFilter("Map files(*.xml)", "xml"));
		
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
		
		
		JLabel lb_mapname = new JLabel("I Name der Map: ");
		this.add(lb_mapname);
		
		
		tx_mapname = new JTextField();
		tx_mapname.setMaximumSize(new Dimension(200, (int)tx_mapname.getPreferredSize().getHeight()));
		tx_mapname.setText("NewMap");
		this.add(tx_mapname);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String action = ((JMenuItem)e.getSource()).getName();
		
		if(action.equals("newmap"))
		{
			setMapName("NewMap");
			me.getMEPanel().init();
		}
		else if(action.equals("load"))
		{
			int returnVal = fc.showOpenDialog(me.getMEPanel());
			if(returnVal == JFileChooser.APPROVE_OPTION)
				me.getMEPanel().loadMap(fc.getSelectedFile().getPath());
		}
		else if(action.equals("save"))
		{
			int returnVal = fc.showSaveDialog(me.getMEPanel());
			if(returnVal == JFileChooser.APPROVE_OPTION)
				if(!tx_mapname.getText().equals(""))
					me.getMEPanel().saveMap(fc.getSelectedFile().getPath(), tx_mapname.getText());
		}
		else if(action.equals("close"))
		{
			System.exit(0);
		}
	}
	
	/**
	 * Sets the name of the map. 
	 * @param mapName
	 */
	public void setMapName(String mapName)
	{
		tx_mapname.setText(mapName);
	}
}
