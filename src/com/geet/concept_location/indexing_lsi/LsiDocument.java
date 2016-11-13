package com.geet.concept_location.indexing_lsi;
import java.io.Serializable;
import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.SimpleDocument;
import com.github.javaparser.Position;
public class LsiDocument extends SimpleDocument implements Serializable{
	public LsiDocument(SimpleDocument vectorDocument, Vector vector) {
		super();
		this.docInJavaFile = vectorDocument.docInJavaFile;
		this.vector = vector;
	}
	public String docInJavaFile;
	public Vector vector;
	
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
	
	public String toCSVString() {
	//	return docName+","+docInJavaFile+","+startPosition.toString()+","+endPosition.toString()+","+vector.toCSVString();
		return docInJavaFile+","+vector.toCSVString();
	}
}
