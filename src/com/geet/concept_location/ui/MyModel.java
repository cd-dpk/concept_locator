package com.geet.concept_location.ui;

import java.awt.BorderLayout;
import java.awt.TextArea;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class MyModel extends JPanel{
	public JTextArea fileName;
	public RSyntaxTextArea rSyntaxTextArea = new RSyntaxTextArea();
	public MyModel() {
		setLayout(new BorderLayout());
		fileName = new JTextArea();
		add(fileName,BorderLayout.NORTH);
		RTextScrollPane rTextScrollPane = new RTextScrollPane(rSyntaxTextArea);
		add(rTextScrollPane,BorderLayout.CENTER);
		rTextScrollPane.setFoldIndicatorEnabled(true);
	}
}
