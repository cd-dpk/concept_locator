package com.geet.concept_location.indexing_vsm;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.aliasi.matrix.SvdMatrix;
import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.SimpleDocument;
import com.geet.concept_location.indexing_lsi.Lsi;
import com.geet.concept_location.indexing_lsi.LsiDocument;
import com.geet.concept_location.indexing_lsi.LsiTerm;
import com.geet.concept_location.indexing_lsi.ScaleMatrix;
import com.geet.concept_location.indexing_lsi.Vector;
public class VectorSpaceModel {
	public List<SimpleDocument> documents = new ArrayList<SimpleDocument>();
	public List<String> terms = new ArrayList<String>();
	private int totalTerm=0;
	private int totalDocs=0;
	private double MAX = 2;
	private double a = 1.0;
	private double b = 2.0;
	private double MIN = 0;
	public VectorSpaceModel() {}
	@Deprecated
	public VectorSpaceModel(List<SimpleDocument> documentList) {
		documents = documentList;
		totalDocs = getDocuments().size();
		terms = getTermS();
		totalTerm = getTermS().size();
		System.out.println("Terms "+totalTerm+" Documents "+totalDocs);
		System.out.println(terms.toString());
	}
	@Deprecated
	public double [][] getTERM_DOCUMENT_MATRIX(){
		/* dont calculate idf from terms rather compute it from matrix*/
		double [] idf = new double[totalTerm];
		double [][] TERM_DOCUMENT_MATRIX = new double[totalTerm][totalDocs];
		System.out.println("Term Document Matrix getting...");
		for (int i = 0; i < totalTerm; i++) {
			// df
			idf[i] = 0;
			for (int j = 0; j < documents.size(); j++) {
				double tf = this.documents.get(j).getTF(terms.get(i).toString());
				if (tf !=0.0) {
						idf[i]++;
				}
				TERM_DOCUMENT_MATRIX[i][j]= tf;
			}
			// idf
			if (idf[i] == 0) {
				System.exit(i);
			}
			idf[i] = 1 + (Math.log10((double)documents.size()/idf[i])/Math.log10(2.0));
			for (int j = 0; j < documents.size(); j++) {
				TERM_DOCUMENT_MATRIX[i][j]= TERM_DOCUMENT_MATRIX[i][j] * idf[i];
				double value = TERM_DOCUMENT_MATRIX[i][j];
				if (value > MAX) {
					MAX = value;
				}else if(value < MIN){
					MIN = value;
				}
			}
		}
		System.out.println("Normalizing...");
		for (int i = 0; i < TERM_DOCUMENT_MATRIX.length; i++) {
			for (int j = 0; j < TERM_DOCUMENT_MATRIX[i].length; j++) {
				TERM_DOCUMENT_MATRIX[i][j] = getNormalizedValue(TERM_DOCUMENT_MATRIX[i][j]);
				System.out.print(TERM_DOCUMENT_MATRIX[i][j]+" ");
			}
			System.out.println();
		}
		return TERM_DOCUMENT_MATRIX;
	}
	
