package com.geet.concept_location.corpus_creation;

import java.io.Serializable;

import javax.crypto.spec.PSource;

public class Position  implements Serializable{

	public int line=0;
	public int column=0;
	
	public Position(int line, int column) {
		setLine(line);
		setColumn(column);
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public Position(com.github.javaparser.Position position){
		this(position.getLine(), position.getColumn());
	}
	
	public  boolean isEqual(Position position) {
		if (getLine() == position.getLine() && getColumn() == position.getColumn()) {
			return true;
		} 
		return false;
	}
	
	@Override
	public String toString() {
		return getLine()+"-"+getColumn();
	}
	public com.github.javaparser.Position toParserPosition(){
		com.github.javaparser.Position position = new com.github.javaparser.Position(line, column);
		return position;
	}
}