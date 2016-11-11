package com.geet.concept_location.utils;
import java.io.File;
import java.io.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
public class FileUtils implements FileFilter {
	public boolean isJavaFile(File file) {
		return true;
	}
	@Override
	public boolean accept(File pathname) {
		FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Java Files Only", ".java");
		if (fileNameExtensionFilter.accept(pathname)) {
			return true;
		}
		return false;
	}
	public boolean acceptFile(File file){
		return accept(file);
	}
}