package application;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

//This project import
import Dijkstra.DijkstraCreateController;
import GestionGraph.EdgeCreation;
import VertexGestion.VertexCreate_Controller;
import error.ErrorInterfaceController;
import graph.Edge;
import graph.Graph;
import graph.Vertex;
import parameter.SaveAsController;
import parameter.Setting;

//JavaFx import
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


/**
 * Main interface class
 * @author Neil Farmer
 *
 */
public class MainInterface_Controller extends Application {

	//Settings of the app
	public Setting setting = new Setting();
	
	//Link to this (for event handler)
	public MainInterface_Controller me = this;
	
	//To create edge after click on two vertex
	public EdgeCreation ec = new EdgeCreation(this);
	
	//true if oriented else false
	public boolean isOriented = false;
	
	//The element that have been selected by the user
	// 0 : None, 1 : Vertex, 2 : Edge
	public int userElementSelected = 0;

	//Default radius of vertex
	protected int radiusOfVertex = 5;

	//Borderpane where element are
	@FXML
	private BorderPane Window;

	//AnchorPane in center
	@FXML
	private AnchorPane BehindOverlay;

	@FXML
	private Button MenuButtonLeft;

	@FXML
	public Pane Overlay;

	@FXML
	private TitledPane EdgeRightMenu;

	@FXML
	private MenuBar MenuTopBar;

	@FXML
	private Button BackLeftSideMenu;

	@FXML
	private AnchorPane LeftSideMenu;

	@FXML
	private TitledPane GraphLeftMenu;

	@FXML
	private TitledPane AlgorithmLeftMenu;

	@FXML
	private TitledPane ParametersLeftMenu;

	@FXML
	private ColorPicker ColorPickerObject;

	@FXML
	public ComboBox<String> TypeGraph;

    @FXML
    private ToggleGroup UserAction;

    @FXML
    private ToggleButton EdgeToggle;

    @FXML
    private ToggleButton NoneToggle;
    
    @FXML
    private ToggleButton VertexToggle;
    
    @FXML
    private Button SaveButton;

    @FXML
    private Button DeleteButton;
    
    @FXML
    private Button DijkstraButton;
    
