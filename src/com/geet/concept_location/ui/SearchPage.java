package com.geet.concept_location.ui;

import java.io.File;

public class SearchPage extends Page{

	SearchUI searchUI;
	public SearchPage(File f) {
		super(f);
		searchUI = new SearchUI();
	}

	public SearchPage(String string) {
		super(string);
		searchUI = new SearchUI();
	}
}
