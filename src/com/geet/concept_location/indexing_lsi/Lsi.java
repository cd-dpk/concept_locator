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
	// lsi bug localization initiated
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
		for (LsiTerm lsiTerm : lsiTerms) {
			System.out.println(lsiTerm.term);
		}
		List<LsiDocument> resultantLsiDocuments = new ArrayList<LsiDocument>();
		lsiQuery.vector = getVectorFromLSI(lsiQuery.getTerms());
		System.out.println(lsiQuery.vector.toCSVString());
		if (!lsiQuery.vector.isNull()) {
			for (int j = 0; j < lsiDocuments.size(); ++j) {
				lsiDocuments.get(j).score = lsiQuery.vector.cosine(lsiDocuments.get(j).vector);
				if (this.lsiDocuments.get(j).score > .75) {
					resultantLsiDocuments.add(lsiDocuments.get(j));
				}
			}
			Collections.sort(resultantLsiDocuments);
			Collections.reverse(resultantLsiDocuments);
			System.out.println("Terms "+ lsiTerms.size() +","+" Documents "+resultantLsiDocuments.size());
		}
		else{
			System.out.println("Terms not present in search space");
		}
		return resultantLsiDocuments;
	}
	public void searchTerm(LsiQuery lsiQuery){
		setLsiDocuments();
		setLsiTerms();
		for (int j = 0; j < lsiTerms.size(); ++j) {
			lsiTerms.get(j).score = getVectorFromLSI(lsiQuery.getTerms()).cosine(lsiTerms.get(j).vector);
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
	public Vector getVectorFromLSI(List<String> terms){
		Vector vector = new Vector(NUM_FACTORS);
		for (String queryTerm : terms) {
			for (LsiTerm lsiTerm : lsiTerms) {
				System.out.print(lsiTerm+" ");
				if (lsiTerm.term.equals(queryTerm)) {
					System.out.println(lsiTerm.vector.toCSVString());
					vector.addWithVector(lsiTerm.vector);
					break;
				}
			}
		}
		return vector;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
