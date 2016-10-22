package com.geet.concept_location.indexing_lsi;

import java.util.ArrayList;
import java.util.List;

public class VectorDocument{
	
	public List<Term> terms = new ArrayList<Term>();
	public List<Term> getTerms() {
		return terms;
	}

	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String identity;
	
	/**
	 * 
	 * @param vectorDocument
	 * @return sim similarity between two vector document
	 */
	public double getSimilarityWith(VectorDocument vectorDocument){
		double sim = 0.0;
		//TODO  Cosine similarity
		/*
		 *  sim (d1,d2) =( V(d1) · V(d2) ) / ( |V(d1)| · |V(d2)| )
		 *  sim (d1,d2) = v(d1) · v (d2)
		 *  v(d1) = V(d1)/|V(d1)|
		 *  v(d2) = V(d2)/|V(d2)|
		 */
		sim = getDotProduct(vectorDocument);
		sim = sim / (getScalarValue() * vectorDocument.getScalarValue());
		return sim;
	}
	
	/**
	 * 
	 * @return scalarValue returns the scalar value of vector
	 */
	public double getScalarValue(){
		double scalarValue = 0.0;
		for (Term term : terms) {
			scalarValue += term.getTF_IDF() * term.getTF_IDF(); 
		}
		scalarValue = Math.sqrt(scalarValue);
		return scalarValue;
	}
	
	/**
	 * 
	 * @param vectorDocument
	 * @return dotProduct dotProduct between two documents
	 */
	public double getDotProduct(VectorDocument vectorDocument){
		double dotProduct = 1.0;
		for (Term term : terms) {
			for (Term trm : vectorDocument.getTerms()) {
				if (term.isSame(trm)) {
					dotProduct *= term.getTF_IDF() * trm.getTF_IDF();
					break;
				}
			}
		}
		return dotProduct;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Ram and Sham are good friends.\nThey are good man also";
	}
}