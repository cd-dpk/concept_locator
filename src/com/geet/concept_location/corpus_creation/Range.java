package com.geet.concept_location.corpus_creation;
import com.github.javaparser.Position;
import com.github.javaparser.ast.Node;
public class Range {
	public Position startPosition;
	public Position endPosition;
	public Range(Position startPosition, Position endPosition) {
		super();
		this.startPosition = startPosition;
		this.endPosition = endPosition;
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
	public int getBeginLine(){
		return startPosition.getLine();
	}
	public int getBeginColumn(){
		return startPosition.getColumn();
	}
	public int getEndLine(){
		return endPosition.getLine();
	}
	public int getEndColumn(){
		return endPosition.getColumn();
	}
	public static Range toRange(Node node){
		return new Range(new Position(node.getBeginLine(), node.getBeginColumn()), new Position(node.getEndLine(), node.getEndColumn()));
	}
	public boolean isEqual(Range range) {
		if (getBeginColumn()== range.getBeginColumn() && getBeginLine() == range.getBeginLine() && getEndColumn() == range.getEndColumn() && getEndLine() == range.getEndLine()) {
			return true;
		}
		return false;
	}
}
