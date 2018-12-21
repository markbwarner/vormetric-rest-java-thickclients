

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class TestJFilePicker extends JFrame {

	public TestJFilePicker() throws IOException {
		super("Vormetric Application Encryption (VAE) Encrypt/Decrypt REST Example");
		//setLayout(new FlowLayout());
		setLayout(new GridLayout(2,3));
		
		// set up a file picker component
		JFilePicker filePicker = new JFilePicker("Pick a file to Encrypt/Decrypt", "Browse...");
		filePicker.setMode(JFilePicker.MODE_SAVE);
		filePicker.addFileTypeFilter(".txt", "Text Files");
	//	filePicker.addFileTypeFilter(".csv", "CSV Files");
		filePicker.addFileTypeFilter("enc", "Encrypted Files");
		// access JFileChooser class directly
		JFileChooser fileChooser = filePicker.getFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		// add the component to the frame
		add(filePicker);
		 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setSize(640, 100);
		setSize(720, 140);
		//setLocationRelativeTo(null);	// center on screen
	}
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new TestJFilePicker().setVisible(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
	}

}