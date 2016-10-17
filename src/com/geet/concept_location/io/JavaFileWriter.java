package com.geet.concept_location.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.geet.concept_location.corpus_creation.Document;
import com.geet.concept_location.corpus_creation.DocumentExtractor;
import com.geet.concept_location.indexing.Term;

public class JavaFileWriter {
	
	private void writeFile() {
		// TODO Auto-generated method stub
		DocumentExtractor documentExtractor = new DocumentExtractor(new File("src/com/geet/concept_location/corpus_creation/DocumentExtractor.java"));
		List<Document> documents = documentExtractor.getAllDocuments();
		try {
			FileWriter fileWriter = new FileWriter("src/com/geet/concept_location/corpus_creation/Hello.txt");
			for (Document document : documents) {
				fileWriter.write(document.toIndentity());
				fileWriter.write("\n");
				for (Term term : document.getTerms()) {
					term.setDocumentFrequencyAndInverseDocumentFrequency(documents);
					fileWriter.write(term.toString());
					fileWriter.write("\n");
				}				
				fileWriter.write("\n");
			}
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
