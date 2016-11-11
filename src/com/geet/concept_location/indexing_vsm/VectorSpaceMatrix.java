package com.geet.concept_location.indexing_vsm;

import java.io.Serializable;
import java.util.List;
import com.geet.concept_location.corpus_creation.SimpleDocument;

public class VectorSpaceMatrix implements Serializable{
	public List<SimpleDocument> documents;
	public List<String> terms;
	public double [][]TERM_DOCUMENT_MATRIX;
	public double [] df;
	private double MAX = 2;
	private double a = 1.0;
	private double b = 2.0;
	private double MIN = 0;
	
	public VectorSpaceMatrix(List<SimpleDocument> documents,
			List<String> terms, double[][] tERM_DOCUMENT_MATRIX, double[] df) {
		super();
		this.documents = documents;
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