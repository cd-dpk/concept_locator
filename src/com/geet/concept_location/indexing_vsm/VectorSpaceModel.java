package com.geet.concept_location.indexing_vsm;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.SimpleDocument;

public class VectorSpaceModel implements Serializable{
	
	private List<SimpleDocument> documents = new ArrayList<SimpleDocument>();
	private List<String> terms = new ArrayList<String>();
	private double [][]TERM_DOCUMENT_MATRIX;
	private double [] df;
	private int totalTerm=0;
	private int totalDocs=0;
	private double MINIMUM_SCORE = 0.0;
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
		setTERM_DOCUMENT_MATRIX(terms, documents);
	}
	public VectorSpaceModel(VectorSpaceMatrix vectorSpaceMatrix){
		for (int i = 0; i < vectorSpaceMatrix.getSimpleDocuments().size(); i++) {
			SimpleDocument simpleDocument = new SimpleDocument.Builder()
					.docInJavaFile(
							vectorSpaceMatrix.getSimpleDocuments().get(i)
									.getDocInJavaFile())
					.docName(
							vectorSpaceMatrix.getSimpleDocuments().get(i)
									.getDocName())
					.startPosition(
							vectorSpaceMatrix.getSimpleDocuments().get(i)
									.getStartPosition())
					.endPosition(
							vectorSpaceMatrix.getSimpleDocuments().get(i)
									.getEndPosition())
					.score(vectorSpaceMatrix.getSimpleDocuments().get(i).getScore())
					.build();
			documents.add(simpleDocument);
		}
		terms = vectorSpaceMatrix.getTerms();
		TERM_DOCUMENT_MATRIX = vectorSpaceMatrix.getTERM_DOCUMENT_MATRIX();
		df = vectorSpaceMatrix.getDf();
	}

	public VectorSpaceMatrix getVectorSpaceMatrix(){
		List<SimpleDocument> simpleDocuments = new ArrayList<SimpleDocument>();
		for (int i = 0; i < documents.size(); i++) {
			SimpleDocument simpleDocument = new SimpleDocument.Builder()
			.docInJavaFile(
					documents.get(i)
							.getDocInJavaFile())
			.docName(
					documents.get(i)
							.getDocName())
			.startPosition(
					documents.get(i)
							.getStartPosition())
			.endPosition(
					documents.get(i)
							.getEndPosition())
			.score(documents.get(i).getScore())
			.build();
			simpleDocuments.add(simpleDocument);
		}
		return new VectorSpaceMatrix(simpleDocuments,terms,TERM_DOCUMENT_MATRIX, df);
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
				documents.get(i).setScore( (dotProduct)/(Math.sqrt(scalarOne)*Math.sqrt(scalarTwo)));
				if (documents.get(i).getScore() > 0) {
					lsiDocuments.add((Document) documents.get(i));
				}
		}
		return lsiDocuments;
	}
	public List<SimpleDocument> searchWithQueryVector(Query query){
		int totalDocument = documents.size() + 1 ;
		List<SimpleDocument>lsiDocuments = new ArrayList<SimpleDocument>();	
		for (int i = 0; i < documents.size(); i++) {
				double dotProduct = 0.0;
				double scalarOne = 0.0;
				double scalarTwo = 0.0;
				for (int j = 0; j < terms.size(); j++) {
					double tempDf = df[j];
					if (query.getVectorInVectorSpaceModel()[j] != 0) {
						tempDf =  1 + ((Math.log10((double)(totalDocument)/(tempDf+1)))/Math.log10(2.0));
					}
					else{
						tempDf =  1 + ((Math.log10((double)(totalDocument)/(tempDf)))/Math.log10(2.0));
					}
					dotProduct +=( query.getVectorInVectorSpaceModel()[j] * tempDf )* (TERM_DOCUMENT_MATRIX[j][i]* tempDf);
					scalarOne += (query.getVectorInVectorSpaceModel()[j] * tempDf)* (query.getVectorInVectorSpaceModel()[j] * tempDf);
					scalarTwo += (TERM_DOCUMENT_MATRIX[j][i] * tempDf)* (TERM_DOCUMENT_MATRIX[j][i]* tempDf);
				}
				/*for (int j = 0; j < query.vectorInExtendedVectorSpaceModel.length; j++) {
				//	System.out.println(j);
					double tempDf =  1 + (Math.log10((double)(totalDocument)/(1))/Math.log10(2.0));
					scalarOne += (query.vectorInExtendedVectorSpaceModel[j] * tempDf)* (query.vectorInExtendedVectorSpaceModel[j]* tempDf);
				//	System.out.println(scalarOne);
				}*/
				if (scalarOne != 0 && scalarTwo != 0) {
					documents.get(i).setScore((dotProduct)/(Math.sqrt(scalarOne)*Math.sqrt(scalarTwo)));
					System.out.println(documents.get(i).getScore());
					if (documents.get(i).getScore() > MINIMUM_SCORE) {
						lsiDocuments.add(documents.get(i));
					}
				}
		}
		Collections.sort(lsiDocuments);
		Collections.reverse(lsiDocuments);
		return lsiDocuments;
	}
	
	public Query getQuery(SimpleDocument simpleDocument){
		Query query = new Query();
		System.out.println(simpleDocument.getTerms().toString());
		query.setVectorInVectorSpaceModel(new double[terms.size()]); 
		List<Term> extensionTerms = new ArrayList<Term>();
		for (Term term : simpleDocument.getTerms()) {
			int flag =0;
			for (int i = 0; i < query.getVectorInVectorSpaceModel().length; i++) {
				if (term.isSameInIR(new Term(terms.get(i)))) {
					query.getVectorInVectorSpaceModel()[i] = term.getTermFrequency();
					flag = 1;
					break;
				}
			}
			if (flag == 0) {
				System.out.println("Not in term_document_matrix"+term);
				extensionTerms.add(term);
			}
		}
		query.setVectorInExtendedVectorSpaceModel(new double[extensionTerms.size()]);
		for (int i = 0; i < extensionTerms.size(); i++) {
			query.getVectorInExtendedVectorSpaceModel()[i] = extensionTerms.get(i).getTermFrequency();
		}
		return query;
	}
	/**
	 * search with a new Document
	 * @param value
	 * @return
	 */
	public List<SimpleDocument> search(SimpleDocument newSimpleDocument){
		double [] newDocumentMatrix = new double[terms.size()];
		//query.vectorInVectorSpaceModel = new double[terms.size()]; 
		List<Term> extensionTerms = new ArrayList<Term>();
		for (Term term : newSimpleDocument.getTerms()) {
			int flag =0;
			for (int i = 0; i < newDocumentMatrix.length; i++) {
				if (term.isSameInIR(new Term(terms.get(i)))) {
					newDocumentMatrix[i] = term.getTermFrequency();
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
		List<SimpleDocument>lsiDocuments = new ArrayList<SimpleDocument>();	
		for (int i = 0; i < documents.size(); i++) {
//			SimpleDocument simpleDocument =  documents.get(i);
//			System.out.println(i+":"+simpleDocument.docInJavaFile+" is computing...");
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
				//	System.out.println(j);
					double tempDf =  1 + (Math.log10((double)(totalDocument)/(1))/Math.log10(2.0));
					scalarOne += (extensionTerms.get(j).getTermFrequency() * tempDf)* (extensionTerms.get(j).getTermFrequency() * tempDf);
				//	System.out.println(scalarOne);
				}
				documents.get(i).setScore((dotProduct)/(Math.sqrt(scalarOne)*Math.sqrt(scalarTwo)));
		//		System.out.println(documents.get(i).score);
		//		if (documents.get(i).score > MINIMUM_SCORE) {
					lsiDocuments.add(documents.get(i));
		//		}
		}
		Collections.sort(lsiDocuments);
		Collections.reverse(lsiDocuments);
		return lsiDocuments;
	}


/*	public List<Document> research(List<Document> relDocuments, List<Document>irrelDocuments){
		updateNewDocumentMatrixWithRelevantDocuments(addAllDocuments(relDocuments));
		updateNewDocumentMatrixWithIrrelevantDocuments(addAllDocuments(irrelDocuments));
		return searchDocuments();
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
*/	/*public List<Document> searchDocuments(){
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
	*/// update with relevant document
	/*private void updateNewDocumentMatrixWithRelevantDocuments(List<Integer>relDocuments){
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
	*//**
	 * validate term weight
	 */
	private static double validateTermWeight(double termWeight){
		if (termWeight < 0) {
			return 0;
		}
		return termWeight;
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
	public List<String> getTerms() {
		return terms;
	}
	public void setTerms(List<String> terms) {
		this.terms = terms;
	}
	public double[] getDf() {
		return df;
	}
	public void setDf(double[] df) {
		this.df = df;
	}
	public int getTotalTerm() {
		return totalTerm;
	}
	public void setTotalTerm(int totalTerm) {
		this.totalTerm = totalTerm;
	}
	public int getTotalDocs() {
		return totalDocs;
	}
	public void setTotalDocs(int totalDocs) {
		this.totalDocs = totalDocs;
	}
	public double getMINIMUM_SCORE() {
		return MINIMUM_SCORE;
	}
	public void setMINIMUM_SCORE(double mINIMUM_SCORE) {
		MINIMUM_SCORE = mINIMUM_SCORE;
	}
	public double getMAX() {
		return MAX;
	}
	public void setMAX(double mAX) {
		MAX = mAX;
	}
	public double getA() {
		return a;
	}
	public void setA(double a) {
		this.a = a;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public double getMIN() {
		return MIN;
	}
	public void setMIN(double mIN) {
		MIN = mIN;
	}
	public void setDocuments(List<SimpleDocument> documents) {
		this.documents = documents;
	}
	public void setTERM_DOCUMENT_MATRIX(double[][] tERM_DOCUMENT_MATRIX) {
		TERM_DOCUMENT_MATRIX = tERM_DOCUMENT_MATRIX;
	}	
	
	
}
