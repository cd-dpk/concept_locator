package com.geet.concept_location.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Document;

import com.geet.concept_location.constants.UIConstants;
import com.geet.concept_location.corpus_creation.Document.DocumentType;
import com.geet.concept_location.io.JavaFileReader;
import com.geet.concept_location.utils.StringUtils;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.CommentsParser;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ConceptLocatorFrame extends JFrame {
	
	FileTree projectTreePanel;
	JTextArea sourceTextArea;
	
	public ConceptLocatorFrame(){
		super("Concept Locator");
		setLayout(null);
		projectTreePanel = new FileTree(new File("."));
		projectTreePanel.setBounds(0, UIConstants.Menu_Height, UIConstants.FILE_TREE_WIDTH, UIConstants.Height-10);
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
								new ClassVisitor().visit(cu, null);
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
		JScrollPane scrollPane = new JScrollPane(sourceTextArea);
		scrollPane.setBounds(UIConstants.FILE_TREE_WIDTH+20, UIConstants.Menu_Height, 800, UIConstants.Height-UIConstants.Menu_Height-40);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
		showFrame();
	}
	
	private static class ClassVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(MethodDeclaration n, Object arg1) {
			 if (n.getComment() != null && n.getComment() instanceof JavadocComment) {
                 System.out.println(n.getBeginLine()+","+n.getBeginColumn());
                 System.out.println(n.getComment());
                 Comment comment = n.getComment();
                 System.out.println(comment.getBeginLine()+","+comment.getBeginColumn());
             }
			
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

}
