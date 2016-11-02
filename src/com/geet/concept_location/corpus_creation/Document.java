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
	protected String docName;
	protected com.geet.concept_location.corpus_creation.Position startPosition, endPosition;
	protected List<JavadocComment> javaDocComments = new ArrayList<JavadocComment>();
	protected List<Comment> implementationComments = new ArrayList<Comment>();
	protected String implementionBody = "";
	public Document() {
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
	public com.geet.concept_location.corpus_creation.Position getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(com.geet.concept_location.corpus_creation.Position startPosition) {
		this.startPosition = startPosition;
	}
	public com.geet.concept_location.corpus_creation.Position getEndPosition() {
		return endPosition;
	}
	public void setEndPosition(com.geet.concept_location.corpus_creation.Position endPosition) {
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
			com.geet.concept_location.corpus_creation.Position startPosition, com.geet.concept_location.corpus_creation.Position endPosition) {
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
		return new Range(startPosition.toParserPosition(), endPosition.toParserPosition());
	}
	@Override
	public String getArticle() {
		article = "";
	//	article += javaDocComments.toString()+"\n"+ implementationComments.toString()+"\n"+ implementionBody.toString()+"\n";
		for (String term : getTermsFromDocument()) {
			article += term+" ";
		}
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
	private List<String> getTermsFromDocument(){
		Set<String> terms = new HashSet<String>();
		// List<String> terms = new ArrayList<String>();
		// add title to article
		StringTokenizer stringTokenizer = new StringTokenizer(StringUtils.getIdentifierSeparationsWithCamelCase(getDocName()), JavaLanguage.getWhiteSpace(), false);
		while (stringTokenizer.hasMoreTokens()) {
			terms.add(stringTokenizer.nextToken());
		}
		terms.addAll(new HashSet<String>(getTermsFromJavaDocComments()));
		terms.addAll(new HashSet<String>(getTermsFromComment()));
		terms.addAll(new HashSet<String>(getTermsFromImplementation()));
		return new ArrayList<String>(terms);
	}
	private List<String> getTermsFromJavaDocComments(){
		/*
		 * remove *, programming syntax, @tag, <tag>, </tag>
		 */
		// List<String> terms = new ArrayList<String>();
		Set<String> termSet = new HashSet<String>();
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
				termSet.add(documentString);
			}
		}
		return new ArrayList<>(termSet);
	}
	private List<String> getTermsFromComment(){
		Set<String> termSet = new HashSet<String>();
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
				termSet.add(documentString);
			}
		}
		return new ArrayList<String>(termSet);
	}
	private List<String> getTermsFromImplementation(){
		Set<String> termSet = new HashSet<String>();
		// List<String> terms = new ArrayList<String>();
		// remove all programming syntax, operators, keywords
		ImplementationStringTokenizer implementationStringTokenizer = new ImplementationStringTokenizer(implementionBody," ", false);
		while (implementationStringTokenizer.hasMoreTokens()) {
			termSet.add(implementationStringTokenizer.nextToken());
		}
		return new ArrayList<String>(termSet);
	}
	/**
	 * @return true when the two document is same 
	 */
	public boolean isSameDocument(Document document){
		boolean status = false;
		if (docName.equals(document.docName) && docInJavaFile.equals(document.docInJavaFile) && startPosition.isEqual(document.getStartPosition()) && endPosition.isEqual(document.getEndPosition())) {
			return true;
		}
		return status;
	}
}
