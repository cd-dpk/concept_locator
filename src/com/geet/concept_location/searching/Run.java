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
import com.geet.concept_location.indexing_vsm.VectorSpaceMatrix;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;
import com.geet.concept_location.preprocessing.JavaClassPreprocessor;
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
		//Run run = new Run(new File("/media/Video/SRC/ResultAnalysisTool"));
		//run.createVectorSpaceMatrix();
		Run run = new Run();
		run.setRatio();
//		run.readFeatures();
//		VectorSpaceMatrix vectorSpaceMatrix = run.loadVectorSpaceMatrix();
//		System.out.println(run.loadVectorSpaceMatrix().toString());
//		System.out.println(vectorSpaceMatrix.terms.size()+"X"+vectorSpaceMatrix.documents.size());
//		for (int i = 0; i < vectorSpaceMatrix.documents.size(); i++) {
//			System.out.println(vectorSpaceMatrix.documents.get(i));
//		}
		
	}
	public void createVectorSpaceMatrix(){
		List<SimpleDocument> allDocuments = new ArrayList<SimpleDocument>();
		int classNo = 0;
		for (String path : javaFilePaths) {
			if (new JavaClassPreprocessor().processJavaFile(new File(path))) {
				System.out.println(classNo);
				System.out.println(path);
				Document document = new DocumentExtractor(new File(path)).getExtractedDocument();
				System.out.println(document.getArticle());
				allDocuments.add(document);
			}
			if (classNo >= 0) {
			//	break;
			}
			classNo++;
		}
		System.out.println("Size "+allDocuments.size());
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
		File outputFile = new File("LSI"+".txt");
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
	        SimpleDocument simpleDocument =new SimpleDocument("Bug",feature.getDescription());
	        System.out.println(simpleDocument.getArticle());
	        System.out.println(simpleDocument.getTermsInString());
	        //System.exit(0);
	        List<SimpleDocument> returnDocuments = vectorSpaceModel.search(simpleDocument);
			int index = 10;// not in desired place
			for (int j = 0; j < feature.getFixedFiles().size(); j++) {
				for (int k = 0; j < returnDocuments.size(); k++) {
					if (feature.getFixedFiles().get(j).equals(returnDocuments.get(k).docInJavaFile)) {
						System.out.println("WOWW"+k);
						if (k <= index) {
							index = k;
						}
						break;
					}
				}
			}
			System.out.println(feature.id+" "+index);
			fileWriter.write(feature.id+","+index+"\n");
			if (index == 0 ) {
				topOne++;
				topFive++;
				topTen++;
				System.out.println(topOne);
			}else if(index < 6){
				topFive++;
				topTen++;
				System.out.println(topFive);
			}
			else if(index <10){
				topTen++;
				System.out.println(topTen);
			}
	    }
		fileWriter.write(nList.getLength()+","+topOne+","+topFive+","+topTen);
		fileWriter.close();
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