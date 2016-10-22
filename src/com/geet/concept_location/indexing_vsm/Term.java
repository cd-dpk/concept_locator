package com.geet.concept_location.indexing_vsm;

import java.util.List;

import com.geet.concept_location.corpus_creation.Document;

public class Term {
	
	public String termString;
	public int termFrequency;
	public int documentFrequency = 1;
	public double inverseDocumentFrequency = 0.0; 
	
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
				if (term.isSame(this)) {
					documentFrequency++;
					break;
				}
			}
		}
		inverseDocumentFrequency = 1+Math.log10((double)vectorDocuments.size()/(double)documentFrequency);
	}
	public double getTF_IDF(){
		return (double) termFrequency * inverseDocumentFrequency;
	}
	
	@Override
	public String toString() {
		return "[ "+termString+","+termFrequency+","+inverseDocumentFrequency+"] ";
	}
}
