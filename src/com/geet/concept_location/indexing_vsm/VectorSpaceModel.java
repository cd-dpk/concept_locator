package com.geet.concept_location.indexing_vsm;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.geet.concept_location.corpus_creation.Document;
public class VectorSpaceModel {
	public List<Document> documents = new ArrayList<Document>();
	public VectorSpaceModel(List<Document> documentList) {
		this.documents = documentList;
		String[] terms = getTermsFromDocuments(documents);
		List<Term> uniqueTerms = new ArrayList<Term>();
		for (int i = 0; i < terms.length; i++) {
			uniqueTerms.add(new Term(terms[i]));
		}
		System.out.println("Loading...");
		System.out.println("Documents "+documents.size()+" terms "+terms.length);
		for (Document document : documents) {
			for (Term term : document.getTerms()) {
				System.out.println(term.toString());
				term.setDocumentFrequencyAndInverseDocumentFrequency(documents);
			}
		}
	}
	public double [][] getTERM_DOCUMENT_MATRIX(){
		String[] terms= getTERMS();
		System.out.println("Term Size " + getTERMS().length);
		String[] documents = getDOCS();
		System.out.println("Doc Size "+ getDOCS().length);
		double [][] TERM_DOCUMENT_MATRIX = new double[terms.length][documents.length];
		for (int i = 0; i < terms.length; i++) {
			for (int j = 0; j < documents.length; j++) {
				System.out.println(terms[i]);
				TERM_DOCUMENT_MATRIX[i][j]=this.documents.get(j).getTF_IDF(terms[i]);
			}
		}
		return TERM_DOCUMENT_MATRIX;
	}
	public String[] getTermsFromDocuments(List<Document> documents){
		Set<String> termSet = new HashSet<String>();
		for (Document document : documents) {
			for (Term term : document.getTerms()) {
				termSet.add(term.termString);
			}
		}
		return termSet.toArray(new String[termSet.size()]);
	}
	public String[] getTERMS(){
		Set<String> termSet = new HashSet<String>();
		for (Document document : documents) {
			for (Term term : document.getTerms()) {
				termSet.add(term.termString);
			}
		}
		return termSet.toArray(new String[termSet.size()]);
	}
	public String [] getDOCS(){
		Set<String> documentSet = new HashSet<String>();
		for (Document document : documents) {
				documentSet.add(document.getArticle());
		}
		return documentSet.toArray(new String[documentSet.size()]);
	}
	public String TERM_DOCUMENT_MATRIX_TO_STRING(){
		String text="";
		String[] terms= getTERMS();
		String[] documents = getDOCS();
		double [][] TERM_DOCUMENT_MATRIX = getTERM_DOCUMENT_MATRIX();
		text += terms.length+"X"+documents.length+"\n";
		for (int i = 0; i < terms.length; i++) {
			text += terms[i]+" ";
			for (int j = 0; j < documents.length; j++) {
				text += TERM_DOCUMENT_MATRIX[i][j]+",";
			}
			text += "\n";
		}
		return text;
	}
}