    @FXML
    void ApplyDijkstra(ActionEvent event) {
    	//Call the interface to solve
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/Dijkstra/DijkstraCreate.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			DijkstraCreateController dcc = fxmlLoader.<DijkstraCreateController>getController();
			dcc.initInterface(Graph.getListVertex(Overlay), Graph.getListEdge(Overlay), this.isOriented);

			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Dijkstra's algorithm : Creation");

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

    @FXML
    void PressSaveButton(ActionEvent event) {
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/parameter/SaveAs.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			SaveAsController sac = fxmlLoader.<SaveAsController>getController();
			sac.init_interface(this);

			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Save as ...");

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
    
    @FXML
    void PressDeleteButton(ActionEvent event) {
    	this.Overlay.getChildren().clear();
    }
    
    //Default mode, where object can move
    @FXML
    void PressNoneToggle(ActionEvent event) {
    	//Force to select a mode (by default the mode "none")
    	if(!NoneToggle.isSelected()) {
    		NoneToggle.setSelected(true);
    		return;
    	}
    	
    	//for each edge put the control visible if they exist
    	List<Edge> edgeList = Graph.getListEdge(this.Overlay);
    	for(Edge e:edgeList) {
    		e.setControlVisible(true);
    	}
    	
    	this.userElementSelected = 0;
    	
    	this.Overlay.setCursor(javafx.scene.Cursor.DEFAULT);
    	
		this.ec.empty();
    }

    //Mode to create vertex
    @FXML
    void PressVertexToggle(ActionEvent event) {
    	//Force to select a mode (by default the mode "none")
    	if(!VertexToggle.isSelected()) {
    		NoneToggle.setSelected(true);
    		this.userElementSelected = 0;
    		
    		//for each edge put the control visible if they exist
        	List<Edge> edgeList = Graph.getListEdge(this.Overlay);
        	for(Edge e:edgeList) {
        		e.setControlVisible(true);
        	}
    		
    		return;
    	}
    	
    	this.userElementSelected = 1;
    	
    	//for each edge put the control non-visible if they exist
    	List<Edge> edgeList = Graph.getListEdge(this.Overlay);
    	for(Edge e:edgeList) {
    		e.setControlVisible(false);
    	}
    	
    	//Change the cursor to a custom one
		String ToIcon = "\\ressources\\Cursor\\CursorEditVertexEdge.png";
		Path currentRelativePath = Paths.get("");
		Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
		String TotalPath = "file:" + curentAbsoluteDirectory.toString() + ToIcon;

		Image image = new Image(TotalPath);
		Overlay.setCursor(new ImageCursor(image, 32, 32));
		
		this.ec.empty();
    }
    
    //mode to create an edge
    @FXML
    void PressEdgeToggle(ActionEvent event) {
    	//Force to select a mode (by default the mode "none")
    	if(!EdgeToggle.isSelected()) {
    		NoneToggle.setSelected(true);
    		this.userElementSelected = 0;
    		
        	List<Edge> edgeList = Graph.getListEdge(this.Overlay);
        	for(Edge e:edgeList) {
        		e.setControlVisible(true);
        	}
    		
    		return;
    	}
    	
    	this.userElementSelected = 2;
    	
    	List<Edge> edgeList = Graph.getListEdge(this.Overlay);
    	for(Edge e:edgeList) {
    		e.setControlVisible(false);
    	}
    	
    	//Change the cursor to a custom one
		String ToIcon = "\\ressources\\Cursor\\CursorEditVertexEdge.png";
		Path currentRelativePath = Paths.get("");
		Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
		String TotalPath = "file:" + curentAbsoluteDirectory.toString() + ToIcon;

		Image image = new Image(TotalPath);
		Overlay.setCursor(new ImageCursor(image, 32, 32));
    }

	Predicate<Node> GetEdge = node -> node.getClass().equals(Edge.class);

	//Change the type of graph (Oriented or Non-oriented)
	@FXML
	void ActionTypeGraph(ActionEvent event) {
		boolean type;
		//Check if the graph has changed type
		if (TypeGraph.getValue() == "Non-oriented graph") {
			type = false;
		} else {
			type = true;
		}

		//if nonn changed do nothing else ...
		if (type == this.isOriented) {
			return;
		}else if(!type){
			List<Vertex> v1 = Graph.getListVertex(this.Overlay);
			List<Vertex> v2 = Graph.getListVertex(this.Overlay);
			
			//Check if there's no more than 1 edge between 2 vertex
			for(Vertex vertex1:v1) {
				v2.remove(vertex1);
				for(Vertex vertex2:v2) {
				    ArrayList<Edge> EdgeList = new ArrayList<Edge>(vertex1.linkToMe);
					ArrayList<Edge> EdgeListVertex2 = new ArrayList<Edge>(vertex2.linkToMe);
					EdgeList.retainAll(EdgeListVertex2);
					
					if(EdgeList.size() == 2) {
						//Error if user try to change type from non-oriented to oriented and 2 edge connect 2 vertex
						try {
							FXMLLoader fxmlLoader = new FXMLLoader(
									getClass().getResource("/error/ErrorInterface.fxml"));
							Parent root1 = (Parent) fxmlLoader.load();

							ErrorInterfaceController eic = fxmlLoader.<ErrorInterfaceController>getController();
							eic.initInterface("Some vertex have more than one edge ! They can't be converted in non-oriented edge");

							Stage stage = new Stage();
							stage.setScene(new Scene(root1));
							stage.setTitle("Error !");

							String ToIcon = "\\ressources\\Icon\\ErrorIcon.png";
							Path currentRelativePath = Paths.get("");
							Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
							String TotalPath = "file:" + curentAbsoluteDirectory.toString() + ToIcon;
							stage.getIcons().add(new Image(TotalPath));	
							
							stage.show();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						TypeGraph.setValue("Oriented graph");
						return;
					}
				}
			}
			
		}

		this.isOriented = type;
		List<Edge> result = new ArrayList<Edge>();
		result = Graph.getListEdge(this.Overlay);
		
		for (Edge e : result) {
			e.setOriented(type);
		}
	}

	@FXML
	void PressBackLeftSideMenu(ActionEvent event) {
		LeftSideMenu_Visible(false);
	}

	@FXML
	void PressMenuButtonLeft(ActionEvent event) {
		LeftSideMenu_Visible(true);
	}

	//Show the left side menu
	private void LeftSideMenu_Visible(boolean visible) {
		LeftSideMenu.setVisible(visible);
		LeftSideMenu.setManaged(visible);
		MenuButtonLeft.setVisible(!visible);
		MenuButtonLeft.setManaged(!visible);
	}

	/**
	 * Init the pae where the graph will be display
	 */
	private void init_Overlay() {
		Overlay.addEventFilter(MouseEvent.MOUSE_CLICKED, CreateNewVertex);
	}

	/**
	 * Init the colorPicker
	 */
	private void init_ColorPickerObject() {
		ColorPickerObject.setValue(setting.getColorVertexDefault());
	}

	/**
	 * Init the type of the graph
	 */
	private void init_TypeGraph() {
		this.TypeGraph.getItems().addAll("Non-oriented graph", "Oriented graph");
		this.TypeGraph.setValue("Non-oriented graph");
	}

	/**
	 * Init the button on the bottom of the interface
	 */
	private void init_buttonUserAction() {
		//Path to icon
		String ToIconNone = "\\ressources\\Icon\\NoneIcon.png";
		String ToIconVertex = "\\ressources\\Icon\\VertexIcon.png";
		String ToIconEdge = "\\ressources\\Icon\\EdgeIcon.png";
		String ToIconSave = "\\ressources\\Icon\\SaveIcon.png";
		String ToIconDelete = "\\ressources\\Icon\\DeleteIcon.png";
		
		Path currentRelativePath = Paths.get("");
		Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
		
		String TotalPathNone = "file:" + curentAbsoluteDirectory.toString() + ToIconNone;
		String TotalPathVertex = "file:" + curentAbsoluteDirectory.toString() + ToIconVertex;
		String TotalPathEdge = "file:" + curentAbsoluteDirectory.toString() + ToIconEdge;
		String TotalPathSave = "file:" + curentAbsoluteDirectory.toString() + ToIconSave;
		String TotalPathDelete = "file:" + curentAbsoluteDirectory.toString() + ToIconDelete;
		
		ImageView imageNone = new ImageView(new Image(TotalPathNone));
		ImageView imageVertex = new ImageView(new Image(TotalPathVertex));
		ImageView imageEdge = new ImageView(new Image(TotalPathEdge));
		ImageView imageSave = new ImageView(new Image(TotalPathSave));
		ImageView imageDelete = new ImageView(new Image(TotalPathDelete));
		
		//Set the icon size
		imageNone.setFitHeight(15);
		imageNone.setFitWidth(15);
		
		imageVertex.setFitHeight(15);
		imageVertex.setFitWidth(15);
		
		imageEdge.setFitHeight(15);
		imageEdge.setFitWidth(15);
		
		imageSave.setFitHeight(15);
		imageSave.setFitWidth(15);
		
		imageDelete.setFitHeight(15);
		imageDelete.setFitWidth(15);
		
		//put the icon on button
		this.NoneToggle.setGraphic(imageNone);
		this.VertexToggle.setGraphic(imageVertex);
		this.EdgeToggle.setGraphic(imageEdge);
		this.SaveButton.setGraphic(imageSave);
		this.DeleteButton.setGraphic(imageDelete);
		
		this.NoneToggle.setTooltip(new Tooltip("None : You will be able to move vertex, delete element, ..."));
		this.VertexToggle.setTooltip(new Tooltip("Vertex"));
		this.EdgeToggle.setTooltip(new Tooltip("Edge"));
	}
	
	//Function call on start
	@FXML
	public void initialize() {
		LeftSideMenu_Visible(false);
		init_Overlay();
		init_ColorPickerObject();
		init_TypeGraph();
		init_buttonUserAction();
	}

	@Override
	public void start(Stage arg0) throws Exception {
	}

	EventHandler<MouseEvent> CreateNewVertex = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			if (me.userElementSelected == 1) {
				try {
					FXMLLoader fxmlLoader = new FXMLLoader(
							getClass().getResource("/VertexGestion/VertexCreation.fxml"));
					Parent root1 = (Parent) fxmlLoader.load();

					VertexCreate_Controller vc = fxmlLoader.<VertexCreate_Controller>getController();
					vc.init_interface((int) event.getX(), (int) event.getY(), me, ColorPickerObject.getValue());

					Stage stage = new Stage();
					stage.setScene(new Scene(root1));
					stage.setTitle("New vertex");

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
		}

	};
}
