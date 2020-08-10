package GestionGraph;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

/**
 * Class to create line that are link to anchorPoint
 * @see AnchorPoint
 * @author Neil Farmer
 *
 */
public class BoundLine extends Line {
	
	/**
	 * Constructor
	 * @param startX (DoubleProperty) : x position of starting point
	 * @param startY (DoubleProperty) : y position of starting point
	 * @param endX (DoubleProperty) : x position of ending point
	 * @param endY (DoubleProperty) : y position of ending point
	 * @param col (Color) : Color of the line
	 */
    public BoundLine(DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY, Color col) {
        //Define the point of the line
    	startXProperty().bind(startX);
        startYProperty().bind(startY);
        endXProperty().bind(endX);
        endYProperty().bind(endY);
        
        setStrokeWidth(2);
        setStroke(col.deriveColor(0, 1, 1, 0.5));
        setStrokeLineCap(StrokeLineCap.BUTT);
        getStrokeDashArray().setAll(10.0, 5.0);
      }
	
	//Constructor
    /**
	 * Constructor
	 * @param c1 (Circle) : First point to connect
	 * @param c2 (Circle) : Second point to connect
     * @param col (Color) : Color of the line
     */
	public BoundLine(Circle c1, Circle c2, Color col) {
		//Connect the 2 circle with this object 
		connect(c1, c2, this);
		
		//Set the width of the line
		setStrokeWidth(3);	
		//Set the color of the line
		setStroke(col.deriveColor(0, 1, 1, 0.5));
	}
	
	/**
	 * 
	 * @param c1 (Circle) : First circle to link to
	 * @param c2 (Circle) : Second circle to link to
	 * @return Point2D
	 */
	public static Point2D getDirection(Circle c1, Circle c2) {
	    return new Point2D(c2.getCenterX() - c1.getCenterX(), c2.getCenterY() - c1.getCenterY()).normalize();
	}
	
	/**
	 * 
	 * @param c1 (Circle) : First circle to link to
	 * @param c2 (Circle) : Second circle to link to
	 * @param line (Line) : Line that will connect the 2 Circle
	 */
	public static void connect(Circle c1, Circle c2, Line line) {
	    InvalidationListener startInvalidated = observable -> {
	        Point2D dir = getDirection(c1, c2);
	        Point2D diff = dir.multiply(c1.getRadius());
	        line.setStartX(c1.getCenterX() + diff.getX());
	        line.setStartY(c1.getCenterY() + diff.getY());
	    };
	    InvalidationListener endInvalidated = observable -> {
	        Point2D dir = getDirection(c2, c1);
	        Point2D diff = dir.multiply(c2.getRadius());
	        line.setEndX(c2.getCenterX() + diff.getX());
	        line.setEndY(c2.getCenterY() + diff.getY());
	    };
	    c1.centerXProperty().addListener(startInvalidated);
	    c1.centerYProperty().addListener(startInvalidated);
	    c1.radiusProperty().addListener(startInvalidated);
	    c1.centerXProperty().addListener(endInvalidated);
	    c1.centerYProperty().addListener(endInvalidated);
	    c1.radiusProperty().addListener(endInvalidated);

	    startInvalidated.invalidated(null);

	    c2.centerXProperty().addListener(endInvalidated);
	    c2.centerYProperty().addListener(endInvalidated);
	    c2.radiusProperty().addListener(endInvalidated);
	    c2.centerXProperty().addListener(startInvalidated);
	    c2.centerYProperty().addListener(startInvalidated);
	    c2.radiusProperty().addListener(startInvalidated);

	    endInvalidated.invalidated(null);
	}
}