	public double [][] getTERM_DOCUMENT_MATRIX(VectorSpaceMatrix vectorSpaceMatrix){
		System.out.println("Multiplying...");
		for (int i = 0; i < vectorSpaceMatrix.TERM_DOCUMENT_MATRIX.length; i++) {
			for (int j = 0; j < vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i].length; j++) {
				vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j] = vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j] * (1 + (Math.log10((double)vectorSpaceMatrix.documents.size()/vectorSpaceMatrix.df[i])/Math.log10(2.0)));
				if (vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j] > MAX) {
					MAX = vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j];
				}else if(vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j] < MIN){
					MIN = vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j];
				}
				System.out.print(vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j]+" ");
			}
			System.out.println(i);
		}
		System.out.println("Normalizing...");
		for (int i = 0; i < vectorSpaceMatrix.TERM_DOCUMENT_MATRIX.length; i++) {
			for (int j = 0; j < vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i].length; j++) {
				double value = vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j] * (1 + (Math.log10((double)vectorSpaceMatrix.documents.size()/vectorSpaceMatrix.df[i])/Math.log10(2.0)));
				vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j] = getNormalizedValue(value);
				System.out.print(vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j]+" ");
			}
			System.out.println(i);
		}
		System.out.println("SVD generating...");
		return vectorSpaceMatrix.TERM_DOCUMENT_MATRIX;
	}

	@Deprecated
	public String[] getTermsFromDocuments(List<Document> documents){
		Set<String> termSet = new HashSet<String>();
		for (Document document : documents) {
				Set<String> candidateSet = new HashSet<String>(document.getTermsInString());
				termSet.addAll(candidateSet);
		}
		return termSet.toArray(new String[termSet.size()]);
	}
	@Deprecated
	public List<String> getTermS(){
		Set<String> termSet = new HashSet<String>();
		for (SimpleDocument document : documents) {
				Set<String> candidateSet = new HashSet<String>(document.getTermsInString());
				termSet.addAll(candidateSet);
		}
		return new ArrayList<String>(termSet);
	}
	public String[] getTERMS(){
		Set<String> termSet = new HashSet<String>();
		for (SimpleDocument document : documents) {
				Set<String> candidateSet = new HashSet<String>(document.getTermsInString());
				termSet.addAll(candidateSet);
		}
		return termSet.toArray(new String[termSet.size()]);
	}
	public String [] getDOCS(){
		Set<String> documentSet = new HashSet<String>();
		for (SimpleDocument document : documents) {
				documentSet.add(document.getArticle());
		}
		return documentSet.toArray(new String[documentSet.size()]);
	}
	public SimpleDocument [] getDocumentArray(){
		Set<SimpleDocument> documentSet = new HashSet<SimpleDocument>(documents);
		return documentSet.toArray(new Document[documentSet.size()]);
	}
	@Deprecated
	public List<SimpleDocument> getDocuments(){
		return documents;
	}
	
	@Deprecated
	public String TERM_DOCUMENT_MATRIX_TO_STRING(){
		String text="";
		String[] terms= getTERMS();
		String[] documents = getDOCS();
		double [][] TERM_DOCUMENT_MATRIX = getTERM_DOCUMENT_MATRIX();
		text += terms.length+"X"+documents.length+"\n";
		for (int i = 0; i < terms.length; i++) {
			text += terms[i]+" ";
			for (int j = 0; j < documents.length; j++) {
				text += TERM_DOCUMENT_MATRIX[i][j]+",";
			}
			text += "\n";
		}
		return text;
	}
	
	public void  generateLsi(VectorSpaceMatrix vectorSpaceMatrix){
		double featureInit = 0.01;
		double initialLearningRate = 0.005;
		int annealingRate = 1000;
		double regularization = 0.00;
		double minImprovement = 0.0000;
		int minEpochs = 10;
		// simple comment
		int maxEpochs = 50000;
		System.out.println("  Computing SVD");
		System.out.println("    maxFactors=" + Lsi.NUM_FACTORS);
		System.out.println("    featureInit=" + featureInit);
		System.out.println("    initialLearningRate=" + initialLearningRate);
		System.out.println("    annealingRate=" + annealingRate);
		System.out.println("    regularization" + regularization);
		System.out.println("    minImprovement=" + minImprovement);
		System.out.println("    minEpochs=" + minEpochs);
		System.out.println("    maxEpochs=" + maxEpochs);
		
		SvdMatrix matrix = SvdMatrix.svd(getTERM_DOCUMENT_MATRIX(vectorSpaceMatrix), Lsi.NUM_FACTORS,
				featureInit, initialLearningRate, annealingRate,
				regularization, null, minImprovement, minEpochs, maxEpochs);
		ScaleMatrix scaleMatrix = new ScaleMatrix(matrix.singularValues());
		double[][] termVectors = matrix.leftSingularVectors();
		double[][] docVectors = matrix.rightSingularVectors();
	
		terms = vectorSpaceMatrix.terms;
		System.out.println("Terms...");
		/* term vectors into lsi terms*/
		try {
			FileOutputStream file = new FileOutputStream("Terms.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
			List<LsiTerm> lsiTerms = new ArrayList<LsiTerm>();
			for (int i = 0; i < termVectors.length; i++) {
				Vector vector = new Vector(Lsi.NUM_FACTORS);
				vector.dimensionValue[0] = termVectors[i][0] * scaleMatrix.getScales()[0];
				vector.dimensionValue[1] = termVectors[i][1] * scaleMatrix.getScales()[1];
				LsiTerm lsiTerm = new LsiTerm(terms.get(i),vector);
				System.out.println(lsiTerm.toCSVString());
				lsiTerms.add(lsiTerm);
			}
			objectOutputStream.writeObject(lsiTerms);
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		documents = new ArrayList<SimpleDocument>();
		for (int i = 0; i < vectorSpaceMatrix.documents.size(); i++) {
			documents.add(new SimpleDocument(vectorSpaceMatrix.documents.get(i), ""));
		}
		System.out.println("DOCS...");
		/* document vectors into lsi docs*/
		try {
			FileOutputStream file = new FileOutputStream("Documents.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
			List<LsiDocument> lsiDocuments = new ArrayList<LsiDocument>();
			for (int i = 0; i < docVectors.length; i++) {
				Vector vector = new Vector(Lsi.NUM_FACTORS);
				vector.dimensionValue[0] = docVectors[i][0]* scaleMatrix.getScales()[0];
				vector.dimensionValue[1] = docVectors[i][1]* scaleMatrix.getScales()[1];
				LsiDocument lsiDocument = new LsiDocument(documents.get(i),vector);
				System.out.println(lsiDocument.toCSVString());
				lsiDocuments.add(lsiDocument);
			}
			objectOutputStream.writeObject(lsiDocuments);
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Deprecated
	public void  generateLsi(){
		double featureInit = 0.01;
		double initialLearningRate = 0.005;
		int annealingRate = 1000;
		double regularization = 0.00;
		double minImprovement = 0.0000;
		int minEpochs = 10;
		// simple comment
		int maxEpochs = 50000;
		System.out.println("  Computing SVD");
		System.out.println("    maxFactors=" + Lsi.NUM_FACTORS);
		System.out.println("    featureInit=" + featureInit);
		System.out.println("    initialLearningRate=" + initialLearningRate);
		System.out.println("    annealingRate=" + annealingRate);
		System.out.println("    regularization" + regularization);
		System.out.println("    minImprovement=" + minImprovement);
		System.out.println("    minEpochs=" + minEpochs);
		System.out.println("    maxEpochs=" + maxEpochs);
		SvdMatrix matrix = SvdMatrix.svd(getTERM_DOCUMENT_MATRIX(), Lsi.NUM_FACTORS,
				featureInit, initialLearningRate, annealingRate,
				regularization, null, minImprovement, minEpochs, maxEpochs);
		ScaleMatrix scaleMatrix = new ScaleMatrix(matrix.singularValues());
		double[][] termVectors = matrix.leftSingularVectors();
		double[][] docVectors = matrix.rightSingularVectors();
	
		
		System.out.println("Terms...");
		/* term vectors into lsi terms*/
		try {
			FileOutputStream file = new FileOutputStream("Terms.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
			List<LsiTerm> lsiTerms = new ArrayList<LsiTerm>();
			for (int i = 0; i < termVectors.length; i++) {
				Vector vector = new Vector(Lsi.NUM_FACTORS);
				vector.dimensionValue[0] = termVectors[i][0] * scaleMatrix.getScales()[0];
				vector.dimensionValue[1] = termVectors[i][1] * scaleMatrix.getScales()[1];
				LsiTerm lsiTerm = new LsiTerm(terms.get(i),vector);
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
			FileOutputStream file = new FileOutputStream("Documents.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
			List<LsiDocument> lsiDocuments = new ArrayList<LsiDocument>();
			for (int i = 0; i < docVectors.length; i++) {
				Vector vector = new Vector(Lsi.NUM_FACTORS);
				vector.dimensionValue[0] = docVectors[i][0]* scaleMatrix.getScales()[0];
				vector.dimensionValue[1] = docVectors[i][1]* scaleMatrix.getScales()[1];
				LsiDocument lsiDocument = new LsiDocument(documents.get(i),vector);
				System.out.println(lsiDocument.toCSVString());
				lsiDocuments.add(lsiDocument);
			}
			objectOutputStream.writeObject(lsiDocuments);
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private double getNormalizedValue(double value){
		double normalizedValue = (value - MIN)/(MAX-MIN);
		normalizedValue = normalizedValue * (b-a);
		normalizedValue = normalizedValue + a;
		return normalizedValue;
	}
}
