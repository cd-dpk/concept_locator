package com.geet.concept_location.ui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.DocumentExtractor;
import com.geet.concept_location.corpus_creation.SimpleDocument;
import com.geet.concept_location.indexing_vsm.VectorSpaceMatrix;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;
import com.geet.concept_location.io.JavaFileReader;
import com.geet.concept_location.preprocessing.JavaClassPreprocessor;
import com.geet.concept_location.utils.JavaFileFilter;
public class FileTree extends JPanel {
	JTree tree;
	public List<String> javaFilePaths = new ArrayList<String>();
	/** Construct a FileTree */
	  public FileTree(File dir) {
	    setLayout(new BorderLayout());
	   // Make a tree list with all the nodes, and make it a JTree
	    tree = new JTree(addNodes(null, dir));
	   // Add a listener
	   // Lastly, put the JTree into a JScrollPane.
	    JScrollPane scrollpane = new JScrollPane();
	    scrollpane.getViewport().add(tree);
	    add(BorderLayout.CENTER, scrollpane);
	  }
	  public JTree getTree() {
		return tree;
	}
	public void setTree(JTree tree) {
		this.tree = tree;
	}
	/** Add nodes from under "dir" into curTop. Highly recursive. */
	  DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
		  String curPath = dir.getPath();
	    DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
	    if (curTop != null) {// should only be null at root
	      curTop.add(curDir);
	    }
	    Vector ol = new Vector();
	    String[] tmp = dir.list();
	    for (int i = 0; i < tmp.length; i++)
	      ol.addElement(tmp[i]);
	    Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
	    File f;
	    Vector files = new Vector();
	   // Make two passes, one for Dirs and one for Files. This is #1.
	    for (int i = 0; i < ol.size(); i++) {
	      String thisObject = (String) ol.elementAt(i);
	      String newPath;
	      if (curPath.equals(".")){
	        newPath = thisObject;
	      }
	      else{
	        newPath = curPath + File.separator + thisObject;
	      }
	      f = new File(newPath);
	      if (f.isDirectory() && !f.isHidden()){
	        addNodes(curDir, f);
	      }
	      else if(!f.isHidden()){
	    	  if (new JavaFileFilter().accept(new File(newPath))) {
			        javaFilePaths.add(newPath);
			        System.out.println(newPath);
		        }  
	    	files.addElement(thisObject);
	      }
	    }
	   // Pass two: for files.
	    for (int fnum = 0; fnum < files.size(); fnum++)
	      curDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
	    return curDir;
	  }
	  public Dimension getMinimumSize() {
	    return new Dimension(200, 400);
	  }
	  public Dimension getPreferredSize() {
	    return new Dimension(200, 400);
	  }

}
