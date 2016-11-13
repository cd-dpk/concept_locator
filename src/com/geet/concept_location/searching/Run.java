package com.geet.concept_location.searching;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import javax.swing.JScrollPane;
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
import com.geet.concept_location.indexing_lsi.Lsi;
import com.geet.concept_location.indexing_lsi.LsiDocument;
import com.geet.concept_location.indexing_lsi.LsiQuery;
import com.geet.concept_location.indexing_lsi.LsiTerm;
import com.geet.concept_location.indexing_vsm.VectorSpaceMatrix;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;
import com.geet.concept_location.preprocessing.JavaClassPreprocessor;
import com.geet.concept_location.utils.JavaFileFilter;
public class Run {
	static String rootPath ="D:\\BSSE0501\\res\\";
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
	public static void main(String[] args) throws Exception, SAXException, IOException {
		Run run = new Run(new File("/media/Video/SRC/ResultAnalysisTool"));
		/*//run.generateLsiSpaceFromVectorSpaceMatrix();
		run.setLsiTerms();
		run.setLsiDocuments();*/
		VectorSpaceMatrix vectorSpaceMatrix = run.loadVectorSpaceMatrix();
		for (int i = 0; i < vectorSpaceMatrix.documents.size(); i++) {
			System.out.println(vectorSpaceMatrix.documents.get(i));
		}
	}
	
	@Deprecated
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
	
	
	public void generateLsiSpaceFromVectorSpaceMatrix() throws IOException{
		VectorSpaceModel vectorSpaceModel = new VectorSpaceModel();
		VectorSpaceMatrix vectorSpaceMatrix = loadVectorSpaceMatrix();
		System.out.println(vectorSpaceMatrix.terms.size() +"X"+ vectorSpaceMatrix.documents.size());
		vectorSpaceModel.generateLsi(vectorSpaceMatrix);
	}
	
	public VectorSpaceMatrix loadVectorSpaceMatrix() throws IOException{
		System.out.println("Vector Space Model is loading...");
		VectorSpaceMatrix vectorSpaceMatrix = null;
		FileInputStream file = new FileInputStream("rat_vectorspace.ser");
		ObjectInputStream objectInputStream = new ObjectInputStream(file);
		try {
			vectorSpaceMatrix = (VectorSpaceMatrix) objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		objectInputStream.close();
		return vectorSpaceMatrix;
	}
	@Deprecated
	public void generateLsiSpace(){
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
			classNo++;
		}
		System.out.println("Size "+allDocuments.size());
		VectorSpaceModel vectorSpaceModel = new VectorSpaceModel(allDocuments);
		System.out.println("Initializing.............");
		vectorSpaceModel.generateLsi();
	}
	
	
	public void setRatio() throws ParserConfigurationException, SAXException, IOException{
		Calendar localCalendar = Calendar.getInstance();
		java.util.Date date = localCalendar.getTime();
		File outputFile = new File(rootPath+"LSI"+date.getDay()+date.getHours()+date.getMinutes()+".txt");
		FileWriter fileWriter = new FileWriter(outputFile);
		File inputFile = new File("D:/BSSE0501/RESOURCE/SWT/bugRepository.xml");
        DocumentBuilderFactory dbFactory 
           = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" 
           + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("bug");
        System.out.println("----------------------------");
		for(int i=0;i<nList.getLength();i++)
	    {
			Bug bug =new Bug();
	        Element bugElement = (Element) nList.item(i);
	        bug.bugID = bugElement.getAttribute("id");
	        NodeList summary = bugElement.getElementsByTagName("summary");
	        for (int j = 0; j < summary.getLength(); ++j)
	        {
	            Element option = (Element) summary.item(j);
	            if (option.hasChildNodes()) {
		            String optionText = option.getFirstChild().getNodeValue();
			        //    System.out.println("Summary :"+optionText);
			            bug.summary += optionText;
				}
	        }
	        NodeList description = bugElement.getElementsByTagName("description");
	        for (int j = 0; j < description.getLength(); ++j)
	        {
	            Element option = (Element) description.item(j);
	            if (option.hasChildNodes()) {
	            	String optionText = option.getFirstChild().getNodeValue();
	    	        //    System.out.println("Description :"+optionText);
	    	            bug.description += optionText;	
				}
	        }
	        NodeList fileList = bugElement.getElementsByTagName("file");
	        for (int j = 0; j < fileList.getLength(); ++j)
	        {
	            Element option = (Element) fileList.item(j);
	            if (option.hasChildNodes()) {
		            String optionText = option.getFirstChild().getNodeValue();
			        //    System.out.println("File :"+optionText);
			            bug.fixedFiles.add(optionText);					
				}
	        }
	        System.out.println(bug.toString());
	        List<LsiDocument> returnDocuments = new Lsi().search(new LsiQuery(bug.summary, new com.geet.concept_location.indexing_lsi.Vector(Lsi.NUM_FACTORS)));
			int index = 10;// not in desired place
			for (int j = 0; j < bug.getFixedFiles().size(); j++) {
				for (int k = 0; j < returnDocuments.size(); k++) {
					if (bug.getFixedFiles().get(j).equals(returnDocuments.get(k).getDocInJavaFile())) {
						if (k <= index) {
							index = k;
						}
						break;
					}
				}
			}
			System.out.println(bug.bugID+" "+index);
			fileWriter.write(bug.bugID+","+index+"\n");
			if (index == 0 ) {
				topOne++;
				System.out.println(topOne);
			}else if(index>= 1 && index <= 4){
				topFive++;
				System.out.println(topFive);
			}
			else if(index>=5 && index <=9){
				topTen++;
				System.out.println(topTen);
			}
	    }
		fileWriter.write(nList.getLength()+","+topOne+","+topFive+","+topTen);
		fileWriter.close();
	}
	public void setLsiTerms() {
		List<LsiTerm> lsiTerms = new ArrayList<LsiTerm>();
		try {
			FileInputStream file = new FileInputStream("Terms.ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(file);
			try {
				lsiTerms = (ArrayList<LsiTerm>) objectInputStream.readObject();
				System.out.println(lsiTerms.toString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			objectInputStream.close();
			System.out.println("Terms");
			for (LsiTerm lsiTerm : lsiTerms) {
				System.out.println(lsiTerm.toCSVString());
			}
			System.out.println(lsiTerms.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setLsiDocuments() {
		List<LsiDocument>lsiDocuments = new ArrayList<LsiDocument>();
		try {
			FileInputStream file = new FileInputStream("Documents.ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(file);
			try {
				lsiDocuments = (ArrayList<LsiDocument>) objectInputStream.readObject();
				System.out.println(lsiDocuments.toString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			objectInputStream.close();
			System.out.println("Documents");
			for (LsiDocument lsiDocument : lsiDocuments) {
				System.out.println(lsiDocument.toCSVString());
			}
			System.out.println(lsiDocuments.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}