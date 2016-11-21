package com.geet.concept_location.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.geet.concept_location.constants.UIConstants;
import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.DocumentExtractor;
import com.geet.concept_location.corpus_creation.SimpleDocument;
import com.geet.concept_location.indexing_vsm.Feedback;
import com.geet.concept_location.indexing_vsm.Query;
import com.geet.concept_location.indexing_vsm.VectorSpaceMatrix;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;
import com.geet.concept_location.io.JavaFileReader;
import com.geet.concept_location.preprocessing.JavaClassPreprocessor;
import com.geet.concept_location.utils.StringUtils;

public class Window {
	public String projectPath="UltimateCalculator-master";
	public List<String> javaFilePaths =new ArrayList<String>();
	ExplorerPage explorerPage;
	int rel = 0, irrel = 0, round = 0;
	SearchPage searchPage;
	private JFrame frame = null;
	private int width, height;
	private JPanel panel;
	private JTabbedPane tabs;
	private Menu menu;
	Query query;
	VectorSpaceModel vectorSpaceModel;
	List<Feedback> feedbacks = new ArrayList<Feedback>();
	List<SimpleDocument> returnDocuments = new ArrayList<SimpleDocument>();
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
	public void setFrame(JFrame f) {
		frame = f;
	}
	private void createControls() throws IOException {
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("res/search.png"));
		frame.setResizable(false);
		panel = new JPanel(new java.awt.BorderLayout()); 
		
		tabs = new JTabbedPane();
		/// Project Explorer
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
		searchPage.getSearchUI().doLayout();
		AppManager.addDocument(searchPage);
		tabs.addTab(searchPage.getFilename(), searchPage.getSearchUI());
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
		
