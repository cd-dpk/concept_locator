package com.geet.concept_location.indexing_lsi;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.geet.concept_location.corpus_creation.JavaLanguage;
import com.geet.concept_location.corpus_creation.StopWords;
import com.geet.concept_location.indexing_vsm.Term;
public class LsiQuery {
	public String query;
	public Vector vector;
	public LsiQuery(String query, Vector vector) {
		super();
		this.query = query;
		this.vector = vector;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public Vector getVector() {
		return vector;
	}
	public void setVector(Vector vector) {
		this.vector = vector;
	}
	public List<Term> getTerms() {
		List<Term> terms = new ArrayList<Term>();
		StringTokenizer stringTokenizer = new StringTokenizer(query, JavaLanguage.getWhiteSpace()+JavaLanguage.getProgrammingLanguageSyntax()+JavaLanguage.getOperators(), false);
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			token = token.toLowerCase();
			if (StopWords.isStopword(token)) {
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
	
}
