package com.geet.concept_location.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.DocumentExtractor;
import com.geet.concept_location.indexing_vsm.VectorDocument;
import com.geet.concept_location.indexing_vsm.VectorSpaceModel;

public class JavaFileWriter {
	
	private void writeFile() {
		// TODO Auto-generated method stub
		DocumentExtractor documentExtractor = new DocumentExtractor(new File("src/com/geet/concept_location/corpus_creation/DocumentExtractor.java"));
		List<Document> documents = documentExtractor.getAllDocuments();
		List<VectorDocument> vectorDocuments = new ArrayList<VectorDocument>();
		
		for (Document document : documents) {
			vectorDocuments.add(new VectorDocument(document));
		}
		try {
			FileWriter fileWriter = new FileWriter("src/com/geet/concept_location/corpus_creation/Hello.txt");
			VectorSpaceModel vectorSpaceModel = new VectorSpaceModel(vectorDocuments);
			fileWriter.write(vectorSpaceModel.TERM_DOCUMENT_MATRIX_TO_STRING());
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
