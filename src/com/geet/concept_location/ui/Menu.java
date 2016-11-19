package com.geet.concept_location.ui;

import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu {
	public JMenuItem fileNewItem,
					 fileOpenItem,
					 fileCloseItem,
					 helpAboutItem = null;
	private JMenu fileMenu, helpMenu;
	private JMenuBar jbar;
	
	public JMenuBar getMenuBar() {
		return jbar;
	}
	
	private void createMenu(ActionListener l) {
		jbar = new JMenuBar();
		//menuItem = new JMenuItem("Open", KeyEvent.VK_O);
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");
		
		fileNewItem = new JMenuItem("New");
		fileNewItem.addActionListener(l);
		fileOpenItem = new JMenuItem("Open");
		fileOpenItem.addActionListener(l);
		fileCloseItem = new JMenuItem("Close");
		fileCloseItem.addActionListener(l);
		helpAboutItem = new JMenuItem("About");
		helpAboutItem.addActionListener(l);
		
		fileMenu.add(fileNewItem);
		fileMenu.add(fileOpenItem);
		fileMenu.add(fileCloseItem);
		
		helpMenu.add(helpAboutItem);
		
		jbar.add(fileMenu);
		jbar.add(helpMenu);
	}
	
	public Menu(ActionListener l) {
		createMenu(l);
	}
}
