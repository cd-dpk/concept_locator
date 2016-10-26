package com.geet.concept_location.corpus_creation;
import java.util.ArrayList;
import java.util.List;
import com.geet.concept_location.utils.CommentStringTokenizer;
import com.geet.concept_location.utils.ImplementationStringTokenizer;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
public class TermExtractorFromDocument {
	Document document;
	public List<String> getTermsFromDocument(Document document){
		List<String> terms = new ArrayList<String>();
		this.document = document;
		for (String term : getTermsFromJavaDocComments()) {
			terms.add(term);
		}
		terms.add("\n");
		for (String term : getTermsFromComment()) {
			terms.add(term);
		}
		terms.add("\n");
		for (String term : getTermsFromImplementation()) {
			terms.add(term);
		}
		terms.add("\n");
		terms = getTermsAfterStemming(terms);
		return terms;
	}
	private List<String> getTermsFromJavaDocComments(){
		/*
		 * remove *, programming syntax, @tag, <tag>, </tag>
		 */
		List<String> terms = new ArrayList<String>();
		for (JavadocComment javadocComment : document.javaDocComments) {
			CommentStringTokenizer customStringTokenizer = new CommentStringTokenizer(javadocComment.getContent()," ", false);
			String documentString = "";
			while (customStringTokenizer.hasMoreTokens()) {
				documentString = customStringTokenizer.nextToken();
				terms.add(documentString);
			}
		}
		return terms;
	}
	private List<String> getTermsFromComment(){
		List<String> terms = new ArrayList<String>();
		// remove *, programming syntax, @tag, <tag>, </tag>
		for (Comment comment : document.implementationComments) {
			CommentStringTokenizer customStringTokenizer = new CommentStringTokenizer(comment.getContent(), " ", false);
			String documentString = "";
			while (customStringTokenizer.hasMoreTokens()) {
				documentString = customStringTokenizer.nextToken();
				terms.add(documentString);
			}
		}
		return terms;
	}
	private List<String> getTermsFromImplementation(){
		List<String> terms = new ArrayList<String>();
		// remove all programming syntax, operators, keywords
		ImplementationStringTokenizer implementationStringTokenizer = new ImplementationStringTokenizer(document.implementionBody," ", false);
		while (implementationStringTokenizer.hasMoreTokens()) {
			terms.add(implementationStringTokenizer.nextToken());
		}
		return terms;
	}
	// Identifier Separation
	private List<String> getTermsAfterIdentifierSeparation(List<String> terms){
		return terms;
	}
	// Terms Stemming
	private List<String> getTermsAfterStemming(List<String>terms){
		return terms;
	}
}
