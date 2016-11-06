package com.geet.concept_location.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.geet.concept_location.constants.UIConstants;
import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.DocumentExtractor;
import com.geet.concept_location.corpus_creation.SimpleDocument;
import com.geet.concept_location.indexing_vsm.Feedback;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;
import com.geet.concept_location.io.JavaFileReader;
import com.geet.concept_location.preprocessing.JavaClassPreprocessor;
import com.geet.concept_location.utils.JavaFileFilter;

public class ConceptLocatorLsiFrame extends JFrame {
	String projectPath = ".";
	List<Document> allDocuments = new ArrayList<Document>();
	String javaClassPath = "src/com/geet/concept_location/corpus_creation/DocumentExtractor.java";
	ProjectExplorerViewPanel projectExplorerViewPanel;
	JavaClassViewPanelUI javaClassViewPanelUI;
	SearchResultsPanelLsiUI searchResultsPanelLsiUI;

	List<Feedback> feedbacks = new ArrayList<Feedback>();
	List<Document> returnDocuments = new ArrayList<Document>();
	SearchBoxPanelRF searchBoxPanel;
	FileNameExtensionFilter javaFileNameExtensionFilter = new FileNameExtensionFilter(
			"Java Files Only", ".java");
	VectorSpaceModel vectorSpaceModel;

	public ConceptLocatorLsiFrame() {
		super("Concept Locator");
		setLayout(null);
		createMenuBar();
		setAndViewSearchBoxPanel();
		setProjectExplorerViewPanel();
		// setJavaClassViewPanelUI();
		showFrame();
	}

