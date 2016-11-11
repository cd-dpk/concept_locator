package com.geet.concept_location.utils;
import java.util.StringTokenizer;
import com.geet.concept_location.corpus_creation.JavaLanguage;
import com.geet.concept_location.corpus_creation.StopWords;
public class CommentStringTokenizer extends StringTokenizer{
	public CommentStringTokenizer(String str) {
		super(str);
		// TODO Auto-generated constructor stub
	}
	public CommentStringTokenizer(String str, String delim, boolean returnDelims) {
		super(str, delim, returnDelims);
		// TODO Auto-generated constructor stub
	}
	public CommentStringTokenizer(String str, String delim) {
		super(str, delim);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String nextToken() {
		// TODO Auto-generated method stub
		String largeToken = "";
		String token = super.nextToken();
		// if token has a substring of programming syntax then replace with " "
		StringTokenizer stringTokenizer = new StringTokenizer(token,JavaLanguage.getProgrammingLanguageSyntax()+JavaLanguage.INLINE_COMMENT+JavaLanguage.BLOCK_OR_JAVADOC_CONTINUE+JavaLanguage.getOperators()+JavaLanguage.getJavaDocs(),false);
		while (stringTokenizer.hasMoreTokens()) {
			String nestedToken = stringTokenizer.nextToken();
			// in line comment or 
			if (StopWords.isStopword(nestedToken)||nestedToken.equals(JavaLanguage.INLINE_COMMENT) || nestedToken.equals(JavaLanguage.BLOCK_OR_JAVADOC_CONTINUE)|| StringUtils.hasStringInList(nestedToken, JavaLanguage.OPERATORS)) {
				continue;
			}
			if (findAtSign(nestedToken)) {
				nestedToken = replaceWordWithAtSign(nestedToken, " ");
			}
			nestedToken = replaceWordWithHTMLTag(nestedToken, " ");
			largeToken += StringUtils.getIdentifierSeparationsWithCamelCase(nestedToken)+" ";
		}
		return largeToken;
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
}