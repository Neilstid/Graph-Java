package Dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.Edge;
import graph.Vertex;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
    
    /**
     * Function to init the interface
     * @param dep (Vertex) : The start of the algorithm
     * @param arr (Vertex) : The end of the algorithm (can be null if we want the path to all vertex)
     * @param vertex (List Vertex) : List of all the vertex
     * @param edge (List Edge) : List of all the edge
     * @param isOriented (boolean) : true if the graph is oriented else false
     */
    public void initInterface(Vertex dep, Vertex arr, List<Vertex> vertex, List<Edge> edge, boolean isOriented) {
    	this.LinkToPrevious = new HashMap<>(); 
    	
    	//create copy of the graph
    	
    	//create copy of vertex
    	for(Vertex v:vertex) {
    		Vertex newV = new Vertex(v.getX(), v.getY(), this.Overlay, v.getColor(), v.getName());
    		LinkToPrevious.put(v, newV);
    		vertexList.add(newV);
    	}
    	
    	this.OriginalVertex = vertex;
    	
    	//create copy of edge
    	for(Edge e:edge) {
    		Edge newE = new Edge(LinkToPrevious.get(e.getV1()), LinkToPrevious.get(e.getV2()), e.getWeight(), Overlay, e.typeOfLine, Color.GREY, isOriented, e);
    		edgeList.add(newE);
    	}
    	
    	//run dijkstra's algorithm
    	dijkstra = new DijkstraAlgo(LinkToPrevious.get(dep), LinkToPrevious.get(arr), isOriented, Overlay);
    	NumberOfStep = dijkstra.solve().size() - 1;
    	if(arr != null) {
    		NumberOfStep += dijkstra.VertexPath().size();
    	}
    	
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
    		e.setColor(Color.RED);
    		stepMade++;
    	}
    	
    	//When all edge have been visited build the path from the end to start using visited edge
    	List<Vertex> VertexOrederForIter = dijkstra.VertexPath();
    	Collections.reverse(VertexOrederForIter);
    	
    	for(Vertex v:VertexOrederForIter) {
    		if(stepMade == step) {
    			return;
    		}
    		v.setColor(Color.BLUE);
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
