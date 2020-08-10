package parameter;

import java.io.File;
import java.util.List;

import application.MainInterface_Controller;
import graph.Edge;
import graph.Graph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SaveAsController {

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
    
    private MainInterface_Controller application;
    private File f;

    @FXML
    void PressBrowse(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
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
    	//Check for error
    	if(this.f == null) {
    		this.ErrorOutput.setText("You must enter an output file !");
    	}else {
        	List<Edge> edgeList = Graph.getListEdge(this.application.Overlay);
        	for(Edge e:edgeList) {
        		e.setControlVisible(false);
        	}
    		
        	//Search the extension of the file
    		String extension = "";
    		int i = this.f.toString().lastIndexOf(".");
    		if (i > 0) {
    			extension = this.f.toString().substring(i+1);
    		}
    		
    		if(extension.equals("png")) {
    			SaveGraph.saveAsPng(this.f, this.application.Overlay);
    		}	
    		
    		if(this.application.userElementSelected == 0) {
            	for(Edge e:edgeList) {
            		e.setControlVisible(true);
            	}
    		}
    		
            Stage stage = (Stage) this.CancelButton.getScene().getWindow();
            stage.close();
    	}
    }
    
    /**
     * Function to initialize the interface
     * @param app (MainInterface_Controller) : Link to the main application
     */
    public void init_interface(MainInterface_Controller app) {
    	this.application = app;
    }

}
