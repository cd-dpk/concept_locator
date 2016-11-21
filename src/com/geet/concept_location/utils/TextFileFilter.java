package com.geet.concept_location.utils;
import java.io.File;
import java.io.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
public class TextFileFilter implements FileFilter{
	@Override
	public boolean accept(File pathname) {
		FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Text file only", "txt");
		return fileNameExtensionFilter.accept(pathname);
	}
}
