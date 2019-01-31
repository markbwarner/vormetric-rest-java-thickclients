import java.security.Guard;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import com.vormetric.rest.sample.VormetricDSMHelper;
import com.vormetric.rest.sample.VormetricCryptoServerHelper;
import com.vormetric.rest.sample.VormetricCryptoServerSettings;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class VormetricDSMRestExample extends Application {

	//curl -X GET "https://10.3.201.140/api/keys/" -H "accept: application/json"
	
	 String ASYMMETRIC = "asymmetric";
	
	 String SYMMETRIC = "symmetric";
	
	ObservableList<String> names = FXCollections.observableArrayList();

	@Override
	public void start(Stage stage) {
		// Label for name
		String delimiter = "/";

		String DSMKeyBUtton = "DSM Keys for: ";

		GUIReplies gr = new GUIReplies();

		Text emailtxt = new Text("DSM UserID");

		Text dsmIPtxt = new Text("DSM IP Address");

		// creating label password
		Text pwdtxt = new Text("Password");

		Text dsmdomainpicked = new Text();
		VormetricDSMHelper vdsm = new VormetricDSMHelper();
		
		// Creating Text Filed for email
		TextField emailtextField = new TextField();
		emailtextField.setText(vdsm.getDsmuserid());
		//emailtextField.setText("superadmin");
		TextField dsmiptextField = new TextField();
	//	dsmiptextField.setText("192.168.159.130");
		dsmiptextField.setText(vdsm.getDsmip());
		
		// Creating Text Filed for password
		PasswordField passwordtextField = new PasswordField();
		passwordtextField.setText(vdsm.getDsmpwd());
		//passwordtextField.setText("Admin123!");
		// Creating Buttons
		Button submittButton = new Button("Submit");
		Button clearButton = new Button("Clear");
		Button getDomainsButton = new Button("GetDomains");
		ChoiceBox vormetricdomainscheckbox = new ChoiceBox();
		// Creating a Grid Pane
		GridPane logonGridPane = new GridPane();

		// Setting size for the pane
		logonGridPane.setMinSize(300, 240);

		// Setting the padding
		logonGridPane.setPadding(new Insets(10, 10, 10, 10));

		// Setting the vertical and horizontal gaps between the columns
		logonGridPane.setVgap(5);
		logonGridPane.setHgap(5);

		// Setting the Grid alignment
		logonGridPane.setAlignment(Pos.CENTER);

		// Arranging all the nodes in the grid
		logonGridPane.add(emailtxt, 0, 0);
		logonGridPane.add(emailtextField, 1, 0);
		logonGridPane.add(pwdtxt, 0, 1);
		logonGridPane.add(passwordtextField, 1, 1);

		logonGridPane.add(dsmIPtxt, 0, 2);
		logonGridPane.add(dsmiptextField, 1, 2);

		logonGridPane.add(submittButton, 0, 4);
		logonGridPane.add(clearButton, 1, 4);

		logonGridPane.add(getDomainsButton, 0, 3);
		logonGridPane.add(vormetricdomainscheckbox, 1, 3);
		vormetricdomainscheckbox.setVisible(false);
		// Styling nodes
		submittButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");
		clearButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white;");

		emailtxt.setStyle("-fx-font: normal bold 20px 'serif' ");
		pwdtxt.setStyle("-fx-font: normal bold 20px 'serif' ");
		dsmIPtxt.setStyle("-fx-font: normal bold 20px 'serif' ");
		logonGridPane.setStyle("-fx-background-color: BEIGE;");

		// Creating a scene object
		Scene longscene = new Scene(logonGridPane);

		// Setting title to the Stage
		stage.setTitle("DSM Rest API Example");

		// Adding scene to the stage
		stage.setScene(longscene);

		// Displaying the contents of the stage
		stage.show();

		submittButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				System.out.println("userid  " + emailtextField.getText());
				System.out.println("pwd  " + passwordtextField.getText());
				System.out.println("dsmip  " + dsmiptextField.getText());
				System.out.println("domain picked is   " + gr.dsmdomain);
				dsmdomainpicked.setText(vormetricdomainscheckbox.getValue().toString().substring(1, vormetricdomainscheckbox.getValue().toString().length()-1));
			//	dsmdomainpicked.setText(gr.dsmdomain);
				// stage.toBack(); vormetricdomainscheckbox

				Text plaintextlabel = new Text("Enter data to Encrypt");
				plaintextlabel.setVisible(false);
				Text encresultslabel = new Text("Encrypted Data");
				encresultslabel.setVisible(false);
				TextField enctext = new TextField();
				enctext.setVisible(false);
				enctext.setEditable(false);
				TextField dectext = new TextField();
				dectext.setVisible(false);
				dectext.setEditable(false);

				// Text field for name
				TextField plaintext = new TextField();
				plaintext.setVisible(false);
				plaintext.setText("DefaultValuetoencrypt");

				Text keysourcelabel = new Text("Key Source");

				// Toggle group of radio buttons
				ToggleGroup keysourcegroup = new ToggleGroup();
				RadioButton vault = new RadioButton("Vault");
				vault.setToggleGroup(keysourcegroup);
				vault.setUserData("vault");
				RadioButton dsmdb = new RadioButton("DSM DB");
				dsmdb.setToggleGroup(keysourcegroup);
				dsmdb.setUserData("DSM DB");
				dsmdb.setSelected(true);

				Button exportKeyButton = new Button("Export Key");
				
				// Label for reservation
				Text keytypelabel = new Text("Key Type");

				// Toggle button for reservation
				ToggleButton keytypetogglebutton = new ToggleButton();
				ToggleButton symtogglebutton = new ToggleButton("Symmetric");
				symtogglebutton.setUserData(SYMMETRIC);
				ToggleButton asymtogglebutton = new ToggleButton("Asymmetric");
				symtogglebutton.setSelected(true);
				asymtogglebutton.setUserData(ASYMMETRIC);
				ToggleGroup keytypetogglegroup = new ToggleGroup();
				symtogglebutton.setToggleGroup(keytypetogglegroup);
				asymtogglebutton.setToggleGroup(keytypetogglegroup);

				Text dsmkeyslabel = new Text(DSMKeyBUtton + gr.keysourcevalue + " " + gr.keytypevalue);
				dsmkeyslabel.setVisible(true);

				Text currentSelectionlabel = new Text("Current selection: ");
				Text errormessage = new Text();
				Text errormessageHeader = new Text("Error: ");
				Text currentSelectionlabel2 = new Text(gr.keysourcevalue + " " + gr.keytypevalue);
				// Label for location
				Text vormetricdomainslabel = new Text("Current Vormetric Domain");
				
				// Choice box for location
				ChoiceBox dsmsymdbkeyschoicebox = new ChoiceBox();
				ChoiceBox dsmasymdbkeyschoicebox = new ChoiceBox();
				ChoiceBox dsmasymvaultkeyschoicebox = new ChoiceBox();
				ChoiceBox dsmsymvaultkeyschoicebox = new ChoiceBox();

				dsmsymdbkeyschoicebox.setVisible(false);
				dsmasymdbkeyschoicebox.setVisible(false);
				dsmasymvaultkeyschoicebox.setVisible(false);
				dsmsymvaultkeyschoicebox.setVisible(false);
				// Choice box for location

				// Label for register
				Button getkeysbutton = new Button("Get Keys");
				Button encryptbutton = new Button("Encrypt");
				Button decryptbutton = new Button("Decrypt");
				encryptbutton.setVisible(false);
				decryptbutton.setVisible(false);
				// Creating a Grid Pane
				GridPane gridPane = new GridPane();

				// Setting size for the pane
				gridPane.setMinSize(500, 540);

				// Setting the padding
				gridPane.setPadding(new Insets(10, 10, 10, 10));

				// Setting the vertical and horizontal gaps between the columns
				gridPane.setVgap(5);
				gridPane.setHgap(5);

				// Setting the Grid alignment
				gridPane.setAlignment(Pos.CENTER);

				// Arranging all the nodes in the grid

				gridPane.add(vormetricdomainslabel, 0, 1);
				gridPane.add(dsmdomainpicked, 1, 1);

				gridPane.add(keysourcelabel, 0, 2);
				gridPane.add(vault, 1, 2);
				gridPane.add(dsmdb, 2, 2);
				gridPane.add(keytypelabel, 0, 3);
				gridPane.add(symtogglebutton, 1, 3);
				gridPane.add(asymtogglebutton, 2, 3);

				gridPane.add(currentSelectionlabel, 0, 6);
				gridPane.add(currentSelectionlabel2, 1, 6);

				gridPane.add(getkeysbutton, 0, 7);
				gridPane.add(encryptbutton, 2, 9);

				gridPane.add(dsmsymdbkeyschoicebox, 1, 7);
				gridPane.add(dsmasymdbkeyschoicebox, 1, 7);
				gridPane.add(dsmasymvaultkeyschoicebox, 1, 7);
				gridPane.add(dsmsymvaultkeyschoicebox, 1, 7);
				gridPane.add(plaintextlabel, 0, 9);
				gridPane.add(plaintext, 1, 9);
				gridPane.add(encresultslabel, 0, 10);
				gridPane.add(enctext, 1, 10);
				gridPane.add(decryptbutton, 2, 11);
				gridPane.add(dectext, 1, 11);
				gridPane.add(errormessageHeader, 0, 12);
				gridPane.add(errormessage, 1, 12);
				gridPane.add(exportKeyButton, 0, 13);
				exportKeyButton.setVisible(false);
				errormessage.setVisible(false);
				errormessageHeader.setVisible(false);
				// Styling nodes
				// -fx-background-color: lightgreen;
					errormessage.setStyle("-fx-font: normal bold 15px 'serif' ");
				//errormessage.setStyle("-fx-base: red");
				errormessageHeader.setStyle("-fx-font: normal bold 15px 'serif' ");
				decryptbutton.setStyle("-fx-background-color: darkslateblue; -fx-textfill: white;");
				getkeysbutton.setStyle("-fx-background-color: darkslateblue; -fx-textfill: white;");
				encryptbutton.setStyle("-fx-background-color: darkslateblue; -fx-textfill: white;");
				// vormetricdomainslabel.setStyle("-fx-font: normal bold 15px
				// 'serif' ")
				exportKeyButton.setStyle("-fx-background-color: darkslateblue; -fx-textfill: white;");
				
				currentSelectionlabel2.setStyle("-fx-font: normal bold 20px 'serif' ");
				currentSelectionlabel.setStyle("-fx-font: normal bold 15px 'serif' ");

				plaintextlabel.setStyle("-fx-font: normal bold 15px 'serif' ");
				encresultslabel.setStyle("-fx-font: normal bold 15px 'serif' ");
				// dobLabel.setStyle("-fx-font: normal bold 15px 'serif' ");
				keysourcelabel.setStyle("-fx-font: normal bold 15px 'serif' ");
				keytypelabel.setStyle("-fx-font: normal bold 15px 'serif' ");
				// technologiesLabel.setStyle("-fx-font: normal bold 15px
				// 'serif' ");
				dsmkeyslabel.setStyle("-fx-font: normal bold 15px 'serif' ");
				vormetricdomainslabel.setStyle("-fx-font: normal bold 15px 'serif' ");
				dsmdomainpicked.setStyle("-fx-font: normal bold 20px 'serif' ");
				// Setting the back ground color
				gridPane.setStyle("-fx-background-color: BEIGE;");

				// Creating a scene object
				Scene scene = new Scene(gridPane);

				// Setting title to the Stage
				stage.setTitle("Vormetric Data Security Manager & Crypto Server REST Example");

				// Adding scene to the stage
				stage.setScene(scene);

				// Displaying the contents of the stage
				stage.show();

				String endpt = gr.dsmdomain + delimiter + "keys" + delimiter + SYMMETRIC + delimiter;
			
				gr.dsmsymkeytm = populateTM(dsmiptextField.getText(), emailtextField.getText(),
						passwordtextField.getText(), endpt, gr, dsmsymdbkeyschoicebox);
				
/*				gr.dsmsymkeysstrarray = populateysArray(dsmiptextField.getText(), emailtextField.getText(),
						passwordtextField.getText(), endpt, gr, dsmsymdbkeyschoicebox);*/
				
				endpt = gr.dsmdomain + delimiter + "keys" + delimiter + ASYMMETRIC + delimiter;
/*				gr.dsmasymkeysstrarray = populateysArray(dsmiptextField.getText(), emailtextField.getText(),
						passwordtextField.getText(), endpt, gr, dsmasymdbkeyschoicebox);*/
				gr.dsmasymkeytm = populateTM(dsmiptextField.getText(), emailtextField.getText(),
						passwordtextField.getText(), endpt, gr, dsmasymdbkeyschoicebox);				
				
				endpt = gr.dsmdomain + delimiter + "keys/vault" + delimiter + ASYMMETRIC + delimiter;
/*				gr.dsmasymvaultkeysstrarray = populateysArray(dsmiptextField.getText(), emailtextField.getText(),
						passwordtextField.getText(), endpt, gr, dsmasymvaultkeyschoicebox);*/
				gr.dsmasymvaultkeytm = populateTM(dsmiptextField.getText(), emailtextField.getText(),
						passwordtextField.getText(), endpt, gr, dsmasymvaultkeyschoicebox);			
				
				endpt = gr.dsmdomain + delimiter + "keys/vault" + delimiter + SYMMETRIC + delimiter;
/*				gr.dsmsymvaultkeysstrarray = populateysArray(dsmiptextField.getText(), emailtextField.getText(),
						passwordtextField.getText(), endpt, gr, dsmsymvaultkeyschoicebox);*/
				
				gr.dsmsymvaultkeytm = populateTM(dsmiptextField.getText(), emailtextField.getText(),
						passwordtextField.getText(), endpt, gr, dsmsymvaultkeyschoicebox);

				getkeysbutton.setOnMouseClicked((new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
				
						dsmkeyslabel.setVisible(true);
						dsmsymdbkeyschoicebox.setVisible(true);
						plaintextlabel.setVisible(true);
						plaintext.setVisible(true);
						encryptbutton.setVisible(true);
						exportKeyButton.setVisible(false);
						System.out.println("keytype " + gr.keytypevalue);
						System.out.println("keysource " + gr.keysourcevalue);
						System.out.println("dsmdomain " + gr.dsmdomain);
						System.out.println("dsmkeyscheckbox " + gr.dsmkeyscheckbox);
						System.out.println(" emailtextField.getText() " + emailtextField.getText());
						System.out.println(" passwd.getText() " + passwordtextField.getText());
						dsmsymvaultkeyschoicebox.setVisible(false);
						dsmsymdbkeyschoicebox.setVisible(false);
						dsmasymdbkeyschoicebox.setVisible(false);
						dsmasymvaultkeyschoicebox.setVisible(false);
						if (gr.keysourcevalue.equalsIgnoreCase("vault")
								&& gr.keytypevalue.equalsIgnoreCase(ASYMMETRIC)) {
							System.out.println("vault and asymmetric");
							dsmasymvaultkeyschoicebox.setVisible(true);
						} else if (gr.keysourcevalue.equalsIgnoreCase("vault")
								&& gr.keytypevalue.equalsIgnoreCase(SYMMETRIC)) {
							dsmsymvaultkeyschoicebox.setVisible(true);
							System.out.println("vault and symmetric");
						} else if (gr.keysourcevalue.equalsIgnoreCase("dsm db")
								&& gr.keytypevalue.equalsIgnoreCase(SYMMETRIC)) {
							dsmsymdbkeyschoicebox.setVisible(true);
							System.out.println("dsm db  and symmetric");
						} else if (gr.keysourcevalue.equalsIgnoreCase("dsm db")
								&& gr.keytypevalue.equalsIgnoreCase(ASYMMETRIC)) {
							dsmasymdbkeyschoicebox.setVisible(true);
							exportKeyButton.setVisible(true);
							System.out.println("dsm db  and asymmetric");
						} else {
							System.out.println("invalid combo");
						}

					}
				}));
				
				exportKeyButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						errormessage.setVisible(false);
						errormessageHeader.setVisible(false);
					
						//VormetricCryptoServerSettings vcs = new VormetricCryptoServerSettings();
						String action = "domains";
						String keypicked = gr.keypicked;
						System.out.println("keypicked before " + keypicked);
						String decryptdecryptkey = keypicked.substring(1, keypicked.length() - 1);
						System.out.println("keypicked after " + decryptdecryptkey);
						String results = null;
						TreeMap<String , String> tm = new TreeMap();
						System.out.println(" in export key with .... " + gr.dsmasymkeytm.get(gr.keypicked));
						if (gr.keytypevalue.equalsIgnoreCase(ASYMMETRIC))
						{
							try {
								String endpt = gr.dsmdomain + delimiter + "keys" + delimiter + ASYMMETRIC + delimiter + gr.dsmasymkeytm.get(gr.keypicked);
								tm = VormetricDSMHelper.getDomainObjectAttributeObjR(dsmiptextField.getText(), emailtextField.getText(),
										passwordtextField.getText(), endpt, action, "publicKey");
								//	passwordtextField.getText(), endpt, action, "publicKey");
								List<String> dsmarraylistresults = VormetricDSMHelper.keysAsList(tm);
								//dsmarraylistresults.forEach(System.out::println);
								
						        for (String string : dsmarraylistresults) {
						            System.out.println(string);
						            results = string;
						        }
						        
								dectext.setText(results);
								
								dectext.setVisible(true);
								// enctext.setEditable(false);
								// encresultslabel.setVisible(true);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								if (e.getMessage().contains("invalid path")) {
									errormessageHeader.setVisible(true);
									errormessage.setVisible(true);
									errormessage.setText("public key not found");
									decryptbutton.setVisible(false);
							//		System.out.println(e.getMessage() );
								}
							}
						}
						else
							System.out.println("Export of symmetric keys not supported");



					}
				}));
				

				decryptbutton.setOnMouseClicked((new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						errormessage.setVisible(false);
						errormessageHeader.setVisible(false);
						System.out.println("plain text " + enctext.getText());
						VormetricCryptoServerSettings vcs = new VormetricCryptoServerSettings();
						String action = "decrypt";
						String keypicked = gr.keypicked;
						System.out.println("keypicked before " + keypicked);
						String decryptdecryptkey = keypicked.substring(1, keypicked.length() - 1);
						System.out.println("keypicked after " + decryptdecryptkey);
						String results = null;
						if (gr.keytypevalue.equalsIgnoreCase(ASYMMETRIC))
							vcs.setvcsalg("RSA");
						else
							vcs.setvcsalg("A128CTR");

						try {
							results = VormetricCryptoServerHelper.doDeCryptData(vcs.getvcstokenserver(),
									vcs.getvcsuserid(), vcs.getvcspassword(), enctext.getText(), vcs.getvcsalg(),
									vcs.getvcsivnumber(), action, decryptdecryptkey);
							dectext.setText(results);
							dectext.setVisible(true);
							// enctext.setEditable(false);
							// encresultslabel.setVisible(true);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (e.getMessage().contains("400")) {
								errormessageHeader.setVisible(true);
								errormessage.setVisible(true);
								errormessage.setText("invalid key: please pick a key defined in vts");
								decryptbutton.setVisible(false);
						//		System.out.println(e.getMessage() );
							}
						}
						vcs.setvcsalg("A128CTR");
					}
				}));

				encryptbutton.setOnMouseClicked((new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						errormessage.setVisible(false);
						errormessageHeader.setVisible(false);
						dectext.setVisible(false);
						System.out.println("plain text " + plaintext.getText());
						VormetricCryptoServerSettings vcs = new VormetricCryptoServerSettings();
						String action = "encrypt";
						String keypicked = gr.keypicked;
						System.out.println("keypicked before " + keypicked);
						String encryptdecryptkey = keypicked.substring(1, keypicked.length() - 1);
						System.out.println("keypicked after " + encryptdecryptkey);
						String results = null;
						decryptbutton.setVisible(true);
						if (gr.keytypevalue.equalsIgnoreCase(ASYMMETRIC))
							vcs.setvcsalg("RSA");
						else
							vcs.setvcsalg("A128CTR");
						try {
							results = VormetricCryptoServerHelper.doEncryptData(vcs.getvcstokenserver(),
									vcs.getvcsuserid(), vcs.getvcspassword(), plaintext.getText(), vcs.getvcsalg(),
									vcs.getvcsivnumber(), action, encryptdecryptkey);
							enctext.setText(results);
							enctext.setVisible(true);
							// enctext.setEditable(false);
							encresultslabel.setVisible(true);
							encresultslabel.setVisible(true);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (e.getMessage().contains("400")) {
								errormessageHeader.setVisible(true);
								errormessage.setVisible(true);
								errormessage.setText("please pick a key defined in vts");
								decryptbutton.setVisible(false);
							//	System.out.println(e.getMessage() );
							}
						}
						vcs.setvcsalg("A128CTR");
					}
				}));

				keytypetogglegroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
						System.out.println(
								"in group groupReservation" + keytypetogglegroup.getSelectedToggle().getUserData());
						gr.keytypevalue = keytypetogglegroup.getSelectedToggle().getUserData().toString();
						currentSelectionlabel2.setText(gr.keysourcevalue + " " + gr.keytypevalue);
						dsmkeyslabel.setText(DSMKeyBUtton + gr.keysourcevalue + " " + gr.keytypevalue);
						dsmsymvaultkeyschoicebox.setVisible(false);
						dsmsymdbkeyschoicebox.setVisible(false);
						dsmasymdbkeyschoicebox.setVisible(false);
						exportKeyButton.setVisible(false);
						dsmasymvaultkeyschoicebox.setVisible(false);
						enctext.setVisible(false);
						dectext.setVisible(false);
						decryptbutton.setVisible(false);
						plaintextlabel.setVisible(false);
						plaintext.setVisible(false);
						encryptbutton.setVisible(false);
						encresultslabel.setVisible(false);
