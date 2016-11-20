package com.geet.concept_location.ui;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
public class SourceViewPanel extends JPanel{
	private JLabel fileName;
	private RSyntaxTextArea sourceTextArea;
	private RTextScrollPane scrollPane;
	public SourceViewPanel(String source,Bound bound){
		super();
		setLayout(new BorderLayout());
		fileName = new JLabel();
		add(fileName,BorderLayout.NORTH);
		sourceTextArea = new RSyntaxTextArea(source);
		sourceTextArea.setEditable(false);
		sourceTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
	    sourceTextArea.setCodeFoldingEnabled(true);
	    sourceTextArea.setAntiAliasingEnabled(true);
	    scrollPane = new RTextScrollPane(sourceTextArea);
	    scrollPane.setFoldIndicatorEnabled(true);
		add(scrollPane,BorderLayout.CENTER);
	}
	public JTextArea getSourceTextArea() {
		return sourceTextArea;
	}
	public void setSourceTextArea(RSyntaxTextArea sourceTextArea) {
		this.sourceTextArea = sourceTextArea;
	}
	public JLabel getFileName() {
		return fileName;
	}
	public void setFileName(JLabel fileName) {
		this.fileName = fileName;
	}
}
