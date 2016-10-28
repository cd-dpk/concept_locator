package com.geet.concept_location.indexing_lsi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.aliasi.matrix.SvdMatrix;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;
public class Lsi {
	public List<LsiTerm> lsiTerms = new ArrayList<LsiTerm>();
	public List<LsiDocument> lsiDocuments = new ArrayList<LsiDocument>();
	public static final int NUM_FACTORS = 2;
	double [] scales = new double[NUM_FACTORS];
	public Lsi(VectorSpaceModel vectorSpaceModel){
		double featureInit = 0.01;
		double initialLearningRate = 0.005;
		int annealingRate = 1000;
		double regularization = 0.00;
		double minImprovement = 0.0000;
		int minEpochs = 10;
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
		List<String> terms;
		terms = vectorSpaceModel.terms;
		System.out.println("Terms...");
		/* term vectors into lsi terms*/
		for (int i = 0; i < termVectors.length; i++) {
				Vector vector = new Vector(NUM_FACTORS);
				vector.dimensionValue[0] = termVectors[i][0];
				vector.dimensionValue[1] = termVectors[i][1];
				LsiTerm lsiTerm = new LsiTerm(terms.get(i), vector); 
				System.out.println(lsiTerm.toCSVString());
				lsiTerms.add(lsiTerm);
		}
		System.out.println("DOCS...");
		/* document vectors into lsi docs*/
		for (int i = 0; i < docVectors.length; i++) {
				Vector vector = new Vector(NUM_FACTORS);
				vector.dimensionValue[0] = docVectors[i][0];
				vector.dimensionValue[1] = docVectors[i][1];
				LsiDocument lsiDocument = new LsiDocument(vectorSpaceModel.documents.get(i),vector);
				System.out.println(lsiDocument.toCSVString());
				lsiDocuments.add(lsiDocument);
		}
		System.out.println("Loading...");
	}
	public void search(LsiQuery lsiQuery){
		for (int j = 0; j < lsiDocuments.size(); ++j) {
			lsiDocuments.get(j).score = lsiQuery.getVectorFromLSI(lsiTerms, scales).getDotProductWith(lsiDocuments.get(j).vector, scales);
		}
		Collections.sort(lsiDocuments);
		Collections.reverse(lsiDocuments);
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
