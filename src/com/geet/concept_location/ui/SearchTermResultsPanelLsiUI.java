/*package com.geet.concept_location.ui;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.Scrollable;
import com.geet.concept_location.indexing_lsi.LsiDocument;
import com.geet.concept_location.indexing_lsi.LsiTerm;
*//**
 * 
 * @author geet
 * show the the search results in a text area
 *//*
public class SearchTermResultsPanelLsiUI extends JPanel{
	DefaultListModel listModel = new DefaultListModel();
	public JList searchResultList = new JList(listModel);
	public List<LsiTerm> lsiTerms = new ArrayList<LsiTerm>();
	public SearchTermResultsPanelLsiUI(List<LsiTerm>vectorDocuments, Bound bound) {
		super();
		setLayout(null);
		this.lsiTerms = vectorDocuments;
		searchResultList.setCellRenderer(new TextAreaListItem(10, 20));
		for (LsiTerm lsiTerm : lsiTerms) {
			String str="";
			str += lsiTerm.getScore()+"\n";
			str += lsiTerm.term+"\n";
			listModel.addElement(str);
		}
		JScrollPane scrollPane =new JScrollPane(searchResultList);
		scrollPane.setBounds(0, 0, bound.width,bound.height);
		add(scrollPane);
	}
	private class TextAreaListItem extends JTextArea implements ListCellRenderer, Scrollable{
		protected TextAreaListItem(int rows, int cols){
			super(rows, cols);
	         setBorder(BorderFactory.createLineBorder(Color.blue));
		}
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			// TODO Auto-generated method stub
			setText(value.toString());
			return this;
		}
	}
}
*/
