package parameter;

import java.io.File;

import application.MainInterface_Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LoadInterfaceController {

	@FXML
	private TextField TextLinkToFile;

	@FXML
	private Button BrowseButton;

	@FXML
	private Button SaveButton;

	@FXML
	private Button CancelButton;

	@FXML
	private Label ErrorOutput;

	private File f;

	private MainInterface_Controller application;

	@FXML
	void PressBrowse(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("xml files (*.xml)", "*.xml"));
		File file = fileChooser.showSaveDialog(null);

		this.f = file;
		this.TextLinkToFile.setText(file.toString());
	}

	@FXML
	void PressCancellButton(ActionEvent event) {
		Stage stage = (Stage) this.CancelButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	void PressSaveButton(ActionEvent event) {
		// Check for error
		if (this.f == null) {
			this.ErrorOutput.setText("You must enter an input file !");
		} else {
			this.application.Overlay.getChildren().clear();
			
			LoadFile.LoadXML(this.f, this.application);
			
			Stage stage = (Stage) this.CancelButton.getScene().getWindow();
			stage.close();
		}
	}

	/**
	 * Function to initialize the interface
	 * 
	 * @param app (MainInterface_Controller) : Link to the main application
	 */
	public void init_interface(MainInterface_Controller app) {
		this.application = app;
	}
}
