/**
 * 
 */
package com.geet.concept_location.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.geet.concept_location.constants.UIConstants;
import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.SimpleDocument;
import com.geet.concept_location.indexing_vsm.Query;
import com.geet.concept_location.indexing_vsm.VectorSpaceMatrix;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;

public class Window {
	
	ExplorerPage explorerPage;
	SearchPage searchPage;
	private JFrame frame = null,
				   searchFrame = null,
				   aboutFrame = null;
	private int width, height;
	private JPanel panel;
	private JTabbedPane tabs;
	private Menu menu;
	List<SimpleDocument> documents = new ArrayList<SimpleDocument>();
	public JFrame getSearchFrame() {
		return searchFrame;
	}
	public JFrame getAboutFrame() {
		return aboutFrame;
	}
	public JPanel getPanel() {
		return panel;
	}
	public Menu getMenu() {
		return menu;
	}
	public JTabbedPane getTabs() {
		return tabs;
	}
	public JFrame getFrame() {
		return frame;
	}
	
	public void setAboutFrame(JFrame f) {
		aboutFrame = f;
	}
	public void setFrame(JFrame f) {
		frame = f;
	}
	public void setSearchFrame(JFrame f) {
		searchFrame = f;
	}
	
	
	private void createControls() {
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("src/res/icon.png"));
		panel = new JPanel(new java.awt.BorderLayout()); 
		
		tabs = new JTabbedPane();
		
		// Project Explorer
		explorerPage = new ExplorerPage("Project Explorer");
		explorerPage.projectExplorerViewPanel = new ProjectExplorerViewPanel(new Bound(0, 0,1300 - 100, 800 - 50),new File( "."));
		AppManager.addDocument(explorerPage);
		tabs.addTab(explorerPage.getFilename(), explorerPage.getProjectExplorerViewPanel());
		int index = tabs.getTabCount()-1;
		JPanel pnlTab = new JPanel(new GridBagLayout()), iconPanel = new JPanel(new GridLayout(1, 1, 4, 4));
		pnlTab.setOpaque(false);
		JLabel lblTitle = new JLabel(explorerPage.getFilename());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.ipadx = 5;
		pnlTab.add(lblTitle, gbc);
		gbc.gridx++;
		gbc.weightx = 0;
		gbc.ipadx = 5;
		tabs.setTabComponentAt(index, pnlTab);
		
		// SearchPage added
		searchPage = new SearchPage("Search Home");
		searchPage.searchUI.doLayout();
		searchPage.searchUI.searchBoxPanel.getSearchButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDocument queryDocument = new SimpleDocument("Query",searchPage.searchUI.searchBoxPanel.getSearchTextField().getText());
				VectorSpaceModel vectorSpaceModel;
				try {
					vectorSpaceModel = new VectorSpaceModel(loadVectorSpaceMatrix());
					Query query = vectorSpaceModel.getQuery(queryDocument);
					documents = vectorSpaceModel.searchWithQueryVector(query);
					Collections.sort(documents);
					Collections.reverse(documents);
					setSearchResultsPanelLsiUI();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		AppManager.addDocument(searchPage);
		tabs.addTab(searchPage.getFilename(), searchPage.searchUI);
		index = tabs.getTabCount()-1;
		JPanel pnlTab1 = new JPanel(new GridBagLayout()), iconPanel1 = new JPanel(new GridLayout(1, 1, 4, 4));
		pnlTab.setOpaque(false);
		JLabel lblTitle1 = new JLabel(searchPage.getFilename());
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1;
		gbc1.ipadx = 5;
		pnlTab1.add(lblTitle1, gbc1);
		gbc1.gridx++;
		gbc1.weightx = 0;
		gbc1.ipadx = 5;
		tabs.setTabComponentAt(index, pnlTab1);
		menu = new Menu(new WindowActionHandler(this));
		panel.add(menu.getMenuBar(), BorderLayout.PAGE_START);
		panel.add(tabs, BorderLayout.CENTER);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public Window(int w, int h) throws Exception {
		width = w;
		height = h;
		frame = new JFrame("Simple Text Editor");
		createControls();
	}
	
	private void setProjectExplorerViewPanel() {
		/*projectExplorerViewPanel = new ProjectExplorerViewPanel(new Bound(0, 0,
				1300 - 100, 800 - 50), new File(projectPath));
		projectExplorerViewPanel.setBounds(UIConstants.PADDING_LEFT,
				UIConstants.Menu_Height + UIConstants.PADDING_TOP, 1300, 800);
		projectExplorerViewPanel.setVisible(true);
		add(projectExplorerViewPanel);
		projectExplorerViewPanel.revalidate();
		initIndexing(projectExplorerViewPanel.getProjectTreePanel().javaFilePaths);*/
	}
	
	private void setSearchResultsPanelLsiUI() {
		searchPage.searchUI.searchResultsPanelLsiUI = new SearchResultsPanelLsiUI(documents,
				new Bound(0, 0, 1300 - 100, 800 - 50));
		searchPage.searchUI.searchResultsPanelLsiUI.setBounds(UIConstants.PADDING_LEFT,
				UIConstants.Menu_Height + UIConstants.PADDING_TOP, 1300, 800);
		searchPage.searchUI.add(searchPage.searchUI.searchResultsPanelLsiUI);
		searchPage.searchUI.searchResultsPanelLsiUI.revalidate();
		
		System.out.println(searchPage.searchUI.searchResultsPanelLsiUI);
		searchPage.searchUI.searchResultsPanelLsiUI.searchResultList
		.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				System.out.println("el");
			}
		});
	}
	private void setAndViewSearchBoxPanel() {
		searchBoxPanel = new SearchBoxPanelUI(UIConstants.Width,
				UIConstants.Menu_Height);
		searchBoxPanel.setBounds(UIConstants.PADDING_LEFT,
				UIConstants.PADDING_TOP, UIConstants.Width
						- UIConstants.PADDING_RIGHT, UIConstants.Menu_Height);
		add(searchBoxPanel);
	}
	private void setSearchResultsPanelLsiUI() {
		searchResultsPanelLsiUI = new SearchResultsPanelLsiUI(documents,
				new Bound(0, 0, 1300 - 100, 800 - 50));
		searchResultsPanelLsiUI.setBounds(UIConstants.PADDING_LEFT,
				UIConstants.Menu_Height + UIConstants.PADDING_TOP, 1300, 800);
		add(searchResultsPanelLsiUI);
		searchResultsPanelLsiUI.revalidate();
	}

	public VectorSpaceMatrix loadVectorSpaceMatrix() throws IOException{
		System.out.println("Vector Space Model is loading...");
		VectorSpaceMatrix vectorSpaceMatrix = null;
		FileInputStream file = new FileInputStream("vectorspace.ser");
		ObjectInputStream objectInputStream = new ObjectInputStream(file);
		try {
			vectorSpaceMatrix = (VectorSpaceMatrix) objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		objectInputStream.close();
		return vectorSpaceMatrix;
	}

	
}
