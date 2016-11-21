package com.geet.concept_location.ui;

import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu {
	public JMenuItem fileNewItem,
					 fileExitItem;
	private JMenu fileMenu, helpMenu;
	private JMenuBar jbar;
	
	public JMenuBar getMenuBar() {
		return jbar;
	}
	private void createMenu(ActionListener l) {
		jbar = new JMenuBar();
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");
		
		fileNewItem = new JMenuItem("New");
		fileNewItem.addActionListener(l);
		fileExitItem = new JMenuItem("Exit");
		fileExitItem.addActionListener(l);
		
		fileMenu.add(fileNewItem);
		fileMenu.add(fileExitItem);
		jbar.add(fileMenu);
		jbar.add(helpMenu);
	}
	
	public Menu(ActionListener l) {
		createMenu(l);
	}
}
