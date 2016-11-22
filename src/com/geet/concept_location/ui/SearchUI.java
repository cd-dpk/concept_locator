package com.geet.concept_location.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.Scrollable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.geet.concept_location.corpus_creation.SimpleDocument;
import com.geet.concept_location.indexing_vsm.Feedback;
import com.geet.concept_location.indexing_vsm.VectorSpaceMatrix;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;
import com.geet.concept_location.io.JavaFileReader;

public class SearchUI extends JPanel{
	
	private Window window;
	private JTextField searchTextField;
	private JButton openButton;
	public JButton getOpenButton() {
		return openButton;
	}
	public void setOpenButton(JButton openButton) {
		this.openButton = openButton;
	}
	private JButton searchButton;
	private RelevanceFeedback relevanceFeedback;
	public RelevanceFeedback getRelevanceFeedback() {
		return relevanceFeedback;
	}
	public void setRelevanceFeedback(RelevanceFeedback relevanceFeedback) {
		this.relevanceFeedback = relevanceFeedback;
	}
	private DefaultListModel<SimpleDocument> listModel = new DefaultListModel();
	private JList searchResultList = new JList(listModel);
	private List<SimpleDocument> lsiDocuments = new ArrayList<SimpleDocument>();
	private Bound bound;
	public Bound getBound() {
		return bound;
	}
	public void setBound(Bound bound) {
		this.bound = bound;
	}
	public SearchUI(Window window,Bound bound){
		setLayout(null);
		setBound(bound);
		this.window = window;
		searchTextField = new JTextField("Enter Query");
		searchTextField.setBounds(0, 0, (int)(.80*bound.getWidth()), 30);
		add(searchTextField);
		searchButton = new JButton("Search");
		searchButton.setBounds((int)(.80*bound.getWidth())+2, 0, (int)(.2*bound.getWidth()), 30);
		//searchButton.setIcon(new ImageIcon("src/res/search.png"));
		add(searchButton);
		openButton = new JButton("OPEN");
		openButton.setBounds(0, 30, 100, 30);
		add(openButton);
		openButton.setVisible(false);
		relevanceFeedback = new RelevanceFeedback(bound);
		relevanceFeedback.setBounds(200, 30, bound.getWidth()-200, 50);
		add(relevanceFeedback);
		relevanceFeedback.setVisible(false);
		searchResultList.setCellRenderer(new SearhResult());
		for (SimpleDocument document : lsiDocuments) {
			listModel.addElement(document);
		}
		JScrollPane scrollPane =new JScrollPane(searchResultList);
		scrollPane.setBounds(0, 80, bound.getWidth(),bound.getHeigh()-80);
		add(scrollPane);
		listenActions();
	}
	
	
	private void listenActions(){
		getRelevanceFeedback().getRelevanceFeedback().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppManager.updateTheQueryOnRelevanceFeedback(AppManager.getVectorSpaceModel().getVectorSpaceMatrix());
				AppManager.setReturnDocuments(AppManager.getVectorSpaceModel().searchWithQueryVector(AppManager.getQuery()));
				AppManager.setFeedbacks(new ArrayList<Feedback>());
				AppManager.setRound(AppManager.getRound()+1);
				getOpenButton().setEnabled(false);
				getRelevanceFeedback().getRoundLabel().setText("Round "+AppManager.getRound()+"");
				for (int i = 0; i < AppManager.getReturnDocuments().size(); i++) {
					AppManager.getFeedbacks().add(Feedback.NORMAL);
				}
				updateNoOfRelDocs();
				getRelevanceFeedback().getRelLabel().setText(AppManager.getRel()+"");
				getRelevanceFeedback().getIrrelLabel().setText(AppManager.getIrrel()+"");
				updateList(AppManager.getReturnDocuments());
			}
		});
		getOpenButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// java file
				int index = getSearchResultList().getSelectedIndex();
				if (index != -1) {
					window.openJavaFile(AppManager.getReturnDocuments().get(index).getDocInJavaFile());
				}
			}
		});
		
		getSearchTextField().addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					performSearch();
				}
			}
		});
		
		getSearchButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performSearch();
			}
		});
		
		getSearchResultList().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int index = getSearchResultList().getSelectedIndex();
				if (index !=-1) {
					if (AppManager.getFeedbacks().get(index).equals(Feedback.NORMAL)) {
						getRelevanceFeedback().getNormalButton()
								.setSelected(true);
					} else if (AppManager.getFeedbacks().get(index).equals(
							Feedback.REL)) {
						getRelevanceFeedback().getRelButton()
								.setSelected(true);
					} else if (AppManager.getFeedbacks().get(index).equals(Feedback.IRRL)) {
						getRelevanceFeedback().getIrrelButton().setSelected(true);
					}
					getOpenButton().setEnabled(true);
				}	
			}
		});
		
		getRelevanceFeedback().getRelButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = getSearchResultList().getSelectedIndex();
				if (index !=-1) {
					AppManager.getFeedbacks().set(index,Feedback.REL);
					System.out.println(AppManager.getFeedbacks().size());
//					System.out.println(feedbacks.get(index).toString());
					updateNoOfRelDocs();
					getRelevanceFeedback().getRelLabel().setText(AppManager.getRel()+"");
					getRelevanceFeedback().getIrrelLabel().setText(AppManager.getIrrel()+"");
				}
			}
		});
		
		getRelevanceFeedback().getIrrelButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = getSearchResultList().getSelectedIndex();
				if (index !=-1) {
					AppManager.getFeedbacks().set(index,Feedback.IRRL);
					updateNoOfRelDocs();
					getRelevanceFeedback().getRelLabel().setText(AppManager.getRel()+"");
					getRelevanceFeedback().getIrrelLabel().setText(AppManager.getIrrel()+"");
				}
			}
		});
		
		getRelevanceFeedback().getNormalButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = getSearchResultList().getSelectedIndex();
				if (index !=-1) {
					AppManager.getFeedbacks().set(index,Feedback.NORMAL);
					updateNoOfRelDocs();
					getRelevanceFeedback().getRelLabel().setText(AppManager.getRel()+"");
					getRelevanceFeedback().getIrrelLabel().setText(AppManager.getIrrel()+"");
				}
			}
		});
		
	}
	/**
	 * @deprecated
	 */
	private class RTextAreaListItem extends RSyntaxTextArea implements ListCellRenderer, Scrollable{
		protected RTextAreaListItem() {
	        setBorder(BorderFactory.createLineBorder(Color.blue));
		}
		protected RTextAreaListItem(int rows, int cols){
			super(rows, cols);
	        setBorder(BorderFactory.createLineBorder(Color.blue));
		}
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			SimpleDocument simpleDocument = (SimpleDocument) value;
			setText(simpleDocument.getArticle());
			setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		    return this;
		}
	}
	
	private class SearhResult extends MyModel implements ListCellRenderer{
		protected SearhResult() {
		    setBorder(BorderFactory.createLineBorder(Color.blue));
		}
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			SimpleDocument simpleDocument = (SimpleDocument) value;
			getFileName().setText(simpleDocument.getDocInJavaFile()+"\n"+simpleDocument.getDocName()+"\n["+simpleDocument.getStartPosition().getLine()+","+simpleDocument.getEndPosition().getLine()+"]\n"+simpleDocument.getScore());
			getFileName().setEditable(false);
//			System.out.println(simpleDocument.docInJavaFile);
			getrSyntaxTextArea().setText(new JavaFileReader().getText(simpleDocument));
			getrSyntaxTextArea().setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
			getrSyntaxTextArea().setCodeFoldingEnabled(true);
			getrSyntaxTextArea().setAntiAliasingEnabled(true);
			if (isSelected) {
				getrSyntaxTextArea().setBackground(Color.LIGHT_GRAY);
			}else{
				getrSyntaxTextArea().setBackground(Color.WHITE);
			}
			return this;
		}
	}

	public JTextField getSearchTextField() {
		return searchTextField;
	}

	public void setSearchTextField(JTextField searchTextField) {
		this.searchTextField = searchTextField;
	}

	public JButton getSearchButton() {
		return searchButton;
	}

	public void setSearchButton(JButton searchButton) {
		this.searchButton = searchButton;
	}

	public JList getSearchResultList() {
		return searchResultList;
	}

	public void setSearchResultList(JList searchResultList) {
		this.searchResultList = searchResultList;
	}
	public void updateList(List<SimpleDocument> documents){
		listModel = new DefaultListModel<SimpleDocument>();
		for (SimpleDocument document : documents) {
			listModel.addElement(document);
		}	
		searchResultList.setModel(listModel);
	}
	public DefaultListModel<SimpleDocument> getListModel() {
		return listModel;
	}
	public void setListModel(DefaultListModel<SimpleDocument> listModel) {
		this.listModel = listModel;
	}
	public List<SimpleDocument> getLsiDocuments() {
		return lsiDocuments;
	}
	public void setLsiDocuments(List<SimpleDocument> lsiDocuments) {
		this.lsiDocuments = lsiDocuments;
	}
	
	public void performSearch(){
		SimpleDocument queryDocument = new SimpleDocument.Builder().docName("Query").article(getSearchTextField().getText()).build();
		try {
			AppManager.setVectorSpaceModel(new VectorSpaceModel(AppManager.loadVectorSpaceMatrix()));
			AppManager.setQuery(AppManager.getVectorSpaceModel().getQuery(queryDocument));
			AppManager.setReturnDocuments(AppManager.getVectorSpaceModel().searchWithQueryVector(AppManager.getQuery()));
			getOpenButton().setVisible(true);
			getOpenButton().setEnabled(false);
			getRelevanceFeedback().setVisible(true);
			AppManager.setRound(1);
			getRelevanceFeedback().getRoundLabel().setText("Round "+AppManager.getRound()+"");
			AppManager.setFeedbacks(new ArrayList<Feedback>());
			for (int i = 0; i < AppManager.getReturnDocuments().size(); i++) {
				AppManager.getFeedbacks().add(Feedback.NORMAL);
			}
			updateNoOfRelDocs();
			getRelevanceFeedback().getRelLabel().setText(AppManager.getRel()+"");
			getRelevanceFeedback().getIrrelLabel().setText(AppManager.getIrrel()+"");
			updateList(AppManager.getReturnDocuments());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	private void updateNoOfRelDocs(){
		AppManager.setRel(0);
		AppManager.setIrrel(0);
		for (Feedback feedback : AppManager.getFeedbacks()) {
			if (feedback.equals(Feedback.REL)) {
				AppManager.setRel(AppManager.getRel()+1);
			}
			else if(feedback.equals(Feedback.IRRL)){
				AppManager.setIrrel(AppManager.getIrrel()+1);
			}
		}
	}
}
