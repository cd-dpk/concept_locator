package com.geet.concept_location.ui;

import java.io.File;

import com.geet.concept_location.constants.UIConstants;

public class SearchPage extends Page{
	private SearchUI searchUI;
	public SearchPage(Window window,File f) {
		super(f);
		searchUI = new SearchUI(window,new Bound(0, 0, UIConstants.WIDTH, UIConstants.HEIGHT-UIConstants.Menu_Height));
	}
	public SearchPage(Window window, String string) {
		super(string);
		searchUI = new SearchUI(window,new Bound(0, 0, UIConstants.WIDTH, UIConstants.HEIGHT-UIConstants.Menu_Height));
	}
	public SearchUI getSearchUI() {
		return searchUI;
	}
	public void setSearchUI(SearchUI searchUI) {
		this.searchUI = searchUI;
	}
}
