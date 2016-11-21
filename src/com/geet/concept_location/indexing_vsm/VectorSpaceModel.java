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
import com.geet.concept_location.indexing_lsi.ScaleMatrix;
import com.geet.concept_location.indexing_lsi.Vector;
public class VectorSpaceModel {
	public List<SimpleDocument> documents = new ArrayList<SimpleDocument>();
	public List<String> terms = new ArrayList<String>();
	public double [][]TERM_DOCUMENT_MATRIX;
	public double [] df;
	private int totalTerm=0;
	private int totalDocs=0;
	private double MAX = 2;
	private double a = 1.0;
	private double b = 2.0;
	private double MIN = 0;
	public VectorSpaceModel() {}
	
	public VectorSpaceModel(List<SimpleDocument> documentList) {
		documents = documentList;
		terms = getTermS();
		totalTerm = terms.size();
		totalDocs = documents.size();
		df = new double[totalTerm];
		TERM_DOCUMENT_MATRIX = new double[totalTerm][totalDocs];
		df = new double [totalTerm];
		System.out.println("Terms "+totalTerm+" Documents "+totalDocs);
		setTERM_DOCUMENT_MATRIX(terms, documents);
	}
	public void setTERM_DOCUMENT_MATRIX(List<String> terms,List<SimpleDocument>documents){
		/* dont calculate idf from terms rather compute it from matrix*/
		System.out.println("Term Document Matrix getting...");
		for (int i = 0; i < totalTerm; i++) {
			// df
			df[i] = 0;
			System.out.print(i+":");
			for (int j = 0; j < totalDocs; j++) {
				double tf = documents.get(j).getTF(terms.get(i).toString());
				if (tf !=0.0) {
						df[i]++;
				}
				TERM_DOCUMENT_MATRIX[i][j]= tf;
				System.out.print(TERM_DOCUMENT_MATRIX[i][j]+" ");
			}
			// idf
			if (df[i] == 0) {
				System.err.println("Something is going wrong");
				System.exit(i);
			}
			System.out.println();
		}
	}
	public VectorSpaceModel(VectorSpaceMatrix vectorSpaceMatrix){
		for (int i = 0; i < vectorSpaceMatrix.simpleDocuments.size(); i++) {
			documents.add(new SimpleDocument(vectorSpaceMatrix.simpleDocuments.get(i).docInJavaFile,vectorSpaceMatrix.simpleDocuments.get(i).docName,vectorSpaceMatrix.simpleDocuments.get(i).getStartPosition(),vectorSpaceMatrix.simpleDocuments.get(i).getEndPosition(),vectorSpaceMatrix.simpleDocuments.get(i).score));
		}
		terms = vectorSpaceMatrix.terms;
		TERM_DOCUMENT_MATRIX = vectorSpaceMatrix.TERM_DOCUMENT_MATRIX;
		df = vectorSpaceMatrix.df;
	}

	public VectorSpaceMatrix getVectorSpaceMatrix(){
		List<SimpleDocument> simpleDocuments = new ArrayList<SimpleDocument>();
		for (int i = 0; i < documents.size(); i++) {
			simpleDocuments.add(new SimpleDocument(documents.get(i).docInJavaFile, documents.get(i).docName, documents.get(i).startPosition, documents.get(i).endPosition, 0.0));
		}
		return new VectorSpaceMatrix(simpleDocuments,terms,TERM_DOCUMENT_MATRIX, df);
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
				vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j] = vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j] * (1 + (Math.log10((double)vectorSpaceMatrix.simpleDocuments.size()/vectorSpaceMatrix.df[i])/Math.log10(2.0)));
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
				double value = vectorSpaceMatrix.TERM_DOCUMENT_MATRIX[i][j] * (1 + (Math.log10((double)vectorSpaceMatrix.simpleDocuments.size()/vectorSpaceMatrix.df[i])/Math.log10(2.0)));
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
			List<Term> termObjects = new ArrayList<Term>(); 
			for (int i = 0; i < termVectors.length; i++) {
				Vector vector = new Vector(Lsi.NUM_FACTORS);
				for (int j = 0; j < vector.dimensionValue.length; j++) {
					vector.dimensionValue[j] = termVectors[i][j] * scaleMatrix.getScales()[j];
				}
				Term termObject = new Term(terms.get(i));
				termObject.vector = vector;
				termObjects.add(termObject);
			}
			objectOutputStream.writeObject(termObjects);
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		documents = vectorSpaceMatrix.simpleDocuments;
		System.out.println("DOCS...");
		/* document vectors into lsi docs*/
		try {
			FileOutputStream file = new FileOutputStream("Documents.ser");
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(file);
			for (int i = 0; i < docVectors.length; i++) {
				Vector vector = new Vector(Lsi.NUM_FACTORS);
				for (int j = 0; j < vector.dimensionValue.length; j++) {
					vector.dimensionValue[j] = termVectors[i][j] * scaleMatrix.getScales()[j];
				}
				documents.get(i).vector = vector;
			}
			objectOutputStream.writeObject(documents);
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