package com.geet.concept_location.indexing_lsi;
public class LsiTerm {
	public String term;
	public Vector vector;
	public double score= -1;
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public LsiTerm(String term, Vector vector) {
		setTerm(term);
		setVector(vector);
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public Vector getVector() {
		return vector;
	}
	public void setVector(Vector vector) {
		this.vector = vector;
	}
	public boolean isSame(LsiTerm lsiTerm) {
		if (term.equals(lsiTerm.term)) {
			return true;
		}
		return false;
	}
	public boolean isSame(String stringTerm) {
		if (term.equals(stringTerm)) {
			return true;
		}
		return false;
	}
}
