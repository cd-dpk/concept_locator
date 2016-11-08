package com.geet.concept_location.corpus_creation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import com.geet.concept_location.indexing_vsm.Term;
import com.geet.concept_location.utils.CommentStringTokenizer;
import com.geet.concept_location.utils.ImplementationStringTokenizer;
import com.geet.concept_location.utils.StringUtils;
import com.github.javaparser.Position;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
public class Document  extends SimpleDocument {
	protected String docInJavaFile;
	protected List<String> docTitles;
	protected List<JavadocComment> javaDocComments = new ArrayList<JavadocComment>();
	protected List<Comment> implementationComments = new ArrayList<Comment>();
	protected String implementionBody = "";
	public Document() {}
	public Document(String docInJavaFile, List<String> docTitles,
			List<JavadocComment> javaDocComments,
			List<Comment> implementationComments, String implementionBody) {
		super();
		this.docInJavaFile = docInJavaFile;
		this.docTitles = docTitles;
		this.javaDocComments = javaDocComments;
		this.implementationComments = implementationComments;
		this.implementionBody = implementionBody;
	}

	public String getDocInJavaFile() {
		return docInJavaFile;
	}
	public void setDocInJavaFile(String docInJavaFile) {
		this.docInJavaFile = docInJavaFile;
	}

	public List<String> getDocTitles() {
		return docTitles;
	}
	public void setDocTitles(List<String> docTitles) {
		this.docTitles = docTitles;
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
		article += " "+ getDocTitles();
		article += " "+ getJavaDocComments();
		article += " "+ getImplementationComments();
		article += " "+ getImplementionBody();
		return article;
	}
	@Override
	public List<Term> getTerms() {
		List<Term> terms = new ArrayList<Term>();
		StringTokenizer stringTokenizer = new StringTokenizer(getArticle(), JavaLanguage.getWhiteSpace(), false);
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			// stem the token if token is a word
			/*if (StringUtils.isWord(token)) {
				token = new Stemmer(token.toLowerCase()).toString();
			}*/
			// if stop word then continue
			if (StopWords.isStopword(token)) {
				continue;
			}
			Term candidateTerm = new Term(token.toLowerCase(), 1);
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
	
	
	private List<String> getTermsFromTitles(){
		List<String> terms = new ArrayList<String>();
		// titles extraction
		for (String title : getDocTitles()) {
			terms.add(StringUtils.getIdentifierSeparationsWithCamelCase(title));
		}
		return terms;
	}
	private String getArticleFromJavaDocComments(){
		/*
		 * remove *, programming syntax, @tag, <tag>, </tag>
		 */
		// List<String> terms = new ArrayList<String>();
		String subArticle ="";
		for (JavadocComment javadocComment : javaDocComments) {
			CommentStringTokenizer customStringTokenizer = new CommentStringTokenizer(javadocComment.getContent()," ", false);
			String documentString = "";
			while (customStringTokenizer.hasMoreTokens()) {
				documentString = customStringTokenizer.nextToken();
				if (!StopWords.isStopword(documentString)) {	
					// make stemming
					Stemmer stemmer = new Stemmer(documentString);
					stemmer.stem();
					documentString = stemmer.toString();
				}
				subArticle += " "+documentString;
			}
		}
		return subArticle;
	}
	private String getArticleFromComment(){
		String subArticle = "";
		// List<String> terms = new ArrayList<String>();
		// remove *, programming syntax, @tag, <tag>, </tag>
		for (Comment comment : implementationComments) {
			CommentStringTokenizer customStringTokenizer = new CommentStringTokenizer(comment.getContent(), " ", false);
			String documentString = "";
			while (customStringTokenizer.hasMoreTokens()) {
				documentString = customStringTokenizer.nextToken();
				// remove stop words
				if (!StopWords.isStopword(documentString)) {	
					// make stemming
					Stemmer stemmer = new Stemmer(documentString);
					stemmer.stem();
					documentString = stemmer.toString();
				}
				subArticle +=" "+documentString;
			}
		}
		return subArticle;
	}
	private String getArticleFromImplementation(){
		String subArticle ="";
		// List<String> terms = new ArrayList<String>();
		// remove all programming syntax, operators, keywords
		ImplementationStringTokenizer implementationStringTokenizer = new ImplementationStringTokenizer(implementionBody," ", false);
		while (implementationStringTokenizer.hasMoreTokens()) {
			subArticle += " "+implementationStringTokenizer.nextToken();
		}
		return subArticle;
	}
}
