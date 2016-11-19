package com.geet.concept_location.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.geet.concept_location.constants.UIConstants;
import com.geet.concept_location.corpus_creation.SimpleDocument;

public class SearchUI extends JPanel{
	
	SearchResultsPanelLsiUI searchResultsPanelLsiUI;
	List<SimpleDocument> documents = new ArrayList<SimpleDocument>();
	public SearchResultsPanelLsiUI getSearchResultsPanelLsiUI() {
		return searchResultsPanelLsiUI;
	}
	public void setSearchResultsPanelLsiUI(
			SearchResultsPanelLsiUI searchResultsPanelLsiUI) {
		this.searchResultsPanelLsiUI = searchResultsPanelLsiUI;
	}
	public List<SimpleDocument> getDocuments() {
		return documents;
	}
	public void setDocuments(List<SimpleDocument> documents) {
		this.documents = documents;
	}
	public SearchBoxPanelUI getSearchBoxPanel() {
		return searchBoxPanel;
	}
	public void setSearchBoxPanel(SearchBoxPanelUI searchBoxPanel) {
		this.searchBoxPanel = searchBoxPanel;
	}
	SearchBoxPanelUI searchBoxPanel;
	
	public SearchUI(){
		setLayout(null);
	}
	
	
}
