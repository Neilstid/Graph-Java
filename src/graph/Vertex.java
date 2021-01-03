package graph;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import GestionGraph.AnchorPoint;
import VertexGestion.VertexModifyController;
import application.MainInterface_Controller;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Vertex
 * 
 * @author Neil Farmer
 *
 */
public class Vertex extends AnchorPoint {

	// The name display of the vertex
	private Text t_name;

	// This vertex
	private Vertex me = this;

	// Link to the main interface
	private MainInterface_Controller Application;

	// The name of the vertex
	private String Name = "";

	// Color of the vertex
	private Color color;

	// The position of the vertex
	private DoubleProperty x, y;

	// List of edge linked to the vertex
	public List<Edge> linkToMe = new ArrayList<Edge>();

	// Constructor
	/**
	 * Constructor
	 * 
	 * @param _x  (DoubleProperty) : x position
	 * @param _y  (DoubleProperty) : y position
	 * @param app (MainInterface_Controller) : Link to the main application
	 * @param col (Color) : Color of the vertex
	 * @param n   (String) : Name of the vertex
	 */
	public Vertex(DoubleProperty _x, DoubleProperty _y, MainInterface_Controller app, Color col, String n) {
		super(app, col, _x, _y);
		this.setRadius(10);

		this.Application = app;
		this.color = col;
		this.x = _x;
		this.y = _y;
		this.Name = n;

		this.t_name = new Text(this.Name);
		int size = 15;
		this.t_name.setFont(Font.font(size));
		while (size > 5 && t_name.getBoundsInLocal().getWidth() > 20) {
			size--;
			this.t_name.setFont(Font.font(size));
		}
		this.t_name.setX(_x.intValue() - t_name.getBoundsInLocal().getWidth() / 2);
		this.t_name.setY(_y.intValue() + t_name.getBoundsInLocal().getHeight() / 4);
		this.t_name.mouseTransparentProperty().setValue(true);
		this.t_name.setFill(app.setting.getColorWrittenVertexName());

		enableDrag(Application);
	}

	/**
	 * Function to create a vertex but can't be dragged or call function
	 * 
	 * @param _x   (DoubleProperty) : x position
	 * @param _y   (DoubleProperty) : y position
	 * @param pane (Pane) : Link to the pane
	 * @param col  (Color) : Color of the vertex
	 * @param n    (String) : Name of the vertex
	 */
	public Vertex(DoubleProperty _x, DoubleProperty _y, Pane pane, Color col, String n) {
		super(col, _x, _y);
		this.setRadius(10);

		this.color = col;
		this.x = _x;
		this.y = _y;
		this.Name = n;

		this.t_name = new Text(this.Name);
		int size = 15;
		this.t_name.setFont(Font.font(size));
		while (size > 5 && t_name.getBoundsInLocal().getWidth() > 20) {
			size--;
			this.t_name.setFont(Font.font(size));
		}
		this.t_name.setX(_x.intValue() - t_name.getBoundsInLocal().getWidth() / 2);
		this.t_name.setY(_y.intValue() + t_name.getBoundsInLocal().getHeight() / 4);
		this.t_name.mouseTransparentProperty().setValue(true);

		pane.getChildren().add(this);
		pane.getChildren().add(this.t_name);
	}

	/**
	 * Constructor for calcul in algorithm
	 * 
	 * @param col
	 * @param n
	 */
	public Vertex(Color col, String n) {
		this.color = col;
		this.Name = n;
	}

	/**
	 * Constructor
	 */
	public Vertex() {
	}

	/**
	 * Function to draw
	 */
	public void draw() {
		this.Application.Overlay.getChildren().add(this);
		this.Application.Overlay.getChildren().add(this.t_name);
		
		this.toFront();
		this.t_name.toFront();
	}

	/**
	 * Function to delete the object
	 */
	public void delete() {
		while (!this.linkToMe.isEmpty()) {
			try {
				this.linkToMe.get(0).delete();
			} catch (ConcurrentModificationException c) {

			}
		}

		this.Application.Overlay.getChildren().remove(this);
		this.Application.Overlay.getChildren().remove(this.t_name);
	}

	/**
	 * 
	 * @return Name of the vertex
	 */
	public String getName() {
		return this.Name;
	}

	/**
	 * 
	 * @param s (String) : The new name
	 */
	public void setName(String s) {
		this.Name = s;
		this.t_name.setText(s);
	}

	/**
	 * 
	 * @return The x position
	 */
	public DoubleProperty getX() {
		return this.x;
	}

	/**
	 * 
	 * @param _x (DoubleProperty) : The new x position
	 */
	public void setX(DoubleProperty _x) {
		if (_x.intValue() > 0 && _x.intValue() < this.Application.Overlay.getWidth()) {
			this.x = _x;
			setCenterX(_x.intValue());
			me.t_name.setX(_x.intValue() - t_name.getBoundsInLocal().getWidth() / 2);

			for (Edge e : me.linkToMe) {
				e.head.update();
			}
		}
	}

	/**
	 * 
	 * @return The y position
	 */
	public DoubleProperty getY() {
		return this.y;
	}

