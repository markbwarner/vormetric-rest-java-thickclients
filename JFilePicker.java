
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

//import com.jayway.jsonpath.JsonPath;
import com.vormetric.rest.sample.VormetricCryptoServerHelper;
import com.vormetric.rest.sample.VormetricCryptoServerSettings;

import java.util.ArrayList;
import java.util.List;

//This sample code tests the VAE Rest calls to the Vormetric Crypto Server.
//It will first encrypt a file and create an output file with enc at the end of it.
//You can then select the file that you just encrypted that ends with an enc and
//the application will then decrypt it.
//The input file format should be a single entry for each line.
//This is for testing only so it will only at most process the first 10 records
//in the file.

public class JFilePicker extends JPanel {
	private String textFieldLabel;
	private String buttonLabel;

	private JLabel label;
	private JTextField textField;
	private JTextField resultsField;

	private JButton button;

	private JFileChooser fileChooser;

	private int mode;
	public static final int MODE_OPEN = 1;
	public static final int MODE_SAVE = 2;
	String vtsdebug = "0";

	public JFilePicker(String textFieldLabel, String buttonLabel) throws IOException {

		this.textFieldLabel = textFieldLabel;
		this.buttonLabel = buttonLabel;

		fileChooser = new JFileChooser();

		//setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		setLayout(new GridLayout(2,3));
		// creates the GUI
		label = new JLabel(textFieldLabel);
		resultsField = new JTextField(10);
		resultsField.setVisible(false);
		resultsField.setBackground(Color.GRAY);
		
		textField = new JTextField(30);
		button = new JButton(buttonLabel);
		textField.setVisible(false);
		
	//	JLabel imgLabel = new JLabel(new ImageIcon("C:\\Users\\mark.warner\\workspace\\JavaVAEUI\\src\\images\\vsign.png"));
	//		wPic = ImageIO.read(this.getClass().getResource("vsign.png"));
	//		wIcon = new JLabel(new ImageIcon(wPic));


		
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				try {
					buttonActionPerformed(evt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	//	add(imgLabel);
		add(label);
		add(textField);
	
		add(button);
		add(resultsField);
	

	}

	private void buttonActionPerformed(ActionEvent evt) throws Exception {
		if (mode == MODE_OPEN) {
			if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				// System.out.println("open code" +
				// fileChooser.getSelectedFile().getAbsolutePath());
				textField.setText(fileChooser.getSelectedFile().getAbsolutePath());

			}
		} else if (mode == MODE_SAVE) {
			//setSize(680, 100);
			//This class uses the environment variables for necessary vcs settings. 
			VormetricCryptoServerSettings vcs = new VormetricCryptoServerSettings();
			//System.out.println("to string " + vcs.toString());

			if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				// System.out.println("save code" +
				// fileChooser.getSelectedFile().getAbsolutePath());
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
				BufferedReader reader = null;
				PrintWriter writer = null;
				boolean decrypt = false;
				if (fileChooser.getSelectedFile().getAbsolutePath().endsWith("enc")) {
					decrypt = true;
					writer = new PrintWriter(fileChooser.getSelectedFile().getAbsolutePath() + "dec", "UTF-8");
				} else {
					writer = new PrintWriter(fileChooser.getSelectedFile().getAbsolutePath() + "enc", "UTF-8");
				}
				textField.setVisible(true);
				resultsField.setVisible(true);
				List<String> list = new ArrayList<String>();
				{

					try {
						reader = new BufferedReader(new FileReader(file));

						String text = null;
						int i = 0;
						String calltype = null;
						while ((text = reader.readLine()) != null && i < 20) {
							if (text.length() > 0) {
							
								String results = null;
								if (decrypt) {
									calltype = "decrypt";
									results = VormetricCryptoServerHelper.doDeCryptData(vcs.getvcstokenserver(),
											vcs.getvcsuserid(), vcs.getvcspassword(), text, vcs.getvcsalg(),
											vcs.getvcsivnumber(), calltype, vcs.getvcsencryptdecryptkey());
								} else {
									calltype = "encrypt";
									results = VormetricCryptoServerHelper.doEncryptData(vcs.getvcstokenserver(),
											vcs.getvcsuserid(), vcs.getvcspassword(), text, vcs.getvcsalg(),
											vcs.getvcsivnumber(), calltype, vcs.getvcsencryptdecryptkey());
								}

								writer.println(results);
							} else {
								writer.println(text);
							}
							list.add((text));
							i++;
							 

						}
						resultsField.setText( calltype +" complete:" + new Integer(i));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (reader != null) {
								reader.close();
							}
						} catch (IOException e) {
						}
					}

					// print out the list
					System.out.println("Original values" + list);

					writer.close();

				}
			}
		}
	}

	public void addFileTypeFilter(String extension, String description) {
		FileTypeFilter filter = new FileTypeFilter(extension, description);
		fileChooser.addChoosableFileFilter(filter);
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getSelectedFilePath() {
		return textField.getText();
	}

	public JFileChooser getFileChooser() {
		return this.fileChooser;
	}
}