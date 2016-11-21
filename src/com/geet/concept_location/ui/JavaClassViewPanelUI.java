package com.geet.concept_location.ui;
import javax.swing.JPanel;
public class JavaClassViewPanelUI extends JPanel{
	private SourceViewPanel sourceViewPanel;
	private Bound bound;
	public SourceViewPanel getSourceViewPanel() {
		return sourceViewPanel;
	}
	public void setSourceViewPanel(SourceViewPanel sourceViewPanel) {
		this.sourceViewPanel = sourceViewPanel;
	}
	public Bound getBound() {
		return bound;
	}
	public void setBound(Bound bound) {
		this.bound = bound;
	}
	public JavaClassViewPanelUI(Bound bound, String source) {
		super();
		setLayout(null);
		sourceViewPanel = new SourceViewPanel(source, bound);
		sourceViewPanel.setBounds(0, 0, bound.getWidth(), bound.getHeigh());
		add(sourceViewPanel);
	}	
}
