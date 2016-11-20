package com.geet.concept_location.ui;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.geet.concept_location.constants.UIConstants;

public class ClassPage extends Page{

	JPanel tabPanel = new JPanel();
	
	public ClassPage(File f) {
		super(f);
		getTabPanel().setLayout(new BorderLayout());
	}
	public JPanel getTabPanel() {
		return tabPanel;
	}
	public void setTabPanel(JPanel tabPanel) {
		this.tabPanel = tabPanel;
	}
	public ClassPage(String string) {
		super(string);
		getTabPanel().setLayout(new BorderLayout());
	}
	private RSyntaxTextArea editor;
	private RTextScrollPane linenumbers;

	public void setEditor(RSyntaxTextArea e) {
		editor = e;
		editor.setEditable(false);
	}
	public void setScroll(RTextScrollPane s) {
		linenumbers = s;
	}
	public void setText(String s) {
		editor.setText(s);
	}
	public void setType(String s) {
		//editor.setContentType(s);
		editor.setSyntaxEditingStyle(s);
	}
	public RSyntaxTextArea getEditor() {
		return editor;
	}
	public RTextScrollPane getScroll() {
		return linenumbers;
	}
	
}
