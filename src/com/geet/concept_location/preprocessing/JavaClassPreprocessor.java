package com.geet.concept_location.preprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


import com.geet.concept_location.utils.StringUtils;

/**
 * remove all unnecessary whitespace line
 * @author Geet
 *
 */
public class JavaClassPreprocessor {

	public String getContentFromJavaFile(File file){
		String fileString = "";
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String token = scanner.nextLine();
				if (!StringUtils.isEmpty(token)) {
					fileString += token+"\n";
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileString;
	}

	/**
	 * return a processed java file 
	 * @return
	 */
	public File getProcessedJavaFile(File javaFile){
		String content = getContentFromJavaFile(javaFile);
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("temp.java");
			fileWriter.write(content);
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new File("temp.java"); 
	}
	
}
