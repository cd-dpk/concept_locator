package com.geet.concept_location.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.geet.concept_location.constants.UIConstants;
import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.DocumentExtractor;
import com.geet.concept_location.corpus_creation.QueryDocument;
import com.geet.concept_location.indexing_vsm.VectorDocument;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;
import com.geet.concept_location.io.JavaFileReader;

public class ConceptLocatorFrame extends JFrame {
	
	
	String projectPath = ".";
	String javaClassPath ="src/com/geet/concept_location/corpus_creation/DocumentExtractor.java";
	ProjectExplorerViewPanel projectExplorerViewPanel;
	SearchResultsPanelUI searchResultsPanelUI;
	JavaClassViewPanelUI javaClassViewPanelUI;
	List<VectorDocument> vectorDocuments = new ArrayList<VectorDocument>();
	SearchBoxPanelUI searchBoxPanel;
	
	public ConceptLocatorFrame(){
		super("Concept Locator");
		setLayout(null);
		createMenuBar();
		setAndViewSearchBoxPanel();
		setJavaClassViewPanelUI();
		showFrame();
	}
	
	
	private void setAndViewSearchBoxPanel(){
		searchBoxPanel = new SearchBoxPanelUI(UIConstants.Width, UIConstants.Menu_Height);
		searchBoxPanel.setBounds(UIConstants.PADDING_LEFT, UIConstants.PADDING_TOP, UIConstants.Width-UIConstants.PADDING_RIGHT, UIConstants.Menu_Height);
		add(searchBoxPanel);
		searchBoxPanel.getSearchButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
						VectorDocument queryVectorDocument = new VectorDocument(
								new QueryDocument(searchBoxPanel
										.getSearchTextField().getText()));
						DocumentExtractor documentExtractor = new DocumentExtractor(
								new File(javaClassPath));
						List<Document> documents = documentExtractor
								.getAllDocuments();
						VectorSpaceModel vectorSpaceModel = new VectorSpaceModel(
								documents);
						for (VectorDocument document : vectorSpaceModel.vectorDocuments) {
							document.dotProduct = document
									.getDotProduct(queryVectorDocument);
						}
						Collections.sort(vectorSpaceModel.vectorDocuments);
						Collections.reverse(vectorSpaceModel.vectorDocuments);
						vectorDocuments = vectorSpaceModel.vectorDocuments;
						setSearchResultsPanelUI();
			}
		});
	}
	private void setProjectExplorerViewPanel(){
		setAllPanelInvisible();
		projectExplorerViewPanel = new ProjectExplorerViewPanel(new Bound(0, 0, 1300-100, 800-50), new File("."));
		projectExplorerViewPanel.setBounds(UIConstants.PADDING_LEFT, UIConstants.Menu_Height+UIConstants.PADDING_TOP, 1300, 800);
		projectExplorerViewPanel.setVisible(true);
		add(projectExplorerViewPanel);
		projectExplorerViewPanel.revalidate();
	}
	
	private void setSearchResultsPanelUI(){
		setAllPanelInvisible();
		searchResultsPanelUI = new SearchResultsPanelUI(vectorDocuments,new Bound(0, 0, 1300-100, 800-50));
		searchResultsPanelUI.setBounds(UIConstants.PADDING_LEFT, UIConstants.Menu_Height+UIConstants.PADDING_TOP, 1300, 800);
		add(searchResultsPanelUI);
		searchResultsPanelUI.revalidate();
		
		searchResultsPanelUI.searchResultList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				javaClassPath = searchResultsPanelUI.vectorDocuments.get(searchResultsPanelUI.searchResultList.getSelectedIndex()).getDocInJavaFile();
				setJavaClassViewPanelUI();
			}
		});
	}
	private void setJavaClassViewPanelUI(){
		setAllPanelInvisible();
		String src="Source";
		JavaFileReader javaFileReader = new JavaFileReader();
		if (javaFileReader.openFile(new File(javaClassPath))){
			src = javaFileReader.getText();
		}
		javaClassViewPanelUI = new JavaClassViewPanelUI(new Bound(0, 0, 1300-100, 800-50),src);
		javaClassViewPanelUI.setBounds(UIConstants.PADDING_LEFT, UIConstants.Menu_Height+UIConstants.PADDING_TOP, 1300, 800);
		add(javaClassViewPanelUI);
	}
	
	private void setAllPanelInvisible(){
		if (projectExplorerViewPanel != null) {
			projectExplorerViewPanel.setVisible(false);			
		}
		if (searchResultsPanelUI != null) {
			searchResultsPanelUI.setVisible(false);
			
		}
		if (javaClassViewPanelUI != null) {
			javaClassViewPanelUI.setVisible(false);
		}		
	}

	private void showFrame(){
	    setForeground(Color.black);
	    setBackground(Color.lightGray);
	    setSize(UIConstants.Width, UIConstants.Height);
	    setVisible(true);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	/**
	 * 
	 */
	/** Main: make a Frame, add a FileTree */
	
	public static void main(String[] av) {
		  new ConceptLocatorFrame();
	  }
	
	private void createMenuBar(){
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem homeItem = new JMenuItem("Home");
		JMenuItem newFileItem = new JMenuItem("New");
		JMenuItem exitItem = new JMenuItem("Exit");
		
		fileMenu.add(homeItem);
		fileMenu.add(newFileItem);
		fileMenu.add(exitItem);
		
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		
		newFileItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser projectPathChooser = new JFileChooser();
				projectPathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue =projectPathChooser.showOpenDialog(ConceptLocatorFrame.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					projectPath = projectPathChooser.getSelectedFile().getAbsolutePath();
					//projectExplorerViewPanel.setProjectTreePanel(new FileTree(new File(projectPath)));
					//					projectExplorerViewPanel.projectTreePanel = new FileTree();
				}
			}
		});
		
		return;
	}
	
	

}
