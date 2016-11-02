package com.geet.concept_location.indexing_lsi;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class Lsi {
	public List<LsiTerm> lsiTerms = new ArrayList<LsiTerm>();
	public List<LsiDocument> lsiDocuments = new ArrayList<LsiDocument>();
	public static final int NUM_FACTORS = 2;
	public double [] scales = new double[NUM_FACTORS];
	public void setLsiTerms() {
		lsiTerms = new ArrayList<LsiTerm>();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setLsiDocuments() {
		lsiDocuments = new ArrayList<LsiDocument>();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public List<LsiDocument> search(LsiQuery lsiQuery){
		setLsiTerms();
		setLsiDocuments();
		List<LsiDocument> resultantLsiDocuments = new ArrayList<LsiDocument>();
		Vector queryVector = getVectorFromLSI(lsiQuery);
		System.out.println(queryVector.toCSVString());
		if (!queryVector.isNull()) {
			for (int j = 0; j < lsiDocuments.size(); ++j) {
				lsiDocuments.get(j).score = queryVector.cosine(lsiDocuments.get(j).vector);
				if (this.lsiDocuments.get(j).score > .50) {
					resultantLsiDocuments.add(lsiDocuments.get(j));
				}
			}
			Collections.sort(resultantLsiDocuments);
			Collections.reverse(resultantLsiDocuments);
			System.out.println("Terms "+ lsiTerms.size() +","+" Documents "+resultantLsiDocuments.size());
		}
		return resultantLsiDocuments;
	}
	public void searchTerm(LsiQuery lsiQuery){
		setLsiDocuments();
		setLsiTerms();
		for (int j = 0; j < lsiTerms.size(); ++j) {
			lsiTerms.get(j).score = getVectorFromLSI(lsiQuery).cosine(lsiTerms.get(j).vector);
			System.out.println(lsiTerms.get(j).score);
		}
		Collections.sort(lsiTerms);
		Collections.reverse(lsiTerms);
	}
	public void printDocumentsVector(){
		for (LsiDocument lsiDocument : lsiDocuments) {
			System.out.println(lsiDocument.vector.toString());
		}
	}
	public void printTermsVector(){
		for (LsiTerm lsiTerm : lsiTerms) {
			System.out.println(lsiTerm.vector.toString());
		}
	}
	public Vector getVectorFromLSI(LsiQuery lsiQuery){
		for (String queryTerm : lsiQuery.getTerms()) {
			for (LsiTerm lsiTerm : lsiTerms) {
				if (lsiTerm.isSame(queryTerm)) {
					lsiQuery.vector.addWithVector(lsiTerm.vector);
					break;
				}
			}
		}
		return lsiQuery.vector;
	}
}
