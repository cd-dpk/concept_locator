package com.geet.concept_location.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JavaFileReader {

	String text="";
	
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
	public String getText(){
		return text;
	}
}
