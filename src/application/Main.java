package application;
	
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

/**
 * 
 * @author Neil Farmer
 *
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("MainInterface.fxml"));
			Scene scene = new Scene(root,700,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//Set the name of the application
			primaryStage.setTitle("Edsger : Graph Solver");
			
			//Set the icon
			String ToIcon = "\\ressources\\Icon\\GraphApp_icon.png";
			Path currentRelativePath = Paths.get("");
			Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
			String TotalPath = "file:" + curentAbsoluteDirectory.toString() + ToIcon;
			primaryStage.getIcons().add(new Image(TotalPath));
			
			//Show the application
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
