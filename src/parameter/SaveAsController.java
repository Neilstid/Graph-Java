package parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import application.MainInterface_Controller;
import error.ErrorInterfaceController;
import graph.Edge;
import graph.Graph;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import loading.LoadingWindowController;

public class SaveAsController {

	@FXML
	private TextField TextLinkToFile;

	@FXML
	private Button BrowseButton;

	@FXML
	private Button SaveButton;

	@FXML
	private Button CancelButton;

	@FXML
	private Label ErrorOutput;

	private SaveAsController me = this;

	private boolean isOriented;

	private Pane pane;

	private File f;

	private MainInterface_Controller application;

	@FXML
	void PressBrowse(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"),
				new FileChooser.ExtensionFilter("html files (*.html)", "*.html"),
				new FileChooser.ExtensionFilter("xml files (*.xml)", "*.xml"));
		File file = fileChooser.showSaveDialog(null);

		this.f = file;
		this.TextLinkToFile.setText(file.toString());
	}

	@FXML
	void PressCancellButton(ActionEvent event) {
		Stage stage = (Stage) this.CancelButton.getScene().getWindow();
		stage.close();
	}

	@FXML
	void PressSaveButton(ActionEvent event) {
		// Check for error
		if (this.f == null) {
			this.ErrorOutput.setText("You must enter an output file !");
		} else {
			if (!(me.application == null)) {
				List<Edge> edgeList = Graph.getListEdge(this.application.Overlay);

				for (Edge e : edgeList) {
					e.setControlVisible(false);
				}
			}

			// Search the extension of the file
			String extension = "";
			int i = this.f.toString().lastIndexOf(".");
			if (i > 0) {
				extension = this.f.toString().substring(i + 1);
			}

			if (extension.equals("png")) {

				if (me.application == null) {
					SaveGraph.saveAsPng(me.f, me.pane);
				} else {
					SaveGraph.saveAsPng(me.f, me.application.Overlay);
				}

			} else if (extension.equals("html")) {

				try {

					Task<Void> task = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							if (me.application == null) {
								SaveGraph.SaveAsHTML(me.f, me.pane, me.isOriented);
							} else {
								SaveGraph.SaveAsHTML(me.f, me.application.Overlay, me.application.isOriented);
							}

							return null;
						}
					};

					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loading/LoadingWindow.fxml"));
					Parent root1;
					root1 = (Parent) fxmlLoader.load();

					LoadingWindowController lwc = fxmlLoader.<LoadingWindowController>getController();
					lwc.init_interface("Loading html file ...", task);

					Stage stage = new Stage();
					stage.setScene(new Scene(root1));
					stage.setTitle("Loading html file ...");

					String ToIcon = "\\ressources\\Icon\\GraphApp_icon.png";
					Path currentRelativePath = Paths.get("");
					Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
					String TotalPath = "file:" + curentAbsoluteDirectory.toString() + ToIcon;
					stage.getIcons().add(new Image(TotalPath));

					stage.show();

					task.setOnSucceeded(e -> {
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
			} else if (extension.equals("xml")) {

				try {

					Task<Void> task = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							if (me.application == null) {
								SaveGraph.SaveAsXML(me.f, me.pane, me.isOriented);
							} else {
								SaveGraph.SaveAsXML(me.f, me.application.Overlay, me.application.isOriented);
							}

							return null;
						}
					};

					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loading/LoadingWindow.fxml"));
					Parent root1;
					root1 = (Parent) fxmlLoader.load();

					LoadingWindowController lwc = fxmlLoader.<LoadingWindowController>getController();
					lwc.init_interface("Loading xml file ...", task);

					Stage stage = new Stage();
					stage.setScene(new Scene(root1));
					stage.setTitle("Loading xml file ...");

					String ToIcon = "\\ressources\\Icon\\GraphApp_icon.png";
					Path currentRelativePath = Paths.get("");
					Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
					String TotalPath = "file:" + curentAbsoluteDirectory.toString() + ToIcon;
					stage.getIcons().add(new Image(TotalPath));

					stage.show();

					task.setOnSucceeded(e -> {
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
			}

			if (!(me.application == null) && this.application.userElementSelected == 0) {
				List<Edge> edgeList = Graph.getListEdge(this.application.Overlay);

				for (Edge e : edgeList) {
					e.setControlVisible(true);
				}
			}

			Stage stage = (Stage) this.CancelButton.getScene().getWindow();
			stage.close();
		}
	}

	/**
	 * Function to initialize the interface
	 * 
	 * @param app (MainInterface_Controller) : Link to the main application
	 */
	public void init_interface(MainInterface_Controller app) {
		this.application = app;
	}

	public void init_interface(Pane p, boolean isOrient) {
		this.application = null;
		this.pane = p;
		this.isOriented = isOrient;
	}

}
