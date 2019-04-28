package window;

import javax.swing.*;

public class FileWindow extends JFrame {
	JFileChooser chooser;
	
	public FileWindow() {
	    chooser = new JFileChooser();
	    chooser.showOpenDialog(null);
	}
	
	public String getPath() {
		return chooser.getSelectedFile().getAbsolutePath();
	}
}
