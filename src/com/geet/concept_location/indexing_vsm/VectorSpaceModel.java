package com.geet.concept_location.indexing_vsm;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.SimpleDocument;

public class VectorSpaceModel {
	
	public List<Document> documents = new ArrayList<Document>();
	public List<String> terms = new ArrayList<String>();
	public double [][]TERM_DOCUMENT_MATRIX;
	public double [] df;
	private int totalTerm=0;
	private int totalDocs=0;

	Query query;
	List<Integer> relDocuments= new ArrayList<Integer>(), irrelDocuments = new ArrayList<Integer>();

	//Rocchio Algorithm 
	private final static double alpha = 1.0, beta = 0.75, gyma = 0.25;
	
	public VectorSpaceModel(List<Document> documentList) {
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
		query = new Query();
	}
	public void setTERM_DOCUMENT_MATRIX(List<String> terms,List<Document>documents){
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
	
	/**
	 * search with a new Document
	 * @param value
	 * @return
	 */
	public List<Document> search(SimpleDocument newSimpleDocument){
		query.vectorInVectorSpaceModel = new double[terms.size()];
		List<Term> extensionTerms = new ArrayList<Term>();
		for (Term term : newSimpleDocument.getTerms()) {
			int flag =0;
			for (int i = 0; i < query.vectorInVectorSpaceModel.length; i++) {
				if (term.termString.equals(terms.get(i))) {
					query.vectorInVectorSpaceModel[i] = term.termFrequency;
					flag = 1;
					break;
				}
				query.vectorInVectorSpaceModel[i] = 0;
			}
			if (flag == 0) {
				System.out.println("Not in term_document_matrix"+term);
				extensionTerms.add(term);
			}
		}
		query.vectorInExtendedVectorSpaceModel = new double [extensionTerms.size()];
		for (int i = 0; i < extensionTerms.size(); i++) {
			query.vectorInExtendedVectorSpaceModel[i] = extensionTerms.get(i).termFrequency;
		}
		return searchDocuments();
	}
	public List<Document> research(List<Document> relDocuments, List<Document>irrelDocuments){
		updateNewDocumentMatrixWithRelevantDocuments(addAllDocuments(relDocuments));
		updateNewDocumentMatrixWithIrrelevantDocuments(addAllDocuments(irrelDocuments));
		return searchDocuments();
	}
	
	public List<Document> searchDocuments(){
		int totalDocument = documents.size() + 1;
		List<Document>rankedDocuments = new ArrayList<Document>();	
		for (int i = 0; i < documents.size(); i++) {
				double dotProduct = 0.0;
				double scalarOne = 0.0;
				double scalarTwo = 0.0;
				for (int j = 0; j < terms.size(); j++) {
					double tempDf = df[j];
					if (query.vectorInVectorSpaceModel[j] != 0) {
						tempDf =  1 + (Math.log10((double)(totalDocument)/(tempDf+1))/Math.log10(2.0));
					}
					else{
						tempDf =  1 + (Math.log10((double)(totalDocument)/(tempDf))/Math.log10(2.0));
					}
					dotProduct +=( query.vectorInVectorSpaceModel[j] * tempDf )* (TERM_DOCUMENT_MATRIX[j][i]* tempDf);
					scalarOne += (query.vectorInVectorSpaceModel[j] * tempDf)* (query.vectorInVectorSpaceModel[j] * tempDf);
					scalarTwo += (TERM_DOCUMENT_MATRIX[j][i] * tempDf)* (TERM_DOCUMENT_MATRIX[j][i]* tempDf);
				}
				for (int j = 0; j < query.vectorInExtendedVectorSpaceModel.length; j++) {
					System.out.println(j);
					double tempDf =  1 + (Math.log10((double)(totalDocument)/(1))/Math.log10(2.0));
					scalarOne += (query.vectorInExtendedVectorSpaceModel[j] * tempDf)* (query.vectorInExtendedVectorSpaceModel[j]* tempDf);
					System.out.println(scalarOne);
				}
				documents.get(i).score = (dotProduct)/(Math.sqrt(scalarOne)*Math.sqrt(scalarTwo));
				System.out.println(documents.get(i).score);;
			//	if (documents.get(i).score > 0) {
					rankedDocuments.add((Document) documents.get(i));
			//	}
		}
		return rankedDocuments;
	}
	
	// update with relevant document
	private void updateNewDocumentMatrixWithRelevantDocuments(List<Integer>relDocuments){
		if (relDocuments.size() > 1) {
			double offset = beta / relDocuments.size();
			for (int i = 0; i < query.vectorInVectorSpaceModel.length ; i++) {
				double weight = 0;
				for (int j = 0; j < relDocuments.size(); j++) {
					weight += TERM_DOCUMENT_MATRIX[i][relDocuments.get(j)];
				}
				query.vectorInVectorSpaceModel[i] += VectorSpaceModel.validateTermWeight(weight * offset);
			}
		}
	}
	// update with irrelevant document
	private void updateNewDocumentMatrixWithIrrelevantDocuments(List<Integer>irrelDocuments){
		if (irrelDocuments.size() > 1) {
			double offset = gyma / irrelDocuments.size();
			for (int i = 0; i < query.vectorInVectorSpaceModel.length ; i++) {
				double weight = 0;
				for (int j = 0; j < irrelDocuments.size(); j++) {
					weight += TERM_DOCUMENT_MATRIX[i][irrelDocuments.get(j)];
				}
				query.vectorInVectorSpaceModel[i] -= VectorSpaceModel.validateTermWeight(weight * offset);
			}
		}
	}
	
	/**
	 * validate term weight
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
	public List<Document> getDocuments(){
		return documents;
	}
	
	public List<Integer>  addAllDocuments(List<Document> docs){
		List<Integer> docsIndex = new ArrayList<Integer>();
		for (Document doc : docs) {
			for (int i = 0; i < documents.size(); i++) {
				if (doc.isSameDocument(documents.get(i))) {
					docsIndex.add(i);
					break;
				}
			}
		}
		return docsIndex;
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
