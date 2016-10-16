package com.geet.concept_location.utils;

import java.util.StringTokenizer;

import com.github.javaparser.Position;

public class CustomStringTokenizer extends StringTokenizer{

	public CustomStringTokenizer(String str) {
		super(str);
		// TODO Auto-generated constructor stub
	}

	public CustomStringTokenizer(String str, String delim, boolean returnDelims) {
		super(str, delim, returnDelims);
		// TODO Auto-generated constructor stub
	}

	public CustomStringTokenizer(String str, String delim) {
		super(str, delim);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String nextToken() {
		// TODO Auto-generated method stub
		String token = nextToken();
		if (findAtSign(token)) {
			token = replaceWordWithAtSign(token, "");
		}
		token = replaceWordWithHTMLTag(token, "");
		return token;
	}
	

	
	
	private boolean findAtSign(String target){
		if (target.startsWith("@")) {
			return true;
		}
		return false;
	}
	
	
	private String replaceWordWithAtSign(String target, String replace){
		return target=replace;
	}
	
	private String replaceWordWithHTMLTag(String target,String replace){
		String str = "";
		boolean lock = false;
		for (int i = 0; i < target.length(); i++) {
			char ch =target.charAt(i);
			if (ch == '<') {
				lock = true;
				continue;
			}
			else if (ch == '>'){
				lock = false;
				continue;
			}
			
			if (!lock) {
				str = str + ch; 
			}
		}
		return str;
	}
	
	private IndexRange findWordWithHTMLTag(String target){
		IndexRange indexRange = new IndexRange();
		for (int i = 0; i < target.length(); i++) {
			char ch = target.charAt(i);
			if (indexRange.a !=-1 ) {
				if (ch == '<') {
					indexRange.a = i;
				}
			}else{
				if (ch == '>') {
					indexRange.b = i;
				}
			}
			if (indexRange.isValid()) {
				return indexRange;
			}
		}
		return indexRange;
	}
}
