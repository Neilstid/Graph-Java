package graphCreation;

import application.MainInterface_Controller;
import graph.GenerateGraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RandomGraphController {

    @FXML
    private ColorPicker ColorVertex;

    @FXML
    private TextField NumOfVertex;

    @FXML
    private TextField NumOfEdge;

    @FXML
    private TextField MaxWeight;

    @FXML
    private Button CreateButton;

    @FXML
    private Button CancelButton;
    
    private MainInterface_Controller application;
    
    @FXML
    private Label ErrorOutput;

    @FXML
    void PressCancelButton(ActionEvent event) {
		//Close the stage
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void PressCreateButton(ActionEvent event) {
    	if(Integer.valueOf(NumOfVertex.getText()) == null) {
    		ErrorOutput.setText("The number of vertex must be entered !");
    	} else if(Integer.valueOf(NumOfEdge.getText()) == null) {
    		ErrorOutput.setText("The number of edge must be entered !");
    	} else if(Integer.valueOf(MaxWeight.getText()) == null) {
    		ErrorOutput.setText("The maximum weight must be entered !");
    	} else {
    		int sum = (Integer.valueOf(NumOfVertex.getText()) * (Integer.valueOf(NumOfVertex.getText())+1))/2;
    		
    		if(Integer.valueOf(NumOfEdge.getText()) >= sum) {
    			ErrorOutput.setText("The number of edge is too big !");
    		}else {
    			//generate the random graph
        		GenerateGraph.CreateRandomGraph(this.application, Integer.valueOf(NumOfVertex.getText()), ColorVertex.getValue(), Integer.valueOf(NumOfEdge.getText()), Integer.valueOf(MaxWeight.getText()));
            	
    			//Close the stage
    	        Stage stage = (Stage) CancelButton.getScene().getWindow();
    	        stage.close();
    		}
    	}
    }
    
    public void init_interface(MainInterface_Controller app) {
    	this.application = app;
    	this.ColorVertex.setValue(app.setting.getColorVertexDefault());
    }

}