	/**
	 * 
	 * @param _y (DoubleProperty) : The new y position
	 */
	public void setY(DoubleProperty _y) {
		if (_y.intValue() > 0 && _y.intValue() < this.Application.Overlay.getHeight()) {
			this.y = _y;
			me.t_name.setY(_y.intValue());
			setCenterY(_y.intValue() - t_name.getBoundsInLocal().getHeight() / 4);

			for (Edge e : me.linkToMe) {
				e.head.update();
			}
		}
	}

	/**
	 * Function to print vertex
	 */
	@Override
	public String toString() {
		return this.Name;
	}

	/**
	 * 
	 * @return The color of the vertex
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * 
	 * @param c (Color) : the new color
	 */
	public void setColor(Color c) {
		this.color = c;
		this.setFill(c);
	}

	/**
	 * 
	 * @return The degree of the vertex
	 */
	public int getDegree() {
		return this.linkToMe.size();
	}

	/**
	 * Function to move a vertex
	 * 
	 * @param xPosition (double) : new x position
	 * @param yPosition (double) : new y position
	 */
	public void move(int xPosition, int yPosition) {
		setX(new SimpleDoubleProperty(xPosition));
		me.t_name.setX(xPosition - t_name.getBoundsInLocal().getWidth() / 2);	

		setY(new SimpleDoubleProperty(yPosition - t_name.getBoundsInLocal().getHeight() / 4));
		me.t_name.setY(yPosition - t_name.getBoundsInLocal().getWidth() / 2);

		for (Edge e : me.linkToMe) {
			e.head.update();
		}
	}
	
	/**
	 * 
	 * @return The object where the name is display
	 */
	public Text get_Name_Object() {
		return this.t_name;
	}

	// make a node movable by dragging it around with the mouse.
	private void enableDrag(MainInterface_Controller app) {
		final Delta dragDelta = new Delta();
		final Position position = new Position();
		//When user click
		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0 && mouseEvent.getButton().equals(MouseButton.PRIMARY)
						&& mouseEvent.getClickCount() == 2) {
					try {
						FXMLLoader fxmlLoader = new FXMLLoader(
								getClass().getResource("/VertexGestion/VertexModify.fxml"));
						Parent root1 = (Parent) fxmlLoader.load();

						VertexModifyController vc = fxmlLoader.<VertexModifyController>getController();
						vc.init_interface(me, me.Application);

						Stage stage = new Stage();
						stage.setScene(new Scene(root1));
						stage.setTitle("Modify vertex");

						String ToIcon = "\\ressources\\Icon\\GraphApp_icon.png";
						Path currentRelativePath = Paths.get("");
						Path curentAbsoluteDirectory = currentRelativePath.toAbsolutePath();
						String TotalPath = "file:" + curentAbsoluteDirectory.toString() + ToIcon;
						stage.getIcons().add(new Image(TotalPath));

						stage.show();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if ((app.userElementSelected == 0 && mouseEvent.getButton().equals(MouseButton.SECONDARY))) {
					me.delete();
					app.ec.empty();
				} else if (app.userElementSelected == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					app.ec.newEdge(me);
				} else if (app.userElementSelected == 0 && mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					// record a delta distance for the drag and drop operation.
					dragDelta.x = getCenterX() - mouseEvent.getX();
					dragDelta.y = getCenterY() - mouseEvent.getY();
					app.Overlay.setCursor(Cursor.MOVE);
				}
			}
		});
		//when release
		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0 && mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					app.Overlay.setCursor(Cursor.HAND);
				}
			}
		});
		//when drag
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0 && mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					double newX = mouseEvent.getX() + dragDelta.x;
					if (newX > 0 && newX < app.Overlay.getWidth()) {
						setCenterX(newX);
						me.t_name.setX(newX - t_name.getBoundsInLocal().getWidth() / 2);
					}
					double newY = mouseEvent.getY() + dragDelta.y;
					if (newY > 0 && newY < app.Overlay.getHeight()) {
						me.t_name.setY(newY);
						setCenterY(newY - t_name.getBoundsInLocal().getHeight() / 4);
					}
				}
				for (Edge e : me.linkToMe) {
					e.head.update();
				}
			}
		});
		//when mouse enter
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0) {
					if (!mouseEvent.isPrimaryButtonDown()) {
						app.Overlay.setCursor(Cursor.HAND);
						
						position.posName = me.t_name.getViewOrder();
						position.posNode = me.getViewOrder();
						
						me.setViewOrder(-1.0);
						me.t_name.setViewOrder(-1.0);
					}
				} else if (app.userElementSelected == 2) {
					me.setRadius(me.getRadius() + 2);
				}
			}
		});
		//When mouse exited
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0) {
					if (!mouseEvent.isPrimaryButtonDown()) {
						app.Overlay.setCursor(Cursor.DEFAULT);
						
						me.setViewOrder(position.posNode);
						me.t_name.setViewOrder(position.posName);
					}
				} else if (app.userElementSelected == 2) {
					me.setRadius(me.getRadius() - 2);
				}
			}
		});
	}

	// records relative x and y co-ordinates.
	private class Delta {
		double x, y;
	}
	
	private class Position {
		double posName, posNode;
	}
}