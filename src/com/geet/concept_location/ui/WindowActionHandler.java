package com.geet.concept_location.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;

public class WindowActionHandler implements ActionListener {
	private Window win;
	public WindowActionHandler(Window w) {
		win = w;
	}
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if("TabClose".equals(command)) {
			// usuń tab
			int pos = win.getTabs().getSelectedIndex();
			AppManager.getDocuments().remove(pos);
			win.getTabs().remove(pos);
		}
		if (e.getSource().equals(win.getMenu().getFileNewItem())) {
			// new project indexing...
			final JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int ret = fc.showOpenDialog(win.getFrame());
			if(ret == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				win.projectPath = file.getAbsolutePath();
			}
		} else if (e.getSource().equals(win.getMenu().getFileExitItem())) {
			System.exit(0);
		}
    }

}
