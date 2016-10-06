package com.geet.concept_location.corpus_creation;

import com.github.javaparser.Position;

public class Document {
	DocumentType documentType;
	String docInJavaFile;
	String docName;
	Position startPosition, endPosition;
	String body;
	
	public Document(String docInJavaFile, String docName,DocumentType documentType,
			Position startPosition, Position endPosition) {
		this.docInJavaFile = docInJavaFile;
		this.docName = docName;
		this.documentType = documentType;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}

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
	
	public enum DocumentType{
			METHOD,CONSTRUCTOR,CLASS_OR_INTERFACE;
	}
	
	public boolean isBetweenPosition(Position position){
		if (position.getLine() == startPosition.getLine() && position.getColumn() > startPosition.getColumn() ) {
			return true;
		} 
		else if(position.getLine() > startPosition.getLine() && position.getLine() < endPosition.getLine()){
			return true;
		}
		else if(position.getLine() == endPosition.getLine() && position.getColumn() < endPosition.getColumn()){
			return true;
		}
		return false;
	}
}
