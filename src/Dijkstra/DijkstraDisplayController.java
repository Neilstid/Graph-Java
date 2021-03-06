package Dijkstra;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.Edge;
import graph.Vertex;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import parameter.SaveAsController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

/**
 * 
 * @author Neil Farmer
 *
 */
public class DijkstraDisplayController {

    @FXML
    private Pane Overlay;
    
    @FXML
    private Slider SliderStep;

    @FXML
    private Label IndicatorStep;
    
    @FXML
    private MenuItem SaveGraph;
    
    //Dijkstra's algrithm
    private DijkstraAlgo dijkstra;
    
    //Number of step in dijkstra algorithm
    private int NumberOfStep;
    
    //List of the original (on the first interface) vertex
    private List<Vertex> OriginalVertex;
    
    //List of all vertex on this interface
    private List<Vertex> vertexList = new ArrayList<Vertex>();
    
    //List of all edge on this interface
    private List<Edge> edgeList = new ArrayList<Edge>();
    
    //Link between the new and original vertex 
    private Map<Vertex, Vertex> LinkToPrevious;
    
    //Color of edge used
    private Color ColEdge;
    
    //Color of vertex used
    private Color ColVertex;
    
    private boolean isOriented;
    
    /**
     * Function to init the interface
     * @param dep (Vertex) : The start of the algorithm
     * @param arr (Vertex) : The end of the algorithm (can be null if we want the path to all vertex)
     * @param vertex (List Vertex) : List of all the vertex
     * @param edge (List Edge) : List of all the edge
     * @param isOriented (boolean) : true if the graph is oriented else false
     * @param usedEdge (Color) : Color of edge used
     * @param usedVertex (Color) : Color of vertex used
     */
    public void initInterface(Vertex dep, Vertex arr, List<Vertex> vertex, List<Edge> edge, boolean _isOriented, Color usedEdge, Color usedVertex) {
    	this.LinkToPrevious = new HashMap<>(); 
    	this.ColEdge = usedEdge;
    	this.ColVertex = usedVertex;
    	this.isOriented = _isOriented;
    	
    	//create copy of the graph
    	
    	//create copy of vertex
    	for(Vertex v:vertex) {
    		Vertex newV = new Vertex(new SimpleDoubleProperty(v.getCenterX()), new SimpleDoubleProperty(v.getCenterY()), this.Overlay, v.getColor(), v.getName());
    		LinkToPrevious.put(v, newV);
    		vertexList.add(newV);
    	}
    	
    	this.OriginalVertex = vertex;
    	
    	//create copy of edge
    	for(Edge e:edge) {
    		Edge newE = new Edge(LinkToPrevious.get(e.getV1()), LinkToPrevious.get(e.getV2()), e.getWeight(), Overlay, e.typeOfLine, Color.GREY, _isOriented, e);
    		edgeList.add(newE);
    	}
    	
    	//run dijkstra's algorithm
    	dijkstra = new DijkstraAlgo(LinkToPrevious.get(dep), LinkToPrevious.get(arr), _isOriented, Overlay);
    	NumberOfStep = dijkstra.solve().size() - 1;
    	if(arr != null) {
    		NumberOfStep += dijkstra.VertexPath().size();
    	}
    	
    	//Set the parameter of the slider
    	IndicatorStep.setText("0/" + String.valueOf(NumberOfStep));
    	SliderStep.setMin(0);
    	SliderStep.setMax(NumberOfStep);
    	
    	SliderStep.setMajorTickUnit(1);
    	SliderStep.setMinorTickCount(0);
    	SliderStep.setShowTickMarks(true);
    	SliderStep.setShowTickLabels(true);
    	SliderStep.setSnapToTicks(true);
    	
    	//If the value change
    	SliderStep.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            		IndicatorStep.setText(String.valueOf((int) SliderStep.getValue()) + "/" + String.valueOf(NumberOfStep));
            		ChangeStep((int) SliderStep.getValue());
            	}
            });
    }
    
    @FXML
    void PressSave(ActionEvent event) {
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/parameter/SaveAs.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			SaveAsController sac = fxmlLoader.<SaveAsController>getController();
			sac.init_interface(this.Overlay, this.isOriented);

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
    
    /**
     * Function to update the graph depending on the step answered by the user
     * @param step (int) : Step answered by the user
     */
    private void ChangeStep(int step) {	
    	//Clear the color
    	clearColor();
    	
    	int stepMade = 0;
    	
    	List<Edge> tocolor = new ArrayList<Edge>();
    	tocolor.clear();
    	tocolor = dijkstra.EdgeVisitedOrder();
    	
    	//Color edge if they have been visited before the step
    	for(Edge e:tocolor) {
    		if(stepMade == step) {
    			return;
    		}
    		e.setColor(this.ColEdge);
    		stepMade++;
    	}
    	
    	//When all edge have been visited build the path from the end to start using visited edge
    	List<Vertex> VertexOrederForIter = dijkstra.VertexPath();
    	Collections.reverse(VertexOrederForIter);
    	
    	for(Vertex v:VertexOrederForIter) {
    		if(stepMade == step) {
    			return;
    		}
    		v.setColor(this.ColVertex);
    		stepMade++;
    	}
    }
    
    /**
     * Reset the color of edge and vertex
     */
    private void clearColor() {
    	for(Edge e:this.edgeList) {
    		e.resetColor();
    	}
    	
    	for(Vertex v:this.OriginalVertex) {
    		this.LinkToPrevious.get(v).setColor(v.getColor());
    	}
    }
    
}
