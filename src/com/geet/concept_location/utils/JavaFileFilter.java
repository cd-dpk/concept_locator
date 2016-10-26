package com.geet.concept_location.utils;
import java.io.File;
import java.io.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
public class JavaFileFilter implements FileFilter{
	@Override
	public boolean accept(File pathname) {
		FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Java Files only", "java");
		return fileNameExtensionFilter.accept(pathname);
	}
}
