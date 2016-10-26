package com.geet.concept_location.ui;
import java.awt.Button;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class SearchBoxPanelUI extends JPanel {
	private JTextField searchTextField;
	private JButton searchButton;
	public SearchBoxPanelUI(int width, int height) {
			super();
			setLayout(null);
			setBounds(5, 5, width, height);
			searchTextField = new JTextField("Enter Query");
			searchTextField.setBounds(0, 0, (int)(.80*width), height);
			add(searchTextField);
			searchButton = new JButton("Search");
			searchButton.setBounds((int)(.80*width)+2, 0, (int)(.2*width), height);
			add(searchButton);
	}
	public JTextField getSearchTextField() {
		return searchTextField;
	}
	public void setSearchTextField(JTextField searchTextField) {
		this.searchTextField = searchTextField;
	}
	public JButton getSearchButton() {
		return searchButton;
	}
	public void setSearchButton(JButton searchButton) {
		this.searchButton = searchButton;
	}
}
