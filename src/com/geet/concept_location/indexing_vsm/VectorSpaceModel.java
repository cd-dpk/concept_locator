package com.geet.concept_location.indexing_vsm;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.SimpleDocument;
public class VectorSpaceModel {
	public List<SimpleDocument> documents = new ArrayList<SimpleDocument>();
	public List<String> terms = new ArrayList<String>();
	public double [][]TERM_DOCUMENT_MATRIX;
	public double [] df;
	private int totalTerm=0;
	private int totalDocs=0;
	double [] newDocumentMatrix;
	List<Term> extensionTerms;
	// Rocchio Algorithm 
	private final static double alpha = 1.0, beta = 0.75, gyma = 0.25;
	
	// add relevance feedback
	public VectorSpaceModel(List<SimpleDocument> documentList) {
		documents = documentList;
		totalTerm = getTermS().size();
		totalDocs = getDocuments().size();
		terms = getTermS();
		df = new double[totalTerm];
		TERM_DOCUMENT_MATRIX = new double[totalTerm][totalDocs];
		df = new double [totalTerm];
		System.out.println("Terms "+totalTerm+" Documents "+totalDocs);
		System.out.println(terms.toString());
		setTERM_DOCUMENT_MATRIX(terms, documents);
		newDocumentMatrix = new double[terms.size()];
	}
	public void setTERM_DOCUMENT_MATRIX(List<String> terms,List<SimpleDocument>documents){
		/* dont calculate idf from terms rather compute it from matrix*/
		System.out.println("Term Document Matrix getting...");
		for (int i = 0; i < totalTerm; i++) {
			// df
			df[i] = 0;
			for (int j = 0; j < documents.size(); j++) {
				double tf = documents.get(j).getTF(terms.get(i).toString());
				if (tf !=0.0) {
						df[i]++;
				}
				TERM_DOCUMENT_MATRIX[i][j]= tf;
			}
			// idf
			if (df[i] == 0) {
				System.err.println("Something is going wrong");
				System.exit(i);
			}
		}
	}
	public List<Document> search(){
		List<Document>lsiDocuments = new ArrayList<Document>();	
		for (int i = 1; i < documents.size(); i++) {
				double dotProduct = 0.0;
				double scalarOne = 0.0;
				double scalarTwo = 0.0;
				for (int j = 0; j < terms.size(); j++) {
					dotProduct += TERM_DOCUMENT_MATRIX[j][0] *(1 + (Math.log10((double)(documents.size())/(df[j]))/Math.log10(2.0)))* TERM_DOCUMENT_MATRIX[j][i] * (1 + (Math.log10((double)(documents.size())/(df[j]))/Math.log10(2.0)));
					scalarOne += TERM_DOCUMENT_MATRIX[j][0] * (1 + (Math.log10((double)(documents.size())/(df[j]))/Math.log10(2.0)))* TERM_DOCUMENT_MATRIX[j][0] * (1 + (Math.log10((double)(documents.size())/(df[j]))/Math.log10(2.0)));
					scalarTwo += TERM_DOCUMENT_MATRIX[j][i] * (1 + (Math.log10((double)(documents.size())/(df[j]))/Math.log10(2.0)))* TERM_DOCUMENT_MATRIX[j][i]*(1 + (Math.log10((double)(documents.size())/(df[j]))/Math.log10(2.0)));
				}
				documents.get(i).score = (dotProduct)/(Math.sqrt(scalarOne)*Math.sqrt(scalarTwo));
				if (documents.get(i).score > 0) {
					lsiDocuments.add((Document) documents.get(i));
				}
		}
		return lsiDocuments;
	}
	/**
	 * search with a new Document
	 * @param value
	 * @return
	 */
	public List<Document> search(SimpleDocument newSimpleDocument){
		newDocumentMatrix = new double[terms.size()];
		extensionTerms = new ArrayList<Term>();
		for (Term term : newSimpleDocument.getTerms()) {
			int flag =0;
			for (int i = 0; i < newDocumentMatrix.length; i++) {
				if (term.termString.equals(terms.get(i))) {
					newDocumentMatrix[i] = term.termFrequency;
					flag = 1;
					break;
				}
				newDocumentMatrix[i] = 0;
			}
			if (flag == 0) {
				System.out.println("Not in term_document_matrix"+term);
				extensionTerms.add(term);
			}
		}
		return getRankedDocuments();
	}
	
	public List<Document> searchWithRelevanceFeedback(List<Integer> relDocuments, List<Integer> irrelDocuments){
		updateNewDocumentMatrixWithRelevantDocuments(relDocuments);
		updateNewDocumentMatrixWithIrrelevantDocuments(irrelDocuments);
		return getRankedDocuments();
	}
	
