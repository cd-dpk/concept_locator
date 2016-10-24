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
}
