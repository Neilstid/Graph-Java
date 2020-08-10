package GestionGraph;

import application.MainInterface_Controller;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * Class to print point and anchored line in it
 * @author Neil Farmer
 */
public class AnchorPoint extends Circle { 

	//Constructor
	/**
	 * Constructor
	 * @param app (MainInterface_Controller) : Link to the main application
	 * @param color  (Color) : Color of the point
	 * @param _x (DoubleProperty) : x position
	 * @param _y (DoubleProperty) : y position
	 */
	public AnchorPoint(MainInterface_Controller app, Color color, DoubleProperty _x, DoubleProperty _y) {
		super(_x.get(), _y.get(), 5);
		setFill(color);

		_x.bind(centerXProperty());
		_y.bind(centerYProperty());
	}
	
	/**
	 * Constructor (not Draggable)
	 * @param color (Color) : Color of the point
	 * @param _x (DoubleProperty) : x position
	 * @param _y (DoubleProperty) : y position
	 */
	public AnchorPoint(Color color, DoubleProperty _x, DoubleProperty _y) {
		super(_x.get(), _y.get(), 5);
		setFill(color);

		_x.bind(centerXProperty());
		_y.bind(centerYProperty());
	}
	
	/**
	 * Constructor
	 * @param color (Color) : Color of the point
	 * @param x (DoubleProperty) : x position
	 * @param y (DoubleProperty) : y position
	 * @param a (Arrow) : Arrow link to the point
	 */
    public AnchorPoint(Color color, DoubleProperty x, DoubleProperty y, Arrow a) {
        super(x.get(), y.get(), 10);
        
        //Set the style of point
        setFill(color.deriveColor(1, 1, 1, 0.5));
        setStroke(color);
        setStrokeWidth(2);
        setStrokeType(StrokeType.OUTSIDE);

        x.bind(centerXProperty());
        y.bind(centerYProperty());
        enableDragArrow(a);
      }
    
    // make a node movable by dragging it around with the mouse.
    private void enableDragArrow(Arrow a) {
      final Delta dragDelta = new Delta();
      //On mouse click
      setOnMousePressed(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = getCenterX() - mouseEvent.getX();
          dragDelta.y = getCenterY() - mouseEvent.getY();
          getScene().setCursor(Cursor.MOVE);
        }
      });
      //On mouse click released
      setOnMouseReleased(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getScene().setCursor(Cursor.HAND);
        }
      });
      //On dragged
      setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          double newX = mouseEvent.getX() + dragDelta.x;
          if (newX > 0 && newX < getScene().getWidth()) {
            setCenterX(newX);
          }  
          double newY = mouseEvent.getY() + dragDelta.y;
          if (newY > 0 && newY < getScene().getHeight()) {
            setCenterY(newY);
          }
          a.update();
        }
      });
      //When mouse entered in object area
      setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.HAND);
          }
        }
      });
      //When mouse exited in object area
      setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.DEFAULT);
          }
        }
      });
    }

    // records relative x and y co-ordinates.
    private class Delta { double x, y; }
}  
