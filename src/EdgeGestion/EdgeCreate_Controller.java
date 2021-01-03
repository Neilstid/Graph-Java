package EdgeGestion;

import application.MainInterface_Controller;
import graph.Edge;
import graph.Graph;
import graph.Vertex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import VertexGestion.VertexConverter;

/**
 * 
 * @author Neil Farmer
 * The controller of the interface to create an edge
 *
 */
public class EdgeCreate_Controller {
	//List of all vertex
	@FXML
	private ComboBox<String> Vertex1List;

	//List of all vertex
	@FXML
	private ComboBox<String> Vertex2List;

	//The field to enter the weight of the edge
	@FXML
	private TextField WeightEdge;

	//Button to create
	@FXML
	private Button CreateButton;

	//Button to cancel
	@FXML
	private Button CancelButton;

	//Label to show error on creation if there is one
	@FXML
	private Label ErrorOutput;

	private MainInterface_Controller application;
	/**Link to the main interface of the application
	 * @see MainInterface_Controller
	 */
	
	private VertexConverter vConvert;
	/**Class to convert Vertex into String and String into Vertex
	 * @see VertexConverter
	 */
	
	//Action of the cancel button
	@FXML
	void PressCancelButton(ActionEvent event) {	
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
	}

	//Action of the create button
	@FXML
	void PressCreateBtton(ActionEvent event) {
		//Fist we verified if there is another edge between the two vertex and if there's one if it is oriented the same way
		int sameOriented = 0;
		int diferentOriented = 0;
		
		//take all edge from the first vertex selected
	    ArrayList<Edge> EdgeList = new ArrayList<Edge>(this.vConvert.getVertex(this.Vertex1List.getValue()).linkToMe);
	    //take all edge from the second vertex selected
	    ArrayList<Edge> EdgeListVertex2 = new ArrayList<Edge>(this.vConvert.getVertex(this.Vertex2List.getValue()).linkToMe);
		//Keep only edge in both list
	    EdgeList.retainAll(EdgeListVertex2);
		
		//for each edge from the first vertex to the second
		for(Edge e:EdgeList) {
			//check if an edge has the same orientation of the new edge
			if(e.getV1() == this.vConvert.getVertex(this.Vertex1List.getValue()) && e.getV1() == this.vConvert.getVertex(this.Vertex1List.getValue())){
				sameOriented++;
				break;
			}else {
				diferentOriented++;
			}
		}
		
		//Check for error
		if (Integer.valueOf(WeightEdge.getText()) == null) {
		//If there's no weight entered
			ErrorOutput.setText("Error : A weight must be entered");
		}else if(this.vConvert.getVertex(this.Vertex1List.getValue()) == this.vConvert.getVertex(this.Vertex2List.getValue())) {
		//If the two vertex selected are the same	
			ErrorOutput.setText("Error : A vertex can't be the start and end of an edge");
		}else if(sameOriented > 0) {
			ErrorOutput.setText("Error : An edge already connect those two vertex");
		}else if(diferentOriented > 0 && !this.application.isOriented) {
			ErrorOutput.setText("Error : An edge already connect those two vertex");
		}else{
			//Create the edge
			Edge e = new Edge(this.vConvert.getVertex(this.Vertex1List.getValue()), this.vConvert.getVertex(this.Vertex2List.getValue()), Integer.valueOf(WeightEdge.getText()), this.application, diferentOriented);
			e.draw();
				
			//Close the stage
	        Stage stage = (Stage) CancelButton.getScene().getWindow();
	        stage.close();
		}
	}
	
	//Initialize the interface
	/**
	 * Function to give all variable that are necessary to run the application
	 * @param app (MainInterface_Controller) : Link to the main application
	 * @param v1 (Vertex) : Link to first vertex
	 * @param v2 (Vertex) : Link to second vertex
	 */
	public void init_interface(MainInterface_Controller app, Vertex v1, Vertex v2) {
		//Get all the vertex
		List<Vertex> result = Graph.getListVertex(app.Overlay);
    	//Convert the vertex into string to put them in combobox
		this.vConvert = new VertexConverter(result);
    	
		//Put the vertex in comboboxes
    	this.Vertex1List.getItems().addAll(vConvert.getListVertexKey());
    	this.Vertex1List.setValue(v1.getName());
    	this.Vertex2List.getItems().addAll(vConvert.getListVertexKey());
    	this.Vertex2List.setValue(v2.getName());
    	
    	this.application = app;
	}

}
