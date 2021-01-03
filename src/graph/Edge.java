package graph;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import EdgeGestion.EdgeModifyController;
import GestionGraph.AnchorPoint;
import GestionGraph.Arrow;
import GestionGraph.ArrowCurve;
import GestionGraph.BoundLine;
import GestionGraph.CurveBoundLine;
import GestionGraph.MouseGestures;
import application.MainInterface_Controller;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Edge
 * @author Neil Farmer
 *
 */
public class Edge{

	//Link to this object (For the eventHandler)
	private Edge me = this;
	
	//Link to the main interface
	private MainInterface_Controller Application;
	
	//Link to the 2 vertex that connect
	private Vertex v1, v2;
	
	//Weight of the edge
	private int weight;
	
	//Display of the weight
	private Text t_weight;
	
	//Arrow if the edge is oriented
	public Arrow head;

	//Line of the edge (either Line or CubicCurve)
	public Shape line;
	
	//Type of the line (0 = straight or 1 = curve)
	public int typeOfLine;
	
	//The control point of a CubicCurve
	private AnchorPoint AnchorControl1, AnchorControl2;
	
	//The control line of a CubicCurve
	private BoundLine LineControl1, LineControl2;
	
	/**
	 * Constructor
	 */
	public Edge() {
		
	}
	
	/**
	 * Constructor
	 * @param _v1 (Vertex) : First vertex
	 * @param _v2 (Vertex) : Second vertex
	 * @param w (int) : weight of the edge
	 * @param app (MainInterface_Controller) : Link to the main application
	 * @param type (int) : Type of the edge (0 = straight or 1 = curve)
	 */
	public Edge(Vertex _v1, Vertex _v2, int w, MainInterface_Controller app, int type) {	
		this.weight = w;
		this.v1 = _v1;
		this.v2 = _v2;
		this.Application = app;
		this.typeOfLine = type;
		
		this.v1.linkToMe.add(this);
		this.v2.linkToMe.add(this);

		this.t_weight = new Text(Integer.toString(weight));
		this.t_weight.setFont(Font.font(15));
		
		
		if(type == 0) {
			this.line = new BoundLine(_v1, _v2, app.setting.getColorEdge());
			//Link the weight display and the edge
			t_weight.layoutXProperty()
					.bind(((Line) this.line).endXProperty().subtract(((Line) this.line).endXProperty().subtract(((Line) this.line).startXProperty()).divide(2)));
			t_weight.layoutYProperty()
					.bind(((Line) this.line).endYProperty().subtract(((Line) this.line).endYProperty().subtract(((Line) this.line).startYProperty()).divide(2)));
		
			this.head = new Arrow((Line) this.line, 0.9999f, app.setting.getColorEdge());
		}else if(type == 1) {
			CurveBoundLine c = new CurveBoundLine(_v1, _v2, app.setting.getColorEdge());
			this.head = new ArrowCurve();
			c.setControl(this.head);
			((ArrowCurve) this.head).setInit(c, 0.99f, app.setting.getColorEdge());
		
			//Link the weight display and the edge
			t_weight.layoutXProperty()
					.bind(c.endXProperty().subtract(c.endXProperty().subtract(c.startXProperty()).divide(2)));
			t_weight.layoutYProperty()
					.bind(c.getLineControl1().startYProperty().subtract(c.getLineControl1().startYProperty().subtract(c.getLineControl2().startYProperty()).divide(2)));
		
			MouseGestures mg = new MouseGestures();
			mg.makeDraggableText(t_weight, app);
			
			app.Overlay.getChildren().addAll(c.getControl1(), c.getControl2(), c.getLineControl1(), c.getLineControl2());
			
			this.AnchorControl1 = c.getControl1();
			this.AnchorControl2 = c.getControl2();
			this.LineControl1 = c.getLineControl1();
			this.LineControl2 = c.getLineControl2();
			
			this.line = c;
		}
		
		//Create the arrow
		this.setOriented(app.isOriented);
		this.head.mouseTransparentProperty().set(true);
		
		//Enable the drag
		enableDrag(this.Application);
	}
	
