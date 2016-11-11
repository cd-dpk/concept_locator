package com.geet.concept_location.corpus_creation;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import com.geet.concept_location.indexing_vsm.Term;
import com.geet.concept_location.utils.CommentStringTokenizer;
import com.geet.concept_location.utils.StringUtils;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
public class Document  extends SimpleDocument {
	public String docInJavaFile;
	public List<String> docTitles = new ArrayList<String>();
	public List<JavadocComment> javaDocComments = new ArrayList<JavadocComment>();
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
		article += " "+ getArticleFromTitles();
		article += " "+ getArticleFromComment();
		article += " "+ getArticleFromJavaDocComments();
		article += " "+ getArticleFromImplementation();
		return article;
	}
	@Override
	public List<Term> getTerms() {
		List<Term> terms = new ArrayList<Term>();
		StringTokenizer stringTokenizer = new StringTokenizer(getArticle(), JavaLanguage.getWhiteSpace(), false);
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
	private String getArticleFromTitles(){
		String subArticle = "";
		// titles extraction
		for (String title : getDocTitles()) {
			subArticle+=" "+(StringUtils.getIdentifierSeparationsWithCamelCase(title));
		}
		return subArticle;
	}
	private String getArticleFromJavaDocComments(){
		/*
		 * remove *, programming syntax, @tag, <tag>, </tag>, #
		 */
		// List<String> terms = new ArrayList<String>();
		String subArticle ="";
		for (JavadocComment javadocComment : javaDocComments) {
			CommentStringTokenizer customStringTokenizer = new CommentStringTokenizer(javadocComment.getContent()," ", false);
			String documentString = "";
			while (customStringTokenizer.hasMoreTokens()) {
				documentString = customStringTokenizer.nextToken();
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
				subArticle +=" "+documentString;
			}
		}
		return subArticle;
	}
	private String getArticleFromImplementation(){
		String subArticle ="";
		// remove all programming syntax, operators, keywords
		StringTokenizer stringTokenizer = new StringTokenizer(implementionBody,
				JavaLanguage.getProgrammingLanguageSyntax()
						+ JavaLanguage.getOperators() + JavaLanguage.getWhiteSpace()
						, false);
		while (stringTokenizer.hasMoreTokens()) {
			String nestedToken = stringTokenizer.nextToken();
			// if token is equal to any keywords, or operators then replace with
			// " "
			if (StringUtils.hasStringInList(nestedToken, JavaLanguage.KEYWORDS)
					|| StringUtils.hasStringInList(nestedToken,
							JavaLanguage.OPERATORS_CONTAINED_ONLY_CHAR)
					|| StringUtils.hasStringInList(nestedToken,
							JavaLanguage.LITERALS)) {
			} else {
				// get identifier separation
				subArticle += StringUtils
						.getIdentifierSeparationsWithCamelCase(nestedToken)
						+ " ";
			}
		}
		return subArticle;
	}
}
