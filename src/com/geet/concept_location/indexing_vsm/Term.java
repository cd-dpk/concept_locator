package com.geet.concept_location.indexing_vsm;

import java.util.List;

import com.geet.concept_location.corpus_creation.Document;

public class Term {
	
	public String termString;
	public int termFrequency=0;
	public int documentFrequency = 0;
	public double inverseDocumentFrequency = 0.0; 
	
	public Term(String termString) {
		this.termString = termString;
	}
	public Term(String termString, int termFrequency) {
		super();
		this.termString = termString;
		this.termFrequency = termFrequency;
	} 
	
	
	public Term(String termString, int termFrequency,
			int documentFrequency) {
		super();
		this.termString = termString;
		this.termFrequency = termFrequency;
		this.documentFrequency = documentFrequency;
	}

	public boolean isSame(Term term){
		if (term.termString.equals(termString)) {
			return true;
		} 
		return false;
	}
	
	public void setDocumentFrequencyAndInverseDocumentFrequency(List<VectorDocument>vectorDocuments){
		for (VectorDocument document : vectorDocuments) {
			for (Term term : document.getTerms()) {
				if (isSame(term)) {
					documentFrequency++;
					break;
				}
			}
		}
		inverseDocumentFrequency = 1+Math.log10((double)vectorDocuments.size()/(double)documentFrequency);
		//System.out.println(inverseDocumentFrequency);
	}
	
	public void setTermFrequencyFromDocument(VectorDocument vectorDocument){
		for (Term term : vectorDocument.getTerms()) {
			if (isSame(term)) {
				termFrequency = term.termFrequency;
				return ;
			}
		}
	}
	
	public double getTF_IDF(){
		return  (double) termFrequency * inverseDocumentFrequency;
	}
	
	public double getTF(){
		return  (double) termFrequency;
	}
	
	
	@Override
	public String toString() {
		return "[ "+termString+","+termFrequency+","+inverseDocumentFrequency+"] ";
	}
}
