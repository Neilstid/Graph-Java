package EdgeGestion;

import java.util.ArrayList;

import application.MainInterface_Controller;
import graph.Edge;
import graph.Vertex;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * 
 * @author Neil Farmer
 *
 */
public class EdgeModifyController {

	@FXML
	private Button ReverseVertex;

	@FXML
	private TextField Weight;

	@FXML
	private Button ModifyButton;

	@FXML
	private Button DeleteButton;

	@FXML
	private Button CancelButton;

	@FXML
	private Label Vertex;

	@FXML
	private Label ErrorOutput;

	//Link to the edge to modify
	private Edge edgeModify;
	
	//Link to the main interface
	private MainInterface_Controller application;

	@FXML
	void PressCancelButton(ActionEvent event) {
		// Close the stage
		Stage stage = (Stage) CancelButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	void PressDeleteButton(ActionEvent event) {
		this.edgeModify.delete();

		// Close the stage
		Stage stage = (Stage) CancelButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	void PressModifyButton(ActionEvent event) {
		if (Integer.valueOf(Weight.getText()) == null) {
			// If there's no weight entered
			ErrorOutput.setText("Error : A weight must be entered");
		} else {
			this.edgeModify.setWeight(Integer.valueOf(Weight.getText()));
			
			// Close the stage
			Stage stage = (Stage) CancelButton.getScene().getWindow();
			stage.close();
		}
	}

	//function to reverse the edge
	@FXML
	void PressReverseVertex(ActionEvent event) {
		//It will create a new edge with all the parameter of the other and delete the other
		
	    ArrayList<Edge> EdgeList = new ArrayList<Edge>(this.edgeModify.getV1().linkToMe);
		ArrayList<Edge> EdgeListVertex2 = new ArrayList<Edge>(this.edgeModify.getV2().linkToMe);
		EdgeList.retainAll(EdgeListVertex2);
		
		if(EdgeList.size() == 2) {
			ErrorOutput.setText("Error : An edge already connect those two vertex");
			return;
		}
		
		Vertex v1 = this.edgeModify.getV2();
		Vertex v2 = this.edgeModify.getV1();
		int w = this.edgeModify.getWeight();
		
		this.edgeModify.delete();
		this.edgeModify = new Edge(v1, v2, w, this.application, 0);
		this.edgeModify.draw();
		
		this.Vertex.setText(String.valueOf(this.edgeModify.getV1().getName()) + " -> " + String.valueOf(this.edgeModify.getV2().getName()));
	}

	/**
	 * Function to init the interface
	 * @param e (Edge) : Link to the edge to modify
	 * @param app (MainInterface_Controller) : Link to the main interface
	 */
	public void init_interface(Edge e, MainInterface_Controller app) {
		this.application = app;
		this.edgeModify = e;
		this.Weight.setText(String.valueOf(e.getWeight()));
		this.Vertex.setText(String.valueOf(e.getV1().getName()) + " -> " + String.valueOf(e.getV2().getName()));
	}

}
