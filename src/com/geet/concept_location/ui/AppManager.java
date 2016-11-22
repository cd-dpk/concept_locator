/**
 * 
 */
package com.geet.concept_location.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

public class AppManager {
	private static Window win = null;
	private static Vector<Page> documents;
	private static String projectPath="UltimateCalculator-master";
	private static List<String> javaFilePaths =new ArrayList<String>();
	private static Query query;
	private static VectorSpaceModel vectorSpaceModel;
	private static List<Feedback> feedbacks = new ArrayList<Feedback>();
	private static List<SimpleDocument> returnDocuments = new ArrayList<SimpleDocument>();
	private static int rel = 0, irrel = 0, round = 0;
	
	public static Vector<Page> getDocuments() {
		return documents;
	}
	public static void addDocument(Page d) {
		documents.addElement(d);
	}
	public static void main(String[] args) {
		documents = new Vector<Page>();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					AppManager.win = new Window(UIConstants.WIDTH, UIConstants.HEIGHT);					
				} catch(Exception e) {
					e.printStackTrace();
					System.out.println("Failed to create the main window; quitting...");
					System.exit(1);
				}
			}
		});
	}
	public static String getProjectPath() {
		return projectPath;
	}
	public static void setProjectPath(String projectPath) {
		AppManager.projectPath = projectPath;
	}
	public static List<String> getJavaFilePaths() {
		return javaFilePaths;
	}
	public static void setJavaFilePaths(List<String> javaFilePaths) {
		AppManager.javaFilePaths = javaFilePaths;
	}
	public static Query getQuery() {
		return query;
	}
	public static void setQuery(Query query) {
		AppManager.query = query;
	}
	public static VectorSpaceModel getVectorSpaceModel() {
		return vectorSpaceModel;
	}
	public static void setVectorSpaceModel(VectorSpaceModel vectorSpaceModel) {
		AppManager.vectorSpaceModel = vectorSpaceModel;
	}
	public static List<Feedback> getFeedbacks() {
		return feedbacks;
	}
	public static void setFeedbacks(List<Feedback> feedbacks) {
		AppManager.feedbacks = feedbacks;
	}
	public static List<SimpleDocument> getReturnDocuments() {
		return returnDocuments;
	}
	public static void setReturnDocuments(List<SimpleDocument> returnDocuments) {
		AppManager.returnDocuments = returnDocuments;
	}
	public static int getRel() {
		return rel;
	}
	public static void setRel(int rel) {
		AppManager.rel = rel;
	}
	public static int getIrrel() {
		return irrel;
	}
	public static void setIrrel(int irrel) {
		AppManager.irrel = irrel;
	}
	public static int getRound() {
		return round;
	}
	public static void setRound(int round) {
		AppManager.round = round;
	}
	public static void setDocuments(Vector<Page> documents) {
		AppManager.documents = documents;
	}
	
	public static VectorSpaceMatrix loadVectorSpaceMatrix() throws IOException{
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
	public static void updateTheQueryOnRelevanceFeedback(VectorSpaceMatrix vectorSpaceMatrix){
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
	
	// indexing...
	public static void createVectorSpaceMatrix() throws IOException{
			FileWriter fileWriter = new FileWriter(new File("D:\\BSSE0501\\Project-801\\Corpus.txt"));
			List<SimpleDocument> allDocuments = new ArrayList<SimpleDocument>();
			int classNo = 0;
			for (String path : AppManager.getJavaFilePaths()) {
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

	private static void storeVectorSpaceMatrix(VectorSpaceMatrix vectorSpaceMatrix) {
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

}
