package com.geet.concept_location.corpus_creation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class DocumentExtractor {
	
	CompilationUnit compilationUnit;
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
		//add Java Doc Comments to each document and set new Position of each documents
		//decompose all the documents
		
		return allDocuments;
	}
	
	private static class MethodVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(MethodDeclaration methodDeclaration, Object arg1) {
			Position startPosition = new Position(methodDeclaration.getBeginLine(), methodDeclaration.getBeginColumn());
			Position endPosition = new Position(methodDeclaration.getEndLine(), methodDeclaration.getEndLine());
			allDocuments.add(new Document(fileName,methodDeclaration.getName(),startPosition, endPosition));
		}
	}
	
	private static class ConstructorVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(ConstructorDeclaration constructorDeclaration, Object arg1) {
			Position startPosition = new Position(constructorDeclaration.getBeginLine(), constructorDeclaration.getBeginColumn());
			Position endPosition = new Position(constructorDeclaration.getEndLine(), constructorDeclaration.getEndLine());
			allDocuments.add(new Document(fileName,constructorDeclaration.getName(),startPosition, endPosition));
			}
	}
	
	private static class ClassOrInterfaceVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Object arg1) {
			Position startPosition = new Position(classOrInterfaceDeclaration.getBeginLine(), classOrInterfaceDeclaration.getBeginColumn());
			Position endPosition = new Position(classOrInterfaceDeclaration.getEndLine(), classOrInterfaceDeclaration.getEndLine());
			allDocuments.add(new Document(fileName,classOrInterfaceDeclaration.getName(),startPosition, endPosition));
		
		}
	}
}
