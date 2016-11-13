package com.geet.concept_location.indexing_vsm;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.SimpleDocument;

public class VectorSpaceModel implements Serializable{
	public List<SimpleDocument> documents = new ArrayList<SimpleDocument>();
	public List<String> terms = new ArrayList<String>();
	public double [][]TERM_DOCUMENT_MATRIX;
	public double [] df;
	private int totalTerm=0;
	private int totalDocs=0;
	private double MINIMUM_SCORE = 0.2;
	private double MAX = 2;
	private double a = 1.0;
	private double b = 2.0;
	private double MIN = 0;
	
	public VectorSpaceModel(List<SimpleDocument> documentList) {
		documents = documentList;
		terms = getTermS();
		totalTerm = terms.size();
		totalDocs = documents.size();
		df = new double[totalTerm];
		TERM_DOCUMENT_MATRIX = new double[totalTerm][totalDocs];
		df = new double [totalTerm];
		System.out.println("Terms "+totalTerm+" Documents "+totalDocs);
		//System.exit(0);
		//writeTermsIntoFile();
		//System.exit(0);
		setTERM_DOCUMENT_MATRIX(terms, documents);
	}
	
	public VectorSpaceModel(VectorSpaceMatrix vectorSpaceMatrix){
		List<String> documents = new ArrayList<String>();
		for (int i = 0; i < this.documents.size(); i++) {
			documents.add(this.documents.get(i).docInJavaFile);
		}
		documents = vectorSpaceMatrix.documents;
		terms = vectorSpaceMatrix.terms;
		TERM_DOCUMENT_MATRIX = vectorSpaceMatrix.TERM_DOCUMENT_MATRIX;
		df = vectorSpaceMatrix.df;
	}

	public VectorSpaceMatrix getVectorSpaceMatrix(){
		List<String> documents = new ArrayList<String>();
		for (int i = 0; i < this.documents.size(); i++) {
			System.out.println();
			documents.add(this.documents.get(i).docInJavaFile);
		}
		return new VectorSpaceMatrix(documents,terms,TERM_DOCUMENT_MATRIX, df);
	}
	
	public void writeTermsIntoFile(){
		try {
			FileWriter fileWriter = new FileWriter(new File("Term.txt"));
			for (String term : terms) {
				fileWriter.write(term + "\n");
			}
			fileWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setTERM_DOCUMENT_MATRIX(List<String> terms,List<SimpleDocument>documents){
		/* dont calculate idf from terms rather compute it from matrix*/
		System.out.println("Term Document Matrix getting...");
		for (int i = 0; i < totalTerm; i++) {
			// df
			df[i] = 0;
			System.out.print(i+":");
			for (int j = 0; j < totalDocs; j++) {
				double tf = documents.get(j).getTF(terms.get(i).toString());
				if (tf !=0.0) {
						df[i]++;
				}
				TERM_DOCUMENT_MATRIX[i][j]= tf;
				System.out.print(TERM_DOCUMENT_MATRIX[i][j]+" ");
			}
			// idf
			if (df[i] == 0) {
				System.err.println("Something is going wrong");
				System.exit(i);
			}
			System.out.println();
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
		double [] newDocumentMatrix = new double[terms.size()];
		List<Term> extensionTerms = new ArrayList<Term>();
		for (Term term : newSimpleDocument.getTerms()) {
			int flag =0;
			for (int i = 0; i < newDocumentMatrix.length; i++) {
				if (term.isSameInIR(new Term(terms.get(i)))) {
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
		int totalDocument = documents.size() + 1 ;
		List<Document>lsiDocuments = new ArrayList<Document>();	
		for (int i = 0; i < documents.size(); i++) {
			Document document = (Document) documents.get(i);
			System.out.println(i+":"+document.getDocInJavaFile()+" is computing...");
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
				System.out.println(documents.get(i).score);
				if (documents.get(i).score > MINIMUM_SCORE) {
					lsiDocuments.add((Document) documents.get(i));
				}
		}
		return lsiDocuments;
	}
	/* for normalization*/
	private double getNormalizedValue(double value){
		double normalizedValue = (value - MIN)/(MAX-MIN);
		normalizedValue = normalizedValue * (b-a);
		normalizedValue = normalizedValue + a;
		return normalizedValue;
	}
	/*
	 * for vsm
	 */
	public void setTERM_DOCUMENT_MATRIX(){
		/**
		 * get all terms 
		 * for query vector, query[terms.length][2], 
		 * column 1 <- tf in query
		 * column 2 <- df in all documents
		 * 
		 * compute score of all documents with query vector
		 * ( query[i][0] * ( 1+ log 10((1+doucuments.size())/query[i][1])) ) * (TERM_DOCUMENT_MATRIX[j][i]( 1+ log 10((1+doucuments.size())/query[i][1])))
		 */
		/* dont calculate idf from terms rather compute it from matrix*/
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
		}
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
