package com.geet.concept_location.ui;

import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
/**
 * 
 * @author geet
 * show the the search results in a text area
 */
public class SearchResultsPanelUI extends JPanel{

	
	DefaultListModel listModel = new DefaultListModel();
	JList searchResultList = new JList(listModel);
	
	public SearchResultsPanelUI() {
		searchResultList.setCellRenderer(new TextAreaListItem(10, 20));
	}
	
	private class TextAreaListItem extends JTextArea implements ListCellRenderer{
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
