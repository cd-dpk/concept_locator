package com.geet.concept_location.searching;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.DocumentExtractor;
import com.geet.concept_location.corpus_creation.SimpleDocument;
import com.geet.concept_location.indexing_vsm.Query;
import com.geet.concept_location.indexing_vsm.VectorSpaceMatrix;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;
import com.geet.concept_location.io.JavaFileReader;
import com.geet.concept_location.preprocessing.JavaClassPreprocessor;
import com.geet.concept_location.utils.JavaFileFilter;
public class Run {
	public int topOne=0;
	public int topFive=0;
	public int topTen=0;
	List<Bug> bugs = new ArrayList<Bug>();
	private JTree tree;
	public List<String> javaFilePaths = new ArrayList<String>();
	//Rocchio Algorithm 
	private final static double alpha = 1.0, beta = 0.75, gyma = 0.25;	
	public Run(File dir) {
		// Make a tree list with all the nodes, and make it a JTree
		tree = new JTree(addNodes(null, dir));
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
	public Run() {
	}
	public static void main(String[] args) throws Exception, SAXException, IOException {
		Run run = new Run(new File("D:\\BSSE0501\\Project-801\\UltimateCalculator-master"));
		System.out.println(run.javaFilePaths.size());
		run.createVectorSpaceMatrix();
	//	Run run = new Run();
		run.setRatio();
//		VectorSpaceMatrix vectorSpaceMatrix = run.loadVectorSpaceMatrix();
//		run.updateTheQueryOnRelevanceFeedback(vectorSpaceMatrix,10);
	}	
	public void updateTheQueryOnRelevanceFeedback(VectorSpaceMatrix vectorSpaceMatrix, int id) throws ParserConfigurationException, SAXException, IOException{
		// read the feedback.xml
		File inputFile = new File("feedback.xml");
        DocumentBuilderFactory dbFactory 
           = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" 
           + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("ret");
        System.out.println("----------------------------");
        List<Integer> relDocs = new ArrayList<Integer>();
        List<Integer> irrelDocs = new ArrayList<Integer>();
		for(int i=0;i<nList.getLength();i++)
	    {
			 
			Element bugElement = (Element) nList.item(i);
			String file ="";
		    String feedback = "";
		    NodeList summary = bugElement.getElementsByTagName("file");
	        for (int j = 0; j < summary.getLength(); ++j)
	        {
	            Element option = (Element) summary.item(j);
	            if (option.hasChildNodes()) {
		            String optionText = option.getFirstChild().getNodeValue();
			        //    System.out.println("Summary :"+optionText);
			            file = optionText;
				}
	        }
	        NodeList fileList = bugElement.getElementsByTagName("rf");
	        for (int j = 0; j < fileList.getLength(); ++j)
	        {
	            Element option = (Element) fileList.item(j);
	            if (option.hasChildNodes()) {
		            String optionText = option.getFirstChild().getNodeValue();
			        //    System.out.println("File :"+optionText);
			            feedback = (optionText);					
				}
	        }
	        if (feedback.equals("1")) {
	        	for (int j = 0; j < vectorSpaceMatrix.documents.size(); j++) {
					if (file.equals(vectorSpaceMatrix.documents.get(j))) {
//						System.out.println("Hello");
						relDocs.add(j);
						break;
					}
				}
			}
	        if (feedback.equals("-1")) {
	        	for (int j = 0; j < vectorSpaceMatrix.documents.size(); j++) {
					if (file.equals(vectorSpaceMatrix.documents.get(j))) {
//						System.out.println("Bye");
						irrelDocs.add(j);
						break;
					}
				}
			}
	    }
		// load the query
		Query query = loadQuery(id);
		// update with relevant docs
		if (relDocs.size() >= 1) {
			double offset = beta / relDocs.size();
			for (int i = 0; i < query.vectorInVectorSpaceModel.length ; i++) {
				double weight = 0;
				for (int j = 0; j < relDocs.size(); j++) {
					weight += vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][relDocs.get(j)];
					System.out.println("YES");
				}
				query.vectorInVectorSpaceModel[i] += (weight * offset);
				query.vectorInVectorSpaceModel[i] = validateTermWeight(query.vectorInVectorSpaceModel[i]);

			}
		}
		
		if (irrelDocs.size() >= 1) {
			double offset = gyma / irrelDocs.size();
			for (int i = 0; i < query.vectorInVectorSpaceModel.length ; i++) {
				double weight = 0;
				for (int j = 0; j < irrelDocs.size(); j++) {
					weight += vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][irrelDocs.get(j)];
				}
				query.vectorInVectorSpaceModel[i] -= (weight * offset);
				query.vectorInVectorSpaceModel[i] = validateTermWeight(query.vectorInVectorSpaceModel[i]);
			}
		}
		storeQuery(query,id);
		
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
	
