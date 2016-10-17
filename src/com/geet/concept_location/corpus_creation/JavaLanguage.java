package com.geet.concept_location.corpus_creation;

/**
 * 
 * @author geet
 *
 */
public class JavaLanguage {
	// hello
	
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
			"=","+","-","*","/","%","++","--","!",
			"==","!=",">",">=","<","<=","&&","||",
			"?:","instanceof","~","<<",">>",">>>",
			"&","^","|" };
	public String getOperators(){
		return getInStringFromStringArray(OPERATORS);
	}
	
	public final static String PROGRAMING_LANGUAGE_SYNTAX[]={"()","{}","[]",",",".",";",":","->","&"," ","?"};
	
	public String getProgrammingLanguageSyntax(){
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
	public String getLiterals(){
		return getInStringFromStringArray(LITERALS);
	}
	
	public final static String INLINE_COMMENT = "//";
	public final static String BLOCK_OR_INLINE_COMMENT_START = "/*";
	public final static String BLOCK_OR_JAVADOC_CONTINUE="*";
	public final static String BLOCK_OR_INLINE_COMMENT_END = "*/";
	public String getComments(){
		return INLINE_COMMENT+BLOCK_OR_INLINE_COMMENT_START+BLOCK_OR_JAVADOC_CONTINUE+BLOCK_OR_INLINE_COMMENT_END;
	}
	
	private String getInStringFromStringArray(String[] array){
		String str= "";
		for (int i = 0; i < array.length; i++) {
				str += array[i];
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
	
	
}
