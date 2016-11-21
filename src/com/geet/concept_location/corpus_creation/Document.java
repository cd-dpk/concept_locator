package com.geet.concept_location.corpus_creation;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.geet.concept_location.corpus_creation.SimpleDocument.Builder;
import com.geet.concept_location.indexing_vsm.Term;
import com.geet.concept_location.utils.CommentStringTokenizer;
import com.geet.concept_location.utils.StringUtils;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;


public class Document extends SimpleDocument {
	
	private Document() {
		super();
	}
	private List<JavadocComment> javaDocComments = new ArrayList<JavadocComment>();
	private List<Comment> implementationComments = new ArrayList<Comment>();
	private String implementionBody = "";
	
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

	@Override
	public String getArticle() {
		article = "";
		// article += " "+ getArticleFromName();
		article += " " + getArticleFromDocName();
		article += " " + getArticleFromComment();
		article += " " + getArticleFromJavaDocComments();
		article += " " + getArticleFromImplementation();
		return article;
	}

	private String getArticleFromName() {
		String subArticle = "";
		subArticle = StringUtils
				.getIdentifierSeparationsWithCamelCase(docInJavaFile);
		return subArticle;
	}

	@Override
	public List<Term> getTerms() {
		List<Term> terms = new ArrayList<Term>();
		StringTokenizer stringTokenizer = new StringTokenizer(getArticle(),
				JavaLanguage.getWhiteSpace(), false);
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			// when getting the term, termString should be in lower case
			token = token.toLowerCase();
			// if stop word then continue
			if (StopWords.isStopword(token) || StringUtils.isConstant(token)) {
				continue;
			}
			Term candidateTerm = new Term(token, 1);
			int pass = -1;
			for (int i = 0; i < terms.size(); i++) {
				if (terms.get(i).isSame(candidateTerm)) {
					pass = i;
					int temp = terms.get(i).getTermFrequency();
					temp++;
					terms.get(i).setTermFrequency(temp);
					continue;
				}
			}
			if (pass == -1) {
				terms.add(candidateTerm);
			}
		}
		return terms;
	}

	private String getArticleFromDocName() {
		String subArticle = "";
		// titles extraction
		subArticle = (StringUtils
				.getIdentifierSeparationsWithCamelCase(docName));
		return subArticle;
	}

	private String getArticleFromJavaDocComments() {
		/*
		 * remove *, programming syntax, @tag, <tag>, </tag>, #
		 */
		// List<String> terms = new ArrayList<String>();
		String subArticle = "";
		for (JavadocComment javadocComment : javaDocComments) {
			CommentStringTokenizer customStringTokenizer = new CommentStringTokenizer(
					javadocComment.getContent(), " ", false);
			String documentString = "";
			while (customStringTokenizer.hasMoreTokens()) {
				documentString = customStringTokenizer.nextToken();
				subArticle += " " + documentString;
			}
		}
		return subArticle;
	}

	private String getArticleFromComment() {
		String subArticle = "";
		// List<String> terms = new ArrayList<String>();
		// remove *, programming syntax, @tag, <tag>, </tag>
		for (Comment comment : implementationComments) {
			CommentStringTokenizer customStringTokenizer = new CommentStringTokenizer(
					comment.getContent(), " ", false);
			String documentString = "";
			while (customStringTokenizer.hasMoreTokens()) {
				documentString = customStringTokenizer.nextToken();
				subArticle += " " + documentString;
			}
		}
		return subArticle;
	}

	private String getArticleFromImplementation() {
		String subArticle = "";
		// remove all programming syntax, operators, keywords
		StringTokenizer stringTokenizer = new StringTokenizer(implementionBody,
				JavaLanguage.getProgrammingLanguageSyntax()
						+ JavaLanguage.getOperatorsInString()
						+ JavaLanguage.getWhiteSpace(), false);
		while (stringTokenizer.hasMoreTokens()) {
			String nestedToken = stringTokenizer.nextToken();
			// if token is equal to any keywords, or operators then replace with
			// " "
			if (StringUtils.hasStringInList(nestedToken, JavaLanguage.getKeywords())
					|| StringUtils.hasStringInList(nestedToken,
							JavaLanguage.getOperatorsContainedOnlyChar())
					|| StringUtils.hasStringInList(nestedToken,
							JavaLanguage.getLiterals())) {
			} else {
				// get identifier separation
				subArticle += StringUtils
						.getIdentifierSeparationsWithCamelCase(nestedToken)
						+ " ";
			}
		}
		return subArticle;
	}

	public boolean isBetweenPosition(Position position) {
		if (position.getLine() == startPosition.getLine()
				&& position.getColumn() > startPosition.getColumn()) {
			return true;
		} else if (position.getLine() > startPosition.getLine()
				&& position.getLine() < endPosition.getLine()) {
			return true;
		} else if (position.getLine() == endPosition.getLine()
				&& position.getColumn() < endPosition.getColumn()) {
			return true;
		}
		return false;
	}

	public Range getRange() {
		return new Range(startPosition.toParserPosition(),
				endPosition.toParserPosition());
	}
	public static class Builder{
		private String article = "";
		private String docInJavaFile = "";
		private String docName = "";
		private double score = 0.0;
		private Position startPosition= new Position(0, 0);
		private Position endPosition = new Position(0, 0);
		
		private List<JavadocComment> javaDocComments = new ArrayList<JavadocComment>();
		private List<Comment> implementationComments = new ArrayList<Comment>();
		private String implementionBody = "";
		
		public Builder docInJavaFile(String docInJavaFile){
			this.docInJavaFile = docInJavaFile;
			return this;
		}
		public Builder docName(String docName){
			this.docName = docName;
			return this;
		}
		public Builder article(String article){
			this.article = article;
			return this;
		}
		public Builder startPosition(Position position){
			this.startPosition = position;
			return this;
		}
		public Builder endPosition(Position position){
			this.endPosition = position;
			return this;
		}
		public Builder score(double score){
			this.score = score;
			return this;
		}
		public Builder javaDocComment(List<JavadocComment> javadocComments){
			this.javaDocComments = javadocComments;
			return this;
		}
		public Builder implementationComments(List<Comment> comments){
			this.implementationComments = comments;
			return this;
		}
		public Builder implementationBody(String body){
			this.implementionBody = body;
			return this;
		}
		public Document build() {
			Document document =  new Document();
			document.setDocInJavaFile(docInJavaFile);
			document.setDocName(docName);
			document.setArticle(article);
			document.setStartPosition(startPosition);
			document.setEndPosition(endPosition);
			document.setScore(score);
			document.setJavaDocComments(javaDocComments);
			document.setImplementationComments(implementationComments);
			document.setImplementionBody(implementionBody);
			return document;
		}
	}
}
