package com.geet.concept_location.corpus_creation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.geet.concept_location.indexing_vsm.Term;
import com.geet.concept_location.utils.StringUtils;

public class SimpleDocument implements Comparable<SimpleDocument>, Serializable {

	protected String article = "";
	protected String docInJavaFile = "";
	protected String docName = "";
	protected double score = 0.0;
	protected Position startPosition;
	protected Position endPosition;

	protected SimpleDocument() {}

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

	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocInJavaFile() {
		return docInJavaFile;
	}

	public void setDocInJavaFile(String docInJavaFile) {
		this.docInJavaFile = docInJavaFile;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public void setArticle(String article) {
		this.article = article;
		StringTokenizer stringTokenizer = new StringTokenizer(getArticle(),
				JavaLanguage.getWhiteSpace()
						+ JavaLanguage.getProgrammingLanguageSyntax()
						+ JavaLanguage.getOperatorsInString(), false);
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			if (!token.equals(StringUtils
					.getIdentifierSeparationsWithCamelCaseOnlyToken(token))) {
				this.article += " "
						+ StringUtils
								.getIdentifierSeparationsWithCamelCaseOnlyToken(token)
						+ " ";
			}
		}
	}

	public String getArticle() {
		return article;
	}

	public double getTF(String termString) {
		double tf = 0.0;
		for (Term term : getTerms()) {
			if (term.isSame(new Term(termString))) {
				return term.getTermFrequency();
			}
		}
		return tf;
	}

	public List<Term> getTerms() {

		// System.out.println(article);
		List<Term> terms = new ArrayList<Term>();
		StringTokenizer stringTokenizer = new StringTokenizer(getArticle(),
				JavaLanguage.getWhiteSpace()
						+ JavaLanguage.getProgrammingLanguageSyntax()
						+ JavaLanguage.getOperatorsInString(), false);
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			// System.out.println(token);
			token = token.toLowerCase();
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

	public boolean hasTerm(Term term) {
		for (Term trm : getTerms()) {
			if (trm.isSame(term)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(SimpleDocument o) {
		return Double.compare(score, o.score);
	}

	public List<String> getTermsInString() {
		List<String> termsInString = new ArrayList<String>();
		for (Term term : getTerms()) {
			termsInString.add(term.getTermString());
		}
		return termsInString;
	}

	/**
	 * @return true when the two document is same
	 */
	public boolean isSameDocument(SimpleDocument simpleDocument) {
		boolean status = false;
		if (docName.equals(simpleDocument.docName)
				&& docInJavaFile.equals(simpleDocument.docInJavaFile)
				&& startPosition.isEqual(simpleDocument.getStartPosition())
				&& endPosition.isEqual(simpleDocument.getEndPosition())) {
			return true;
		}
		return status;
	}

	public static void main(String[] args) {
		SimpleDocument simpleDocument = new SimpleDocument.Builder().article("Prime Number").build();
		System.out.println(simpleDocument.getArticle());
		System.out.println(simpleDocument.getTermsInString());

		System.out.println(StringUtils
				.getIdentifierSeparationsWithCamelCase("HelloWorld"));
	}
	
	public static class Builder{
		private String article = "";
		private String docInJavaFile = "";
		private String docName = "";
		private double score = 0.0;
		private Position startPosition= new Position(0, 0);
		private Position endPosition = new Position(0, 0);
		
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
		
		public SimpleDocument build() {
			SimpleDocument simpleDocument = new SimpleDocument();
			simpleDocument.setDocInJavaFile(docInJavaFile);
			simpleDocument.setDocName(docName);
			simpleDocument.setArticle(article);
			simpleDocument.setStartPosition(startPosition);
			simpleDocument.setEndPosition(endPosition);
			simpleDocument.setScore(score);
			return simpleDocument;
		}
		
	}
}