	public List<Document> getRankedDocuments(){
		int totalDocument = documents.size() + 1 ;
		List<Document>lsiDocuments = new ArrayList<Document>();	
		for (int i = 0; i < documents.size(); i++) {
				double dotProduct = 0.0;
				double scalarOne = 0.0;
				double scalarTwo = 0.0;
				for (int j = 0; j < terms.size(); j++) {
					double tempDf = df[j];
					if (newDocumentMatrix[j] != 0) {
						tempDf =  1 + (Math.log10((double)(totalDocument)/(tempDf+1))/Math.log10(2.0));
					}
					else{
						tempDf =  1 + (Math.log10((double)(totalDocument)/(tempDf))/Math.log10(2.0));
					}
					dotProduct +=( newDocumentMatrix[j] * tempDf )* (TERM_DOCUMENT_MATRIX[j][i]* tempDf);
					scalarOne += (newDocumentMatrix[j] * tempDf)* (newDocumentMatrix[j] * tempDf);
					scalarTwo += (TERM_DOCUMENT_MATRIX[j][i] * tempDf)* (TERM_DOCUMENT_MATRIX[j][i]* tempDf);
				}
				for (int j = 0; j < extensionTerms.size(); j++) {
					System.out.println(j);
					double tempDf =  1 + (Math.log10((double)(totalDocument)/(1))/Math.log10(2.0));
					scalarOne += (extensionTerms.get(j).termFrequency * tempDf)* (extensionTerms.get(j).termFrequency* tempDf);
					System.out.println(scalarOne);
				}
				documents.get(i).score = (dotProduct)/(Math.sqrt(scalarOne)*Math.sqrt(scalarTwo));
				System.out.println(documents.get(i).score);;
			//	if (documents.get(i).score > 0) {
					lsiDocuments.add((Document) documents.get(i));
			//	}
		}
		return lsiDocuments;
	}
	
	// update with relevant document
	private void updateNewDocumentMatrixWithRelevantDocuments(List<Integer>relDocuments){
		if (relDocuments.size() > 1) {
			double offset = beta / relDocuments.size();
			for (int i = 0; i < newDocumentMatrix.length ; i++) {
				double weight = 0;
				for (int j = 0; j < relDocuments.size(); j++) {
					weight += TERM_DOCUMENT_MATRIX[i][relDocuments.get(j)];
				}
				newDocumentMatrix[i] += VectorSpaceModel.validateTermWeight(weight * offset);
			}
		}
	}
	// update with irrelevant document
	private void updateNewDocumentMatrixWithIrrelevantDocuments(List<Integer>irrelDocuments){
		if (irrelDocuments.size() > 1) {
			double offset = gyma / irrelDocuments.size();
			for (int i = 0; i < newDocumentMatrix.length ; i++) {
				double weight = 0;
				for (int j = 0; j < irrelDocuments.size(); j++) {
					weight += TERM_DOCUMENT_MATRIX[i][irrelDocuments.get(j)];
				}
				newDocumentMatrix[i] -= VectorSpaceModel.validateTermWeight(weight * offset);
			}
		}
	}
	
	/**
	 * validate term weigh
	 */
	private static double validateTermWeight(double termWeight){
		if (termWeight < 0) {
			return 0;
		}
		return termWeight;
	}
	public double [][] getTERM_DOCUMENT_MATRIX(){
		/* dont calculate idf from terms rather compute it from matrix*/
		double [] df = new double[totalTerm];
		double [][] TERM_DOCUMENT_MATRIX = new double[totalTerm][totalDocs];
		for (int i = 0; i < totalTerm; i++) {
			// df
			df[i] = 0;
			for (int j = 0; j < documents.size(); j++) {
				double tf = this.documents.get(j).getTF(terms.get(i).toString());
				if (tf !=0.0) {
						df[i]++;
				}
				TERM_DOCUMENT_MATRIX[i][j]= tf;
			}
			// idf
			df[i] = 1 + Math.log10((double)documents.size()/df[i]);
			for (int j = 0; j < documents.size(); j++) {
				TERM_DOCUMENT_MATRIX[i][j]= TERM_DOCUMENT_MATRIX[i][j] * df[i];
				System.out.print(TERM_DOCUMENT_MATRIX[i][j]+" ");
			}
			System.out.println();
		}
		return TERM_DOCUMENT_MATRIX;
	}
	/**
	 * @deprecated
	 * @param documents
	 * @return
	 */
	public String[] getTermsFromDocuments(List<Document> documents){
		Set<String> termSet = new HashSet<String>();
		for (SimpleDocument document : documents) {
				Set<String> candidateSet = new HashSet<String>(document.getTermsInString());
				termSet.addAll(candidateSet);
		}
		return termSet.toArray(new String[termSet.size()]);
	}
	public List<String> getTermS(){
		Set<String> termSet = new HashSet<String>();
		for (SimpleDocument document : documents) {
				Set<String> candidateSet = new HashSet<String>(document.getTermsInString());
				termSet.addAll(candidateSet);
		}
		return new ArrayList<String>(termSet);
	}
	public String[] getTERMS(){
		Set<String> termSet = new HashSet<String>();
		for (SimpleDocument document : documents) {
				Set<String> candidateSet = new HashSet<String>(document.getTermsInString());
				termSet.addAll(candidateSet);
		}
		return termSet.toArray(new String[termSet.size()]);
	}
	public String [] getDOCS(){
		Set<String> documentSet = new HashSet<String>();
		for (SimpleDocument document : documents) {
				documentSet.add(document.getArticle());
		}
		return documentSet.toArray(new String[documentSet.size()]);
	}
	public List<SimpleDocument> getDocuments(){
		return documents;
	}
	/**
	 * 
	 * @return
	 */
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
