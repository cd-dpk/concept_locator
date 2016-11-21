package com.geet.concept_location.io;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import com.geet.concept_location.corpus_creation.SimpleDocument;
import com.geet.concept_location.utils.StringUtils;

public class JavaFileReader {
	private String text="";
	
	public boolean openFile(File file){
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				text += scanner.nextLine()+"\n";
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public String getText(){
		return text;
	}
	public String getText(SimpleDocument simpleDocument){
		int start = simpleDocument.getStartPosition().getLine();
		int end = simpleDocument.getEndPosition().getLine();
		String text ="";
		try {
			Scanner scanner = new Scanner(new File(StringUtils.getFilePathAmongOS(simpleDocument.getDocInJavaFile())));
			int lineNumber = 1;
			while (scanner.hasNext()) {
				String token = scanner.nextLine();
				if (lineNumber >= start  && lineNumber <= end) {
					text += token+"\n";
				}
				lineNumber++;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
}
