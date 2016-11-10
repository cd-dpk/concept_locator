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
					fileString += (token)+"\n";
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
	public boolean processJavaFile(File javaFile){
		String content = getContentFromJavaFile(javaFile);
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(javaFile);
			fileWriter.write(content);
			fileWriter.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	private static String exceptionHandle(String target){
		String ret="";
		// if there any space among a element and a comment
		for (int i = 0; i < target.length(); i++) {
			char ch = target.charAt(i);
			if (i+1<target.length()) {
				char next = target.charAt(i+1);
				if (ch==' ' && next =='/') {
					continue;
				}
			}
			ret += ch;
		}
		return ret;
	}
	public static void main(String[] args) {
		System.out.println(exceptionHandle(("int/*Hello*/ i,/*Hello*/ j=10,k;")));
	}
}