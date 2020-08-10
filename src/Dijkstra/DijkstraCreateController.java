package Dijkstra;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import VertexGestion.VertexConverter;
import graph.Edge;
import graph.Vertex;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * 
 * @author Neil Farmer
 *
 */
public class DijkstraCreateController {

    @FXML
    private ComboBox<String> EndingVertex;

    @FXML
    private ComboBox<String> StartingVertex;

    @FXML
    private Button CreateButton;

    @FXML
    private Button CancelButton;
    
    //List of all vertex
    private List<Vertex> ListVertex;
    
    //List of all edge
    private List<Edge> ListEdge;
    
    //true if the graph is oriented, else false
    private boolean isOriented;
    
    //Class use to convert vertex to string and then display them in combobox
	private VertexConverter vConvert;

    @FXML
    void PressCancelButton(ActionEvent event) {
    	//Close te stage
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void PressCreateButton(ActionEvent event) {
    	//Run a new interface to show Dijkstra algorithm
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/Dijkstra/DijkstraDisplay.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			DijkstraDisplayController vc = fxmlLoader.<DijkstraDisplayController>getController();
			Vertex start = this.vConvert.getVertex(this.StartingVertex.getValue());

			Vertex end = null;
			if(!this.EndingVertex.getValue().equals("none")) {
				end = this.vConvert.getVertex(this.EndingVertex.getValue());
			}
				
			vc.initInterface(start, end, this.ListVertex, this.ListEdge, this.isOriented);

			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Dijkstra's algorithm");

			String ToIcon = "\\ressources\\Icon\\GraphApp_icon.png";
			Path currentRelativePath = Paths.get("");
			Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
			String TotalPath = "file:" + curentAbsoluteDirectory.toString() + ToIcon;
			stage.getIcons().add(new Image(TotalPath));

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Close the stage
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Function to init the interface
     * @param vertex (List Vertex ) : List of vertex on the graph
     * @param edge (List Edge) : List of edge on the graph
     * @param _isOriented (boolean) : true if the graph is oriented, else false
     */
    public void initInterface(List<Vertex> vertex, List<Edge> edge, boolean _isOriented) {
    	this.ListVertex = vertex;
    	this.ListEdge = edge;
    	this.isOriented = _isOriented;
    	
    	this.vConvert = new VertexConverter(vertex);
    	
		//Put the vertex in comboboxes
    	this.StartingVertex.getItems().addAll(vConvert.getListVertexKey());
    	this.StartingVertex.setValue(vConvert.getListVertexKey().get(0));
    	
    	this.EndingVertex.getItems().addAll(vConvert.getListVertexKey());
    	this.EndingVertex.getItems().add("none");
    	this.EndingVertex.setValue("none");
    }

}