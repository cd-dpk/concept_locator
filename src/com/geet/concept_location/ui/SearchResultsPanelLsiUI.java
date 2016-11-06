package com.geet.concept_location.ui;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.Scrollable;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geet.concept_location.constants.UIConstants;
import com.geet.concept_location.corpus_creation.Document;
/**
 * 
 * @author geet
 * show the the search results in a text area
 */
public class SearchResultsPanelLsiUI extends JPanel{
	DefaultListModel listModel = new DefaultListModel();
	public JList searchResultList = new JList(listModel);
	public List<Document> lsiDocuments = new ArrayList<Document>();
	SearchResultPanel searchResultPanel;
	RelevanceFeedbackPanel relevanceFeedbackPanel;
	JButton openButton;
	public SearchResultsPanelLsiUI(List<Document>vectorDocuments, Bound bound) {
		super();
		setLayout(null);
		setOpaque(true);
		openButton = new JButton("OPEN");
		openButton.setBounds(0, 0, 100, 20);
		add(openButton);
		relevanceFeedbackPanel = new RelevanceFeedbackPanel(bound);
		relevanceFeedbackPanel.setBounds(200, 0, 450, 50);
		add(relevanceFeedbackPanel);
		this.lsiDocuments = vectorDocuments;searchResultPanel  = new SearchResultPanel(bound);
		searchResultList.setFixedCellHeight(UIConstants.LIST_CELL_HEIGHT);
		searchResultList.setCellRenderer(new TextAreaListItem(20, 100));
		for (Document document : lsiDocuments) {
			String str="";
			str += document.score+"\n";
			str += document.getDocName()+"\n";
			str += document.getDocInJavaFile()+"\n";
			str += document.getArticle()+"\n";
			listModel.addElement(str);
		}
		JScrollPane scrollPane =new JScrollPane(searchResultList);
		scrollPane.setBounds(0, 50, bound.width,bound.height);
		add(scrollPane);
	}
	private class TextAreaListItem extends JTextArea implements ListCellRenderer, ActionListener{
		protected TextAreaListItem(int rows, int cols){
			super(rows, cols);
	         setBorder(BorderFactory.createLineBorder(Color.blue));
		}
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setText(value.toString());
			if (isSelected) {
				setBackground(Color.BLUE);
			}else{
				setBackground(Color.white);
			}
			return this;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Selected");
		}
	}
	
	class SearchResultPanel extends JPanel implements ListCellRenderer{
		public JPanel feedbackPanel;
		public TextAreaListItem textAreaListItem;
		public JRadioButton relButton, irrelButton;
		public SearchResultPanel(Bound bound) {
			setLayout(null);
			feedbackPanel = new JPanel();
			feedbackPanel.setLayout(null);
			
			relButton = new JRadioButton("REL");
			relButton.setBounds(0, 0, 100, 20);
			feedbackPanel.add(relButton);
			
			irrelButton = new JRadioButton("IRREL");
			irrelButton.setBounds(110, 0, 100, 20);
			feedbackPanel.add(irrelButton);
			
			ButtonGroup feedbackGroup = new ButtonGroup();
			feedbackGroup.add(relButton);
			feedbackGroup.add(irrelButton);

			feedbackPanel.setBounds(0, 0, bound.width, UIConstants.LIST_CELL_FEEDBACK_HEIGHT);
			add(feedbackPanel);
			
			textAreaListItem = new TextAreaListItem(30, 100);
			textAreaListItem.setBounds(0, UIConstants.LIST_CELL_FEEDBACK_HEIGHT, bound.width, UIConstants.LIST_CELL_TEXTAREA);
			add(textAreaListItem);
			
		}
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Document document = (Document) value;
			String str="";
			str += document.score+"\n";
			str += document.getDocName()+"\n";
			str += document.getDocInJavaFile()+"\n";
			str += document.getArticle()+"\n";
			textAreaListItem.setText(str);
//			relButton.setSelected(true);
			return this;
		}
	}
}
