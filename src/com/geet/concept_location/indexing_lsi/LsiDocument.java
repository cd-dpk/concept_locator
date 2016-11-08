package com.geet.concept_location.indexing_lsi;
import java.io.Serializable;
import com.geet.concept_location.corpus_creation.Document;
import com.github.javaparser.Position;
public class LsiDocument implements Comparable<LsiDocument>,Serializable{
	public LsiDocument(Document vectorDocument, Vector vector) {
		super();
		this.docInJavaFile = vectorDocument.getDocInJavaFile();
		this.article = vectorDocument.getArticle();
		this.vector = vector;
	}
	public String docInJavaFile;
	public String article = "";
	public Vector vector;
	public double score= -1;
	public String getDocInJavaFile() {
		return docInJavaFile;
	}
	public void setDocInJavaFile(String docInJavaFile) {
		this.docInJavaFile = docInJavaFile;
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
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	@Override
	public int compareTo(LsiDocument lsiDocument) {
		return Double.compare(score, lsiDocument.score);
	}
	public String toCSVString() {
	//	return docName+","+docInJavaFile+","+startPosition.toString()+","+endPosition.toString()+","+vector.toCSVString();
		return vector.toCSVString();
	}
}
