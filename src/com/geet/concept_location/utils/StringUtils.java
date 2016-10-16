package com.geet.concept_location.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtils {

	public static char underScoreCase = '_';
	public static int upperCase_low = 65;
	public static char upperCase_high  = 96;
	
	
	public static String getFilePathName(String treePath){
		String filePath ="";
		List<String> uniqueNames = new ArrayList<String>();
		StringTokenizer stringTokenizer = new StringTokenizer(treePath,"[],\\ /",false);
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			if (!hasStringInList(token, uniqueNames)) {
				filePath += token;
				filePath +="/";
				uniqueNames.add(token);
			}
		}	
		return filePath;
	}
	
	public static boolean hasStringInList(String string,List<String>list){
		for (String str : list) {
			if (str.equals(string)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasStringInList(String string,String[] list){
		for (String str : list) {
			if (str.equals(string)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param target identifier
	 * @return resultant 
	 * separate the identifiers written in camel case, Pascal case, or underscore case into multiple words
	 * also the identifier itself 
	 */
	public static String getIdentifierSeparations(String target){
		String resultant = target+" ";
		for (int i = 0; i < target.length(); i++) {
			char ch = target.charAt(i);
			if (ch == underScoreCase ||( ch > upperCase_low && ch < upperCase_high)) {
				resultant += ' '+ch;
			}
		}
		return resultant;
	}	
}