	/**
	 * 
	 * Constructor
	 * @param _v1 (Vertex) : First vertex
	 * @param _v2 (Vertex) : Second vertex
	 * @param w (int) : weight
	 * @param pane (Pane) : Where the graph is display
	 * @param type (int) : Type of the edge (0 = straight or 1 = curve)
	 * @param col (Color) : Color of the edge
	 * @param isOriented (boolean) : true if the edge is oriented, else false
	 * @param e (Edge) : Original edge
	 */
	public Edge(Vertex _v1, Vertex _v2, int w, Pane pane, int type, Color col, boolean isOriented, Edge e) {	
		this.weight = w;
		this.v1 = _v1;
		this.v2 = _v2;
		this.typeOfLine = type;
		
		this.v1.linkToMe.add(this);
		this.v2.linkToMe.add(this);

		this.t_weight = new Text(Integer.toString(weight));
		this.t_weight.setFont(Font.font(15));
		
		if(type == 0) {
			this.line = new BoundLine(_v1, _v2, col);
			//Link the weight display and the edge
			t_weight.layoutXProperty()
					.bind(((Line) this.line).endXProperty().subtract(((Line) this.line).endXProperty().subtract(((Line) this.line).startXProperty()).divide(2)));
			t_weight.layoutYProperty()
					.bind(((Line) this.line).endYProperty().subtract(((Line) this.line).endYProperty().subtract(((Line) this.line).startYProperty()).divide(2)));
		
			this.head = new Arrow((Line) this.line, 0.9999f, col);
		}else if(type == 1) {
			CurveBoundLine c = new CurveBoundLine(_v1, _v2, col, e.AnchorControl1.getCenterX(), e.AnchorControl1.getCenterY(), e.AnchorControl2.getCenterX(), e.AnchorControl2.getCenterY());
			this.head = new ArrowCurve();
			c.setControl(this.head);
			((ArrowCurve) this.head).setInit(c, 0.99f, col);
		
			//Link the weight display and the edge
			t_weight.setX(e.getTextObject().getLayoutX() + e.getTextObject().getTranslateX());
			t_weight.setY(e.getTextObject().getLayoutY() + e.getTextObject().getTranslateY());
			
			pane.getChildren().addAll(c.getControl1(), c.getControl2(), c.getLineControl1(), c.getLineControl2());
			
			//Put the control point
			this.AnchorControl1 = c.getControl1();
			this.AnchorControl2 = c.getControl2();
			this.LineControl1 = c.getLineControl1();
			this.LineControl2 = c.getLineControl2();
			
			this.AnchorControl1.setVisible(false);
			this.AnchorControl2.setVisible(false);
			this.LineControl1.setVisible(false);
			this.LineControl2.setVisible(false);
			
			this.line = c;
		}
		
		//add all element to the pane
		pane.getChildren().addAll(this.line);
		pane.getChildren().addAll(this.t_weight);
		pane.getChildren().addAll(this.head);
		
		if(isOriented) {
			this.head.setVisible(true);
		}else {
			this.head.setVisible(false);
		}
	}

	/**
	 * Constructor for calcul in algorithm
	 * @param _v1
	 * @param _v2
	 * @param w
	 */
	public Edge(Vertex _v1, Vertex _v2, int w) {	
		this.weight = w;
		this.v1 = _v1;
		this.v2 = _v2;
		
		this.v1.linkToMe.add(this);
		this.v2.linkToMe.add(this);	
	}
	
	/**
	 * Function to draw the edge
	 */
	public void draw() {
		this.Application.Overlay.getChildren().add(this.line);
		this.Application.Overlay.getChildren().add(this.t_weight);
		this.Application.Overlay.getChildren().add(this.head);
		
		this.t_weight.toBack();
		this.head.toBack();
		this.line.toBack();
	}

	/**
	 * Function to delete the edge
	 */
	public void delete() {
		this.Application.Overlay.getChildren().remove(this.line);
		this.Application.Overlay.getChildren().remove(this.t_weight);
		this.Application.Overlay.getChildren().remove(this.head);
		
		this.v1.linkToMe.remove(this);
		this.v2.linkToMe.remove(this);
		
		if(this.typeOfLine == 1) {
			this.Application.Overlay.getChildren().remove(this.AnchorControl1);
			this.Application.Overlay.getChildren().remove(this.AnchorControl2);
			
			this.Application.Overlay.getChildren().remove(this.LineControl1);
			this.Application.Overlay.getChildren().remove(this.LineControl2);
		}
	}
	
	/**
	 * Function to delete the edge on virtual (it will be no more link to his vertex but still print)
	 */
	public void RemoveFomVertex() {
		this.v1.linkToMe.remove(this);
		this.v2.linkToMe.remove(this);
	}
	
	/**
	 * 
	 * @return Return the weight of the edge
	 */
	public int getWeight() {
		return this.weight;
	}
	
	/**
	 * 
	 * @param w (int) : The new weight
	 */
	public void setWeight(int w) {
		this.weight = w;
		this.t_weight.setText(String.valueOf(w));
	}
	
	/**
	 * 
	 * @return Return vertex where the edge start
	 */
	public Vertex getV1() {
		return this.v1;
	}
	
	/**
	 * 
	 * @return Return vertex where the edge end
	 */
	public Vertex getV2() {
		return this.v2;
	}
	
	public Vertex getSecondVertex(Vertex v) {
		if(this.v1.equals(v)) {
			return this.v2;
		}else {
			return this.v1;
		}
	}
	
	/**
	 * 
	 * @param s (String) : The new weight (Or new element to display over the edge)
	 */
	public void updateText(String s) {
		this.t_weight.setText(s);
	}
	
