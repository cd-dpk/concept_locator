package com.geet.concept_location.corpus_creation;
/**
 * 
 * @author geet
 *
 */
public class JavaLanguage {
	// java keywords
	private final static String KEYWORDS[] = { 
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
	
	public static String[] getKeywords() {
		return KEYWORDS;
	}
	public static String getKeywordsInString(){
		return getInStringFromStringArray(KEYWORDS);
	}
	private final static String OPERATORS[]={
			"=","+","-","*","/","%","++","--","!","+=",
			"==","!=",">",">=","<","<=","&&","||","*=","/=",
			"?:","~","<<",">>",">>>",
			"&","^","|" };
	
	public static String[] getOperators() {
		return OPERATORS;
	}
	private final static String OPERATORS_CONTAINED_ONLY_CHAR[]={"instanceof"};
	public static String getOperatorsInString(){
		return getInStringFromStringArray(OPERATORS);
	}
	private final static String PROGRAMING_LANGUAGE_SYNTAX[]={"()","{}","[]",",",".",";",":","->","&"," ","?","<>","//","'","\"",":","\\"};
	public static String getProgrammingLanguageSyntax(){
		return getInStringFromStringArray(PROGRAMING_LANGUAGE_SYNTAX);
	}
	private final static String ANNOTATIONS[]={
			"@Deprecated","@Override ","@SuppressWarnings","@SafeVarargs",
			"@FunctionalInterface","@Retention","@Documented ","@Target",
			"@Inherited","@Repeatable"};
	public String getAnnotations(){
		return getInStringFromStringArray(ANNOTATIONS);
	}
	//	@tag
	//	<tag>element</tag>
	private final static String JAVA_DOC[]={"#","$",};
	public static String getJavaDocs(){
		return getInStringFromStringArray(JAVA_DOC);
	}
	private final static String LITERALS[]={
			"true", "false","null","\n","\r",
			"\f","\\","\'","\"","\t","\b"};
	
	public static String[] getLiterals() {
		return LITERALS;
	}
	public static String getLITERALSInString(){
		return getInStringFromStringArray(LITERALS);
	}
	private final static String INLINE_COMMENT = "//";
	private final static String BLOCK_OR_INLINE_COMMENT_START = "/*";
	private final static String BLOCK_OR_JAVADOC_CONTINUE="*";
	private final static String BLOCK_OR_INLINE_COMMENT_END = "*/";
	private static String getComments(){
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
		delimString += getKeywordsInString();
		delimString += getOperatorsInString();
		delimString += getAnnotations();
		delimString += getComments();
		delimString += getLITERALSInString();
		delimString += getProgrammingLanguageSyntax();
		delimString += getJavaDocs();
		return delimString;
	}
	private final static String WHITE_SPACE[]={
		"\n","\r","\f","\t","\b"," "};
	public static String getWhiteSpace() {
		return getInStringFromStringArray(WHITE_SPACE);
	}
	public static String[] getOperatorsContainedOnlyChar() {
		return OPERATORS_CONTAINED_ONLY_CHAR;
	}
	public static String getInlineComment() {
		return INLINE_COMMENT;
	}
	public static String getBlockOrJavadocContinue() {
		return BLOCK_OR_JAVADOC_CONTINUE;
	}
	
}
