package error;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * 
 * @author Neil Farmer
 *
 */
public class ErrorInterfaceController {

	//label to print the error message
    @FXML
    private Label ErrorOutput;

    @FXML
    private Button OKButton;

    //When the user click on ok button
    @FXML
    void PressOkButton(ActionEvent event) {
		//Close the stage
        Stage stage = (Stage) OKButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Function to init the interface
     * @param errorMessage (String) : Error message
     */
    public void initInterface(String errorMessage) {
    	//Display the error message
    	ErrorOutput.setText(errorMessage);
    }
}
