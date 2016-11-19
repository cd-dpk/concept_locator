package com.geet.concept_location.ui;

import java.io.File;

public class Page {
	private String filename; //
	private File file;
	
	public void setFile(File f) {
		file = f;
	}

	
	public String getFilename() {
		return filename;
	}
	public File getFile() {
		return file;
	}
	
	public Page(String name) {
		filename = name;
		//file = new File(filename);
	}
	public Page(File f) {
		file = f;
		if(f != null) filename = f.getName();
	}

}