	public void createVectorSpaceMatrix(){
		List<SimpleDocument> allDocuments = new ArrayList<SimpleDocument>();
		int classNo = 0;
		for (String path : javaFilePaths) {
			if (new JavaClassPreprocessor().processJavaFile(new File(path))) {
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
			System.out.println("------------------------------------------------------------------------");
			Document document = (Document)simpleDocument;
			System.out.println(document.docInJavaFile+","+document.docName);
			System.out.println(document.getStartPosition().line+" , "+document.getEndPosition().line);
			System.out.println(new JavaFileReader().getText(document));
			System.out.println("------------------------------------------------------------------------");
			System.out.println(document.getArticle());
		}
		System.exit(0);
		VectorSpaceModel vectorSpaceModel = new VectorSpaceModel(allDocuments);
		System.out.println("Initializing.............");
		storeVectorSpaceMatrix(vectorSpaceModel.getVectorSpaceMatrix());
	}
	/*
	 * @deprecated
	 */
	public void readFeatures() throws Exception{
		File inputFile = new File("features.xml");
        DocumentBuilderFactory dbFactory 
           = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" 
           + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("feature");
        System.out.println("----------------------------");
		for(int i=0;i<nList.getLength();i++)
	    {
			Feature feature =new Feature();
	        Element bugElement = (Element) nList.item(i);
	        feature.id = bugElement.getAttribute("id");
	        NodeList summary = bugElement.getElementsByTagName("des");
	        for (int j = 0; j < summary.getLength(); ++j)
	        {
	            Element option = (Element) summary.item(j);
	            if (option.hasChildNodes()) {
		            String optionText = option.getFirstChild().getNodeValue();
			        //    System.out.println("Summary :"+optionText);
			            feature.description += optionText;
				}
	        }
	        NodeList fileList = bugElement.getElementsByTagName("file");
	        for (int j = 0; j < fileList.getLength(); ++j)
	        {
	            Element option = (Element) fileList.item(j);
	            if (option.hasChildNodes()) {
		            String optionText = option.getFirstChild().getNodeValue();
			        //    System.out.println("File :"+optionText);
			            feature.fixedFiles.add(optionText);					
				}
	        }
	        System.out.println(feature.toString());
	    }
	}
	public void setRatio() throws ParserConfigurationException, SAXException, IOException{
		Calendar localCalendar = Calendar.getInstance();
		java.util.Date date = localCalendar.getTime();
		File outputFile = new File("VSM"+".txt");
		FileWriter fileWriter = new FileWriter(outputFile);
		VectorSpaceModel vectorSpaceModel = new VectorSpaceModel(loadVectorSpaceMatrix());
		File inputFile = new File("features.xml");
        DocumentBuilderFactory dbFactory 
           = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" 
           + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("feature");
        System.out.println("----------------------------");
		for(int i=10;i<nList.getLength();i++)
	    {
			Feature feature =new Feature();
	        Element bugElement = (Element) nList.item(i);
	        feature.id = bugElement.getAttribute("id");
	        NodeList summary = bugElement.getElementsByTagName("des");
	        for (int j = 0; j < summary.getLength(); ++j)
	        {
	            Element option = (Element) summary.item(j);
	            if (option.hasChildNodes()) {
		            String optionText = option.getFirstChild().getNodeValue();
			        //    System.out.println("Summary :"+optionText);
			            feature.description += optionText;
				}
	        }
	        NodeList fileList = bugElement.getElementsByTagName("file");
	        for (int j = 0; j < fileList.getLength(); ++j)
	        {
	            Element option = (Element) fileList.item(j);
	            if (option.hasChildNodes()) {
		            String optionText = option.getFirstChild().getNodeValue();
			        //    System.out.println("File :"+optionText);
			            feature.fixedFiles.add(optionText);					
				}
	        }
	        System.out.println(feature.toString());
	        SimpleDocument simpleDocument =new SimpleDocument("Bug",feature.getDescription());
	        System.out.println(simpleDocument.getArticle());
	        System.out.println(simpleDocument.getTermsInString());
	        //System.exit(0);
	        Query query = loadQuery(i);
	        List<SimpleDocument> returnDocuments = vectorSpaceModel.searchWithQueryVector(query);
	        int index = 10;// not in desired place
			for (int j = 0; j < feature.getFixedFiles().size(); j++) {
				for (int k = 0; j < returnDocuments.size(); k++) {
					if (feature.getFixedFiles().get(j).equals(returnDocuments.get(k).docInJavaFile)) {
						System.out.println("WOWW"+k+returnDocuments.get(k).docInJavaFile);
						if (k <= index) {
							index = k;
						}
						break;
					}
				}
			}
			printSearchResults(returnDocuments);
			System.out.println(feature.id+" "+index);
			fileWriter.write(feature.id+","+index+"\n");
			if (index == 0 ) {
				topOne++;
				topFive++;
				topTen++;
//				System.out.println(topOne);
			}else if(index < 6){
				topFive++;
				topTen++;
//				System.out.println(topFive);
			}
			else if(index <10){
				topTen++;
//				System.out.println(topTen);
			}
			if (i >= 10) {
				break;
			}
	    }
		fileWriter.write(nList.getLength()+","+topOne+","+topFive+","+topTen);
		fileWriter.close();
	}
	
	public void printSearchResults(List<SimpleDocument> simpleDocuments) throws IOException{
		FileWriter fileWriter = new FileWriter(new File("feedback.xml"));
		fileWriter.write("<feedbacks>\n");
		for (SimpleDocument simpleDocument : simpleDocuments) {
			String toString = "<ret>\n";
			toString += "<file>"+simpleDocument.docInJavaFile+"</file>\n";
			toString += "<rf>"+0+"</rf>\n";
			toString +="</ret>\n";
			fileWriter.write(toString);
		}
		fileWriter.write("</feedbacks>");
		fileWriter.close();
		
	}
	public void storeQuery(Query query, int id){
		System.out.println("Query Space Model is storing...");
		try {
			FileOutputStream file = new FileOutputStream(id+"query.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
			objectOutputStream.writeObject(query);
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Query loadQuery(int id) throws IOException{
		System.out.println("Query Space Model is loading...");
		Query query = null;
		FileInputStream file = new FileInputStream(id+"query.ser");
		ObjectInputStream objectInputStream = new ObjectInputStream(file);
		try {
			query = (Query) objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		objectInputStream.close();
		return query;
	}
	public void storeVectorSpaceMatrix(VectorSpaceMatrix vectorSpaceMatrix){
		System.out.println("Vector Space Model is storing...");
		try {
			FileOutputStream file = new FileOutputStream("vectorspace.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
			objectOutputStream.writeObject(vectorSpaceMatrix);
			objectOutputStream.close();
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
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		objectInputStream.close();
		return vectorSpaceMatrix;
	}
}