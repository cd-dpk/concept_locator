package com.geet.concept_location.indexing_vsm;

import java.util.ArrayList;
import java.util.List;

import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.QueryDocument;
import com.github.javaparser.Position;

public class VectorDocument implements Comparable<VectorDocument>{
	
	protected String docInJavaFile;
	protected String docName;
	public String getDocInJavaFile() {
		return docInJavaFile;
	}
	public void setDocInJavaFile(String docInJavaFile) {
		this.docInJavaFile = docInJavaFile;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public Position getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(Position startPosition) {
		this.startPosition = startPosition;
	}
	public Position getEndPosition() {
		return endPosition;
	}
	public void setEndPosition(Position endPosition) {
		this.endPosition = endPosition;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public VectorDocument(String docInJavaFile, String docName,
			Position startPosition, Position endPosition, List<Term> uniqueTerms,
			String article) {
		super();
		this.docInJavaFile = docInJavaFile;
		this.docName = docName;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.terms = uniqueTerms;
		this.article = article;
	}
	
	protected Position startPosition, endPosition;
	public List<Term> terms = new ArrayList<Term>();
	public String article = "";

	/**
	 * constructor from query
	 * @param terms
	 */
	public VectorDocument(QueryDocument queryDocument) {
		this.terms= queryDocument.getTerms();
		this.article = queryDocument.getArticle();
	}
	public VectorDocument(Document document) {
		this.terms = document.getTerms(); 
		this.article = document.getArticle();
	}
	
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

	
	public double getScalarValue(){
		double scalarValue = 0.0;
		for (Term term : terms) {
				scalarValue += term.getTF_IDF()* term.getTF_IDF();
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
		double scalarValue = 0.0;
		for (Term term : terms) {
			for (Term trm : vectorDocument.getTerms()) {
				if (term.isSame(trm)) {
					dotProduct *= term.getTF_IDF() * term.getTF_IDF();
					scalarValue += term.getTF_IDF() * term.getTF_IDF();
					break;
				}
			}
		}
		
		// cosine similarity
		
		return dotProduct;
	}
	
	@Override
	public String toString() {
		return "Ram and Sham are good friends.\nThey are good man also";
	}
	
	public double dotProduct=0.0;
	
	@Override
	public int compareTo(VectorDocument vectorDocument) {
		if (dotProduct > vectorDocument.dotProduct) {
			return 1;
		}
		return -1;
	}
	
	public double getTF_IDF(String termString){
		double tf_idf = 0.0;
		for (Term term : getTerms()) {
			if (term.isSame(new Term(termString))) {
				return term.getTF_IDF();
			}
		}
		return tf_idf;
	}
}