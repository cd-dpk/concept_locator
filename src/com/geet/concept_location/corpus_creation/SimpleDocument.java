package com.geet.concept_location.corpus_creation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.geet.concept_location.indexing_vsm.Term;
import com.geet.concept_location.utils.StringUtils;

public class SimpleDocument implements Comparable<SimpleDocument>, Serializable{
	protected String article= "";
	public String docInJavaFile;
	public String docName = "";
	public double score = 0.0;
	public Position startPosition;
	public Position endPosition;
	
	
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

	public SimpleDocument( String docInJavaFile, String docName,
			Position startPosition, Position endPosition, double score) {
		super();
		this.docInJavaFile = docInJavaFile;
		this.docName = docName;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.score = score;
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
	
	public SimpleDocument(){
	}
	
	
	
	public SimpleDocument( String docInJavaFile, String article) {
		super();
		this.article = article;
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
	}
	public String getArticle() {
		return article;
	}
	public double getTF(String termString){
		double tf = 0.0;
		for (Term term : getTerms()) {
			if (term.isSame(new Term(termString))) {
				return term.termFrequency;
			}
		}
		return tf;
	}
	public List<Term> getTerms() {
		List<Term> terms = new ArrayList<Term>();
		StringTokenizer stringTokenizer = new StringTokenizer(getArticle(), JavaLanguage.getWhiteSpace()+JavaLanguage.getProgrammingLanguageSyntax()+JavaLanguage.getOperators(), false);
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			System.out.println(token);
			token = token.toLowerCase();
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
	public boolean hasTerm(Term term){
		for (Term trm : getTerms()) {
			if (trm.isSame(term)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public int compareTo(SimpleDocument o) {
		// TODO Auto-generated method stub
		return Double.compare(score, o.score);
	}
	public List<String> getTermsInString(){
		List<String> termsInString = new ArrayList<String>();
		for (Term term : getTerms()) {
			termsInString.add(term.termString);
		}
		return termsInString;
	}
}
