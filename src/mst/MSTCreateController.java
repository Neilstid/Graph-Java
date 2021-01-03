package mst;

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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MSTCreateController {
    @FXML
    private ColorPicker ColorUsedEdge;

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
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/mst/MSTDisplay.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			MSTDisplayController vc = fxmlLoader.<MSTDisplayController>getController();
			Vertex start = this.vConvert.getVertex(this.StartingVertex.getValue());
				
			vc.initInterface(start, this.ListVertex, this.ListEdge, this.ColorUsedEdge.getValue());

			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Minimum spanning tree");

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
     */
    public void initInterface(List<Vertex> vertex, List<Edge> edge) {
    	this.ListVertex = vertex;
    	this.ListEdge = edge;
    	
    	this.vConvert = new VertexConverter(vertex);
    	
		//Put the vertex in comboboxes
    	this.StartingVertex.getItems().addAll(vConvert.getListVertexKey());
    	this.StartingVertex.setValue(vConvert.getListVertexKey().get(0));
    	
    	this.ColorUsedEdge.setValue(Color.RED);
    }
}
