package VertexGestion;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import application.MainInterface_Controller;
import graph.Graph;
import graph.Vertex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VertexModifyController {

    @FXML
    private ColorPicker ColorVertex;

    @FXML
    private TextField NameText;

    @FXML
    private Button ModifyButton;

    @FXML
    private Button DeleteButton;

    @FXML
    private Button CancelButton;
    
    @FXML
    private Label ErrorOutput;

    private Vertex vertexModify;
    
    private MainInterface_Controller application;
    
    private String startName;
    
    @FXML
    void PressCancelButton(ActionEvent event) {
        Stage stage = (Stage) this.CancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void PressDeleteButton(ActionEvent event) {
    	this.vertexModify.delete();
    	
        Stage stage = (Stage) this.CancelButton.getScene().getWindow();
        stage.close();
    }

    Predicate<Node> GetVertex = node -> node.getClass().equals(Vertex.class);
    
    @FXML
    void PressModifyButton(ActionEvent event) {
    	List<Vertex> result = new ArrayList<Vertex>();
    	result = Graph.getListVertex(this.application.Overlay);
    	List<String> name_Vertex = result.stream().map(p -> p.getName()).collect(Collectors.toList()); 
    	
    	if(NameText.getText().isBlank()) {
    		ErrorOutput.setText("Error : The vertex need a name !");
    	}else if(!NameText.getText().toString().equals(startName) && name_Vertex.contains(NameText.getText())){
    		ErrorOutput.setText("Error : This name is already use !");
    	}else{
    		this.vertexModify.setName(this.NameText.getText());
    		this.vertexModify.setColor(this.ColorVertex.getValue());
    		
            Stage stage = (Stage) this.CancelButton.getScene().getWindow();
            stage.close();
    	}
    }

    /**
     * Function to init the interface
     * @param v (Vertex) : Vertex to modify
     * @param app (MainInterface_Controller) : Link to the main interface
     */
    public void init_interface(Vertex v, MainInterface_Controller app) {
    	this.vertexModify = v;
    	this.ColorVertex.setValue(v.getColor());
    	this.NameText.setText(v.getName());
    	this.startName = v.getName();
    	this.application = app;
    }
}