		searchPage.getSearchUI().getSearchTextField().addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					SimpleDocument queryDocument = new SimpleDocument.Builder().docName("Query").article(searchPage.getSearchUI().getSearchTextField().getText()).build();
					try {
						vectorSpaceModel = new VectorSpaceModel(loadVectorSpaceMatrix());
						query = vectorSpaceModel.getQuery(queryDocument);
						returnDocuments = vectorSpaceModel.searchWithQueryVector(query);
						searchPage.getSearchUI().getOpenButton().setVisible(true);
						searchPage.getSearchUI().getOpenButton().setEnabled(false);
						searchPage.getSearchUI().getRelevanceFeedback().setVisible(true);
						round=1;
						searchPage.getSearchUI().getRelevanceFeedback().getRoundLabel().setText("Round "+round+"");
						feedbacks = new ArrayList<Feedback>();
						for (int i = 0; i < returnDocuments.size(); i++) {
							feedbacks.add(Feedback.NORMAL);
						}
						updateNoOfRelDocs();
						searchPage.getSearchUI().getRelevanceFeedback().getRelLabel().setText(rel+"");
						searchPage.getSearchUI().getRelevanceFeedback().getIrrelLabel().setText(irrel+"");
						searchPage.getSearchUI().updateList(returnDocuments);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		searchPage.getSearchUI().getSearchButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SimpleDocument queryDocument = new SimpleDocument.Builder().docName("Query").article(searchPage.getSearchUI().getSearchTextField().getText()).build();
				try {
					vectorSpaceModel = new VectorSpaceModel(loadVectorSpaceMatrix());
					query = vectorSpaceModel.getQuery(queryDocument);
					returnDocuments = vectorSpaceModel.searchWithQueryVector(query);
					searchPage.getSearchUI().getOpenButton().setVisible(true);
					searchPage.getSearchUI().getOpenButton().setEnabled(false);
					searchPage.getSearchUI().getRelevanceFeedback().setVisible(true);
					round=1;
					searchPage.getSearchUI().getRelevanceFeedback().getRoundLabel().setText("Round "+round+"");
					feedbacks = new ArrayList<Feedback>();
					for (int i = 0; i < returnDocuments.size(); i++) {
						feedbacks.add(Feedback.NORMAL);
					}
					updateNoOfRelDocs();
					searchPage.getSearchUI().getRelevanceFeedback().getRelLabel().setText(rel+"");
					searchPage.getSearchUI().getRelevanceFeedback().getIrrelLabel().setText(irrel+"");
					searchPage.getSearchUI().updateList(returnDocuments);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		searchPage.getSearchUI().getSearchResultList().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = searchPage.getSearchUI().getSearchResultList().getSelectedIndex();
				if (index !=-1) {
					if (feedbacks.get(index).equals(Feedback.NORMAL)) {
						searchPage.getSearchUI().getRelevanceFeedback().getNormalButton()
								.setSelected(true);
					} else if (feedbacks.get(index).equals(
							Feedback.REL)) {
						searchPage.getSearchUI().getRelevanceFeedback().getRelButton()
								.setSelected(true);
					} else if (feedbacks.get(index).equals(Feedback.IRRL)) {
						searchPage.getSearchUI().getRelevanceFeedback().getIrrelButton().setSelected(true);
					}
					searchPage.getSearchUI().getOpenButton().setEnabled(true);
				}	
			}
		});
		searchPage.getSearchUI().getRelevanceFeedback().getRelButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = searchPage.getSearchUI().getSearchResultList().getSelectedIndex();
				if (index !=-1) {
					feedbacks.set(index,Feedback.REL);
					System.out.println(feedbacks.size());
//					System.out.println(feedbacks.get(index).toString());
					updateNoOfRelDocs();
					searchPage.getSearchUI().getRelevanceFeedback().getRelLabel().setText(rel+"");
					searchPage.getSearchUI().getRelevanceFeedback().getIrrelLabel().setText(irrel+"");
				}
			}
		});
		searchPage.getSearchUI().getRelevanceFeedback().getIrrelButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = searchPage.getSearchUI().getSearchResultList().getSelectedIndex();
				if (index !=-1) {
					feedbacks.set(index,Feedback.IRRL);
					updateNoOfRelDocs();
					searchPage.getSearchUI().getRelevanceFeedback().getRelLabel().setText(rel+"");
					searchPage.getSearchUI().getRelevanceFeedback().getIrrelLabel().setText(irrel+"");
				}
			}
		});
		searchPage.getSearchUI().getRelevanceFeedback().getNormalButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = searchPage.getSearchUI().getSearchResultList().getSelectedIndex();
				if (index !=-1) {
					feedbacks.set(index,Feedback.NORMAL);
					updateNoOfRelDocs();
					searchPage.getSearchUI().getRelevanceFeedback().getRelLabel().setText(rel+"");
					searchPage.getSearchUI().getRelevanceFeedback().getIrrelLabel().setText(irrel+"");
				}
			}
		});
		searchPage.getSearchUI().getRelevanceFeedback().getRelevanceFeedback().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTheQueryOnRelevanceFeedback(vectorSpaceModel.getVectorSpaceMatrix());
				returnDocuments = vectorSpaceModel.searchWithQueryVector(query);
				feedbacks = new ArrayList<Feedback>();
				round++;
				searchPage.getSearchUI().getOpenButton().setEnabled(false);
				searchPage.getSearchUI().getRelevanceFeedback().getRoundLabel().setText("Round "+round+"");
				for (int i = 0; i < returnDocuments.size(); i++) {
					feedbacks.add(Feedback.NORMAL);
				}
				System.out.println(returnDocuments.size()+","+feedbacks.size());
				updateNoOfRelDocs();
				searchPage.getSearchUI().getRelevanceFeedback().getRelLabel().setText(rel+"");
				searchPage.getSearchUI().getRelevanceFeedback().getIrrelLabel().setText(irrel+"");
				searchPage.getSearchUI().updateList(returnDocuments);
			}
		});
		searchPage.getSearchUI().getOpenButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// java file
				int index = searchPage.getSearchUI().getSearchResultList().getSelectedIndex();
				if (index != -1) {
					openJavaFile(returnDocuments.get(index).getDocInJavaFile());
				}
			}
		});
	}
	private void setProjectExplorerPage() throws IOException {
		explorerPage = new ExplorerPage("Project Explorer");
		explorerPage.setProjectExplorerViewPanel(new ProjectExplorerViewPanel(new Bound(0, 0,UIConstants.WIDTH, UIConstants.HEIGHT-50),new File( projectPath)));
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
		javaFilePaths = explorerPage.getProjectExplorerViewPanel().projectTreePanel.getJavaFilePaths();
		System.out.println(javaFilePaths.size());
		if (javaFilePaths.size() > 0) {
			JavaFileReader javaFileReader = new JavaFileReader();
			File selectedFile = new File(javaFilePaths.get(0));
			if (!selectedFile.isDirectory()) {
				if (javaFileReader.openFile(selectedFile)) {
					explorerPage.getProjectExplorerViewPanel().sourceViewPanel.getSourceTextArea().setText(
							javaFileReader.getText());
					explorerPage.getProjectExplorerViewPanel().sourceViewPanel.getFileName().setText(
							selectedFile.getName());
				}
			}
		}
		explorerPage.getProjectExplorerViewPanel().projectTreePanel.getTree().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				JavaFileReader javaFileReader = new JavaFileReader();
				System.out.println(e.getPath().toString());
				String filePath = StringUtils.getFilePathNameNew(e
						.getPath().toString());
				System.out.println("File Path:"+filePath);
				File selectedFile = new File(filePath);
				if (!selectedFile.isDirectory()) {
					if (javaFileReader.openFile(selectedFile)) {
						explorerPage.getProjectExplorerViewPanel().sourceViewPanel.getSourceTextArea().setText(
								javaFileReader.getText());
						explorerPage.getProjectExplorerViewPanel().sourceViewPanel.getFileName().setText(
								selectedFile.getName());
						
					}
				}
			}
		});
