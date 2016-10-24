package com.geet.concept_location.indexing_vsm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.geet.concept_location.corpus_creation.Document;

public class VectorSpaceModel {
	
	public List<VectorDocument> vectorDocuments = new ArrayList<VectorDocument>();
	
	public VectorSpaceModel(List<Document> documents) {
		String[] terms = getTermsFromDocuments(documents);
		List<Term> uniqueTerms = new ArrayList<Term>();
		for (int i = 0; i < terms.length; i++) {
			uniqueTerms.add(new Term(terms[i]));
		}
		for (Document document : documents) {
			vectorDocuments.add(new VectorDocument(document.getDocInJavaFile(), document.getDocName(), document.getStartPosition(), document.getEndPosition(), document.getTerms(), document.getArticle()));
		}

		for (VectorDocument vectorDocument : vectorDocuments) {
			for (Term term : vectorDocument.getTerms()) {
				term.setDocumentFrequencyAndInverseDocumentFrequency(vectorDocuments);
			}
		}
	}
	
	public void setTERM_DOCUMENT_MATRIX(){
		String[] terms= getTERMS();
		String[] documents = getDOCS();
		for (int i = 0; i < terms.length; i++) {
			for (int j = 0; j < documents.length; j++) {
				vectorDocuments.get(j).getTerms().get(i).setTermFrequencyFromDocument(vectorDocuments.get(j));
			}
		}
		for (int i = 0; i < terms.length; i++) {
			for (int j = 0; j < documents.length; j++) {
				vectorDocuments.get(j).getTerms().get(i).setDocumentFrequencyAndInverseDocumentFrequency(vectorDocuments);
			}
		}
		return ;
	}

	public double [][] getTERM_DOCUMENT_MATRIX(){
		String[] terms= getTERMS();
		String[] documents = getDOCS();
		double [][] TERM_DOCUMENT_MATRIX = new double[terms.length][documents.length];
		for (int i = 0; i < terms.length; i++) {
			for (int j = 0; j < documents.length; j++) {
				vectorDocuments.get(j).getTerms().get(i).setTermFrequencyFromDocument(vectorDocuments.get(j));
				vectorDocuments.get(j).getTerms().get(i).setDocumentFrequencyAndInverseDocumentFrequency(vectorDocuments);
				TERM_DOCUMENT_MATRIX[i][j]=vectorDocuments.get(j).getTerms().get(i).getTF_IDF();
			}
		}
		return TERM_DOCUMENT_MATRIX;
	}
	
	public String[] getTermsFromDocuments(List<Document> documents){
		Set<String> termSet = new HashSet<String>();
		for (Document document : documents) {
			for (Term term : document.getTerms()) {
				termSet.add(term.termString);
			}
		}
		return termSet.toArray(new String[termSet.size()]);
	}

	public String[] getTERMS(){
		Set<String> termSet = new HashSet<String>();
		for (VectorDocument document : vectorDocuments) {
			for (Term term : document.getTerms()) {
				termSet.add(term.termString);
			}
		}
		return termSet.toArray(new String[termSet.size()]);
	}

	public String [] getDOCS(){
		Set<String> documentSet = new HashSet<String>();
		for (VectorDocument document : vectorDocuments) {
				documentSet.add(document.article);
		}
		return documentSet.toArray(new String[documentSet.size()]);
	}
	
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
