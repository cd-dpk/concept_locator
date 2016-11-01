package com.geet.concept_location.indexing_vsm;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.geet.concept_location.corpus_creation.Document;

public class VectorSpaceModel {
	public List<Document> documents = new ArrayList<Document>();
	public List<String> terms = new ArrayList<String>();
	public double [][]TERM_DOCUMENT_MATRIX;
	public double [] df;
	private int totalTerm=0;
	private int totalDocs=0;
	private double MAX = 2;
	private double a = 1.0;
	private double b = 2.0;
	private double MIN = 0;
	
	public VectorSpaceModel(List<Document> documentList) {
		documents = documentList;
		totalTerm = getTermS().size();
		totalDocs = getDocuments().size();
		terms = getTermS();
		df = new double[totalTerm];
		TERM_DOCUMENT_MATRIX = new double[totalTerm][totalDocs];
		df = new double [totalTerm];
		System.out.println("Terms "+totalTerm+" Documents "+totalDocs);
		System.out.println(terms.toString());
	}

	public void setTERM_DOCUMENT_MATRIX(List<Term> terms,List<Document>documents){
		/* dont calculate idf from terms rather compute it from matrix*/
		System.out.println("Term Document Matrix getting...");
		for (int i = 0; i < totalTerm; i++) {
			// df
			df[i] = 0;
			for (int j = 0; j < documents.size(); j++) {
				double tf = documents.get(j).getTF(terms.get(i).toString());
				if (tf !=0.0) {
						df[i]++;
				}
				TERM_DOCUMENT_MATRIX[i][j]= tf;
			}
			// idf
			if (df[i] == 0) {
				System.exit(i);
			}
			df[i] = 1 + (Math.log10((double)documents.size()/df[i])/Math.log10(2.0));
			for (int j = 0; j < documents.size(); j++) {
				TERM_DOCUMENT_MATRIX[i][j]= TERM_DOCUMENT_MATRIX[i][j] * df[i];
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
	}
	
	
	public List<Document> search(){
		List<Document>lsiDocuments = new ArrayList<Document>();	
		for (int i = 1; i < documents.size(); i++) {
				double dotProduct = 0.0;
				double scalarOne = 0.0;
				double scalarTwo = 0.0;
				for (int j = 0; j < terms.size(); j++) {
					dotProduct += TERM_DOCUMENT_MATRIX[0][j] * TERM_DOCUMENT_MATRIX[0][i];
					scalarOne += TERM_DOCUMENT_MATRIX[0][j] * TERM_DOCUMENT_MATRIX[0][j];
					scalarTwo += TERM_DOCUMENT_MATRIX[0][i] * TERM_DOCUMENT_MATRIX[0][i];
				}
				documents.get(i).score = (dotProduct)/(Math.sqrt(scalarOne)*Math.sqrt(scalarTwo));
				if (documents.get(i).score > 0.75 ) {
					lsiDocuments.add(documents.get(i));
				}
		}
		return lsiDocuments;
	}
	
	private double getNormalizedValue(double value){
		double normalizedValue = (value - MIN)/(MAX-MIN);
		normalizedValue = normalizedValue * (b-a);
		normalizedValue = normalizedValue + a;
		return normalizedValue;
	}
	/*
	 * for vsm
	 */
	public void setTERM_DOCUMENT_MATRIX(){
		/**
		 * get all terms 
		 * for query vector, query[terms.length][2], 
		 * column 1 <- tf in query
		 * column 2 <- df in all documents
		 * 
		 * compute score of all documents with query vector
		 * ( query[i][0] * ( 1+ log 10((1+doucuments.size())/query[i][1])) ) * (TERM_DOCUMENT_MATRIX[j][i]( 1+ log 10((1+doucuments.size())/query[i][1])))
		 */
		
		/* dont calculate idf from terms rather compute it from matrix*/
		for (int i = 0; i < totalTerm; i++) {
			// df
			df[i] = 0;
			for (int j = 0; j < documents.size(); j++) {
				double tf = this.documents.get(j).getTF(terms.get(i).toString());
				if (tf !=0.0) {
						df[i]++;
				}
				TERM_DOCUMENT_MATRIX[i][j]= tf;
			}
		}
	}
	
	
	public double [][] getTERM_DOCUMENT_MATRIX(){
		/* dont calculate idf from terms rather compute it from matrix*/
		double [] df = new double[totalTerm];
		double [][] TERM_DOCUMENT_MATRIX = new double[totalTerm][totalDocs];
		for (int i = 0; i < totalTerm; i++) {
			// df
			df[i] = 0;
			for (int j = 0; j < documents.size(); j++) {
				double tf = this.documents.get(j).getTF(terms.get(i).toString());
				if (tf !=0.0) {
						df[i]++;
				}
				TERM_DOCUMENT_MATRIX[i][j]= tf;
			}
			// idf
			df[i] = 1 + Math.log10((double)documents.size()/df[i]);
			for (int j = 0; j < documents.size(); j++) {
				TERM_DOCUMENT_MATRIX[i][j]= TERM_DOCUMENT_MATRIX[i][j] * df[i];
				System.out.print(TERM_DOCUMENT_MATRIX[i][j]+" ");
			}
			System.out.println();
		}
		return TERM_DOCUMENT_MATRIX;
	}
	
	/**
	 * @deprecated
	 * @param documents
	 * @return
	 */
	public String[] getTermsFromDocuments(List<Document> documents){
		Set<String> termSet = new HashSet<String>();
		for (Document document : documents) {
				Set<String> candidateSet = new HashSet<String>(document.getTermsInString());
				termSet.addAll(candidateSet);
		}
		return termSet.toArray(new String[termSet.size()]);
	}
	public List<String> getTermS(){
		Set<String> termSet = new HashSet<String>();
		for (Document document : documents) {
				Set<String> candidateSet = new HashSet<String>(document.getTermsInString());
				termSet.addAll(candidateSet);
		}
		return new ArrayList<String>(termSet);
	}
	
	
	public String[] getTERMS(){
		Set<String> termSet = new HashSet<String>();
		for (Document document : documents) {
				Set<String> candidateSet = new HashSet<String>(document.getTermsInString());
				termSet.addAll(candidateSet);
		}
		return termSet.toArray(new String[termSet.size()]);
	}
	
	
	public String [] getDOCS(){
		Set<String> documentSet = new HashSet<String>();
		for (Document document : documents) {
				documentSet.add(document.getArticle());
		}
		return documentSet.toArray(new String[documentSet.size()]);
	}
	
	public Document [] getDocumentArray(){
		Set<Document> documentSet = new HashSet<Document>(documents);
		return documentSet.toArray(new Document[documentSet.size()]);
		
	}
	
	public List<Document> getDocuments(){
		return documents;
		
	}
	/**
	 * 
	 * @return
	 */
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
}