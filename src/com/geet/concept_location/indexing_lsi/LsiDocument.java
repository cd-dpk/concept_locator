package com.geet.concept_location.indexing_lsi;

import com.geet.concept_location.indexing_vsm.VectorDocument;
import com.github.javaparser.Position;

public class LsiDocument implements Comparable<LsiDocument>{
	
	public LsiDocument(VectorDocument vectorDocument, Vector vector) {
		super();
		this.docInJavaFile = vectorDocument.getDocInJavaFile();
		this.docName = vectorDocument.getDocName();
		this.article = vectorDocument.getArticle();
		this.startPosition = vectorDocument.getStartPosition();
		this.endPosition = vectorDocument.getEndPosition();
	}
	public String docInJavaFile;
	public String docName;
	public String article = "";
	public Vector vector;
	public Position startPosition, endPosition;
	
	public double score= -1;
	
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
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public Vector getVector() {
		return vector;
	}
	public void setVector(Vector vector) {
		this.vector = vector;
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
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
	@Override
	public int compareTo(LsiDocument lsiDocument) {
		if (score >= lsiDocument.score ) {
			return 1;
		}
		return -1;
	}
	
	
	
	
	

}
