package com.geet.concept_location.corpus_creation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class DocumentExtractor {
	
	CompilationUnit compilationUnit;
	private static List<MethodOrConstructorDocument> myMethodOrConstructorDocuments = new ArrayList<MethodOrConstructorDocument>();
	private static List<ClassDocument> myClassDocuments = new ArrayList<ClassDocument>();
	private static List<Document> allDocuments = new ArrayList<Document>();
	static String fileName;
	
	public DocumentExtractor(File javaFile) {
		// TODO Auto-generated constructor stub
		try {
			fileName = javaFile.getAbsolutePath();
			compilationUnit = JavaParser.parse(javaFile);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Document> getAllDocuments(){
		new MethodVisitor().visit(compilationUnit, null);
		new ConstructorVisitor().visit(compilationUnit, null);
		new ClassOrInterfaceVisitor().visit(compilationUnit, null);
		return allDocuments;
	}
	
	
	
	private int getDocumentIndexWhichLieWithinPosition(List<Document> documents,Position position){
		for (Document document : documents) {
			int i=0;
			if (document.isBetweenPosition(position)) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	private static class MethodVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(MethodDeclaration methodDeclaration, Object arg1) {
			Comment methodComment = methodDeclaration.getComment();
			Position startPosition = new Position(methodComment.getBeginLine(), methodComment.getBeginColumn());
			Position endPosition = new Position(methodComment.getEndLine(), methodComment.getEndLine());
			MethodOrConstructorDocument methodOrConstructorDocument = new MethodOrConstructorDocument(fileName,methodDeclaration.getName(),startPosition, endPosition);
			if (methodComment != null && methodComment instanceof JavadocComment) {
				methodOrConstructorDocument.javaDocComments.add((JavadocComment) methodComment);
			}else if ((methodComment != null )&& (methodComment instanceof JavadocComment == false)) {
				methodOrConstructorDocument.implementationComments.add(methodComment);
			}
			for (Comment containedComment : methodDeclaration.getAllContainedComments()) {
				if (containedComment instanceof JavadocComment) {
					methodOrConstructorDocument.javaDocComments.add((JavadocComment) containedComment);
				}else {
					methodOrConstructorDocument.implementationComments.add(containedComment);
				}
			}
			methodOrConstructorDocument.implementionBody = methodDeclaration.getBody().toStringWithoutComments();
			myMethodOrConstructorDocuments.add(methodOrConstructorDocument);
		}
	}
	
	private static class ConstructorVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(ConstructorDeclaration constructorDeclaration, Object arg1) {
			Comment methodComment = constructorDeclaration.getComment();
			Position startPosition = new Position(methodComment.getBeginLine(), methodComment.getBeginColumn());
			Position endPosition = new Position(methodComment.getEndLine(), methodComment.getEndLine());
			MethodOrConstructorDocument methodOrConstructorDocument = new MethodOrConstructorDocument(fileName,constructorDeclaration.getName(),startPosition, endPosition);
			if (methodComment != null && methodComment instanceof JavadocComment) {
				methodOrConstructorDocument.javaDocComments.add((JavadocComment) methodComment);
			}else if ((methodComment != null )&& (methodComment instanceof JavadocComment == false)) {
				methodOrConstructorDocument.implementationComments.add(methodComment);
			}
			for (Comment containedComment : constructorDeclaration.getAllContainedComments()) {
				if (containedComment instanceof JavadocComment) {
					methodOrConstructorDocument.javaDocComments.add((JavadocComment) containedComment);
				}else {
					methodOrConstructorDocument.implementationComments.add(containedComment);
				}
			}
			methodOrConstructorDocument.implementionBody = constructorDeclaration.getBlock().toStringWithoutComments();
			myMethodOrConstructorDocuments.add(methodOrConstructorDocument);
		}
	}
	
	private static class ClassOrInterfaceVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Object arg1) {
			Comment methodComment = classOrInterfaceDeclaration.getComment();
			Position startPosition = new Position(methodComment.getBeginLine(), methodComment.getBeginColumn());
			Position endPosition = new Position(methodComment.getEndLine(), methodComment.getEndLine());
			MethodOrConstructorDocument methodOrConstructorDocument = new MethodOrConstructorDocument(fileName,classOrInterfaceDeclaration.getName(),startPosition, endPosition);
			if (methodComment != null && methodComment instanceof JavadocComment) {
				methodOrConstructorDocument.javaDocComments.add((JavadocComment) methodComment);
			}else if ((methodComment != null )&& (methodComment instanceof JavadocComment == false)) {
				methodOrConstructorDocument.implementationComments.add(methodComment);
			}
			for (Comment containedComment : classOrInterfaceDeclaration.getAllContainedComments()) {
				if (containedComment instanceof JavadocComment) {
					methodOrConstructorDocument.javaDocComments.add((JavadocComment) containedComment);
				}else {
					methodOrConstructorDocument.implementationComments.add(containedComment);
				}
			}
			// get Data
//			methodOrConstructorDocument.implementionBody = classOrInterfaceDeclaration.toStringWithoutComments();
			myMethodOrConstructorDocuments.add(methodOrConstructorDocument);
		}
	}
}