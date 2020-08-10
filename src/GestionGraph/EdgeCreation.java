package GestionGraph;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import EdgeGestion.EdgeCreate_Controller;
import application.MainInterface_Controller;
import graph.Vertex;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Class to show up the interface for edge creation when 2 vertex have been selected
 * @author Neil Farmer
 */
public class EdgeCreation {

	private MainInterface_Controller application;
	/**
	 * Link to the main interface of the application
	 * @see MainInterface_Controller
	 */
	
	private Vertex v1;
	/**
	 * Link to the first vertex selected
	 * @see Vertex
	 */

	//Constructor
	/**
	 * Constructor
	 * @param app (MainInterface_Controller) : Link to the main interface of the application 
	 */
	public EdgeCreation(MainInterface_Controller app) {
		this.application = app;
	}

	//Function that is called when 2 vertex have been selected
	/**
	 * Function that is called when 2 vertex have been selected
	 * @param secondVertex (Vertex) : Second vertex selected
	 */
	private void creationEdge(Vertex secondVertex) {
		//Show the interface to create an edge between the 2 vertex
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EdgeGestion/EdgeCreation.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			EdgeCreate_Controller evc = fxmlLoader.<EdgeCreate_Controller>getController();
			evc.init_interface(this.application, this.v1, secondVertex);

			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("New Edge");

			String ToIcon = "\\ressources\\Icon\\GraphApp_icon.png";
			Path currentRelativePath = Paths.get("");
			Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
			String TotalPath = "file:" + curentAbsoluteDirectory.toString() + ToIcon;
			stage.getIcons().add(new Image(TotalPath));

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to create an edge
	 * @param v (Vertex) : Vertex chosen by the user
	 */
	public void newEdge(Vertex v) {
		if (this.v1 == null) {
			this.v1 = v;
		} else {
			creationEdge(v);
			this.v1 = null;
		}
	}

	/**
	 * Function to clean the class (If a vertex has been selected, it will be forgotten)
	 */
	public void empty() {
		this.v1 = null;
	}
}
