package com.geet.concept_location.corpus_creation;

import java.util.ArrayList;
import java.util.List;

public class TermExtractorFromDocument {

	
	public List<String> getTermsFromDocument(Document document){
		List<String> terms = new ArrayList<String>();
		terms.addAll(getTermsFromJavaDocComment());
		terms.addAll(getTermsFromComment());
		terms.addAll(getTermsFromImplementation());
		terms = getTermsAfterStemming(terms);
		return terms;
	}
	
	private List<String> getTermsFromJavaDocComment(){
		/*
		 * remove *, @tag, <tag>, </tag>
		 */
		List<String> terms = new ArrayList<String>();
		String documentString = "";
		return terms;
	}
	
	private List<String> getTermsFromComment(){
		List<String> terms = new ArrayList<String>();
		return terms;
	}
	
	private List<String> getTermsFromImplementation(){
		List<String> terms = new ArrayList<String>();
		return terms;
	}
	
	
	// Identifier Separation
	private List<String> getTermsAfterIdentifierSeparation(List<String> terms){
		return terms;
	}
	
	// Terms Stemming
	private List<String> getTermsAfterStemming(List<String>terms){
		return terms;
	}
}
