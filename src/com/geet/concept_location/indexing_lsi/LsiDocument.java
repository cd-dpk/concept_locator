package com.geet.concept_location.indexing_lsi;
import java.io.Serializable;
import com.geet.concept_location.corpus_creation.Document;
import com.github.javaparser.Position;
public class LsiDocument implements Comparable<LsiDocument>,Serializable{
	
	public LsiDocument(Document vectorDocument, Vector vector) {
		super();
		this.docInJavaFile = vectorDocument.getDocInJavaFile();
		this.docName = vectorDocument.getDocName();
		this.article = vectorDocument.getArticle();
		this.startPosition = vectorDocument.getStartPosition();
		this.endPosition = vectorDocument.getEndPosition();
		this.vector = vector;
	}
	public String docInJavaFile;
	public String docName;
	public String article = "";
	public Vector vector;
	public com.geet.concept_location.corpus_creation.Position startPosition, endPosition;
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
	public com.geet.concept_location.corpus_creation.Position getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(com.geet.concept_location.corpus_creation.Position startPosition) {
		this.startPosition = startPosition;
	}
	public com.geet.concept_location.corpus_creation.Position getEndPosition() {
		return endPosition;
	}
	public void setEndPosition(com.geet.concept_location.corpus_creation.Position endPosition) {
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
	public String toCSVString() {
		return docName+","+docInJavaFile+","+startPosition.toString()+","+endPosition.toString()+","+vector.toCSVString();
	}
}
