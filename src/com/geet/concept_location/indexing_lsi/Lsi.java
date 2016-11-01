package com.geet.concept_location.indexing_lsi;
<<<<<<< HEAD
import java.io.FileInputStream;
import java.io.FileOutputStream;
=======
import java.io.File;
import java.io.FileWriter;
>>>>>>> origin/full
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aliasi.matrix.SvdMatrix;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;
public class Lsi {
	public List<LsiTerm> lsiTerms = new ArrayList<LsiTerm>();
	public List<LsiDocument> lsiDocuments = new ArrayList<LsiDocument>();
	public static final int NUM_FACTORS = 2;
<<<<<<< HEAD
	public double [] scales = new double[NUM_FACTORS];
	
	public Lsi(){
		
	}
	/**
	 * @deprecated
	 * @param vectorSpaceModel
	 */
=======
	double [] scales = new double[NUM_FACTORS];
	
>>>>>>> origin/full
	public Lsi(VectorSpaceModel vectorSpaceModel){
		double featureInit = 0.01;
		double initialLearningRate = 0.005;
		int annealingRate = 1000;
		double regularization = 0.00;
		double minImprovement = 0.0000;
		int minEpochs = 10;
		// simple comment
		int maxEpochs = 50000;
		System.out.println("  Computing SVD");
		System.out.println("    maxFactors=" + NUM_FACTORS);
		System.out.println("    featureInit=" + featureInit);
		System.out.println("    initialLearningRate=" + initialLearningRate);
		System.out.println("    annealingRate=" + annealingRate);
		System.out.println("    regularization" + regularization);
		System.out.println("    minImprovement=" + minImprovement);
		System.out.println("    minEpochs=" + minEpochs);
		System.out.println("    maxEpochs=" + maxEpochs);
		SvdMatrix matrix = SvdMatrix.svd(vectorSpaceModel.getTERM_DOCUMENT_MATRIX(), NUM_FACTORS,
				featureInit, initialLearningRate, annealingRate,
				regularization, null, minImprovement, minEpochs, maxEpochs);
		scales = matrix.singularValues();
		double[][] termVectors = matrix.leftSingularVectors();
		double[][] docVectors = matrix.rightSingularVectors();
		
		System.out.println("Terms...");
		/* term vectors into lsi terms*/
		try {
			FileOutputStream file = new FileOutputStream("Terms.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
			List<LsiTerm> lsiTerms = new ArrayList<LsiTerm>();
			for (int i = 0; i < termVectors.length; i++) {
				Vector vector = new Vector(NUM_FACTORS);
<<<<<<< HEAD
				vector.dimensionValue[0] = docVectors[i][0];
				vector.dimensionValue[1] = docVectors[i][1];
				LsiTerm lsiTerm = new LsiTerm(vectorSpaceModel.terms.get(i),vector);
=======
				vector.dimensionValue[0] = termVectors[i][0];
				vector.dimensionValue[1] = termVectors[i][1];
				System.out.println(vector.toCSVString());
				LsiTerm lsiTerm = new LsiTerm(terms.get(i), vector); 
>>>>>>> origin/full
				System.out.println(lsiTerm.toCSVString());
				lsiTerms.add(lsiTerm);
			}
			objectOutputStream.writeObject(lsiTerms);
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		System.out.println("DOCS...");
		/* document vectors into lsi docs*/
		try {
<<<<<<< HEAD
			FileOutputStream file = new FileOutputStream("Documents.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
			List<LsiDocument> lsiDocuments = new ArrayList<LsiDocument>();
=======
			FileWriter fileWriter = new FileWriter(new File("Documents.csv"));
//			FileOutputStream file = new FileOutputStream("Documents.ser");
//			ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
>>>>>>> origin/full
			for (int i = 0; i < docVectors.length; i++) {
				System.out.println("Writing Documents");
				Vector vector = new Vector(NUM_FACTORS);
				vector.dimensionValue[0] = docVectors[i][0];
				vector.dimensionValue[1] = docVectors[i][1];
				System.out.println(vector.toCSVString());
				LsiDocument lsiDocument = new LsiDocument(vectorSpaceModel.documents.get(i),vector);
				System.out.println(lsiDocument.toCSVString());
<<<<<<< HEAD
				lsiDocuments.add(lsiDocument);
			}
			objectOutputStream.writeObject(lsiDocuments);
			objectOutputStream.close();
=======
				fileWriter.write(lsiDocument.toCSVString()+"\n");
			//	objectOutputStream.writeObject(lsiDocument);
//				objectOutputStream.reset();
				lsiDocuments.add(lsiDocument);
			}
//			objectOutputStream.writeObject(lsiDocuments);
//			objectOutputStream.close();
			fileWriter.close();
>>>>>>> origin/full
		} catch (IOException e) {
			e.printStackTrace();
		}
<<<<<<< HEAD
		
	}

	public void setLsiTerms() {
		lsiTerms = new ArrayList<LsiTerm>();
		try {
			FileInputStream file = new FileInputStream("Terms.ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(file);
			try {
				lsiTerms = (ArrayList<LsiTerm>) objectInputStream.readObject();
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
=======
		System.out.println("Loading...");
		/*// temp 
		 document vectors reading in serializable into lsi docs
		try {
//			FileWriter fileWriter = new FileWriter(new File("Documents.csv"));
>>>>>>> origin/full
			FileInputStream file = new FileInputStream("Documents.ser");
			ObjectInputStream objectInputStream = new ObjectInputStream(file);
			try {
				lsiDocuments = (ArrayList<LsiDocument>) objectInputStream.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			objectInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
<<<<<<< HEAD
	}
=======
		// temp
*/	}
>>>>>>> origin/full
	
	public void search(LsiQuery lsiQuery){
		setLsiTerms();
		setLsiDocuments();
		for (int j = 0; j < lsiDocuments.size(); ++j) {
			lsiDocuments.get(j).score = lsiQuery.getVectorFromLSI(lsiTerms, scales).cosine(lsiDocuments.get(j).vector, scales);
		}
		Collections.sort(lsiDocuments);
		Collections.reverse(lsiDocuments);
	}
	public void searchTerm(LsiQuery lsiQuery){
		setLsiDocuments();
		setLsiTerms();
		for (int j = 0; j < lsiTerms.size(); ++j) {
			lsiTerms.get(j).score = lsiQuery.getVectorFromLSI(lsiTerms, scales).cosine(lsiTerms.get(j).vector, scales);
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
}
