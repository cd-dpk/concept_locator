package com.geet.concept_location.utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
public class StringUtils {
	private static char underScoreCase = '_';
	private static int upperCase_low = 65;
	private static char upperCase_high  = 96;
	private static char lowerCase_low  = 97;
	private static char lowerCase_high  = 97+25;
	
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
	public static String getFilePathNameNew(String treePath){
		String filePath ="";
		StringTokenizer stringTokenizer = new StringTokenizer(treePath,"[], ",false);
		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			System.out.print(token+"->");
			if (token.startsWith(filePath)) {
				filePath = token;
			}
			else{
				filePath +=File.separatorChar+token;
			}
			System.out.println(filePath);
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
	
	/**
	 * @param target identifier
	 * @return resultant 
	 * separate the identifiers written in camel case or underscore case into multiple words
	 * also the identifier itself 
	 */
	public static String getIdentifierSeparationsWithCamelCaseOnlyToken(String target){
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
		return resultant;
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
    	System.out.println(File.separatorChar);
    	System.out.println(StringUtils.getFilePathAmongOS("UltimateCalculator-master\\src\\com\\minhasKamal\\ultimateCalculator\\calculators\\simpleCalculator\\SimpleCalculatorOperation.java"));
    	//    	System.out.println(uderscoreDeletedString("_hel_lo__"));
    }
    public static boolean isConstant(String target){
    	if (target.startsWith("0x")) {
			return true;
		}
    	for (int i = 0; i < 10; i++) {
        	if (target.startsWith(i+"") ) {
    			return true;
    		}			
		}
    	return false;
    }
    public static String uderscoreDeletedString(String target){
    	String retString ="";
    	if (target.startsWith("_") && target.endsWith("_")) {
    		for (int i = 1; i < target.length()-1; i++) {
				retString+= target.charAt(i);
			}
    		return retString;
    	}
    	if (target.startsWith("_")) {
			for (int i = 1; i < target.length(); i++) {
				retString+= target.charAt(i);
			}
		}
    	if (target.endsWith("_")) {
    		for (int i = 0; i < target.length()-1; i++) {
				retString+= target.charAt(i);
			}
		}
    	return retString;
    }
    public static String getFilePathAmongOS(String filePath){
    	String retString = "";
    	int index = filePath.indexOf('\\');
    	if (index != -1) {
			for (int i = 0; i < filePath.length(); i++) {
				char ch = filePath.charAt(i);
				if (ch == '\\') {
					retString += '/';
					continue;
				}
				retString += ch;
			}
		}else{
			return filePath;
		}
    	return retString ;
    }
}
