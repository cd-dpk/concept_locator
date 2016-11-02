package com.geet.concept_location.corpus_creation;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.geet.concept_location.indexing_vsm.Term;
public class SimpleDocument implements Comparable<SimpleDocument>{
	protected String article= "";
	public double score = 0.0;
	public SimpleDocument(){
	}
	public SimpleDocument(String article){
		setArticle(article);
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
		StringTokenizer stringTokenizer = new StringTokenizer(getArticle(), JavaLanguage.getWhiteSpace(), false);
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			// stem the token if token is a word
			if (!StopWords.isStopword(token)) {
				Stemmer stemmer = new Stemmer(token);
				stemmer.stem();
				token = stemmer.toString();
				System.out.println(token);
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