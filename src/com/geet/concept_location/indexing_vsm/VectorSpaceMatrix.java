package com.geet.concept_location.indexing_vsm;

import java.io.Serializable;
import java.util.List;

import com.geet.concept_location.corpus_creation.Position;
import com.geet.concept_location.corpus_creation.SimpleDocument;

public class VectorSpaceMatrix implements Serializable{
	private List<SimpleDocument> simpleDocuments;
	private List<String> terms;
	private double [][]TERM_DOCUMENT_MATRIX;
	private double [] df;
	
	public List<SimpleDocument> getSimpleDocuments() {
		return simpleDocuments;
	}
	public void setSimpleDocuments(List<SimpleDocument> simpleDocuments) {
		this.simpleDocuments = simpleDocuments;
	}
	public List<String> getTerms() {
		return terms;
	}
	public void setTerms(List<String> terms) {
		this.terms = terms;
	}
	public double[][] getTERM_DOCUMENT_MATRIX() {
		return TERM_DOCUMENT_MATRIX;
	}
	public void setTERM_DOCUMENT_MATRIX(double[][] tERM_DOCUMENT_MATRIX) {
		TERM_DOCUMENT_MATRIX = tERM_DOCUMENT_MATRIX;
	}
	public double[] getDf() {
		return df;
	}
	public void setDf(double[] df) {
		this.df = df;
	}
	public VectorSpaceMatrix(List<SimpleDocument> simpleDocuments,
			List<String> terms, double[][] tERM_DOCUMENT_MATRIX, double[] df) {
		super();
		this.simpleDocuments = simpleDocuments;
		this.terms = terms;
		TERM_DOCUMENT_MATRIX = tERM_DOCUMENT_MATRIX;
		this.df = df;
	}
	@Override
	public String toString() {
		String toString ="";
		for (int i = 0; i < TERM_DOCUMENT_MATRIX.length; i++) {
			for (int j = 0; j < TERM_DOCUMENT_MATRIX[i].length; j++) {
				toString +=(TERM_DOCUMENT_MATRIX[i][j]+" ");
			}
			toString+="\n";
		}
		return toString;
	}
	
}