//		createVectorSpaceMatrix();
//		System.exit(0);
	}
	public Window(int w, int h) throws Exception {
		width = w;
		height = h;
		frame = new JFrame("Concept Locator");
		createControls();
	}
	// indexing...
	private void createVectorSpaceMatrix() throws IOException{
			FileWriter fileWriter = new FileWriter(new File("D:\\BSSE0501\\Project-801\\Corpus.txt"));
			List<SimpleDocument> allDocuments = new ArrayList<SimpleDocument>();
			int classNo = 0;
			for (String path : javaFilePaths) {
				if (new JavaClassPreprocessor().processJavaFile(new File(path))) {
					fileWriter.write(classNo+"\n");
					fileWriter.write(path+"\n");
					System.out.println(classNo);
					System.out.println(path);
					allDocuments.addAll(new DocumentExtractor(new File(path)).getAllDocuments());
				}
				if (classNo >= 0) {
				//	break;
				}
				classNo++;
			}
			
			System.out.println("Size "+allDocuments.size());
			for (SimpleDocument simpleDocument : allDocuments) {
				fileWriter.write("------------------------\n");
				System.out.println("------------------------------------------------------------------------");
				Document document = (Document)simpleDocument;
				fileWriter.write(document.getDocInJavaFile()+","+document.getDocName()+"\n");
				System.out.println(document.getDocInJavaFile()+","+document.getDocName());
				fileWriter.write(document.getStartPosition().getLine()+" , "+document.getEndPosition().getLine()+"\n");
				System.out.println(document.getStartPosition().getLine()+" , "+document.getEndPosition().getLine());
				System.out.println(new JavaFileReader().getText(document));
				fileWriter.write("------------------------------------------------------------------------\n");
				fileWriter.write(new JavaFileReader().getText(document)+"\n");
				fileWriter.write("------------------------------------------------------------------------\n");
				fileWriter.write(document.getArticle()+"\n");
				fileWriter.write("------------------------------------------------------------------------\n");
				fileWriter.write(document.getTermsInString()+"\n");
			}
			fileWriter.close();
//			System.exit(0);
			VectorSpaceModel vectorSpaceModel = new VectorSpaceModel(allDocuments);
			System.out.println("Initializing.............");
			storeVectorSpaceMatrix(vectorSpaceModel.getVectorSpaceMatrix());
	}

	private void storeVectorSpaceMatrix(VectorSpaceMatrix vectorSpaceMatrix) {
		System.out.println("Vector Space Model is storing...");
		try {
			FileOutputStream file = new FileOutputStream("vectorspace.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
			objectOutputStream.writeObject(vectorSpaceMatrix);
			objectOutputStream.close();
			System.out.println("Vector Space Model is stored");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public VectorSpaceMatrix loadVectorSpaceMatrix() throws IOException{
		System.out.println("Vector Space Model is loading...");
		VectorSpaceMatrix vectorSpaceMatrix = null;
		FileInputStream file = new FileInputStream("vectorspace.ser");
		ObjectInputStream objectInputStream = new ObjectInputStream(file);
		try {
			vectorSpaceMatrix = (VectorSpaceMatrix) objectInputStream.readObject();
			objectInputStream.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return vectorSpaceMatrix;
	}
	private void openJavaFile(String fileName){
		File file = new File(StringUtils.getFilePathAmongOS(fileName));
		JavaFileReader javaFileReader = new JavaFileReader();
		String val = "<code>Nothing present here</code>";
		if (javaFileReader.openFile(file)) {
			val = javaFileReader.getText();
		}
		ClassPage d = new ClassPage(file);
		d.setEditor(new RSyntaxTextArea());
	    d.getEditor().setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
	    d.getEditor().setCodeFoldingEnabled(true);
	    d.getEditor().setAntiAliasingEnabled(true);
		d.setScroll(new RTextScrollPane(d.getEditor()));
		d.getScroll().setFoldIndicatorEnabled(true);
		JPanel t = d.getTabPanel();
		t.add(d.getScroll(), BorderLayout.CENTER);
		t.doLayout();
		d.setType(SyntaxConstants.SYNTAX_STYLE_JAVA);
		d.setText(val);
		getTabs().addTab(d.getFilename(), t);
		int index = getTabs().getTabCount()-1;
		JPanel pnlTab = new JPanel(new GridBagLayout()), iconPanel = new JPanel(new GridLayout(1, 1, 4, 4));
		pnlTab.setOpaque(false);
		JLabel lblTitle = new JLabel(d.getFilename());
		ImageIcon icon = new ImageIcon("res/close.png");
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
			System.out.println("Error!");
		}
		getFrame().repaint();
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int pos = getTabs().getSelectedIndex();
				AppManager.getDocuments().remove(pos);
				getTabs().remove(pos);	
			}
		});
	}
	private void updateTheQueryOnRelevanceFeedback(VectorSpaceMatrix vectorSpaceMatrix){
        List<Integer> relDocs = new ArrayList<Integer>();
        List<Integer> irrelDocs = new ArrayList<Integer>();
		for(int i=0;i<feedbacks.size();i++)
		{
			if (feedbacks.get(i).equals(Feedback.REL)) {
				for (int j = 0; j < vectorSpaceMatrix.getSimpleDocuments().size(); j++) {
					if (returnDocuments.get(i).isSameDocument(
							vectorSpaceMatrix.getSimpleDocuments().get(j))) {
						// System.out.println("Hello");
						relDocs.add(j);
						break;
					}
				}
			} else if (feedbacks.get(i).equals(Feedback.IRRL)) {
				for (int j = 0; j < vectorSpaceMatrix.getSimpleDocuments().size(); j++) {
					if (returnDocuments.get(i).isSameDocument(
							vectorSpaceMatrix.getSimpleDocuments().get(j))) {
						// System.out.println("Hello");
						irrelDocs.add(j);
						break;
					}
				}
			}
		}
	    double alpha = 1.0,beta=0.75, gyma=0.25;
		// update with relevant docs
		if (relDocs.size() >= 1) {
			double offset = beta / relDocs.size();
			for (int i = 0; i < query.getVectorInVectorSpaceModel().length ; i++) {
				double weight = 0;
				for (int j = 0; j < relDocs.size(); j++) {
					weight += vectorSpaceMatrix.getTERM_DOCUMENT_MATRIX()[i][relDocs.get(j)];
					System.out.println("YES");
				}
				query.getVectorInVectorSpaceModel()[i] += (weight * offset);
				query.getVectorInVectorSpaceModel()[i] = validateTermWeight(query.getVectorInVectorSpaceModel()[i]);
			}
		}
		// update with irrelevant docs
		if (irrelDocs.size() >= 1) {
			double offset = gyma / irrelDocs.size();
			for (int i = 0; i < query.getVectorInVectorSpaceModel().length ; i++) {
				double weight = 0;
				for (int j = 0; j < irrelDocs.size(); j++) {
					weight += vectorSpaceMatrix.getTERM_DOCUMENT_MATRIX()[i][irrelDocs.get(j)];
				}
				query.getVectorInVectorSpaceModel()[i] -= (weight * offset);
				query.getVectorInVectorSpaceModel()[i] = validateTermWeight(query.getVectorInVectorSpaceModel()[i]);
			}
		}
	}
	/**
	 * validate term weight
	 */
	private static double validateTermWeight(double termWeight){
		if (termWeight < 0) {
			return 0;
		}
		return termWeight;
	}
	private void updateNoOfRelDocs(){
		rel =0;
		irrel =0;
		for (Feedback feedback : feedbacks) {
			if (feedback.equals(Feedback.REL)) {
				rel++;
			}
			else if(feedback.equals(Feedback.IRRL)){
				irrel++;
			}
		}
	}
	
}
