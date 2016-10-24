package com.geet.concept_location.corpus_creation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.geet.concept_location.preprocessing.JavaClassPreprocessor;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
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
			compilationUnit = JavaParser.parse(new JavaClassPreprocessor().getProcessedJavaFile(javaFile));
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
		for (ClassDocument document : myClassDocuments) {
			new FieldVisitor(document).visit(compilationUnit, null);
		}
		allDocuments.addAll(myMethodOrConstructorDocuments);
		allDocuments.addAll(myClassDocuments);
		return allDocuments;
	}
	private static class MethodVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(MethodDeclaration methodDeclaration, Object arg1) {
			Comment methodComment = methodDeclaration.getComment();
			Position startPosition = new Position(methodDeclaration.getBeginLine(), methodDeclaration.getBeginColumn());
			Position endPosition = new Position(methodDeclaration.getEndLine(), methodDeclaration.getEndLine());
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
			Position startPosition = new Position(constructorDeclaration.getBeginLine(), constructorDeclaration.getBeginColumn());
			Position endPosition = new Position(constructorDeclaration.getEndLine(), constructorDeclaration.getEndLine());
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
			Comment classComment = classOrInterfaceDeclaration.getComment();
			Position startPosition = new Position(classOrInterfaceDeclaration.getBeginLine(), classOrInterfaceDeclaration.getBeginColumn());
			Position endPosition = new Position(classOrInterfaceDeclaration.getEndLine(), classOrInterfaceDeclaration.getEndLine());
			ClassDocument classDocument = new ClassDocument(fileName,classOrInterfaceDeclaration.getName(),startPosition, endPosition);
			if (classComment != null && classComment instanceof JavadocComment) {
				classDocument.javaDocComments.add((JavadocComment) classComment);
			}else if ((classComment != null )&& (classComment instanceof JavadocComment == false)) {
				classDocument.implementationComments.add(classComment);
			}
/*			// all contained comments
			for (Comment containedComment : classOrInterfaceDeclaration.getAllContainedComments()) {
				// check the comments are on its comments collection
				// check whether it is on one of the methods comment or not
				// TODO if it is done, it will increase the performance
				if (containedComment instanceof JavadocComment) {
					classDocument.javaDocComments.add((JavadocComment) containedComment);
				}else {
					classDocument.implementationComments.add(containedComment);
				}
			}
*/			myClassDocuments.add(classDocument);
		}
	}
	private static class FieldVisitor extends VoidVisitorAdapter{
		ClassDocument classDocument;
		public FieldVisitor(ClassDocument classDocument) {
			// TODO Auto-generated constructor stub
			this.classDocument = classDocument;
		}
		@Override
		public void visit(FieldDeclaration fieldDeclaration, Object arg1) {
			// if the field declaration belongs to that class, then it is added to this class
			// belonging to a class is determined by the position of class and field declaration 
			Position startPosition = new Position(fieldDeclaration.getBeginLine(), fieldDeclaration.getBeginColumn());
			Position endPosition = new Position(fieldDeclaration.getEndLine(), fieldDeclaration.getEndColumn());
			if (isParamOneBelongsToParamTwo( new Range(startPosition, endPosition), classDocument.getRange())) {
				Comment classComment = fieldDeclaration.getComment();
				if (classComment != null && classComment instanceof JavadocComment) {
					classDocument.javaDocComments.add((JavadocComment) classComment);
				}else if ((classComment != null )&& (classComment instanceof JavadocComment == false)) {
					classDocument.implementationComments.add(classComment);
				}
				classDocument.implementionBody += fieldDeclaration.toStringWithoutComments()+"\n";
			}			
		}
	}
	private static boolean isParamOneBelongsToParamTwo(Range paramOne, Range paramTwo){
		if (isPositionBelongsToANode(new Position(paramOne.getBeginLine(), paramOne.getBeginColumn()), paramTwo) && isPositionBelongsToANode(new Position(paramOne.getEndLine(), paramOne.getEndColumn()), paramTwo)) {
			return true;
		}
		return false;
	}
	private static boolean isPositionBelongsToANode(Position position, Range node){
		if (position.getLine() == node.getBeginLine() && position.getColumn() > node.getBeginColumn() ) {
			return true;
		} 
		else if(position.getLine() > node.getBeginLine()  && position.getLine() < node.getEndLine()){
			return true;
		}
		else if(position.getLine() == node.getEndLine() && position.getColumn() < node.getEndColumn()){
			return true;
		}
		return false;
	}
}
