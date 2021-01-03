package loading;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class LoadingWindowController {

    @FXML
    private ProgressBar LoadingBar;

    @FXML
    private Label LoadingMessage;
    
    @FXML
    private Button CancelButton;

    private Task<Void> task;
    
    @FXML
    void PressCancel(ActionEvent event) {
    	//Cancel the task
    	task.cancel();
    }
    
    /**
     * Funtion to init the graph
     * @param message (String) : message to print to user
     * @param _task (Task) : Task run by the user
     */
    public void init_interface(String message, Task<Void> _task) {
    	this.LoadingMessage.setText(message);
    	this.LoadingBar.setProgress(-1f);
    	this.task = _task;
    }

    public void close() {
    	Stage stage = (Stage) this.LoadingBar.getScene().getWindow();
    	stage.close();
    }
    
}
