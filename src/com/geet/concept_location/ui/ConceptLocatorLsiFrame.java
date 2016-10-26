package com.geet.concept_location.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.geet.concept_location.constants.UIConstants;
import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.DocumentExtractor;
import com.geet.concept_location.corpus_creation.QueryDocument;
import com.geet.concept_location.indexing_lsi.Lsi;
import com.geet.concept_location.indexing_lsi.LsiDocument;
import com.geet.concept_location.indexing_lsi.LsiQuery;
import com.geet.concept_location.indexing_lsi.Vector;
import com.geet.concept_location.indexing_vsm.VectorDocument;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;
import com.geet.concept_location.io.JavaFileReader;
import com.geet.concept_location.utils.FileUtils;
import com.geet.concept_location.utils.JavaFileFilter;
import com.geet.concept_location.utils.StringUtils;

public class ConceptLocatorLsiFrame extends JFrame {

	String projectPath = ".";
	String javaClassPath = "src/com/geet/concept_location/corpus_creation/DocumentExtractor.java";
	ProjectExplorerViewPanel projectExplorerViewPanel;
	JavaClassViewPanelUI javaClassViewPanelUI;
	SearchResultsPanelLsiUI searchResultsPanelLsiUI;
	List<LsiDocument> lsiDocuments = new ArrayList<LsiDocument>();
	SearchBoxPanelUI searchBoxPanel;
	
	FileNameExtensionFilter javaFileNameExtensionFilter = new FileNameExtensionFilter("Java Files Only", ".java");
	
	public ConceptLocatorLsiFrame() {
		super("Concept Locator");
		setLayout(null);
		createMenuBar();
		setAndViewSearchBoxPanel();
		setProjectExplorerViewPanel();
		//setJavaClassViewPanelUI();
		showFrame();
		String path = getClass().getResource("").getPath();;
		System.out.println("E : "+ path);
	}

	private void setAndViewSearchBoxPanel() {
		searchBoxPanel = new SearchBoxPanelUI(UIConstants.Width,
				UIConstants.Menu_Height);
		searchBoxPanel.setBounds(UIConstants.PADDING_LEFT,
				UIConstants.PADDING_TOP, UIConstants.Width
						- UIConstants.PADDING_RIGHT, UIConstants.Menu_Height);
		add(searchBoxPanel);
		searchBoxPanel.getSearchButton().addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						DocumentExtractor documentExtractor = new DocumentExtractor(
								new File(javaClassPath));
						List<Document> documents = documentExtractor
								.getAllDocuments();
						VectorSpaceModel vectorSpaceModel = new VectorSpaceModel(
								documents);
						Lsi lsi = new Lsi(vectorSpaceModel);
						/*lsi.search(new LsiQuery(searchBoxPanel.getSearchTextField().getText(), new Vector(Lsi.NUM_FACTORS)));
						lsiDocuments = lsi.lsiDocuments;
						setSearchResultsPanelLsiUI();;*/
						System.out.println("Terms");
						lsi.printTermsVector();
						System.out.println("Documents");
						lsi.printDocumentsVector();
					}
				});
	}

	private void setProjectExplorerViewPanel() {
		setAllPanelInvisible();
		projectExplorerViewPanel = new ProjectExplorerViewPanel(new Bound(0, 0,
				1300 - 100, 800 - 50), new File(projectPath));
		projectExplorerViewPanel.setBounds(UIConstants.PADDING_LEFT,
				UIConstants.Menu_Height + UIConstants.PADDING_TOP, 1300, 800);
		projectExplorerViewPanel.setVisible(true);
		add(projectExplorerViewPanel);
		projectExplorerViewPanel.revalidate();
	}

	private void setSearchResultsPanelLsiUI() {
		setAllPanelInvisible();
		searchResultsPanelLsiUI = new SearchResultsPanelLsiUI(lsiDocuments,
				new Bound(0, 0, 1300 - 100, 800 - 50));
		searchResultsPanelLsiUI.setBounds(UIConstants.PADDING_LEFT,
				UIConstants.Menu_Height + UIConstants.PADDING_TOP, 1300, 800);
		add(searchResultsPanelLsiUI);
		searchResultsPanelLsiUI.revalidate();

		searchResultsPanelLsiUI.searchResultList
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						javaClassPath = searchResultsPanelLsiUI.lsiDocuments
								.get(searchResultsPanelLsiUI.searchResultList
										.getSelectedIndex()).getDocInJavaFile();
						setJavaClassViewPanelUI();
					}
				});
	}

	private void setJavaClassViewPanelUI() {
		setAllPanelInvisible();
		String src = "Source";
		JavaFileReader javaFileReader = new JavaFileReader();
		if (javaFileReader.openFile(new File(javaClassPath))) {
			src = javaFileReader.getText();
		}
		javaClassViewPanelUI = new JavaClassViewPanelUI(new Bound(0, 0,
				1300 - 100, 800 - 50), src);
		javaClassViewPanelUI.setBounds(UIConstants.PADDING_LEFT,
				UIConstants.Menu_Height + UIConstants.PADDING_TOP, 1300, 800);
		add(javaClassViewPanelUI);
	}

	private void setAllPanelInvisible() {
		if (projectExplorerViewPanel != null) {
			projectExplorerViewPanel.setVisible(false);
		}
		if (searchResultsPanelLsiUI != null) {
			searchResultsPanelLsiUI.setVisible(false);
		}
		if (javaClassViewPanelUI != null) {
			javaClassViewPanelUI.setVisible(false);
		}
	}

	private void showFrame() {
		setForeground(Color.black);
		setBackground(Color.lightGray);
		setSize(UIConstants.Width, UIConstants.Height);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/** Main: make a Frame, add a FileTree */
	public static void main(String[] av) {
		new ConceptLocatorLsiFrame();
	}

	private void createMenuBar() {

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
				projectPathChooser
						.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = projectPathChooser
						.showOpenDialog(ConceptLocatorLsiFrame.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String selectedPath = projectPathChooser.getSelectedFile()
							.getAbsolutePath();
					File file = new File(selectedPath);
					if (file.isDirectory()) {
						File [] files = file.listFiles(new JavaFileFilter());
						if (files.length > 0) {
							projectPath = selectedPath;
							System.out.println(projectPath);
							setProjectExplorerViewPanel();
						}
					}
					
				}
			}
		});
		return;
	}

}