	private void setAndViewSearchBoxPanel() {
		searchBoxPanel = new SearchBoxPanelRF(UIConstants.Width,
				UIConstants.Menu_Height);
		searchBoxPanel.setBounds(UIConstants.PADDING_LEFT,
				UIConstants.PADDING_TOP, UIConstants.Width
						- UIConstants.PADDING_RIGHT, UIConstants.Menu_Height);
		add(searchBoxPanel);
		searchBoxPanel.getSearchButton().addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						SimpleDocument queryDocument = new SimpleDocument(
								searchBoxPanel.getSearchTextField().getText());
						returnDocuments = vectorSpaceModel.search(queryDocument);
						Collections.sort(returnDocuments);
						Collections.reverse(returnDocuments);
						setSearchResultsPanelLsiUI();
					}
				});
		searchBoxPanel.getRelevanceFeedback().addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {						
						List<Document> relDocuments = new ArrayList<Document>();
						List<Document> irrelDocuments = new ArrayList<Document>();
						for (int i = 0; i < returnDocuments.size(); i++) {
							if (feedbacks.get(i).equals(Feedback.REL)) {
								relDocuments.add(returnDocuments.get(i));
								System.out.println("REL "+i);
							}else if (feedbacks.get(i).equals(Feedback.IRRL)) {
								irrelDocuments.add(returnDocuments.get(i));
								System.out.println("IRRL "+i);
							}
						}
						returnDocuments = vectorSpaceModel.research(relDocuments, irrelDocuments);
						Collections.sort(returnDocuments);
						Collections.reverse(returnDocuments);
						setSearchResultsPanelLsiUI();
					}
				});
	}

	private void initIndexing(List<String> javaClassPathList) {
		// read all the documents
		allDocuments = new ArrayList<Document>();
		int classNo = 0;
		// String
		// path="src/com/geet/concept_location/corpus_creation/DocumentExtractor.java";
		for (String path : javaClassPathList) {
			if (new JavaClassPreprocessor().processJavaFile(new File(path))) {
				if (path.equals("src/com/geet/concept_location/corpus_creation/JavaLanguage.java")) {
					continue;
				}
				DocumentExtractor documentExtractor = new DocumentExtractor(
						new File(path));
				int size = 0;
				for (Document document : documentExtractor.getAllDocuments()) {
					allDocuments.add(document);
					size++;
				}
				classNo++;
				System.out.println(path + " has " + size + " document(s)");
			}
			if (classNo > 5) {
				break;
			}
		}
		System.out.println("Size " + allDocuments.size());
		// turn into vector documents
		// get the vector space model
		vectorSpaceModel = new VectorSpaceModel(allDocuments);
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
		initIndexing(projectExplorerViewPanel.getProjectTreePanel().javaFilePaths);
	}

	private void setSearchResultsPanelLsiUI() {
		setAllPanelInvisible();
		searchResultsPanelLsiUI = new SearchResultsPanelLsiUI(returnDocuments,
				new Bound(0, 0, 1300 - 100, 800 - 50));
		searchResultsPanelLsiUI.setBounds(UIConstants.PADDING_LEFT,
				UIConstants.Menu_Height + UIConstants.PADDING_TOP, 1300, 800);
		add(searchResultsPanelLsiUI);
		searchResultsPanelLsiUI.revalidate();
		feedbacks = new ArrayList<Feedback>();
		for (Document document : returnDocuments) {
			feedbacks.add(Feedback.NORMAL);
		}
		searchResultsPanelLsiUI.searchResultList
				.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (feedbacks.get(
								searchResultsPanelLsiUI.searchResultList
										.getSelectedIndex()).equals(
								Feedback.NORMAL)) {
							searchResultsPanelLsiUI.relevanceFeedbackPanel.normalButton
									.setSelected(true);
						} else if (feedbacks.get(
								searchResultsPanelLsiUI.searchResultList
										.getSelectedIndex()).equals(
								Feedback.REL)) {
							searchResultsPanelLsiUI.relevanceFeedbackPanel.relButton
									.setSelected(true);
						} else if (feedbacks.get(
								searchResultsPanelLsiUI.searchResultList
										.getSelectedIndex()).equals(
								Feedback.IRRL)) {
							searchResultsPanelLsiUI.relevanceFeedbackPanel.irrelButton
									.setSelected(true);
						}
					}
				});

		searchResultsPanelLsiUI.relevanceFeedbackPanel.relButton
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (searchResultsPanelLsiUI.searchResultList
								.getSelectedIndex() != -1) {
							feedbacks.set(
									searchResultsPanelLsiUI.searchResultList
											.getSelectedIndex(), Feedback.REL);
						}
					}
				});

		searchResultsPanelLsiUI.relevanceFeedbackPanel.irrelButton
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (searchResultsPanelLsiUI.searchResultList
								.getSelectedIndex() != -1) {
							feedbacks.set(
									searchResultsPanelLsiUI.searchResultList
											.getSelectedIndex(), Feedback.IRRL);

						}
					}
				});

		searchResultsPanelLsiUI.relevanceFeedbackPanel.normalButton
		.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchResultsPanelLsiUI.searchResultList
						.getSelectedIndex() != -1) {
					feedbacks.set(
							searchResultsPanelLsiUI.searchResultList
									.getSelectedIndex(), Feedback.NORMAL);

				}
			}
		});

		searchResultsPanelLsiUI.openButton
				.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (searchResultsPanelLsiUI.searchResultList
								.getSelectedIndex() != -1) {
							javaClassPath = searchResultsPanelLsiUI.lsiDocuments
									.get(searchResultsPanelLsiUI.searchResultList
											.getSelectedIndex())
									.getDocInJavaFile();
							setJavaClassViewPanelUI();
						}
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
						File[] files = file.listFiles(new JavaFileFilter());
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

	// disable the view when the background task is loading
	private void enableView() {
		// TODO do later time
	}

	class FeedbackDialog extends JDialog {
		public JButton relButton, irrelButton, openButton;

		public FeedbackDialog(JFrame frame, boolean modal) {
			super(frame, modal);
			setLayout(new FlowLayout());
			relButton = new JButton("REL");
			add(relButton);
			irrelButton = new JButton("IRREL");
			add(irrelButton);
			openButton = new JButton("OPEN");
			add(openButton);
			setSize(200, 50);
			setLocation(200, 200);
			relButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("REL");
					dismissDialog();
				}
			});

		}

		public void showDialog() {
			show();
		}

		public void dismissDialog() {
			dispose();
		}
	}
}
