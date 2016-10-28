package com.geet.concept_location.corpus_creation;
/**
 * 
 * @author geet
 *
 */
public class JavaLanguage {
	// java keywords
	public final static String KEYWORDS[] = { 
			"abstract", "continue", "for",	"new", "switch",
			"assert", "default", "goto", "package", "synchronized",
			"boolean", "do", "if", "private", "this",
			"double", "implements", "protected", "throw", "byte",
			"else","import", "public", "throws", "case",
			"enum", "instanceof","return", "transient", "catch", 
			"extends", "int", "short", "try","char", 
			"final", "interface", "static", "void", "class", 
			"finally","long", "strictfp", "volatile", "const",
			"float", "native","super", "while", "break" };
	public String getKeywords(){
		return getInStringFromStringArray(KEYWORDS);
	}
	public final static String OPERATORS[]={
			"=","+","-","*","/","%","++","--","!","+=",
			"==","!=",">",">=","<","<=","&&","||","*=","/=",
			"?:","~","<<",">>",">>>",
			"&","^","|" };
	public final static String OPERATORS_CONTAINED_ONLY_CHAR[]={"instanceof"};
	public static String getOperators(){
		return getInStringFromStringArray(OPERATORS);
	}
	public final static String PROGRAMING_LANGUAGE_SYNTAX[]={"()","{}","[]",",",".",";",":","->","&"," ","?","<>","//","'","\"",":","\\"};
	public static String getProgrammingLanguageSyntax(){
		return getInStringFromStringArray(PROGRAMING_LANGUAGE_SYNTAX);
	}
	public final static String ANNOTATIONS[]={
			"@Deprecated","@Override ","@SuppressWarnings","@SafeVarargs",
			"@FunctionalInterface","@Retention","@Documented ","@Target",
			"@Inherited","@Repeatable"};
	public String getAnnotations(){
		return getInStringFromStringArray(ANNOTATIONS);
	}
	//	@tag
	//	<tag>element</tag>
	public final static String JAVA_DOC[]={"@see","@link","@throws","@since"," @serial", 
		"@serialField","@serialData",""};
	public String getJavaDocs(){
		return getInStringFromStringArray(JAVA_DOC);
	}
	public final static String LITERALS[]={
			"true", "false","null","\n","\r",
			"\f","\\","\'","\"","\t","\b"};
	public static String getLiterals(){
		return getInStringFromStringArray(LITERALS);
	}
	public final static String INLINE_COMMENT = "//";
	public final static String BLOCK_OR_INLINE_COMMENT_START = "/*";
	public final static String BLOCK_OR_JAVADOC_CONTINUE="*";
	public final static String BLOCK_OR_INLINE_COMMENT_END = "*/";
	public static String getComments(){
		return INLINE_COMMENT+BLOCK_OR_INLINE_COMMENT_START+BLOCK_OR_JAVADOC_CONTINUE+BLOCK_OR_INLINE_COMMENT_END;
	}
	private static String getInStringFromStringArray(String[] array){
		String str= " ";
		for (int i = 0; i < array.length; i++) {
				str += array[i]+"";
		}
		return str;
	}
	public String getDelimeters(){
		String delimString="";
		delimString += getKeywords();
		delimString += getOperators();
		delimString += getAnnotations();
		delimString += getComments();
		delimString += getLiterals();
		delimString += getProgrammingLanguageSyntax();
		delimString += getJavaDocs();
		return delimString;
	}
	public final static String WHITE_SPACE[]={
		"\n","\r","\f","\t","\b"," "};
	public static String getWhiteSpace() {
		return getInStringFromStringArray(WHITE_SPACE);
	}
}
