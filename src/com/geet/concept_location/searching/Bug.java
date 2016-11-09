package com.geet.concept_location.searching;
import java.util.ArrayList;
import java.util.List;
public class Bug {
	String bugID="";
	String summary="";
	String description="";
	List<String> fixedFiles=new ArrayList<String>();
	public Bug() {
		// TODO Auto-generated constructor stub
	}
	public String getBugID() {
		return bugID;
	}
	public void setBugID(String bugID) {
		this.bugID = bugID;
	}
	public String getSummary() {
		return summary;
	}
	public Bug(String bugID, String summary, String description,
			List<String> fixedFiles) {
		super();
		this.bugID = bugID;
		this.summary = summary;
		this.description = description;
		this.fixedFiles = fixedFiles;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
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
		String files = "[";
		for (int i = 0; i < fixedFiles.size(); i++) {
			files += fixedFiles.get(i)+",";
		}
		return bugID+","+summary+","+description+","+files+"]";
	}
}
