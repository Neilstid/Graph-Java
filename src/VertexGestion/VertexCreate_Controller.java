package VertexGestion;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import application.MainInterface_Controller;
import graph.Graph;
import graph.Vertex;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class VertexCreate_Controller implements Initializable {

	@FXML
    private ColorPicker ColorVertex;

    @FXML
    private TextField NameVertex;

    @FXML
    private Button CreateVertex;

    @FXML
    private Button CancelVertex;
    
    @FXML
    private Label ErrorOutput;

    private int x;
    
    private int y;
    
    private MainInterface_Controller application;
    
    /**
     * Function to give data that are important for this interface
     * @param _x (int) : x position
     * @param _y (int) : y position
     * @param app (MainInterface_Controller) : Link to the main application
     * @param col (Color) : Color of the vertex
     */
    public void init_interface(int _x, int _y, MainInterface_Controller app, Color col) {
    	ColorVertex.setValue(col);
    	this.x = _x;
    	this.y = _y;
    	this.application = app;
		this.NameVertex.requestFocus();
    }
    
    @FXML
    void PressCancelVertex(ActionEvent event) {
        Stage stage = (Stage) CancelVertex.getScene().getWindow();
        stage.close();
    }

	Predicate<Node> GetVertex = node -> node.getClass().equals(Vertex.class);
    
    @FXML
    void PressCreateVertex(ActionEvent event) {
    	List<Vertex> result = new ArrayList<Vertex>();
    	result = Graph.getListVertex(this.application.Overlay);
    	List<String> name_Vertex = result.stream().map(p -> p.getName()).collect(Collectors.toList()); 
    	
    	if(NameVertex.getText().isBlank()) {
    		ErrorOutput.setText("Error : The vertex need a name !");
    	}else if(name_Vertex.contains(NameVertex.getText())){
    		ErrorOutput.setText("Error : This name is already use !");
    	}else{
        	Vertex v = new Vertex(new SimpleDoubleProperty(this.x), new SimpleDoubleProperty(this.y), this.application, ColorVertex.getValue(), NameVertex.getText());
    		v.draw();
    		
            Stage stage = (Stage) CancelVertex.getScene().getWindow();
            stage.close();
    	}
    }

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.NameVertex.requestFocus();
	}
	
}
