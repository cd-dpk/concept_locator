package com.geet.concept_location.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.Scrollable;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.geet.concept_location.corpus_creation.SimpleDocument;

public class SearchUI extends JPanel{
	
	
	private JTextField searchTextField;
	JButton openButton;
	private JButton searchButton;
	RelevanceFeedback relevanceFeedback;
	DefaultListModel<SimpleDocument> listModel = new DefaultListModel();
	private JPanel rfPanel;
	public JList searchResultList = new JList(listModel);
	public List<SimpleDocument> lsiDocuments = new ArrayList<SimpleDocument>();
	Bound bound;
	public Bound getBound() {
		return bound;
	}
	public void setBound(Bound bound) {
		this.bound = bound;
	}
	public SearchUI(Bound bound){
		setLayout(null);
		setBound(bound);
		
		searchTextField = new JTextField("Enter Query");
		searchTextField.setBounds(0, 0, (int)(.80*bound.width), 30);
		add(searchTextField);
		searchButton = new JButton("Search");
		searchButton.setBounds((int)(.80*bound.width)+2, 0, (int)(.2*bound.width), 30);
		add(searchButton);
		openButton = new JButton("OPEN");
		openButton.setBounds(0, 30, 100, 20);
		add(openButton);
		
		relevanceFeedback = new RelevanceFeedback(bound);
		relevanceFeedback.setBounds(200, 30, 450, 50);
		add(relevanceFeedback);
		
		searchResultList.setCellRenderer(new SearhResult());
		listModel.addElement(new SimpleDocument("Main", "public dipok"));
		listModel.addElement(new SimpleDocument("Query","public dipok\npublic dipok\npublic dipok\npublic dipok\npublic dipokpublic dipok\npublic dipok\npublic dipok\npublic dipok\npublic dipokpublic dipok\npublic dipok\npublic dipok\npublic dipok\npublic dipok"));
		listModel.addElement(new SimpleDocument("QueryHHHHHHHHHHHHHH","public dipok\npublic dipok\npublic dipok\npublic dipok\npublic dipokpublic dipok\npublic dipok\npublic dipok\npublic dipok\npublic dipokpublic dipok\npublic dipok\npublic dipok\npublic dipok\npublic dipok"));
		for (SimpleDocument document : lsiDocuments) {
//			String str="";
//			str += document.score+"\n";
		//	str += document.getDocInJavaFile()+"\n";
		//	str += document.getDocInJavaFile()+"\n";
		//	str += document.getArticle()+"\n";
			listModel.addElement(document);
		}
		JScrollPane scrollPane =new JScrollPane(searchResultList);
		scrollPane.setBounds(0, 80, bound.width,bound.height);
		add(scrollPane);
	}
	private class RTextAreaListItem extends RSyntaxTextArea implements ListCellRenderer, Scrollable{
		protected RTextAreaListItem() {
	        setBorder(BorderFactory.createLineBorder(Color.blue));
		}
		protected RTextAreaListItem(int rows, int cols){
			super(rows, cols);
	        setBorder(BorderFactory.createLineBorder(Color.blue));
		}
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			// TODO Auto-generated method stub
			SimpleDocument simpleDocument = (SimpleDocument) value;
			setText(simpleDocument.getArticle());
			setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		    return this;
		}
	}
	private class SearhResult extends MyModel implements ListCellRenderer{
		
		protected SearhResult() {
		    setBorder(BorderFactory.createLineBorder(Color.blue));
		}
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			// TODO Auto-generated method stub
			SimpleDocument simpleDocument = (SimpleDocument) value;
			fileName.setText(simpleDocument.docInJavaFile);
			rSyntaxTextArea.setText(simpleDocument.getArticle());
			rSyntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
			rSyntaxTextArea.setCodeFoldingEnabled(true);
			rSyntaxTextArea.setAntiAliasingEnabled(true);
				//RTextScrollPane sp = new RTextScrollPane(d.getEditor());
			
		//	add(new RTextScrollPane(rSyntaxTextArea));
			return this;
		}
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

	public JList getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(JList searchResultList) {
		this.searchResultList = searchResultList;
	}
	public void updateList(List<SimpleDocument> documents){
		listModel = new DefaultListModel<SimpleDocument>();
		for (SimpleDocument document : documents) {
			String str="";
			System.out.println("Hello");
			str += document.score+"\n";
		//	str += document.getDocInJavaFile()+"\n";
		//	str += document.getDocInJavaFile()+"\n";
		//	str += document.getArticle()+"\n";
			listModel.addElement(document);
		}	
		searchResultList.setModel(listModel);
	}

	
}
