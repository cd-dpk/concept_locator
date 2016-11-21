package com.geet.concept_location.indexing_lsi;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.geet.concept_location.corpus_creation.SimpleDocument;
import com.geet.concept_location.indexing_vsm.Term;
public class Lsi {
	public List<Term> spaceTerms = new ArrayList<Term>();
	public List<SimpleDocument> simpleDocuments = new ArrayList<SimpleDocument>();
	public static final int NUM_FACTORS = 2;
	public final static double MINIMUM_SCORE = 0.2;
	public double [] scales = new double[NUM_FACTORS];
	// Lsi bug localization initiated
	
	public void setLsiScales() {
		try {
			FileInputStream file = new FileInputStream("Scales.ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(file);
			try {
				ScaleMatrix scaleMatrix = (ScaleMatrix) objectInputStream.readObject();
				scales = scaleMatrix.getScales();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			objectInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setLsiTerms() {
		spaceTerms = new ArrayList<Term>();
		try {
			FileInputStream file = new FileInputStream("Terms.ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(file);
			try {
				spaceTerms = (ArrayList<Term>) objectInputStream.readObject();
				} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			objectInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setLsiDocuments() {
		simpleDocuments = new ArrayList<SimpleDocument>();
		try {
			FileInputStream file = new FileInputStream("Documents.ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(file);
			try {
				simpleDocuments = (ArrayList<SimpleDocument>) objectInputStream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			objectInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public List<SimpleDocument> search(LsiQuery lsiQuery){
//		setLsiScales();
		setLsiTerms();
		setLsiDocuments();
		List<SimpleDocument> resultantLsiDocuments = new ArrayList<SimpleDocument>();
		lsiQuery.vector = getVectorFromLSI(lsiQuery.getTerms());
		if (!lsiQuery.vector.isNull()) {
			for (int j = 0; j < simpleDocuments.size(); ++j) {
				simpleDocuments.get(j).score = lsiQuery.vector.cosine(simpleDocuments.get(j).vector);
			//	if (this.lsiDocuments.get(j).score > MINIMUM_SCORE) {
					resultantLsiDocuments.add(simpleDocuments.get(j));
			//	}
			}
			Collections.sort(resultantLsiDocuments);
			Collections.reverse(resultantLsiDocuments);
//			System.out.println("Terms "+ lsiTerms.size() +","+" Documents "+resultantLsiDocuments.size());
		}
		else{
			System.out.println("Terms not present in search space");
		}
		return resultantLsiDocuments;
	}
	public List<Term> searchTerm(LsiQuery lsiQuery){
		setLsiDocuments();
		setLsiTerms();
		List<Term> resultantTerms = new ArrayList<Term>();
		lsiQuery.vector = getVectorFromLSI(lsiQuery.getTerms());
		System.out.println(lsiQuery.vector.isNull());
		for (int j = 0; j < spaceTerms.size(); ++j) {
			spaceTerms.get(j).score = lsiQuery.vector.cosine(spaceTerms.get(j).vector);
			resultantTerms.add(spaceTerms.get(j));
		}
		Collections.sort(resultantTerms);
		Collections.reverse(resultantTerms);
		return resultantTerms;
	}
	public void printDocumentsVector(){
		for (SimpleDocument lsiDocument : simpleDocuments) {
			System.out.println(lsiDocument.vector.toString());
		}
	}
	public void printTermsVector(){
		for (Term lsiTerm : spaceTerms) {
			System.out.println(lsiTerm.vector.toString());
		}
	}
	public Vector getVectorFromLSI(List<Term> terms){
		Vector vector = new Vector(NUM_FACTORS);
		for (Term term : terms) {
			for (Term lsiTerm : spaceTerms) {
//				System.out.print(lsiTerm+" ");
				if (lsiTerm.isSameInIR(term)) {
//					System.out.println(lsiTerm.vector.toCSVString());
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