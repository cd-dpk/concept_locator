package com.geet.concept_location.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.DocumentExtractor;
import com.geet.concept_location.corpus_creation.QueryDocument;
import com.geet.concept_location.indexing_vsm.Term;
import com.geet.concept_location.indexing_vsm.VectorDocument;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;

public class JavaFileWriter {
	
	private void writeFile() {
		
		VectorDocument queryVectorDocument = new VectorDocument(new QueryDocument("extract document"));
		DocumentExtractor documentExtractor = new DocumentExtractor(new File("src/com/geet/concept_location/corpus_creation/DocumentExtractor.java"));
		List<Document> documents = documentExtractor.getAllDocuments();
		try {
			FileWriter fileWriter = new FileWriter("src/com/geet/concept_location/corpus_creation/Hello.txt");
			VectorSpaceModel vectorSpaceModel = new VectorSpaceModel(documents);
			fileWriter.write(queryVectorDocument.article+"\n");
			for (VectorDocument document : vectorSpaceModel.vectorDocuments) {
				document.dotProduct = document.getDotProduct(queryVectorDocument);
			}
			Collections.sort(vectorSpaceModel.vectorDocuments);
			Collections.reverse(vectorSpaceModel.vectorDocuments);
			for (VectorDocument document : vectorSpaceModel.vectorDocuments) {
				document.dotProduct = document.getDotProduct(queryVectorDocument);
				fileWriter.write(document.dotProduct+"\n");
				fileWriter.write(document.getDocName()+"\n");
				fileWriter.write(document.getDocInJavaFile()+"\n");
				fileWriter.write(document.article+"\n");
			}
			//			fileWriter.write(vectorSpaceModel.TERM_DOCUMENT_MATRIX_TO_STRING());
			fileWriter.close();
			System.out.println("DOne");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new JavaFileWriter().writeFile();
	}
}
