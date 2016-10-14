package com.geet.concept_location.corpus_creation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.geet.concept_location.utils.StringUtils;
import com.github.javaparser.Position;

public class MethodOrConstructorDocument extends Document {

	public MethodOrConstructorDocument(String docInJavaFile, String docName,
			Position startPosition, Position endPosition) {
		super(docInJavaFile, docName, startPosition, endPosition);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void extractDocument(){
		
		// process all java doc comments
		// start
			// remove all the @tag, <tag> , </tag>
		
		// end
		// process all comments
		
		// process all implementation body
		// start
			// remove all spaces, keywords, operators,structures, annotations, literals 
		// end
		
		String article ="";
		// scanner open
		Scanner scanner = new Scanner("");
		// Step 1: read every line 
		List<String> words = new ArrayList<String>();
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			// Step 2: delete all spaces, operators, programming syntax, comment
			String delim = new JavaLanguage().getOperators()+ new JavaLanguage().getProgrammingLanguageSyntax()+new JavaLanguage().getComments();
			StringTokenizer stringTokenizer = new StringTokenizer(line,delim,false);
			// Step 3: take all the words
			while (stringTokenizer.hasMoreTokens()) {
				words.add(stringTokenizer.nextToken());
			}
			// Step 4: remove all keywords, structures, annotations, literals					
			for (String string : words) {
				if (StringUtils.hasStringInList(string, new JavaLanguage().KEYWORDS) || StringUtils.hasStringInList(string, new JavaLanguage().ANNOTATIONS) || StringUtils.hasStringInList(string, new JavaLanguage().LITERALS)|| StringUtils.hasStringInList(string, new JavaLanguage().JAVA_DOC)) {
					continue;
				}
				article += string+" ";
			}
		}
		// Step 5: until
		scanner.close();
		// scanner close
	}
}
