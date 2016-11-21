/**
 * 
 */
package com.geet.concept_location.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Vector;
import com.geet.concept_location.constants.UIConstants;

public class AppManager {
	private static Window win = null;
	private static Vector<Page> documents;
	
	public static Vector<Page> getDocuments() {
		return documents;
	}
	public static void addDocument(Page d) {
		documents.addElement(d);
	}

	public static String readFile(File file) throws IOException {
		  FileInputStream stream = new FileInputStream(file);
		  try {
		    FileChannel fc = stream.getChannel();
		    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		    /* Instead of using default, pass in a decoder. */
		    return Charset.defaultCharset().decode(bb).toString();
		  }
		  finally {
		    stream.close();
		  }
		}
	public static void main(String[] args) {
		documents = new Vector<Page>();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					AppManager.win = new Window(UIConstants.WIDTH, UIConstants.HEIGHT);					
				} catch(Exception e) {
					e.printStackTrace();
					System.out.println("Failed to create the main window; quitting...");
					System.exit(1);
				}
			}
		});
	}

}
