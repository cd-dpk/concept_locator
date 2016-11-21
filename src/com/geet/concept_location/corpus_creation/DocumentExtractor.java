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
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
public class DocumentExtractor {
	private CompilationUnit compilationUnit;
	private  List<Document> myMethodOrConstructorDocuments = new ArrayList<Document>();
	private  List<Document> myClassDocuments = new ArrayList<Document>();
	private  List<Document> myEnumDocuments = new ArrayList<Document>();
	private  List<Document> allDocuments = new ArrayList<Document>();
	private static String fileName;
	public DocumentExtractor(File javaFile) {
		allDocuments = new ArrayList<Document>();
		try {
			fileName = javaFile.getPath();
			compilationUnit = JavaParser.parse(javaFile);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public List<Document> getAllDocuments(){
		new MethodVisitor().visit(compilationUnit, null);
		new ConstructorVisitor().visit(compilationUnit, null);
		new EnumVisitor().visit(compilationUnit, null);
		new ClassOrInterfaceVisitor().visit(compilationUnit, null);
		for (Document document : myClassDocuments) {
			new FieldVisitor(document).visit(compilationUnit, null);
		}
		allDocuments.addAll(myMethodOrConstructorDocuments);
		allDocuments.addAll(myEnumDocuments);
		allDocuments.addAll(myClassDocuments);
		return allDocuments;
	}
	private  class MethodVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(MethodDeclaration methodDeclaration, Object arg1) {
			Comment methodComment = methodDeclaration.getComment();
			Position startPosition = new Position(methodDeclaration.getBeginLine(), methodDeclaration.getBeginColumn());
			Position endPosition = new Position(methodDeclaration.getEndLine(), methodDeclaration.getEndLine());
			Document methodOrConstructorDocument = new Document.Builder().build();
			methodOrConstructorDocument.docInJavaFile = fileName;
			methodOrConstructorDocument.docName = methodDeclaration.getName();
			if (methodComment != null && methodComment instanceof JavadocComment) {
				methodOrConstructorDocument.getJavaDocComments().add((JavadocComment) methodComment);
			}else if ((methodComment != null )&& (methodComment instanceof JavadocComment == false)) {
				methodOrConstructorDocument.getImplementationComments().add(methodComment);
			}
			for (Comment containedComment : methodDeclaration.getAllContainedComments()) {
				if (containedComment instanceof JavadocComment) {
					methodOrConstructorDocument.getJavaDocComments().add((JavadocComment) containedComment);
				}else {
					methodOrConstructorDocument.getImplementationComments().add(containedComment);
				}
			}
			if (methodComment!=null) {
				startPosition = new Position(methodComment.getBeginLine(), methodComment.getBeginColumn());
			}
			methodOrConstructorDocument.setStartPosition(new com.geet.concept_location.corpus_creation.Position(startPosition));
			methodOrConstructorDocument.setEndPosition(new com.geet.concept_location.corpus_creation.Position(endPosition));			
			methodOrConstructorDocument.setImplementionBody(methodDeclaration.getBody().toStringWithoutComments());
			myMethodOrConstructorDocuments.add(methodOrConstructorDocument);
		}
	}
	private class ConstructorVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(ConstructorDeclaration constructorDeclaration, Object arg1) {
			Comment methodComment = constructorDeclaration.getComment();
			Position startPosition = new Position(constructorDeclaration.getBeginLine(), constructorDeclaration.getBeginColumn());
			Position endPosition = new Position(constructorDeclaration.getEndLine(), constructorDeclaration.getEndLine());
			Document methodOrConstructorDocument = new Document.Builder().build();
			methodOrConstructorDocument.docInJavaFile = fileName;
			methodOrConstructorDocument.docName = constructorDeclaration.getName();
			if (methodComment != null && methodComment instanceof JavadocComment) {
				methodOrConstructorDocument.getJavaDocComments().add((JavadocComment) methodComment);
			}else if ((methodComment != null )&& (methodComment instanceof JavadocComment == false)) {
				methodOrConstructorDocument.getImplementationComments().add(methodComment);
			}
			for (Comment containedComment : constructorDeclaration.getAllContainedComments()) {
				if (containedComment instanceof JavadocComment) {
					methodOrConstructorDocument.getJavaDocComments().add((JavadocComment) containedComment);
				}else {
					methodOrConstructorDocument.getImplementationComments().add(containedComment);
				}
			}
			if (methodComment!=null) {
				startPosition = new Position(methodComment.getBeginLine(), methodComment.getBeginColumn());
			}
			methodOrConstructorDocument.setStartPosition(new com.geet.concept_location.corpus_creation.Position(startPosition));
			methodOrConstructorDocument.setEndPosition(new com.geet.concept_location.corpus_creation.Position(endPosition));			
			methodOrConstructorDocument.setImplementionBody(constructorDeclaration.getBlock().toStringWithoutComments());
			myMethodOrConstructorDocuments.add(methodOrConstructorDocument);
		}
	}
	private class ClassOrInterfaceVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Object arg1) {
			Comment classComment = classOrInterfaceDeclaration.getComment();
			Position startPosition = new Position(classOrInterfaceDeclaration.getBeginLine(), classOrInterfaceDeclaration.getBeginColumn());
			Position endPosition = new Position(classOrInterfaceDeclaration.getEndLine(), classOrInterfaceDeclaration.getEndLine());
			Document classDocument = new Document.Builder().build();
			classDocument.docInJavaFile = fileName;
			classDocument.docName = classOrInterfaceDeclaration.getName();
			if (classComment != null && classComment instanceof JavadocComment) {
				classDocument.getJavaDocComments().add((JavadocComment) classComment);
			}else if ((classComment != null )&& (classComment instanceof JavadocComment == false)) {
				classDocument.getImplementationComments().add(classComment);
			}
			if (classComment!=null) {
				startPosition = new Position(classComment.getBeginLine(), classComment.getBeginColumn());
			}
			classDocument.setStartPosition(new com.geet.concept_location.corpus_creation.Position(startPosition));
			classDocument.setEndPosition(new com.geet.concept_location.corpus_creation.Position(endPosition));			
			myClassDocuments.add(classDocument);
		}
	}
	private static class FieldVisitor extends VoidVisitorAdapter{
		Document classDocument;
		public FieldVisitor(Document classDocument) {
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
					classDocument.getJavaDocComments().add((JavadocComment) classComment);
				}else if ((classComment != null )&& (classComment instanceof JavadocComment == false)) {
					classDocument.getImplementationComments().add(classComment);
				}
				String text = classDocument.getImplementionBody();
				text += fieldDeclaration.toStringWithoutComments()+"\n";
				classDocument.setImplementionBody(text) ;
			}			
		}
	}
	private  class EnumVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(EnumDeclaration enumDeclaration, Object arg1) {
				Comment classComment = enumDeclaration.getComment();
				Position startPosition = new Position(enumDeclaration.getBeginLine(), enumDeclaration.getBeginColumn());
				Position endPosition = new Position(enumDeclaration.getEndLine(), enumDeclaration.getEndLine());
				Document enumDocument = new Document.Builder().build();
				enumDocument.docInJavaFile = fileName;
				enumDocument.docName = enumDeclaration.getName();
				if (classComment != null && classComment instanceof JavadocComment) {
					enumDocument.getJavaDocComments().add((JavadocComment) classComment);
				}else if ((classComment != null )&& (classComment instanceof JavadocComment == false)) {
					enumDocument.getImplementationComments().add(classComment);
				}
				if (classComment!=null) {
					startPosition = new Position(classComment.getBeginLine(), classComment.getBeginColumn());
				}
				enumDocument.setStartPosition(new com.geet.concept_location.corpus_creation.Position(startPosition));
				enumDocument.setEndPosition(new com.geet.concept_location.corpus_creation.Position(endPosition));			
				enumDocument.setImplementionBody(enumDeclaration.toStringWithoutComments()+"\n");
				myEnumDocuments.add(enumDocument);
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
	public static void main(String[] args) {
			String path ="src/com/geet/concept_location/corpus_creation/Document.java";
			DocumentExtractor documentExtractor = new DocumentExtractor(
					new File(path));
			System.out.println(path+" has "+documentExtractor.getAllDocuments().size()+" document(s)");
	}	
	
}