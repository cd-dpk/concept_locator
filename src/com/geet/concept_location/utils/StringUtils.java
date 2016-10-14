package com.geet.concept_location.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtils {

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
	
	
}
