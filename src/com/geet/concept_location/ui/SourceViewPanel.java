package com.geet.concept_location.ui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class SourceViewPanel extends JPanel{
	
	private JTextArea sourceTextArea;
	private JScrollPane scrollPane;
	
	public SourceViewPanel(String source,Bound bound){
		super();
		setLayout(null);
		sourceTextArea = new JTextArea(source);
		scrollPane = new JScrollPane(sourceTextArea);
		scrollPane.setBounds(bound.getX(), bound.getY(), bound.getWidth(), bound.getHeigh());
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
	}

	public JTextArea getSourceTextArea() {
		return sourceTextArea;
	}

	public void setSourceTextArea(JTextArea sourceTextArea) {
		this.sourceTextArea = sourceTextArea;
	}
}