	/**
	 * Function to show or not the arrow
	 * @param vision (boolean) : The arrow should be visible or not
	 */
	public void setOriented(boolean vision) {
		this.head.setVisible(vision);
	}
	
	/**
	 * Function to paint an edge
	 * @param c (Color) : The new color of the edge
	 */
	public void setColor(Color c) {
		this.line.setStroke(c);
		this.head.setFill(c);
	}
	
	/**
	 * Function to paint with the default color
	 */
	public void resetColor() {
		this.line.setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
		this.head.setFill(Color.GRAY.deriveColor(0, 1, 1, 0.5));
	}
	
	/**
	 * Function to get color of the edge
	 * @return The color of the edge
	 */
	public Color getColor() {
		return (Color) this.line.getStroke();
	}
	
	/**
	 * Function to the text object 
	 * @return The text object
	 */
	public Text getTextObject() {
		return this.t_weight;
	}
	
	/**
	 * 
	 * @return The start x of the line
	 */
	public double GetStartX() {
		if (this.typeOfLine == 0) {
			return ((Line) this.line).getStartX();
		}else {
			return ((CurveBoundLine) this.line).getStartX();
		}
	}
	
	/**
	 * 
	 * @return The start y of the line
	 */
	public double GetStartY() {
		if (this.typeOfLine == 0) {
			return ((Line) this.line).getStartY();
		}else {
			return ((CurveBoundLine) this.line).getStartY();
		}
	}
	
	/**
	 * 
	 * @return The end x of the line
	 */
	public double GetEndX() {
		if (this.typeOfLine == 0) {
			return ((Line) this.line).getEndX();
		}else {
			return ((CurveBoundLine) this.line).getEndX();
		}
	}
	
	/**
	 * 
	 * @return The end y of the line
	 */
	public double GetEndY() {
		if (this.typeOfLine == 0) {
			return ((Line) this.line).getEndY();
		}else {
			return ((CurveBoundLine) this.line).getEndY();
		}
	}
	
	/**
	 * 
	 * @return The x of the control point 1
	 */
	public double getControlX1() {
		if(this.typeOfLine == 1) {
			return ((CurveBoundLine) this.line).getControlX1();
		}
		return 0;
	}
	
	/**
	 *  
	 * @return The y of the control point 1
	 */
	public double getControlY1() {
		if(this.typeOfLine == 1) {
			return ((CurveBoundLine) this.line).getControlY1();
		}
		return 0;
	}
	
	/**
	 * 
	 * @return The x of the control point 2
	 */
	public double getControlX2() {
		if(this.typeOfLine == 1) {
			return ((CurveBoundLine) this.line).getControlX2();
		}
		return 0;
	}
	
	/**
	 *  
	 * @return The y of the control point 2
	 */
	public double getControlY2() {
		if(this.typeOfLine == 1) {
			return ((CurveBoundLine) this.line).getControlY2();
		}
		return 0;
	}
	
	/**
	 * Function to put the control of the line visible or not
	 * @param vision (boolean) : If the control should be visible or not
	 */
	public void setControlVisible(boolean vision) {
		if(this.typeOfLine == 1) {
			this.AnchorControl1.setVisible(vision);
			this.AnchorControl2.setVisible(vision);
			this.LineControl1.setVisible(vision);
			this.LineControl2.setVisible(vision);
			
			this.AnchorControl1.setMouseTransparent(!vision);
			this.AnchorControl2.setMouseTransparent(!vision);
		}
	}
	
	/**
	 * Function to in back an edge
	 */
	public void toBack() {
		this.t_weight.toBack();
		this.head.toBack();
		this.line.toBack();
	}
	
	/**
	 * Function for all mouseEvent
	 * @param userRequest : the action of the user want
	 */
	private void enableDrag(MainInterface_Controller app) {
		line.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0 && mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
					me.delete();
				}else if(app.userElementSelected == 0 && mouseEvent.getButton().equals(MouseButton.PRIMARY)  && mouseEvent.getClickCount() == 2) {
					try {
						FXMLLoader fxmlLoader = new FXMLLoader(
								getClass().getResource("/EdgeGestion/EdgeModify.fxml"));
						Parent root1 = (Parent) fxmlLoader.load();

						EdgeModifyController ec = fxmlLoader.<EdgeModifyController>getController();
						ec.init_interface(me, me.Application);

						Stage stage = new Stage();
						stage.setScene(new Scene(root1));
						stage.setTitle("Modify edge");

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
		});
		line.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0) {
					if (!mouseEvent.isPrimaryButtonDown()) {
						app.Overlay.setCursor(Cursor.HAND);
					}
				}
			}
		});
		line.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0) {
					if (!mouseEvent.isPrimaryButtonDown()) {
						app.Overlay.setCursor(Cursor.DEFAULT);
					}
				}
			}
		});
	}
	
	public static Comparator<Edge> compareByWeight = (Edge o1, Edge o2) -> Integer.compare(o1.getWeight(), o2.getWeight());
}
