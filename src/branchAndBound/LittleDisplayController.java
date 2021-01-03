package branchAndBound;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.Edge;
import graph.Vertex;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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

public class LittleDisplayController {

    @FXML
    private Pane Overlay;

    @FXML
    private Slider SliderStep;

    @FXML
    private Label IndicatorStep;
    
    @FXML
    private Label EvalLabel;
    
    @FXML
    private MenuItem SaveGraph;
    
    //Dijkstra's algrithm
    private Little little;
    
    //Number of step in dijkstra algorithm
    private int NumberOfStep;
    
    //List of all vertex on this interface
    private List<Vertex> vertexList = new ArrayList<Vertex>();
    
    //List of all edge on this interface
    private List<Edge> edgeList = new ArrayList<Edge>();
    
    //Link between the new and original vertex 
    private Map<Vertex, Vertex> LinkToPrevious;
    
    //Color of edge used
    private Color colBest;
    
    //Color of vertex used
    private Color colActual;
    
    //True if the graph is oriented
    private boolean isOriented;
    
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
     * Function to initialize the interface
     * @param vertex (List) : List of vertex of the graph
     * @param edge (List) : List of edge of the graph
     * @param isOrient (boolean) : If the graph is oriented or not
     * @param l (Little) : Solution of tsp
     * @param colorLastPath (Color)
     * @param colorBestPath (Color) 
     */
    public void initInterface(List<Vertex> vertex, List<Edge> edge, boolean isOrient, Little l, Color colorLastPath, Color colorBestPath) {
    	this.LinkToPrevious = new HashMap<>(); 
    	this.isOriented = isOrient;
    	this.colActual = colorLastPath;
    	this.colBest = colorBestPath;
    	
    	//create copy of the graph
    	
    	//create copy of vertex
    	for(Vertex v:vertex) {
    		Vertex newV = new Vertex(new SimpleDoubleProperty(v.getCenterX()), new SimpleDoubleProperty(v.getCenterY()), this.Overlay, v.getColor(), v.getName());
    		LinkToPrevious.put(v, newV);
    		vertexList.add(newV);
    	}
    	
    	//create copy of edge
    	for(Edge e:edge) {
    		Edge newE = new Edge(LinkToPrevious.get(e.getV1()), LinkToPrevious.get(e.getV2()), e.getWeight(), Overlay, e.typeOfLine, Color.GREY, isOriented, e);
    		edgeList.add(newE);
    	}
    	
    	this.little = l;
    	
    	
    	//Set the slider parameter
    	NumberOfStep = l.getHisto_path().size();
    	
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
            		ColorEdge((int) SliderStep.getValue());
            	}
            });
    }
    
    //Function call when the value change
    private void ColorEdge(int step) {		
    	clearColor();
    	
    	if(step == this.little.getHisto_path().size()) {
    		ColorBestPath();
    		return;
    	}
    	
    	int fitness = 0;
    	List<Vertex> listVertex = convert(this.little.getHisto_path().get(step));
    	String obj;
		//If the graph is non-oriented (The vertex can be either v1 or v2 of the edge)
		if (!this.isOriented) {
			for (Vertex v : listVertex) {
				for (Edge e : v.linkToMe) {
					//Search for the edge by having order of vertex
					obj = this.little.getHisto_path().get(step).get( (listVertex.indexOf(v) + 1) % listVertex.size());
					if (e.getV1().getName().equals( obj) || e.getV2().getName().equals(obj)) {
						e.setColor(this.colActual);
						fitness += e.getWeight();
					}else {
        				e.toBack();
        			}
				}
			}
		}else { //if the graph is oriented
			for (Vertex v : listVertex) {
				for (Edge e : v.linkToMe) {
					//Search for the edge by having order of vertex
					obj = this.little.getHisto_path().get(step).get( (listVertex.indexOf(v) + 1) % listVertex.size());
					if (e.getV2().getName().equals(obj)) {
						e.setColor(this.colActual);
						fitness += e.getWeight();
					}else {
        				e.toBack();
        			}
				}
			}
		}
		this.EvalLabel.setText("Evaluation of cycle : " + String.valueOf(fitness));
    }
    
    //Function to color the best path
    private void ColorBestPath() {
    	int fitness = 0;
    	List<Vertex> listVertex = convert(this.little.getBest_path());
    	String obj;
		//If the graph is non-oriented (The vertex can be either v1 or v2 of the edge)
		if (!this.isOriented) {
			for (Vertex v : listVertex) {
				for (Edge e : v.linkToMe) {
					//Search for the edge by having order of vertex
					obj = this.little.getBest_path().get( (listVertex.indexOf(v) + 1) % listVertex.size());
					if (e.getV1().getName().equals( obj) || e.getV2().getName().equals(obj)) {
						e.setColor(this.colBest);
						fitness += e.getWeight();
					}else {
        				e.toBack();
        			}
				}
			}
		}else { //if the graph is oriented
			for (Vertex v : listVertex) {
				for (Edge e : v.linkToMe) {
					//Search for the edge by having order of vertex
					obj = this.little.getBest_path().get( (listVertex.indexOf(v) + 1) % listVertex.size());
					if (e.getV2().getName().equals(obj)) {
						e.setColor(this.colBest);
						fitness += e.getWeight();
					}else {
        				e.toBack();
        			}
				}
			}
		}
		this.EvalLabel.setText("Evaluation of cycle : " + String.valueOf(fitness));
    }
    
    //Function to convert list of string into list of vertex
    private List<Vertex> convert(List<String> sPath) {
    	List<Vertex> toReturn = new ArrayList<Vertex>();
    	for(String s:sPath) {
    		for(Vertex v:this.vertexList) {
    			if(s == v.getName()) {
    				toReturn.add(v);
    				break;
    			}		
    		}
    	}
    	return toReturn;
    }
    
    //Function to clear the color
    private void clearColor() {
    	//for each edge
    	for(Edge e:this.edgeList) {
    		//clear the color
    		e.resetColor();
    	}
    }
	
}
