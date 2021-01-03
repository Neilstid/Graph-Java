package mst;

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

public class MSTDisplayController {

    @FXML
    private Pane Overlay;
    
    @FXML
    private Slider SliderStep;

    @FXML
    private Label IndicatorStep;
    
    private PrimAlgo mst;
    
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
    
    @FXML
    private MenuItem SaveGraph;

    @FXML
    void PressSave(ActionEvent event) {
    	try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getResource("/parameter/SaveAs.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();

			SaveAsController sac = fxmlLoader.<SaveAsController>getController();
			sac.init_interface(this.Overlay, false);

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

    public void initInterface(Vertex dep, List<Vertex> vertex, List<Edge> edge, Color usedEdge) {
    	this.LinkToPrevious = new HashMap<>(); 
    	this.ColEdge = usedEdge;
    	
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
    		Edge newE = new Edge(LinkToPrevious.get(e.getV1()), LinkToPrevious.get(e.getV2()), e.getWeight(), Overlay, e.typeOfLine, Color.GREY, false, e);
    		edgeList.add(newE);
    	}
    	
    	mst = new PrimAlgo(LinkToPrevious.get(dep), Overlay);
    	NumberOfStep = mst.solve().size() - 1;
    	
    	//set slider parameter
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
    	tocolor = mst.EdgeVisitedOrder();
    	
    	//Color edge if they have been visited before the step
    	for(Edge e:tocolor) {
    		if(stepMade == step) {
    			return;
    		}
    		e.setColor(this.ColEdge);
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
