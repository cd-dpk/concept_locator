package com.geet.concept_location.ui;

import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu {
	private JMenuItem fileNewItem,
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
	public JMenuItem getFileNewItem() {
		return fileNewItem;
	}
	public void setFileNewItem(JMenuItem fileNewItem) {
		this.fileNewItem = fileNewItem;
	}
	public JMenuItem getFileExitItem() {
		return fileExitItem;
	}
	public void setFileExitItem(JMenuItem fileExitItem) {
		this.fileExitItem = fileExitItem;
	}
	public JMenu getFileMenu() {
		return fileMenu;
	}
	public void setFileMenu(JMenu fileMenu) {
		this.fileMenu = fileMenu;
	}
	public JMenu getHelpMenu() {
		return helpMenu;
	}
	public void setHelpMenu(JMenu helpMenu) {
		this.helpMenu = helpMenu;
	}
	public JMenuBar getJbar() {
		return jbar;
	}
	public void setJbar(JMenuBar jbar) {
		this.jbar = jbar;
	}

	
}
