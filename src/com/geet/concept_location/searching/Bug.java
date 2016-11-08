package com.geet.concept_location.searching;

import java.util.ArrayList;
import java.util.List;


public class Bug {
	String bugDescription="";
	List<String> fixedFiles=new ArrayList<String>();
	public Bug(String bugDescription, List<String> fixedFiles) {
		super();
		setBugDescription(bugDescription);
		setFixedFiles(fixedFiles);
	}
	public String getBugDescription() {
		return bugDescription;
	}
	public void setBugDescription(String bugDescription) {
		this.bugDescription = bugDescription;
	}
	public List<String> getFixedFiles() {
		return fixedFiles;
	}
	public void setFixedFiles(List<String> fixedFiles) {
		this.fixedFiles = fixedFiles;
	}
	
	
}