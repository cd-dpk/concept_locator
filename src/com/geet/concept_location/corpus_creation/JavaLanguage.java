package com.geet.concept_location.corpus_creation;

import com.github.javaparser.Position;

public class JavaLanguage {

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
	
	public final static String OPERATORS[]={
			"=","+","-","*","/","%","++","--","!",
			"==","!=",">",">=","<","<=","&&","||",
			"?:","instanceof","~","<<",">>",">>>",
			"&","^","|" };
	
	public final static String PROGRAMING_LANGUAGE_SYNTAX[]={"()","{}","[]",",",".",";",":","->","&"," "};
	
	public final static String ANNOTATIONS[]={
			"@Deprecated","@Override ","@SuppressWarnings","@SafeVarargs",
			"@FunctionalInterface","@Retention","@Documented ","@Target",
			"@Inherited","@Repeatable"};
	
	public final static String JAVA_DOC[]={""};
	
	public final static String LITERALS[]={
			"true", "false","null","\n","\r",
			"\f","\\","\'","\"","\t","\b"};
	
	public final static String INLINE_COMMENT = "//";
	public final static String BLOCK_OR_INLINE_COMMENT_START = "/*";
	public final static String BLOCK_OR_JAVADOC_CONTINUE="*";
	public final static String BLOCK_OR_INLINE_COMMENT_END = "*/";
	
}
