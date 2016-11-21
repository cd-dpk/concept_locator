package com.geet.concept_location.ui;

import java.awt.BorderLayout;
import java.awt.TextArea;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class MyModel extends JPanel{
	private JTextArea fileName;
	private RSyntaxTextArea rSyntaxTextArea = new RSyntaxTextArea();
	public MyModel() {
		setLayout(new BorderLayout());
		fileName = new JTextArea();
		add(fileName,BorderLayout.NORTH);
		RTextScrollPane rTextScrollPane = new RTextScrollPane(rSyntaxTextArea);
		add(rTextScrollPane,BorderLayout.CENTER);
		rTextScrollPane.setFoldIndicatorEnabled(true);
	}
	public JTextArea getFileName() {
		return fileName;
	}
	public void setFileName(JTextArea fileName) {
		this.fileName = fileName;
	}
	public RSyntaxTextArea getrSyntaxTextArea() {
		return rSyntaxTextArea;
	}
	public void setrSyntaxTextArea(RSyntaxTextArea rSyntaxTextArea) {
		this.rSyntaxTextArea = rSyntaxTextArea;
	}
	
}
