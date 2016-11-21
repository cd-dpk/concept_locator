package com.geet.concept_location.searching;

import java.util.ArrayList;
import java.util.List;

public class Feature {
	String id;
	public Feature() {
		// TODO Auto-generated constructor stub
	}
	public Feature(String id, String description, List<String> fixedFiles) {
		super();
		this.id = id;
		this.description = description;
		this.fixedFiles = fixedFiles;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	String description="";
	List<String> fixedFiles=new ArrayList<String>();
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getFixedFiles() {
		return fixedFiles;
	}
	public void setFixedFiles(List<String> fixedFiles) {
		this.fixedFiles = fixedFiles;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String files = "";
		for (int i = 0; i < fixedFiles.size(); i++) {
			files+=(fixedFiles.get(i))+" ";
		}
		return id+","+description+","+files;
	}
	
}
