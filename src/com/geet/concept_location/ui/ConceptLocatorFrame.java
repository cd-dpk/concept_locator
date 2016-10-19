package com.geet.concept_location.ui;

import java.awt.Color;
import java.awt.Menu;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.geet.concept_location.constants.UIConstants;
import com.geet.concept_location.io.JavaFileReader;
import com.geet.concept_location.utils.StringUtils;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ConceptLocatorFrame extends JFrame {
	
	FileTree projectTreePanel;
	JTextArea sourceTextArea;
	SearchBoxPanelUI searchBoxPanel;
	public ConceptLocatorFrame(){
		super("Concept Locator");
		setLayout(null);
		createMenuBar();
		searchBoxPanel = new SearchBoxPanelUI(UIConstants.Width, UIConstants.Menu_Height);
		searchBoxPanel.setBounds(UIConstants.PADDING_LEFT, UIConstants.PADDING_TOP, UIConstants.Width-UIConstants.PADDING_RIGHT, UIConstants.Menu_Height);
		add(searchBoxPanel);
		projectTreePanel = new FileTree(new File("."));
		projectTreePanel.setBounds(UIConstants.PADDING_LEFT, UIConstants.Menu_Height+UIConstants.PADDING_TOP, UIConstants.FILE_TREE_WIDTH, UIConstants.Height-UIConstants.Menu_Height-50-UIConstants.PADDING_TOP);
		projectTreePanel.tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				JavaFileReader javaFileReader = new JavaFileReader();
				String filePath = StringUtils.getFilePathName(e.getPath().toString());
				try {
					FileInputStream in = new FileInputStream(filePath);
					if(javaFileReader.openFile(new File (filePath))){
						sourceTextArea.setText(javaFileReader.getText());
					}
					 CompilationUnit cu;
				            try {
								cu = JavaParser.parse(in);
								System.out.println(cu.toString());
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}				        
				        // prints the resulting compilation unit to default system output
				       
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		add(projectTreePanel);

		sourceTextArea = new JTextArea("Hello");
		SourceViewPanel sourceViewPanel = new SourceViewPanel(sourceTextArea,new Bound(0,0, 800, UIConstants.Height-UIConstants.Menu_Height-50-UIConstants.PADDING_TOP));
		sourceViewPanel.setBounds(UIConstants.FILE_TREE_WIDTH+20, UIConstants.Menu_Height+UIConstants.PADDING_TOP, 800, UIConstants.Height-UIConstants.Menu_Height-50-UIConstants.PADDING_TOP);
		add(sourceViewPanel);
		/*JScrollPane scrollPane = new JScrollPane(sourceTextArea);
		scrollPane.setBounds(UIConstants.FILE_TREE_WIDTH+20, UIConstants.Menu_Height+UIConstants.PADDING_TOP, 800, UIConstants.Height-UIConstants.Menu_Height-50-UIConstants.PADDING_TOP);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
		*/showFrame();
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
		
		JMenuItem newFileItem = new JMenuItem("New");
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(newFileItem);
		fileMenu.add(exitItem);
		
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		
		return;
	}

}
