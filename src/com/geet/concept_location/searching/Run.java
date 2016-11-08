package com.geet.concept_location.searching;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.SimpleDocument;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;
import com.geet.concept_location.utils.JavaFileFilter;

public class Run {
	
	public int topOne=0;
	public int topFive=0;
	public int topTen=0;
	
	List<Bug> bugs = new ArrayList<Bug>();
	private JTree tree;
	public List<String> javaFilePaths = new ArrayList<String>();

	public Run(File dir) {
		// Make a tree list with all the nodes, and make it a JTree
		tree = new JTree(addNodes(null, dir));
	}

	/** Add nodes from under "dir" into curTop. Highly recursive. */
	DefaultMutableTreeNode addNodes(DefaultMutableTreeNode curTop, File dir) {
		String curPath = dir.getPath();
		DefaultMutableTreeNode curDir = new DefaultMutableTreeNode(curPath);
		if (curTop != null) { // should only be null at root
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
			if (curPath.equals(".")) {
				newPath = thisObject;
			} else {
				newPath = curPath + File.separator + thisObject;
			}
			f = new File(newPath);
			if (f.isDirectory() && !f.isHidden()) {
				addNodes(curDir, f);
			} else if (!f.isHidden()) {
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

	public static void main(String[] args) {
		Run run = new Run(new File("org"));
		for (String string : run.javaFilePaths) {
			System.out.println(string);
		}
		System.out.println(run.javaFilePaths.size());
	}
	
	public void setRatio(){
		VectorSpaceModel vectorSpaceModel = new VectorSpaceModel(new ArrayList<SimpleDocument>());
		for (Bug bug: bugs) {
			List<Document> returnDocuments = vectorSpaceModel.search(new SimpleDocument(bug.getBugDescription()));
			int index = 10; // not in desired place
			for (int i = 0; i < bug.getFixedFiles().size(); i++) {
				for (int j = 0; j < returnDocuments.size(); j++) {
					if (bug.getFixedFiles().get(i).equals(returnDocuments.get(j).getDocInJavaFile())) {
						if (j <= index) {
							index = j;
						}
						break;
					}
				}
			}
			if (index == 0 ) {
				topOne++;
			}else if(index>= 1 && index <= 4){
				topFive++;
			}
			else if(index>=5 && index <=9){
				topTen++;
			}
		}
	}
	
}
