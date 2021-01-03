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
import TabuSearchTSP.TabuSearchCreateController;
import VertexGestion.VertexCreate_Controller;
import bfs.BFSCreateController;
import branchAndBound.LittleCreateController;
import dfs.DFSCreateController;
import error.ErrorInterfaceController;
import graph.Edge;
import graph.Graph;
import graph.Vertex;
import graphCreation.CompleteGraphController;
import graphCreation.RandomGraphController;
import parameter.LoadInterfaceController;
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
import mst.MSTCreateController;


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

    @FXML
    private Button BFSButton;
	
    @FXML
    private Button PrimMSTButton;
	
	//Borderpane where element are
	@FXML
	private BorderPane Window;

	//AnchorPane in center
	@FXML
	private AnchorPane BehindOverlay;

	//Button to open the left menu (for algorihtm)
	@FXML
	private Button MenuButtonLeft;

	//Main pane (where the graph is display)
	@FXML
	public Pane Overlay;

	@FXML
	private TitledPane EdgeRightMenu;

	//The top bar 
	@FXML
	private MenuBar MenuTopBar;

	//Button to hide the left menu (when it is open)
	@FXML
	private Button BackLeftSideMenu;

	//The anchorpane where the left menu is
	@FXML
	private AnchorPane LeftSideMenu;

	//The graph section of the left menu
	@FXML
	private TitledPane GraphLeftMenu;

	//The algorithm section of the left menu
	@FXML
	private TitledPane AlgorithmLeftMenu;

	@FXML
	private TitledPane GraphParameterLeftMenu;

	//Color picker for the default color of the vertex
	@FXML
	private ColorPicker ColorPickerObject;

	//Selection of oriented or non oriented graph
	@FXML
	public ComboBox<String> TypeGraph;

	//Group of button for the action of the user (non selected, edge, vertex)
    @FXML
    private ToggleGroup UserAction;

    //Creation of edge selected button
    @FXML
    private ToggleButton EdgeToggle;

    //Mouse selected button (to move, delete, update vertex or edge)
    @FXML
    private ToggleButton NoneToggle;
    
    //Creation of Vertex selected button
    @FXML
    private ToggleButton VertexToggle;
    
    //Button to save the graph
    @FXML
    private Button SaveButton;

    //Button to delete the graph
    @FXML
    private Button DeleteButton;
    
    //Button to apply dijkstra on the graph
    @FXML
    private Button DijkstraButton;
    
    //Button to create a new complete graph
    @FXML
    private Button CompleteGraph;

    //Button to create a new random graph
    @FXML
    private Button RandomGraph;
    
    //Button to applytabu search for tsp
    @FXML
    private Button TabuSearchTSPButton;
    
    //Button tp apply branch and bound
    @FXML
    private Button BranchAndBoundButton;
    
    @FXML
    private Button DFSButton;

    @FXML
    private Button LoadButton;
    

    @FXML
    void PressLoad(ActionEvent event) {
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/parameter/LoadInterface.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			LoadInterfaceController sac = fxmlLoader.<LoadInterfaceController>getController();
			sac.init_interface(this);

			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Load graph");

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
    
    //To create DFS
    @FXML
    void PressDFS(ActionEvent event) {
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/dfs/DFSCreate.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			DFSCreateController dcc = fxmlLoader.<DFSCreateController>getController();
			dcc.initInterface(Graph.getListVertex(this.Overlay), Graph.getListEdge(this.Overlay), this.isOriented);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("DFS");

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
    
    //To create BFS
    @FXML
    void PressBFSButton(ActionEvent event) {
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/bfs/BFSCreate.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			BFSCreateController bcc = fxmlLoader.<BFSCreateController>getController();
			bcc.initInterface(Graph.getListVertex(this.Overlay), Graph.getListEdge(this.Overlay), this.isOriented);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("BFS");

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
     * Run the branch and bound Algorithm
     * @param event Click of the user
     */
    @FXML
    void PressBranchAndBound(ActionEvent event) {
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/branchAndBound/LittleCreate.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			LittleCreateController lcc = fxmlLoader.<LittleCreateController>getController();
			lcc.init_interface(this.Overlay, this.isOriented);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Branch and Bound : Little Algorithm");

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
     * Run prim algorithm
     * @param event  Click of the user
     */
    @FXML
    void PressPrimMSTButton(ActionEvent event) {
    	
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/mst/MSTCreate.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			MSTCreateController tscc = fxmlLoader.<MSTCreateController>getController();
			tscc.initInterface(Graph.getListVertex(this.Overlay), Graph.getListEdge(this.Overlay));
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Prim Algorithm");

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
     * Run the tabu search
     * @param event  Click of the user
     */
    @FXML
    void PressTabuSearchTSPButton(ActionEvent event) {
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/TabuSearchTSP/TabuSearchCreate.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			TabuSearchCreateController tscc = fxmlLoader.<TabuSearchCreateController>getController();
			tscc.init_interface(this);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Tabu search for Travelling Salesman Problem");

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
     * Create a complete graph
     * @param event  Click of the user
     */
    @FXML
    void PressCompleteGraph(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/graphCreation/CompleteGraph.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			CompleteGraphController rgc = fxmlLoader.<CompleteGraphController>getController();
			rgc.init_interface(this);
			
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Create a new complete graph");

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
     * Create a random graph
     * @param event  Click of the user
     */
    @FXML
    void PressRandomGraph(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/graphCreation/RandomGraph.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			RandomGraphController rgc = fxmlLoader.<RandomGraphController>getController();
			rgc.init_interface(this);

			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Create a new random graph");

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
     * Run dijkstra algorithm
     * @param event  Click of the user
     */
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

    /**
     * Save the graph
     * @param event  Click of the user
     */
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
    	
    	//Indicate what element have been selected
    	this.userElementSelected = 0;
    	
    	//Change cursor
    	this.Overlay.setCursor(javafx.scene.Cursor.DEFAULT);
    	
    	//Clear the creation of edge
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

		//if none changed do nothing else ...
		if (type == this.isOriented) {
			return;
		}else if(!type){
			
			//Get list of vertex
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

		//Change type of graph
		this.isOriented = type;
		
		//Put the button for the MST disable
		this.PrimMSTButton.setDisable(this.isOriented);
		
		//Update each edge
		
		//Get the list of edge
		List<Edge> result = new ArrayList<Edge>();
		result = Graph.getListEdge(this.Overlay);
		
		//update them
		for (Edge e : result) {
			e.setOriented(type);
		}
	}

	/**
	 * Hide the left menu
	 * @param event  Click of the user
	 */
	@FXML
	void PressBackLeftSideMenu(ActionEvent event) {
		LeftSideMenu_Visible(false);
	}

	/**
	 * Show the left menu
	 * @param event Click of the user
	 */
	@FXML
	void PressMenuButtonLeft(ActionEvent event) {
		LeftSideMenu_Visible(true);
	}

	//Show/Hide the left side menu
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
		String ToIconLoad = "\\ressources\\Icon\\LoadIcon.png";
		
		Path currentRelativePath = Paths.get("");
		Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
		
		String TotalPathNone = "file:" + curentAbsoluteDirectory.toString() + ToIconNone;
		String TotalPathVertex = "file:" + curentAbsoluteDirectory.toString() + ToIconVertex;
		String TotalPathEdge = "file:" + curentAbsoluteDirectory.toString() + ToIconEdge;
		String TotalPathSave = "file:" + curentAbsoluteDirectory.toString() + ToIconSave;
		String TotalPathDelete = "file:" + curentAbsoluteDirectory.toString() + ToIconDelete;
		String TotalPathLoad = "file:" + curentAbsoluteDirectory.toString() + ToIconLoad;
		
		ImageView imageNone = new ImageView(new Image(TotalPathNone));
		ImageView imageVertex = new ImageView(new Image(TotalPathVertex));
		ImageView imageEdge = new ImageView(new Image(TotalPathEdge));
		ImageView imageSave = new ImageView(new Image(TotalPathSave));
		ImageView imageDelete = new ImageView(new Image(TotalPathDelete));
		ImageView imageLoad = new ImageView(new Image(TotalPathLoad));
		
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
		
		imageLoad.setFitHeight(15);
		imageLoad.setFitWidth(15);
		
		//put the icon on button
		this.NoneToggle.setGraphic(imageNone);
		this.VertexToggle.setGraphic(imageVertex);
		this.EdgeToggle.setGraphic(imageEdge);
		this.SaveButton.setGraphic(imageSave);
		this.DeleteButton.setGraphic(imageDelete);
		this.LoadButton.setGraphic(imageLoad);
		
		this.NoneToggle.setTooltip(new Tooltip("None : You will be able to move vertex, delete element, ..."));
		this.VertexToggle.setTooltip(new Tooltip("Vertex"));
		this.EdgeToggle.setTooltip(new Tooltip("Edge"));
		this.SaveButton.setTooltip(new Tooltip("Save the graph"));
		this.DeleteButton.setTooltip(new Tooltip("Delete the graph"));
		this.LoadButton.setTooltip(new Tooltip("Load a graph"));
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
	public void start(Stage arg0) {
	}

	//When user click on pane
	EventHandler<MouseEvent> CreateNewVertex = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent event) {
			//Check if the user want to create a new Vertex
			if (me.userElementSelected == 1) {
				//Launch interface to create a new vertex
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
