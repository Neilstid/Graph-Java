package TabuSearchTSP;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import parameter.SaveAsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class TabuSearchDisplayController {

	private Color ColorBestPath;
	
	private TabuSearchTSP tabuSearch;

    @FXML
    private Pane Overlay;
    
    @FXML
    private Slider SliderStep;

    @FXML
    private Label IndicatorStep;

    @FXML
    private Label EvalLabel;
    
    private boolean isOriented;

    private List<Edge> edgeList;
    
    private List<Vertex> vertexList;
    
    @FXML
    private MenuItem SaveGraph;

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
     * @param p (Pane) : Pane where the graph is display
     * @param BestPath (Color) : Color of best path found
     * @param tsp (TabuSearchTSP) : Link to solution found
     * @param isOrient (boolean)
     */
    public void initInterface(Pane p, Color BestPath, TabuSearchTSP tsp, boolean isOrient) {
    	ColorBestPath = BestPath;
    	tabuSearch = tsp;
    	this.isOriented = isOrient;
    	this.edgeList = new ArrayList<Edge>();
    	this.vertexList = new ArrayList<Vertex>();
    	
    	//create copy of the graph
    	List<Vertex> vertex = Graph.getListVertex(p);
    	List<Edge> edge = Graph.getListEdge(p);
    	Map<Vertex, Vertex> LinkToPrevious = new HashMap<Vertex, Vertex>();
    	//create copy of vertex
    	for(Vertex v:vertex) {	
    		Vertex newV = new Vertex(new SimpleDoubleProperty(v.getCenterX()), new SimpleDoubleProperty(v.getCenterY()), this.Overlay, v.getColor(), v.getName());
    		LinkToPrevious.put(v, newV);
    		vertexList.add(newV);
    	}

    	//create copy of edge
    	List<String> first_Path = convertVertexString(this.tabuSearch.histoBestPath.get(0));
    	for(Edge e:edge) {
    		if (this.isOriented && e.getSecondVertex(e.getV1()).getName().equals(first_Path.get((first_Path.indexOf(e.getV1().getName())+ 1) % (first_Path.size())))) {
        		Edge newE = new Edge(LinkToPrevious.get(e.getV1()), LinkToPrevious.get(e.getV2()), e.getWeight(), this.Overlay, e.typeOfLine, this.ColorBestPath, isOriented, e);	
        		edgeList.add(newE);	
			} else if(e.getSecondVertex(e.getV1()).getName().equals(first_Path.get((first_Path.indexOf(e.getV1().getName())+ 1) % (first_Path.size()))) || e.getSecondVertex(e.getV2()).getName().equals(first_Path.get((first_Path.indexOf(e.getV2().getName())+ 1) % (first_Path.size())))){
	    		Edge newE = new Edge(LinkToPrevious.get(e.getV1()), LinkToPrevious.get(e.getV2()), e.getWeight(), this.Overlay, e.typeOfLine, this.ColorBestPath, isOriented, e);	
	    		edgeList.add(newE);
			}else {
	    		Edge newE = new Edge(LinkToPrevious.get(e.getV1()), LinkToPrevious.get(e.getV2()), e.getWeight(), this.Overlay, e.typeOfLine, Color.GREY, isOriented, e);	
	    		edgeList.add(newE);
			}

    	}	
    	
    	this.EvalLabel.setText("Evaluation of cycle : " + String.valueOf(this.tabuSearch.fit(this.tabuSearch.histoBestPath.get(0))));
    	
    	IndicatorStep.setText("0/" + String.valueOf(this.tabuSearch.histoBestPath.size() - 1));
    	SliderStep.setMin(0);
    	SliderStep.setMax(this.tabuSearch.histoBestPath.size() - 1);
    	
    	SliderStep.setMajorTickUnit(1);
    	SliderStep.setMinorTickCount(0);
    	SliderStep.setShowTickMarks(true);
    	SliderStep.setShowTickLabels(true);
    	SliderStep.setSnapToTicks(true);
    	
    	//If the value change
    	SliderStep.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            		IndicatorStep.setText(String.valueOf((int) SliderStep.getValue()) + "/" + String.valueOf(tabuSearch.histoBestPath.size()  - 1));
            		ChangeStep((int) SliderStep.getValue());
            	}
            });
    }
    
    /**
     * Function call when slider change value
     * @param step (int) :  Slider value
     */
    private void ChangeStep(int step) {	
    	this.clearColor();
    	this.EvalLabel.setText("Evaluation of cycle : " + String.valueOf(this.tabuSearch.fit(this.tabuSearch.histoBestPath.get(step))));
    	
    	
    	List<Vertex> to_print = convertVertexList(this.tabuSearch.histoBestPath.get(step));
    	for (int position = 0; position < to_print.size(); position++) {
			if (this.isOriented) {
				for (Edge e : to_print.get(position).linkToMe) {
					if (e.getV1().equals(to_print.get(position)) && e.getSecondVertex(to_print.get(position))
							.equals(to_print.get((position + 1) % (to_print.size())))) {
						e.setColor(this.ColorBestPath);
					}else {
        				e.toBack();
        			}
				}
			} else {
				for (Edge e : to_print.get(position).linkToMe) {
					if (e.getSecondVertex(to_print.get(position))
							.equals(to_print.get((position + 1) % (to_print.size())))) {
						e.setColor(this.ColorBestPath);
					}else {
        				e.toBack();
        			}
				}
			}
		}
    }
    
    private void clearColor() {
    	for(Edge e:this.edgeList) {
    		e.resetColor();
    	}
    }
    
    /**
     * Funtion to convert an vertex from the original pane to a vertex of this interface
     * @param vertex (List) : List of original vertex
     * @return List of new vertex
     */
    private List<Vertex> convertVertexList(List<Vertex> vertex){
    	List<Vertex> orderVertex = new ArrayList<Vertex>();
    	for(Vertex v:vertex) {
    		for(Vertex vfind:this.vertexList) {
    			if(v.getName() == vfind.getName()) {
    				orderVertex.add(vfind);
    				break;
    			}
    		}
    	}
    	return orderVertex;
    }
    
    private List<String> convertVertexString(List<Vertex> vertex){
    	List<String> orderVertex = new ArrayList<String>();
    	for(Vertex v:vertex) {
    		for(Vertex vfind:this.vertexList) {
    			if(v.getName() == vfind.getName()) {
    				orderVertex.add(vfind.getName());
    				break;
    			}
    		}
    	}
    	return orderVertex;
    } 
}
