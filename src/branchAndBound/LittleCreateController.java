package branchAndBound;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import error.ErrorInterfaceController;
import graph.Edge;
import graph.Graph;
import graph.Vertex;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import loading.LoadingWindowController;

public class LittleCreateController {

	//Button to launch the algorithm
    @FXML
    private Button CreateButton;

    //Button to cancel the algorithm
    @FXML
    private Button CancelButton;

    //Color of path viewed
    @FXML
    private ColorPicker ColorActualPath;

    //Color of the best path
    @FXML
    private ColorPicker ColorBestPath;
    
    //Pane where the graph will be display
    private Pane Overlay;
    
    //True if the graph is oriented
    private boolean isOriented;

    //To close and don't run the algo
    @FXML
    void PressCancelButton(ActionEvent event) {	
		//Close the stage
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }

    //To run the algo
    @FXML
    void PressCreateButton(ActionEvent event) {
    	try {
			List<Vertex> vList = Graph.getListVertex(this.Overlay);
			List<Edge> eList = Graph.getListEdge(this.Overlay);
			
			//Create the object for the algo
			Little l = new Little(vList, eList, isOriented);
			
			//Thread to do the algo in background
	        Task<Void> task = new Task<Void>() {
	            @Override
	            protected Void call() throws Exception {
	            	//Run the algorithm
	            	l.runAlgo();
	                return null;
	            }
	        };
	        
	        //Loading window where user can see what happen and stop if he want
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loading/LoadingWindow.fxml"));
			Parent root1;
			root1 = (Parent) fxmlLoader.load();

			LoadingWindowController lwc = fxmlLoader.<LoadingWindowController>getController();
			lwc.init_interface("Search TSP best solution ...", task);

			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Running Little's algorithm ...");

			String ToIcon = "\\ressources\\Icon\\GraphApp_icon.png";
			Path currentRelativePath = Paths.get("");
			Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
			String TotalPath = "file:" + curentAbsoluteDirectory.toString() + ToIcon;
			stage.getIcons().add(new Image(TotalPath));

			stage.show();
	        
			//If the algo finished normally
	        task.setOnSucceeded(e -> {
	        	try {
	        		//Display solution found by the algo 
	        		
	    			FXMLLoader fxmlLoaderSucceed = new FXMLLoader(
	    					getClass().getResource("/branchAndBound/LittleDisplay.fxml"));
	    			Parent rootSucceed = (Parent) fxmlLoaderSucceed.load();

	    			LittleDisplayController ldc = fxmlLoaderSucceed.<LittleDisplayController>getController();
	    			ldc.initInterface(vList, eList, isOriented, l, this.ColorActualPath.getValue(), this.ColorBestPath.getValue());
	    			
	    			Stage stageSucceed = new Stage();
	    			stageSucceed.setScene(new Scene(rootSucceed));
	    			stageSucceed.setTitle("Little's Algorithm");

	    			String ToIconSucceed = "\\ressources\\Icon\\GraphApp_icon.png";
	    			Path currentRelativePathSucceed = Paths.get("");
	    			Path curentAbsoluteDirectorySucceed = currentRelativePathSucceed.toAbsolutePath();
	    			String TotalPathSucceed = "file:" + curentAbsoluteDirectorySucceed.toString() + ToIconSucceed;
	    			stageSucceed.getIcons().add(new Image(TotalPathSucceed));

	    			stageSucceed.show();
	    		} catch (IOException e4) {
	    			e4.printStackTrace();
	    		}
	        	
	        	//Close the loading window
	        	lwc.close();
	        });
	        
	        //If the user click on cancel
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
	        
	        //If the algo failled
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

			//Close the stage
	        Stage ThisStage = (Stage) CancelButton.getScene().getWindow();
	        ThisStage.close();
			
	        //LAunch the thread where the algo will run
	        new Thread(task).start();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}
    
    /**
     * Function to give all element necessary to run the algo
     * @param p (Pane) : Pane where the graph is display
     * @param isOrient (boolean) : true if the graph is oriented
     */
    public void init_interface(Pane p, boolean isOrient) {
    	this.Overlay = p;
    	this.isOriented = isOrient;
    	this.ColorActualPath.setValue(Color.RED);
    	this.ColorBestPath.setValue(Color.BLUE);
    }

}
