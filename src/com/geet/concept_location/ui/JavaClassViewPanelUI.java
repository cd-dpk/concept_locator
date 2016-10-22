package com.geet.concept_location.ui;
import javax.swing.JPanel;

public class JavaClassViewPanelUI extends JPanel{
	
	SourceViewPanel sourceViewPanel;
	Bound bound;
	public JavaClassViewPanelUI(Bound bound, String source) {
		super();
		setLayout(null);
		sourceViewPanel = new SourceViewPanel(source, bound);
		sourceViewPanel.setBounds(0, 0, bound.width, bound.height);
		add(sourceViewPanel);
	}	
}