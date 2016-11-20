package com.geet.concept_location.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class MyModel extends JPanel{
	public JLabel fileName;
	public RSyntaxTextArea rSyntaxTextArea = new RSyntaxTextArea();
	public MyModel() {
		setLayout(new BorderLayout());
		fileName = new JLabel();
		add(fileName,BorderLayout.NORTH);
//		add(rSyntaxTextArea, BorderLayout.SOUTH);	
		RTextScrollPane rTextScrollPane = new RTextScrollPane(rSyntaxTextArea);
		add(rTextScrollPane,BorderLayout.CENTER);
		rTextScrollPane.setFoldIndicatorEnabled(true);
	}
}
