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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
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
		setProjectExplorerPage();
		
		// SearchPage added
		setSearchPage();
		
		menu = new Menu(new WindowActionHandler(this));
		panel.add(menu.getMenuBar(), BorderLayout.PAGE_START);
		panel.add(tabs, BorderLayout.CENTER);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	private void setSearchPage() {
		searchPage = new SearchPage("Search Home");
		searchPage.searchUI.doLayout();
		searchPage.searchUI.getSearchButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDocument queryDocument = new SimpleDocument("Query",searchPage.searchUI.getSearchTextField().getText());
				VectorSpaceModel vectorSpaceModel;
				try {
					vectorSpaceModel = new VectorSpaceModel(loadVectorSpaceMatrix());
					Query query = vectorSpaceModel.getQuery(queryDocument);
					documents = vectorSpaceModel.searchWithQueryVector(query);
					Collections.sort(documents);
					Collections.reverse(documents);
					searchPage.searchUI.updateList(documents);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		searchPage.searchUI.openButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// java file
				openJavaFile("");
			}
		});
		
		
		AppManager.addDocument(searchPage);
		tabs.addTab(searchPage.getFilename(), searchPage.searchUI);
		int index = tabs.getTabCount()-1;
		JPanel pnlTab = new JPanel(new GridBagLayout()), iconPanel1 = new JPanel(new GridLayout(1, 1, 4, 4));
		pnlTab.setOpaque(false);
		JLabel lblTitle = new JLabel(searchPage.getFilename());
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
	}
	private void setProjectExplorerPage() {
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
	
	
	/*private void setAndViewSearchBoxPanel() {
		searchBoxPanel = new SearchBoxPanelUI(UIConstants.Width,
				UIConstants.Menu_Height);
		searchBoxPanel.setBounds(UIConstants.PADDING_LEFT,
				UIConstants.PADDING_TOP, UIConstants.Width
						- UIConstants.PADDING_RIGHT, UIConstants.Menu_Height);
		add(searchBoxPanel);
	}
	*/
	/*private void setSearchResultsPanelLsiUI() {
		searchPage.searchUI.searchResultsPanelLsiUI = new SearchResultsPanelLsiUI(documents,
				new Bound(0, 0, 1300 - 100, 800 - 50));
		searchPage.searchUI.searchResultsPanelLsiUI.setBounds(UIConstants.PADDING_LEFT,
				UIConstants.Menu_Height + UIConstants.PADDING_TOP, 1300, 800);
		searchPage.searchUI.add(searchPage.searchUI.searchResultsPanelLsiUI);
		searchPage.searchUI.searchResultsPanelLsiUI.revalidate();
		searchPage.searchUI.searchResultsPanelLsiUI.searchResultList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Hello");
			}
		});
		
	}
*/
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
	
	public void openJavaFile(String fileName){
		File file = new File(fileName);
		String val = "";
		try {
			val = AppManager.readFile(file);
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		ClassPage d = new ClassPage(file);
		d.setEditor(new RSyntaxTextArea());
	    d.getEditor().setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
	    d.getEditor().setCodeFoldingEnabled(true);
	    d.getEditor().setAntiAliasingEnabled(true);
		//RTextScrollPane sp = new RTextScrollPane(d.getEditor());
		d.setScroll(new RTextScrollPane(d.getEditor()));
		d.getScroll().setFoldIndicatorEnabled(true);
		JPanel t = d.getTabPanel();
		t.add(d.getScroll(), BorderLayout.CENTER);
		t.doLayout();
		d.setType(SyntaxConstants.SYNTAX_STYLE_JAVA);
		d.setText(val);
		//d.getEditor().setLocation(0, 0);
		
		//d.getEditor().
		//AppManager.addDocument(d);
		getTabs().addTab(d.getFilename(), t);
		int index = getTabs().getTabCount()-1;
		JPanel pnlTab = new JPanel(new GridBagLayout()), iconPanel = new JPanel(new GridLayout(1, 1, 4, 4));
		//iconPanel.setPreferredSize(new Dimension(12, 12));
		pnlTab.setOpaque(false);
		JLabel lblTitle = new JLabel(d.getFilename());
		ImageIcon icon = new ImageIcon("src/res/close.png");
		JButton btnClose = new JButton(new ImageIcon(icon.getImage().getScaledInstance(12, 12, 4)));
		btnClose.setActionCommand("TabClose");
		btnClose.setOpaque(false);
		btnClose.setBorderPainted(false);
		btnClose.setContentAreaFilled(false);
		btnClose.setFocusPainted(false);
		btnClose.setPreferredSize(new Dimension(11, 15));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.ipadx = 5;

		pnlTab.add(lblTitle, gbc);

		gbc.gridx++;
		gbc.weightx = 0;
		gbc.ipadx = 5;
		iconPanel.add(btnClose);
		pnlTab.add(btnClose, gbc);
		
		getTabs().setTabComponentAt(index, pnlTab);

		try {
			AppManager.addDocument(d);
		} catch (Exception exc) {
			System.out.println("Nie spodziewałem się Hiszpańskiej Inkwizycji!");
		}
		getFrame().repaint();

	}

	
}
