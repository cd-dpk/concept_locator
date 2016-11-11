package com.geet.concept_location.corpus_creation;
import java.io.File;
import java.io.IOException;
import com.geet.concept_location.preprocessing.JavaClassPreprocessor;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
/**
 * In bug localization, each java class return a document
 * @author iit
 *
 */
public class DocumentExtractor {
	CompilationUnit compilationUnit;
	Document/*Hello*/ document ;
	static String fileName;
	int/*Hello*/ i,/*Hello*/ j=10,k;
	// bug localization starts
	public DocumentExtractor(File javaFile) {
		try {
			fileName = javaFile.getAbsolutePath();
			compilationUnit = JavaParser.parse(javaFile);
			} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		document = new Document();
	}
	public Document getExtractedDocument(){
		// all methods
		new MethodVisitor().visit(compilationUnit, null);		
		// all constructors
		new ConstructorVisitor().visit(compilationUnit, null);		
		// all enum
		new EnumVisitor().visit(compilationUnit, null);
		// all classes
		new ClassOrInterfaceVisitor().visit(compilationUnit, null);
		// all field
		new FieldVisitor().visit(compilationUnit, null);
		return document;
	}
	private  class MethodVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(MethodDeclaration methodDeclaration, Object arg1) {
			document.docTitles.add(methodDeclaration.getName());
			Comment methodComment = methodDeclaration.getComment();
			if (methodComment != null && methodComment instanceof JavadocComment) {
				document.javaDocComments.add((JavadocComment) methodComment);
			}else if ((methodComment != null )&& (methodComment instanceof JavadocComment == false)) {
				document.implementationComments.add(methodComment);
			}
			for (Comment containedComment : methodDeclaration.getAllContainedComments()) {
				if (containedComment instanceof JavadocComment) {
					document.javaDocComments.add((JavadocComment) containedComment);
				}else {
					document.implementationComments.add(containedComment);
				}
			}
			if (methodDeclaration.getBody() != null) {
				document.implementionBody += methodDeclaration.getBody().toStringWithoutComments()+"\n";
			}
		}
	}
	private class ConstructorVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(ConstructorDeclaration constructorDeclaration, Object arg1) {
			document.docTitles.add(constructorDeclaration.getName());
			Comment methodComment = constructorDeclaration.getComment();
			if (methodComment != null && methodComment instanceof JavadocComment) {
				document.javaDocComments.add((JavadocComment) methodComment);
			}else if ((methodComment != null )&& (methodComment instanceof JavadocComment == false)) {
				document.implementationComments.add(methodComment);
			}
			for (Comment containedComment : constructorDeclaration.getAllContainedComments()) {
				if (containedComment instanceof JavadocComment) {
					document.javaDocComments.add((JavadocComment) containedComment);
				}else {
					document.implementationComments.add(containedComment);
				}
			}
			if (constructorDeclaration.getBlock() != null) {
				document.implementionBody += constructorDeclaration.getBlock().toStringWithoutComments()+"\n";
			}
		}
	}
	private class ClassOrInterfaceVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Object arg1) {
			document.docTitles.add(classOrInterfaceDeclaration.getName());
			Comment classComment = classOrInterfaceDeclaration.getComment();
			if (classComment != null && classComment instanceof JavadocComment) {
				document.javaDocComments.add((JavadocComment) classComment);
			}else if ((classComment != null )&& (classComment instanceof JavadocComment == false)) {
				document.implementationComments.add(classComment);
			}
		}
	}
	private  class EnumVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(EnumDeclaration enumDeclaration, Object arg1) {
				Comment classComment = enumDeclaration.getComment();
				if (classComment != null && classComment instanceof JavadocComment) {
					document.javaDocComments.add((JavadocComment) classComment);
				}else if ((classComment != null )&& (classComment instanceof JavadocComment == false)) {
					document.implementationComments.add(classComment);
				}
				document.implementionBody += enumDeclaration.toStringWithoutComments()+"\n";
			}			
		}
	private  class FieldVisitor extends VoidVisitorAdapter{
		@Override
		public void visit(FieldDeclaration fieldDeclaration, Object arg1) {
			// if the field declaration belongs to that class, then it is added to this class
			// belonging to a class is determined by the position of class and field declaration 
			Comment classComment = fieldDeclaration.getComment();
			if (classComment != null && classComment instanceof JavadocComment) {
				document.javaDocComments
						.add((JavadocComment) classComment);
			} else if ((classComment != null)
					&& (classComment instanceof JavadocComment == false)) {
				document.implementationComments.add(classComment);
			}
			document.implementionBody += fieldDeclaration
					.toStringWithoutComments() + "\n";
		}
	}
	public static void main(String[] args) {
			String path ="D:\\org\\eclipse\\swt\\internal\\image\\WinBMPFileFormat.java";
			if(new JavaClassPreprocessor().processJavaFile(new File(path))){
				DocumentExtractor documentExtractor = new DocumentExtractor(
						new File(path));
				System.out.println(documentExtractor.getExtractedDocument().getArticle());				
			}
	}
}