package fart.dungeoncrawler.mapeditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MEObjectSettings extends JDialog implements ActionListener
{
	private boolean cancelled;
	private MapToInfo mapToInfo;
	private JTextField tx_name, tx_posX, tx_posY;
	
	public MEObjectSettings()
	{
		super();
		
		init();
	}
	
	public void init()
	{
		cancelled = true;
		mapToInfo = new MapToInfo();
		
		// create MainPanel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setPreferredSize(new Dimension(230, 140));
		
		// add mapToName input option
		JLabel lb_name = new JLabel("mapToName: ");
		lb_name.setSize((int)lb_name.getPreferredSize().getWidth(), (int)lb_name.getPreferredSize().getHeight());
		lb_name.setLocation(10, 10);
		mainPanel.add(lb_name);
		
		int textFieldX = (int)(lb_name.getLocation().getX()+lb_name.getSize().getWidth())+10;
		
		tx_name = new JTextField();
		tx_name.setSize(100, (int)tx_name.getPreferredSize().getHeight());
		tx_name.setLocation(textFieldX, 10);
		mainPanel.add(tx_name);
		
		// add mapToX input option
		JLabel lb_posX = new JLabel("mapToX:");
		lb_posX.setSize((int)lb_posX.getPreferredSize().getWidth(), (int)lb_posX.getPreferredSize().getHeight());
		lb_posX.setLocation(10, 40);
		mainPanel.add(lb_posX);
		
		tx_posX = new JTextField();
		tx_posX.setSize(100, (int)tx_posX.getPreferredSize().getHeight());
		tx_posX.setLocation(textFieldX, 40);
		mainPanel.add(tx_posX);
		
		// add mapToY input option
		JLabel lb_posY = new JLabel("mapToY:");
		lb_posY.setSize((int)lb_posY.getPreferredSize().getWidth(), (int)lb_posY.getPreferredSize().getHeight());
		lb_posY.setLocation(10, 70);
		mainPanel.add(lb_posY);
		
		tx_posY = new JTextField();
		tx_posY.setSize(100, (int)tx_posY.getPreferredSize().getHeight());
		tx_posY.setLocation(textFieldX, 70);
		mainPanel.add(tx_posY);
		
		// add OK Button
		JButton btn_ok = new JButton("Okay");
		btn_ok.setName("ok");
		btn_ok.setSize(97,30);
		btn_ok.setLocation(10, 100);
		btn_ok.addActionListener(this);
		mainPanel.add(btn_ok);
		
		// add CANCEL Button
		JButton btn_cancel = new JButton("Cancel");
		btn_cancel.setName("cancel");
		btn_cancel.setSize(97,30);
		btn_cancel.setLocation(118, 100);
		btn_cancel.addActionListener(this);
		mainPanel.add(btn_cancel);
		
		// show dialog
		this.add(mainPanel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setModal(true);
		this.setVisible(true);
	}
	
	public MapToInfo getMapToInfo()
	{
		return mapToInfo;
	}
	
	public boolean wasCancelled()
	{
		return cancelled;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String name = ((JButton) e.getSource()).getName();
		
		if(name.equals("ok"))
		{
			cancelled = false;
			mapToInfo.setMapToName(tx_name.getText());
			mapToInfo.setMapToX(tx_posX.getText());
			mapToInfo.setMapToY(tx_posY.getText());
			this.dispose();
		}
		else if(name.equals("cancel"))
		{
			cancelled = true;
			this.dispose();
		}
		else if(name.equals("search"))
		{
			
		}
	}

}
