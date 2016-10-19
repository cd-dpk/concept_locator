package com.geet.concept_location.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class JavaClassViewPanelUI extends JPanel{
	
	JLabel positionLabel;
	SourceViewPanel sourceViewPanel;
	Bound bound;
	public JavaClassViewPanelUI(Bound bound) {
		super();
		setLayout(null);
	}
	
	public void setPositionLabel(JLabel positionLabel) {
		this.positionLabel = positionLabel;
	}
	
	public void setSourceViewPanel(SourceViewPanel sourceViewPanel) {
		this.sourceViewPanel = sourceViewPanel;
	}

}
