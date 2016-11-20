package com.geet.concept_location.ui;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class RelevanceFeedback extends JPanel {
	
	JRadioButton relButton, irrelButton, normalButton;
	JLabel relLabel, irrelLabel, normalLabel;
	JLabel roundLabel;
	JButton relevanceFeedback;
	
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
		relevanceFeedback.setBounds(480, 0, 100, 20);
		add(relevanceFeedback);
		
		roundLabel = new JLabel("RF");
		roundLabel.setBounds(420, 0, 50, 20);
		add(roundLabel);
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(relButton);
		buttonGroup.add(irrelButton);
		buttonGroup.add(normalButton);

 
	}
	

}