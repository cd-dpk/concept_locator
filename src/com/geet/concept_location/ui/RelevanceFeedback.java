package com.geet.concept_location.ui;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class RelevanceFeedback extends JPanel {
	
	private JRadioButton relButton, irrelButton, normalButton;
	private JLabel relLabel, irrelLabel, normalLabel;
	private JLabel roundLabel;
	private JButton relevanceFeedback;
	
	public RelevanceFeedback(Bound bound) {
		super();
		setLayout(null);
		relButton = new JRadioButton("REL");
		relButton.setBounds(0, 0, 70, 20);
		add(relButton);
		
		relLabel = new JLabel("REL");
		relLabel.setBounds(75, 0, 70, 20);
		add(relLabel);
		
		irrelButton = new JRadioButton("IRREL");
		irrelButton.setBounds(150, 0, 70, 20);
		add(irrelButton);
		
		irrelLabel = new JLabel("IRREL");
		irrelLabel.setBounds(230, 0, 70, 20);
		add(irrelLabel);
		
		normalButton = new JRadioButton("NORMAL");
		normalButton.setBounds(310, 0, 100, 20);
		add(normalButton);

		
		relevanceFeedback = new JButton("IRRF");
		relevanceFeedback.setBounds(602, 0, 200, 30);
		add(relevanceFeedback);
		
		roundLabel = new JLabel("RF");
		roundLabel.setBounds(420, 0, 100, 20);
		add(roundLabel);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(relButton);
		buttonGroup.add(irrelButton);
		buttonGroup.add(normalButton); 
	}

	public JRadioButton getRelButton() {
		return relButton;
	}

	public void setRelButton(JRadioButton relButton) {
		this.relButton = relButton;
	}

	public JRadioButton getIrrelButton() {
		return irrelButton;
	}

	public void setIrrelButton(JRadioButton irrelButton) {
		this.irrelButton = irrelButton;
	}

	public JRadioButton getNormalButton() {
		return normalButton;
	}

	public void setNormalButton(JRadioButton normalButton) {
		this.normalButton = normalButton;
	}

	public JLabel getRelLabel() {
		return relLabel;
	}

	public void setRelLabel(JLabel relLabel) {
		this.relLabel = relLabel;
	}

	public JLabel getIrrelLabel() {
		return irrelLabel;
	}

	public void setIrrelLabel(JLabel irrelLabel) {
		this.irrelLabel = irrelLabel;
	}

	public JLabel getNormalLabel() {
		return normalLabel;
	}

	public void setNormalLabel(JLabel normalLabel) {
		this.normalLabel = normalLabel;
	}

	public JLabel getRoundLabel() {
		return roundLabel;
	}

	public void setRoundLabel(JLabel roundLabel) {
		this.roundLabel = roundLabel;
	}

	public JButton getRelevanceFeedback() {
		return relevanceFeedback;
	}

	public void setRelevanceFeedback(JButton relevanceFeedback) {
		this.relevanceFeedback = relevanceFeedback;
	}
	
	
	

}