package com.geet.concept_location.indexing_lsi;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
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
	public List<String> getTerms(){
		List<String> terms = new ArrayList<String>();
		StringTokenizer stringTokenizer = new StringTokenizer(query," ", false);
		while (stringTokenizer.hasMoreTokens()) {
			terms.add(stringTokenizer.nextToken().toLowerCase());
		}
		return terms;
	}
}
