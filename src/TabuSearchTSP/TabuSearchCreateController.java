package TabuSearchTSP;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import application.MainInterface_Controller;
import error.ErrorInterfaceController;
import graph.Graph;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import loading.LoadingWindowController;

public class TabuSearchCreateController {

    @FXML
    private ColorPicker ColorBestPath;

    @FXML
    private TextField NumIteration;

    @FXML
    private TextField LengthTabuList;

    @FXML
    private Button RunButton;

    @FXML
    private Button CancelButton;
    
    @FXML
    private Label ErrorOutput;
    
    @FXML
    private ComboBox<String> TypeTabuElement;
    
    public MainInterface_Controller application;

    @FXML
    void PressCancel(ActionEvent event) {
		Stage stage = (Stage) this.CancelButton.getScene().getWindow();
		stage.close();
    }

    @FXML
    void PressRun(ActionEvent event) {
    	if (Integer.valueOf(NumIteration.getText()) == null) {
    		ErrorOutput.setText("A number of iteration must be entered");
    	}else if(Integer.valueOf(LengthTabuList.getText()) == null) {
    		ErrorOutput.setText("A size of tabu list must be entered");
    	}else {
    		try {
    			int type;
    			if(this.TypeTabuElement.getValue() == "First vertex") {
    				type = 0;
    			}else if(this.TypeTabuElement.getValue() == "Second vertex") {
    				type = 1;
    			}else {
    				type = 2; 
    			}
    			
    			TabuSearchTSP tabu = new TabuSearchTSP(Integer.valueOf(NumIteration.getText()), Integer.valueOf(LengthTabuList.getText()), Graph.getListVertex(this.application.Overlay), Graph.getListEdge(this.application.Overlay), this.application.isOriented, type);
		        Task<Void> task = new Task<Void>() {
		            @Override
		            protected Void call() throws Exception {
		            	tabu.runTabuSearch();
		                return null;
		            }
		        };
				
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loading/LoadingWindow.fxml"));
				Parent root1;
				root1 = (Parent) fxmlLoader.load();

				LoadingWindowController lwc = fxmlLoader.<LoadingWindowController>getController();
				lwc.init_interface("Running tabu search ...", task);

				Stage stage = new Stage();
				stage.setScene(new Scene(root1));
				stage.setTitle("Running tabu search ...");

				String ToIcon = "\\ressources\\Icon\\GraphApp_icon.png";
				Path currentRelativePath = Paths.get("");
				Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
				String TotalPath = "file:" + curentAbsoluteDirectory.toString() + ToIcon;
				stage.getIcons().add(new Image(TotalPath));

				stage.show();
		        
		        task.setOnSucceeded(e -> {
		        	try {
						FXMLLoader fxmlLoaderDisplay = new FXMLLoader(
								getClass().getResource("/TabuSearchTSP/TabuSearchDisplay.fxml"));
						Parent Displayroot1 = (Parent) fxmlLoaderDisplay.load();

						TabuSearchDisplayController tdc = fxmlLoaderDisplay.<TabuSearchDisplayController>getController();
						tdc.initInterface(this.application.Overlay, this.ColorBestPath.getValue(), tabu, this.application.isOriented);

						Stage stageDisplay = new Stage();
						stageDisplay.setScene(new Scene(Displayroot1));
						stageDisplay.setTitle("Tabu Search for Travelling Salesman Problem");

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
    }

    /**
     * Function to init the interface
     * @param app (MainInterface_Controller) : Link to the main application
     */
    public void init_interface(MainInterface_Controller app) {
    	this.application = app;
    	this.ColorBestPath.setValue(Color.RED);
    	this.TypeTabuElement.getItems().addAll("First vertex", "Second Vertex", "Both Vertex");
    	this.TypeTabuElement.setValue("Both Vertex");
    }
    
}
