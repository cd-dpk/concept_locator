package com.geet.concept_location.ui;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import com.geet.concept_location.constants.UIConstants;
import com.geet.concept_location.io.JavaFileReader;
import com.geet.concept_location.utils.StringUtils;
public class ProjectExplorerViewPanel extends JPanel{
	FileTree projectTreePanel;
	File project;
	SourceViewPanel sourceViewPanel;     
	String source = "<code>sample</code>";
	public ProjectExplorerViewPanel(Bound bound, File project){
		super();
		setLayout(null);
		this.project = project;
		projectTreePanel = new FileTree(this.project);
		projectTreePanel.setBounds(0, 0, UIConstants.FILE_TREE_WIDTH, bound.height-20);
		add(projectTreePanel);
		sourceViewPanel = new SourceViewPanel(source,new Bound(0,0, bound.width - UIConstants.FILE_TREE_WIDTH-20, bound.height-20));
		sourceViewPanel.setBounds(UIConstants.FILE_TREE_WIDTH+20,0, bound.width - UIConstants.FILE_TREE_WIDTH-20, bound.height-20);
		add(sourceViewPanel);
	}
	public FileTree getProjectTreePanel() {
		return projectTreePanel;
	}
	public void setProjectTreePanel(FileTree projectTreePanel) {
		this.projectTreePanel = projectTreePanel;
	}
	public File getProject() {
		return project;
	}
	public void setProject(File project) {
		this.project = project;
	}
	public SourceViewPanel getSourceViewPanel() {
		return sourceViewPanel;
	}
	public void setSourceViewPanel(SourceViewPanel sourceViewPanel) {
		this.sourceViewPanel = sourceViewPanel;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
}
