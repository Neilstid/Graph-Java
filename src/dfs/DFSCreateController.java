package dfs;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import VertexGestion.VertexConverter;
import bfs.BFS;
import bfs.BFSDisplayController;
import error.ErrorInterfaceController;
import graph.Edge;
import graph.Vertex;
import javafx.concurrent.Task;
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
import loading.LoadingWindowController;

public class DFSCreateController {
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
		
		private boolean isOriented;

	    @FXML
	    void PressCancelButton(ActionEvent event) {
	    	//Close the stage
	        Stage stage = (Stage) CancelButton.getScene().getWindow();
	        stage.close();
	    }

	    @FXML
	    void PressCreateButton(ActionEvent event) {
	    	try {
				DFS dfs = new DFS(vConvert.getVertex(this.StartingVertex.getValue()), this.ListVertex, this.ListEdge, this.isOriented);
		        Task<Void> task = new Task<Void>() {
		            @Override
		            protected Void call() throws Exception {
		            	dfs.solve();
		                return null;
		            }
		        };
				
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loading/LoadingWindow.fxml"));
				Parent root1;
				root1 = (Parent) fxmlLoader.load();

				LoadingWindowController lwc = fxmlLoader.<LoadingWindowController>getController();
				lwc.init_interface("Running DFS algorithm ...", task);

				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.setTitle("Running DFS algorithm ...");

				String ToIcon = "\\ressources\\Icon\\GraphApp_icon.png";
				Path currentRelativePath = Paths.get("");
				Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
				String TotalPath = "file:" + curentAbsoluteDirectory.toString() + ToIcon;
				stage.getIcons().add(new Image(TotalPath));

				stage.show();
		        
		        task.setOnSucceeded(e -> {
		        	try {
						FXMLLoader fxmlLoaderDisplay = new FXMLLoader(
								getClass().getResource("/dfs/DFSDisplay.fxml"));
						Parent Displayroot1 = (Parent) fxmlLoaderDisplay.load();

						DFSDisplayController ddc = fxmlLoaderDisplay.<DFSDisplayController>getController();
						ddc.initInterface(this.ListVertex, this.ListEdge, this.isOriented, this.ColorUsedEdge.getValue(), dfs);

						Stage stageDisplay = new Stage();
						stageDisplay.setScene(new Scene(Displayroot1));
						stageDisplay.setTitle("DFS");

						String ToIconDisplay = "\\ressources\\Icon\\GraphApp_icon.png";
						Path currentRelativePathDisplay = Paths.get("");
						Path curentAbsoluteDirectoryDisplay = currentRelativePathDisplay.toAbsolutePath();
						String TotalPathDisplay = "file:" + curentAbsoluteDirectoryDisplay.toString() + ToIconDisplay;
						stageDisplay.getIcons().add(new Image(TotalPathDisplay));	
						
						stageDisplay.show();
					} catch (IOException e3) {
						e3.printStackTrace();
					}
		        	
		        	lwc.close();
		        });
		        task.setOnCancelled(e -> {
		        	try {
						FXMLLoader fxmlLoaderError = new FXMLLoader(
								getClass().getResource("/error/ErrorInterface.fxml"));
						Parent Errorroot1 = (Parent) fxmlLoaderError.load();

						ErrorInterfaceController eic = fxmlLoaderError.<ErrorInterfaceController>getController();
						eic.initInterface("Error : the task has been stoped by the user !");

						Stage stageError = new Stage();
						stageError.setScene(new Scene(Errorroot1));
						stageError.setTitle("Error !");

						String ToIconError = "\\ressources\\Icon\\ErrorIcon.png";
						Path currentRelativePathError = Paths.get("");
						Path curentAbsoluteDirectoryError = currentRelativePathError.toAbsolutePath();
						String TotalPathError = "file:" + curentAbsoluteDirectoryError.toString() + ToIconError;
						stageError.getIcons().add(new Image(TotalPathError));	
						
						stageError.show();
					} catch (IOException e3) {
						e3.printStackTrace();
					}
		        	
		        	lwc.close();
		        });
		        task.setOnFailed(e -> {
		        	try {
						FXMLLoader fxmlLoaderError = new FXMLLoader(
								getClass().getResource("/error/ErrorInterface.fxml"));
						Parent Errorroot1 = (Parent) fxmlLoaderError.load();

						ErrorInterfaceController eic = fxmlLoaderError.<ErrorInterfaceController>getController();
						eic.initInterface("Error : the task has been stoped for an unknown reason !");

						Stage stageError = new Stage();
						stageError.setScene(new Scene(Errorroot1));
						stageError.setTitle("Error !");

						String ToIconError = "\\ressources\\Icon\\ErrorIcon.png";
						Path currentRelativePathError = Paths.get("");
						Path curentAbsoluteDirectoryError = currentRelativePathError.toAbsolutePath();
						String TotalPathError = "file:" + curentAbsoluteDirectoryError.toString() + ToIconError;
						stageError.getIcons().add(new Image(TotalPathError));	
						
						stageError.show();
					} catch (IOException e3) {
						e3.printStackTrace();
					}
		        	
		        	lwc.close();
		        });

				
		        new Thread(task).start();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
			Stage stage = (Stage) this.CancelButton.getScene().getWindow();
			stage.close();
	    }
	    
	    /**
	     * Function to init the interface
	     * @param vertex (List Vertex ) : List of vertex on the graph
	     * @param edge (List Edge) : List of edge on the graph
	     */
	    public void initInterface(List<Vertex> vertex, List<Edge> edge, boolean isOrient) {
	    	this.ListVertex = vertex;
	    	this.ListEdge = edge;
	    	this.isOriented = isOrient;
	    	
	    	this.vConvert = new VertexConverter(vertex);
	    	
			//Put the vertex in comboboxes
	    	this.StartingVertex.getItems().addAll(vConvert.getListVertexKey());
	    	this.StartingVertex.setValue(vConvert.getListVertexKey().get(0));
	    	
	    	this.ColorUsedEdge.setValue(Color.RED);
	    }
}
