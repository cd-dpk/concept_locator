package com.geet.concept_location.ui;

import java.io.File;

import com.geet.concept_location.constants.UIConstants;

public class SearchPage extends Page{
	SearchUI searchUI;
	public SearchPage(File f) {
		super(f);
		searchUI = new SearchUI(new Bound(0, 0, UIConstants.WIDTH, UIConstants.HEIGHT-UIConstants.Menu_Height));
	}
	public SearchPage(String string) {
		super(string);
		searchUI = new SearchUI(new Bound(0, 0, UIConstants.WIDTH, UIConstants.HEIGHT-UIConstants.Menu_Height));
	}
}
