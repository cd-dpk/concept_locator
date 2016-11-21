package com.geet.concept_location.indexing_vsm;
import java.util.List;
import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.Stemmer;
import com.geet.concept_location.utils.StringUtils;
public class Term {
	
	private String termString="";
	private int termFrequency=0;
	private int documentFrequency = 0;
	private double inverseDocumentFrequency = 0.0; 
	
	public String getTermString() {
		return termString;
	}
	public void setTermString(String termString) {
		this.termString = termString;
	}
	public int getTermFrequency() {
		return termFrequency;
	}
	public void setTermFrequency(int termFrequency) {
		this.termFrequency = termFrequency;
	}
	public int getDocumentFrequency() {
		return documentFrequency;
	}
	public void setDocumentFrequency(int documentFrequency) {
		this.documentFrequency = documentFrequency;
	}
	public double getInverseDocumentFrequency() {
		return inverseDocumentFrequency;
	}
	public void setInverseDocumentFrequency(double inverseDocumentFrequency) {
		this.inverseDocumentFrequency = inverseDocumentFrequency;
	}
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
	public void setDocumentFrequencyAndInverseDocumentFrequency(List<Document>documents){
		for (Document document : documents) {
			for (Term term : document.getTerms()) {
				if (isSame(term)) {
					documentFrequency++;
					break;
				}
			}
		}
		inverseDocumentFrequency = 1+Math.log10((double)documents.size()/(double)documentFrequency);
		//System.out.println(inverseDocumentFrequency);
	}
	public void setTermFrequencyFromDocument(Document document){
		for (Term term : document.getTerms()) {
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
	public boolean isSameInIR(Term term){
		if (termString.equals(term.termString)) {
			return true;
		}
		Stemmer stemmer = new Stemmer(term.termString);
		if (termString.equals(stemmer.toString())) {
			return true;
		}
		stemmer = new Stemmer(termString);
		if (term.termString.equals(stemmer.toString())) {
			return true;
		}
		if (termString.equals(StringUtils.uderscoreDeletedString(term.termString))) {
			return true;
		}
		return false;
	}
}
