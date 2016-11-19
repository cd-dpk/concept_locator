package com.geet.concept_location.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

import com.geet.concept_location.constants.UIConstants;

public class WindowActionHandler implements ActionListener {
	private Window win;
	public WindowActionHandler(Window w) {
		win = w;
	}
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if("TabClose".equals(command)) {
			// usuń tab
			int pos = win.getTabs().getSelectedIndex();
			AppManager.getDocuments().remove(pos);
			win.getTabs().remove(pos);
		}
		if(e.getSource().equals(win.getMenu().fileOpenItem)) {
			final JFileChooser fc = new JFileChooser();
			int ret = fc.showOpenDialog(win.getFrame());
			if(ret == JFileChooser.APPROVE_OPTION) {
			/*	File file = fc.getSelectedFile();
				String val = "";
				try {
					val = AppManager.readFile(file);
				} catch (IOException exc) {
					exc.printStackTrace();
				}
				Page d = new Page(file);
				d.setEditor(new RSyntaxTextArea());
			    d.getEditor().setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
			    d.getEditor().setCodeFoldingEnabled(true);
			    d.getEditor().setAntiAliasingEnabled(true);
				//RTextScrollPane sp = new RTextScrollPane(d.getEditor());
				d.setScroll(new RTextScrollPane(d.getEditor()));
				d.getScroll().setFoldIndicatorEnabled(true);
				JPanel t = d.getTab();
				t.add(d.getScroll(), BorderLayout.CENTER);
				t.doLayout();
				d.setType(SyntaxConstants.SYNTAX_STYLE_JAVA);
				d.setText(val);
				//d.getEditor().setLocation(0, 0);
				
				//d.getEditor().
				//AppManager.addDocument(d);
				win.getTabs().addTab(d.getFilename(), t);
				int index = win.getTabs().getTabCount()-1;
				JPanel pnlTab = new JPanel(new GridBagLayout()), iconPanel = new JPanel(new GridLayout(1, 1, 4, 4));
				//iconPanel.setPreferredSize(new Dimension(12, 12));
				pnlTab.setOpaque(false);
				JLabel lblTitle = new JLabel(d.getFilename());
				ImageIcon icon = new ImageIcon("res/close.png");
				JButton btnClose = new JButton(new ImageIcon(icon.getImage().getScaledInstance(12, 12, 4)));
				btnClose.setActionCommand("TabClose");
				btnClose.setOpaque(false);
				btnClose.setBorderPainted(false);
				btnClose.setContentAreaFilled(false);
				btnClose.setFocusPainted(false);
				btnClose.setPreferredSize(new Dimension(11, 15));

				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.weightx = 1;
				gbc.ipadx = 5;

				pnlTab.add(lblTitle, gbc);

				gbc.gridx++;
				gbc.weightx = 0;
				gbc.ipadx = 5;
				iconPanel.add(btnClose);
				pnlTab.add(btnClose, gbc);

				win.getTabs().setTabComponentAt(index, pnlTab);

				btnClose.addActionListener(this);
				try {
					AppManager.addDocument(d);
				} catch (Exception exc) {
					System.out.println("Nie spodziewałem się Hiszpańskiej Inkwizycji!");
				}
				win.getFrame().repaint();
*/			}
		} else if (e.getSource().equals(win.getMenu().fileNewItem)) {
			ClassPage classPage = new ClassPage("New");
			classPage.setEditor(new RSyntaxTextArea());
		    classPage.getEditor().setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		    classPage.getEditor().setCodeFoldingEnabled(true);
		    classPage.getEditor().setAntiAliasingEnabled(true);
			//RTextScrollPane sp = new RTextScrollPane(d.getEditor());
			classPage.setScroll(new RTextScrollPane(classPage.getEditor()));
			classPage.getScroll().setFoldIndicatorEnabled(true);
			JPanel t = classPage.getTabPanel();
			t.add(classPage.getScroll(), BorderLayout.CENTER);
			t.doLayout();
			classPage.setType(SyntaxConstants.SYNTAX_STYLE_JAVA);
			AppManager.addDocument(classPage);
			ImageIcon icon = new ImageIcon("src/res/close.png");
			win.getTabs().addTab(classPage.getFilename(), t);
			int index = win.getTabs().getTabCount()-1;
			JPanel pnlTab = new JPanel(new GridBagLayout()), iconPanel = new JPanel(new GridLayout(1, 1, 4, 4));
			//iconPanel.setPreferredSize(new Dimension(12, 12));
			pnlTab.setOpaque(false);
			JLabel lblTitle = new JLabel(classPage.getFilename());
			JButton btnClose = new JButton(new ImageIcon(icon.getImage().getScaledInstance(12, 12, 4)));
			btnClose.setActionCommand("TabClose");
			btnClose.setOpaque(false);
			btnClose.setBorderPainted(false);
			btnClose.setContentAreaFilled(false);
			btnClose.setFocusPainted(false);
			//btnClose.setSize(10, 10);
			btnClose.setPreferredSize(new Dimension(11, 15));

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.ipadx = 5;

			pnlTab.add(lblTitle, gbc);

			gbc.gridx++;
			gbc.weightx = 0;
			gbc.ipadx = 5;
			iconPanel.add(btnClose);
			pnlTab.add(btnClose, gbc);

			win.getTabs().setTabComponentAt(index, pnlTab);

			btnClose.addActionListener(this);
		} else if (e.getSource().equals(win.getMenu().fileCloseItem)) {
			AppManager.getDocuments().remove(win.getTabs().getSelectedIndex());
			win.getTabs().remove(win.getTabs().getSelectedIndex());
		} else if(e.getSource().equals(win.getMenu().helpAboutItem)) {			/*win.setAboutFrame(new JFrame("About"));
			win.getAboutFrame().setSize(500, 500);
			win.getAboutFrame().setVisible(true);
			JPanel panel = new JPanel(new BorderLayout());
			JLabel top = new JLabel("Simple Text Editor version " + AppManager.getVersion());
			String text = "Simple Text Editor (copyright &copy; Michał Bartecki, Krzysztof 'hun7er' Marciniak)\n" +
						  ",abbreviated STE, is a text edittor written ina Java for OOP classes at\n" +
						  "Poznań University of Technology.";
			JLabel center = new JLabel(text);
			panel.add(top, BorderLayout.PAGE_START);
			panel.add(center, BorderLayout.CENTER);
			win.getAboutFrame().add(panel);*/
		}
		//System.out.println("wat");
        //...Get information from the action event...
        //...Display it in the text area...
    }

}
