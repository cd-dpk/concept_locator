package com.geet.concept_location.corpus_creation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.geet.concept_location.utils.StringUtils;
import com.github.javaparser.Position;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;

public abstract class Document {
	protected String docInJavaFile;
	protected String docName;
	protected Position startPosition, endPosition;
	
	protected List<JavadocComment> javaDocComments = new ArrayList<JavadocComment>();
	protected List<Comment> implementationComments = new ArrayList<Comment>();
	protected String implementionBody = "";
	protected String article="";

	
	
	public Document(String docInJavaFile, String docName,
			Position startPosition, Position endPosition) {
		super();
		this.docInJavaFile = docInJavaFile;
		this.docName = docName;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
	}


	protected abstract void extractDocument();
	/*public String extractDocumentOld(){
		// process all java doc comments
		// process all comments
		// process all implementation body
		
		String article ="";
		// scanner open
		Scanner scanner = new Scanner(body);
		// Step 1: read every line 
		List<String> words = new ArrayList<String>();
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			// Step 2: delete all spaces, operators, programming syntax, comment
			String delim = new JavaLanguage().getOperators()+ new JavaLanguage().getProgrammingLanguageSyntax()+new JavaLanguage().getComments();
			StringTokenizer stringTokenizer = new StringTokenizer(line,delim,false);
			// Step 3: take all the words
			while (stringTokenizer.hasMoreTokens()) {
				words.add(stringTokenizer.nextToken());
			}
			// Step 4: remove all keywords, structures, annotations, literals					
			for (String string : words) {
				if (StringUtils.hasStringInList(string, new JavaLanguage().KEYWORDS) || StringUtils.hasStringInList(string, new JavaLanguage().ANNOTATIONS) || StringUtils.hasStringInList(string, new JavaLanguage().LITERALS)|| StringUtils.hasStringInList(string, new JavaLanguage().JAVA_DOC)) {
					continue;
				}
				article += string+" ";
			}
		}
		// Step 5: until
		scanner.close();
		// scanner close
		return article;	
	}*/

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
		article = docName+ " "+ docInJavaFile+"\n";
		article += javaDocComments.toString()+"\n"+ implementationComments.toString()+"\n"+ implementionBody.toString()+"\n";
		return article;
	}
	
}