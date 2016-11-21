package com.geet.concept_location.ui;

import java.io.File;

public class ExplorerPage extends Page {

	public ProjectExplorerViewPanel getProjectExplorerViewPanel() {
		return projectExplorerViewPanel;
	}

	public void setProjectExplorerViewPanel(
			ProjectExplorerViewPanel projectExplorerViewPanel) {
		this.projectExplorerViewPanel = projectExplorerViewPanel;
	}

	private ProjectExplorerViewPanel projectExplorerViewPanel;
	
	public ExplorerPage(File f) {
		super(f);
	
	}
	
	public ExplorerPage(String string) {
		super(string);
	}

}
