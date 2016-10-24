package com.geet.concept_location.corpus_creation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.geet.concept_location.indexing_vsm.Term;
import com.geet.concept_location.utils.StringUtils;
import com.github.javaparser.Position;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;

public class Document {
	
	protected String docInJavaFile;
	protected String docName;
	protected Position startPosition, endPosition;
	
	protected List<JavadocComment> javaDocComments = new ArrayList<JavadocComment>();
	protected List<Comment> implementationComments = new ArrayList<Comment>();
	protected String implementionBody = "";
	protected String article="";

	
	public Document() {
		// TODO Auto-generated constructor stub
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


	public List<JavadocComment> getJavaDocComments() {
		return javaDocComments;
	}


	public void setJavaDocComments(List<JavadocComment> javaDocComments) {
		this.javaDocComments = javaDocComments;
	}


	public List<Comment> getImplementationComments() {
		return implementationComments;
	}


	public void setImplementationComments(List<Comment> implementationComments) {
		this.implementationComments = implementationComments;
	}


	public String getImplementionBody() {
		return implementionBody;
	}


	public void setImplementionBody(String implementionBody) {
		this.implementionBody = implementionBody;
	}


	public void setArticle(String article) {
		this.article = article;
	}


	public Document(String docInJavaFile, String docName,
			Position startPosition, Position endPosition) {
		super();
		this.docInJavaFile = docInJavaFile;
		this.docName = docName;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
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
	
	public Range getRange(){
		return new Range(startPosition, endPosition);
	}
	
	public String getArticle() {
		article = "";
	//	article += javaDocComments.toString()+"\n"+ implementationComments.toString()+"\n"+ implementionBody.toString()+"\n";
		for (String term : new TermExtractorFromDocument().getTermsFromDocument(this)) {
			article += term+" ";
		}
		return article;
	}
	
	public List<Term> getTerms() {
		List<Term> terms = new ArrayList<Term>();
		StringTokenizer stringTokenizer = new StringTokenizer(getArticle(), JavaLanguage.getWhiteSpace(), false);
		while (stringTokenizer.hasMoreTokens()) {
			Term candidateTerm = new Term(stringTokenizer.nextToken().toLowerCase(), 1);
			int pass = -1;
			for (int i = 0; i < terms.size(); i++) {
				if (terms.get(i).isSame(candidateTerm)) {
					pass = i;
					terms.get(i).termFrequency++;
					continue;
				}
			}
			if (pass == -1) {
				terms.add(candidateTerm);
			}
		}
		return terms;
	}
	
	public boolean hasTerm(Term term){
		for (Term trm : getTerms()) {
			if (trm.isSame(term)) {
				return true;
			}
		}
		return false;
	}
	
	public String toIndentity(){
		return docName+" "+docInJavaFile;
	}
}