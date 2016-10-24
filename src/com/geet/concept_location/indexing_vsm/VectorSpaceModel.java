package com.geet.concept_location.indexing_vsm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class VectorSpaceModel {
	
	public List<VectorDocument> documentList = new ArrayList<VectorDocument>();
	
	public VectorSpaceModel(List<VectorDocument> vectorDocuments) {
		// TODO Auto-generated constructor stub
		documentList = vectorDocuments;
	}
	public double [][] getTERM_DOCUMENT_MATRIX(){
		String[] terms= getTERMS();
		String[] documents = getDOCUMENTS();
		double [][] TERM_DOCUMENT_MATRIX = new double[terms.length][documents.length];
		for (int i = 0; i < terms.length; i++) {
			for (int j = 0; j < documents.length; j++) {
				Term term = new Term(terms[i]);
				term.setTermFrequencyFromDocument(documentList.get(j));
				term.setDocumentFrequencyAndInverseDocumentFrequency(documentList);
				TERM_DOCUMENT_MATRIX[i][j]=term.getTF_IDF();
			}
		}
		return TERM_DOCUMENT_MATRIX;
	}
	
	public String[] getTERMS(){
		Set<String> termSet = new HashSet<String>();
		for (VectorDocument document : documentList) {
			for (Term term : document.terms) {
				termSet.add(term.termString);
			}
		}
		return termSet.toArray(new String[termSet.size()]);
	}
	
	public String [] getDOCUMENTS(){
		Set<String> documentSet = new HashSet<String>();
		for (VectorDocument document : documentList) {
				documentSet.add(document.article);
		}
		return documentSet.toArray(new String[documentSet.size()]);
	}
	
	
	public String TERM_DOCUMENT_MATRIX_TO_STRING(){
		String text="";
		String[] terms= getTERMS();
		String[] documents = getDOCUMENTS();
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