/*						if (gr.keytypevalue.equalsIgnoreCase(ASYMMETRIC))
							exportKeyButton.setVisible(true);
						else
							exportKeyButton.setVisible(false);*/
					}
				});

				keysourcegroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
					public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
						System.out
								.println("in group keysourcegroup" + keysourcegroup.getSelectedToggle().getUserData());
						gr.keysourcevalue = keysourcegroup.getSelectedToggle().getUserData().toString();
						currentSelectionlabel2.setText(gr.keysourcevalue + " " + gr.keytypevalue);
						dsmkeyslabel.setText(DSMKeyBUtton + gr.keysourcevalue + " " + gr.keytypevalue);
						dsmsymvaultkeyschoicebox.setVisible(false);
						dsmsymdbkeyschoicebox.setVisible(false);
						dsmasymdbkeyschoicebox.setVisible(false);
						dsmasymvaultkeyschoicebox.setVisible(false);
						enctext.setVisible(false);
						dectext.setVisible(false);
						decryptbutton.setVisible(false);
						plaintextlabel.setVisible(false);
						plaintext.setVisible(false);
						encryptbutton.setVisible(false);
						encresultslabel.setVisible(false);

					}
				});

				dsmsymvaultkeyschoicebox.getSelectionModel().selectedIndexProperty()
						.addListener(new ChangeListener<Number>() {
							public void changed(ObservableValue ov, Number value, Number new_value) {
								gr.keypicked = VormetricDSMHelper.keysAsArray(gr.dsmsymvaultkeytm)[new_value.intValue()];
							//	gr.keypicked = (gr.dsmsymvaultkeysstrarray[new_value.intValue()]);
								System.out.println("dsmsymvaultkeyschoicebox " + gr.keypicked);

							}
						});
				dsmasymvaultkeyschoicebox.getSelectionModel().selectedIndexProperty()
						.addListener(new ChangeListener<Number>() {
							public void changed(ObservableValue ov, Number value, Number new_value) {
								gr.keypicked = VormetricDSMHelper.keysAsArray(gr.dsmasymvaultkeytm)[new_value.intValue()];
								//gr.keypicked = (gr.dsmasymvaultkeysstrarray[new_value.intValue()]);
								System.out.println("dsmasymvaultkeyschoicebox " + gr.keypicked);
							}
						});
				dsmasymdbkeyschoicebox.getSelectionModel().selectedIndexProperty()
						.addListener(new ChangeListener<Number>() {
							public void changed(ObservableValue ov, Number value, Number new_value) {
								gr.keypicked = VormetricDSMHelper.keysAsArray(gr.dsmasymkeytm)[new_value.intValue()];
							//	gr.keypicked = (gr.dsmasymkeysstrarray[new_value.intValue()]);
								System.out.println("dsmasymdbkeyschoicebox " + gr.keypicked);
							}
						});

				dsmsymdbkeyschoicebox.getSelectionModel().selectedIndexProperty()
						.addListener(new ChangeListener<Number>() {
							public void changed(ObservableValue ov, Number value, Number new_value) {
								gr.keypicked = VormetricDSMHelper.keysAsArray(gr.dsmsymkeytm)[new_value.intValue()];	
								//gr.keypicked = (gr.dsmsymkeysstrarray[new_value.intValue()]);
								System.out.println("dsmsymdbkeyschoicebox " + gr.keypicked);
							}
						});

			}

		})); // mouse end... event...

		getDomainsButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				System.out.println("getDomainsButton ");
				VormetricDSMHelper vdsm = new VormetricDSMHelper();
				TreeMap<String, String> dsmtm = null;
				//vdsm.getDomainObjectAttributeListObj2(dsmserver, userid, password, endpt, action, attrib);
				List<String> dsmarraylistresults = null;
				//ArrayList<String> dsmarraylistresults = null;
				try {
/*					dsmarraylistresults = VormetricDSMHelper.getDomainObjectAttributeList(dsmiptextField.getText(),
							emailtextField.getText(), passwordtextField.getText(), "", "domains", "id");*/
					dsmtm = vdsm.getDomainObjectAttributeListObj2(dsmiptextField.getText(),emailtextField.getText(), passwordtextField.getText(), "", "domains", "name");
					dsmarraylistresults = VormetricDSMHelper.keysAsList(dsmtm);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//final String[] dsmdomainsstrarray = dsmarraylistresults.toArray(new String[dsmarraylistresults.size()]);
				final String[] dsmdomainsstrarray = VormetricDSMHelper.keysAsArray(dsmtm);
				gr.dsmdomainsstrarray = dsmdomainsstrarray;
				gr.dsmdomainstm = dsmtm;
				vormetricdomainscheckbox.getItems().addAll(dsmarraylistresults);
				vormetricdomainscheckbox.getSelectionModel().select(0);
				vormetricdomainscheckbox.setVisible(true);

			}
		}));

		clearButton.setOnMouseClicked((new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				emailtextField.setText("");
				passwordtextField.setText("");
				dsmiptextField.setText("");
				// stage.toBack();

			}
		}));
		vormetricdomainscheckbox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue ov, Number value, Number new_value) {
				String domainpicked = (gr.dsmdomainsstrarray[new_value.intValue()]);
				gr.dsmdomain = gr.dsmdomainstm.get(domainpicked);
				System.out.println("in dsmdomainsstrarray " + gr.dsmdomain);
			}
		});

	}

	public String[] populateysArray(String dsmip, String userid, String pwd, String endpt, GUIReplies gr,
			ChoiceBox cb) {

		String value = null;
		ArrayList<String> dsmkeysarraylistresults = null;
		try {
			dsmkeysarraylistresults = VormetricDSMHelper.getDomainObjectAttributeList(dsmip, userid, pwd, endpt,
					"domains", "name");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(dsmkeysarraylistresults);

		// Let us print the sorted list
		System.out.println("List after the use of" + " Collection.sort() :\n" + dsmkeysarraylistresults);
		// removed....
		// names = FXCollections.observableArrayList(dsmkeysarraylistresults);

		final String[] dsmkeysstrarray = dsmkeysarraylistresults.toArray(new String[dsmkeysarraylistresults.size()]);

		cb.getItems().addAll(dsmkeysarraylistresults);
		cb.getSelectionModel().select(0);

		return dsmkeysstrarray;
	}
	public TreeMap<String, String> populateTM(String dsmip, String userid, String pwd, String endpt, GUIReplies gr,
			ChoiceBox cb) {
		VormetricDSMHelper vdsm = new VormetricDSMHelper();
		TreeMap<String,String> tm = new TreeMap();
		String value = null;
		List<String> dsmkeysarraylistresults = null;
		try {
			tm =  vdsm.getDomainObjectAttributeListObj2(dsmip, userid, pwd, endpt,
					"domains", "name");
/*			dsmkeysarraylistresults = VormetricDSMHelper.getDomainObjectAttributeList(dsmip, userid, pwd, endpt,
					"domains", "name");*/

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dsmkeysarraylistresults =  VormetricDSMHelper.keysAsList(tm);
		//Collections.sort(dsmkeysarraylistresults);

		// Let us print the sorted list
		System.out.println("List after the use of" + " Collection.sort() :\n" + dsmkeysarraylistresults);
		// removed....
		// names = FXCollections.observableArrayList(dsmkeysarraylistresults);

		final String[] dsmkeysstrarray = VormetricDSMHelper.keysAsArray(tm);
		cb.getItems().addAll(dsmkeysarraylistresults);
		cb.getSelectionModel().select(0);

		return tm;
	}
	

	public static void main(String args[]) {
		launch(args);
	}
}