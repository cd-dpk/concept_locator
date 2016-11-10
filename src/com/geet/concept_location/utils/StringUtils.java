package com.geet.concept_location.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
public class StringUtils {
	public static char underScoreCase = '_';
	public static int upperCase_low = 65;
	public static char upperCase_high  = 96;
	public static char lowerCase_low  = 97;
	public static char lowerCase_high  = 97+25;
	public static String getFilePathName(String treePath){
		String filePath ="";
		List<String> uniqueNames = new ArrayList<String>();
		StringTokenizer stringTokenizer = new StringTokenizer(treePath,"[],\\/",false);
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
		String resultant ="";
		if (allLowerCaseCharacters(target)|| allUpperCaseCharacters(target)) {
			return target;
		}
		for (int i = 0; i < target.length(); i++) {
			char ch = target.charAt(i);
			if (ch == underScoreCase ||( ch >= upperCase_low && ch <= upperCase_high)) {
				resultant += " "+ch;
				continue;
			}
			resultant += ch;
		}
		if (resultant.equals(target)) {
			return target;
		}
		return resultant+" "+ target;
	}	
	/**
	 * @param target identifier
	 * @return resultant 
	 * separate the identifiers written in camel case or underscore case into multiple words
	 * also the identifier itself 
	 */
	public static String getIdentifierSeparationsWithCamelCase(String target){
		String resultant ="";
		StringTokenizer stringTokenizer = new StringTokenizer(splitCamelCase(target)," ",false);
		int i=0;
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			if (!token.equals("_")) {
				resultant += token+" ";
				i++;
			}
		}
		if (i<=1) {
			return target;
		}
		return resultant+" "+ target;
	}
	public static boolean allUpperCaseCharacters(String target){
		for (int i = 0; i < target.length(); i++) {
			char ch = target.charAt(i);
			if (ch<upperCase_low || ch> upperCase_high) {
				return false;
			}
		}
		return true;
	}
	public static boolean allLowerCaseCharacters(String target){
		for (int i = 0; i < target.length(); i++) {
			char ch = target.charAt(i);
			if (ch<lowerCase_low || ch> lowerCase_high) {
				return false;
			}
		}
		return true;
	}
	public static boolean isEmpty(String target){
		for (int i = 0; i < target.length(); i++) {
			char ch = target.charAt(i);
			if (ch != ' ' && ch != '\t') {
				return false;
			}
		}
		return true;
	}
	public static boolean isWord(String target){
		for (int i = 0; i < target.length(); i++) {
			char ch = target.charAt(i);
			if (!Character.isLetter(ch)) {
				return false;
			}
		}
		return true;	
	}
	public static String validateFolderPath(String path){
		String returnPath="";
		for (int i = 0; i < path.length(); i++) {
			if (path.charAt(i)==' ') {
				returnPath+= '/';
			}else{
				returnPath += path.charAt(i); 
			}
		}
		return returnPath;
	}
	/**
	 * split camel case
	 * @param str
	 * @return
	 */
    private static String splitCamelCase(String str) {
 	   return str.replaceAll(
 	      String.format("%s|%s|%s",
 	         "(?<=[A-Z])(?=[A-Z][a-z])",
 	         "(?<=[^A-Z])(?=[A-Z])",
 	         "(?<=[A-Za-z])(?=[^A-Za-z])"
 	      ),
 	      " "
 	   );
 	}
    public static void main(String[] args) {
		System.out.println(getIdentifierSeparationsWithCamelCase("Hello_Sir_Why"));
	}
}
