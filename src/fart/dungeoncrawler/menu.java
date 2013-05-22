package fart.dungeoncrawler;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.event.*;
import javax.swing.*;


public class menu {

	public static void main(String[] args){
		JFrame frame = new JFrame("Menu");
		frame.setVisible(true);
		frame.setSize(600,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menubar = new JMenuBar();
		frame.setJMenuBar(menubar);
		
		JMenu file = new JMenu("Spiel");
		menubar.add(file);
		JMenuItem exit = new JMenuItem("Beenden");
		file.add(exit);
		
		JMenu help = new JMenu("Hilfe");
		menubar.add(help);
		JMenuItem about = new JMenuItem("About[FAQ]");
		help.add(about);
		
		class exitaction implements ActionListener{
			public void actionPerformed (ActionEvent e){
				System.exit(0);
			}
			
		}
		
		exit.addActionListener(new exitaction());
	}
}
