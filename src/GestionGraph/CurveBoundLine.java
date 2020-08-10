package GestionGraph;

import javafx.beans.InvalidationListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;

public class CurveBoundLine extends CubicCurve {

	//Control line
	private BoundLine controlLine1;
	
	private BoundLine controlLine2;
	
	//Control points
	private AnchorPoint control1;
	
	private AnchorPoint control2;

	// Constructor
	/**
	 * Constructor
	 * @param c1 (Circle) : First point to connect
	 * @param c2 (Circle) : Second point to connect
	 * @param col (Color) : Color of the line
	 */
	public CurveBoundLine(Circle c1, Circle c2, Color col) {
		// Connect the 2 circle with this object
		connect(c1, c2, this);

		// Set the width of the line
		setStrokeWidth(3);
		// Set the color of the line
		setStroke(col.deriveColor(0, 1, 1, 0.5));
		setFill(Color.CORNSILK.deriveColor(0, 1.2, 1, 0.0));

		this.setControlX1(c1.getCenterX() + 20);
		this.setControlX2(c2.getCenterX() + 20);
		this.setControlY1(c1.getCenterY() + 20);
		this.setControlY2(c2.getCenterY() + 20);

		this.controlLine1 = new BoundLine(this.controlX1Property(), this.controlY1Property(), this.startXProperty(),
				this.startYProperty(), Color.GREY);
		this.controlLine2 = new BoundLine(this.controlX2Property(), this.controlY2Property(), this.endXProperty(),
				this.endYProperty(), Color.GREY);

		//Put line unclickable
		this.controlLine1.setMouseTransparent(true);
		this.controlLine2.setMouseTransparent(true);
	}
	
	/**
	 * Constructor
	 * @param c1 (Circle) : First point
	 * @param c2  (Circle) : Second point
	 * @param col (Color) : Color of the line
	 * @param ControlX1 (double) : x position of the control point 1
	 * @param ControlY1 (double) : y position of the control point 1
	 * @param ControlX2 (double) : x position of the control point 2
	 * @param ControlY2 (double) : y position of the control point 2
	 */
	public CurveBoundLine(Circle c1, Circle c2, Color col, double ControlX1, double ControlY1, double ControlX2, double ControlY2) {
		// Connect the 2 circle with this object
		connect(c1, c2, this);

		// Set the width of the line
		setStrokeWidth(3);
		// Set the color of the line
		setStroke(col.deriveColor(0, 1, 1, 0.5));
		setFill(Color.CORNSILK.deriveColor(0, 1.2, 1, 0.0));

		this.setControlX1(ControlX1);
		this.setControlX2(ControlX2);
		this.setControlY1(ControlY1);
		this.setControlY2(ControlY2);
		
		this.controlLine1 = new BoundLine(this.controlX1Property(), this.controlY1Property(), this.startXProperty(),
				this.startYProperty(), Color.GREY);
		this.controlLine2 = new BoundLine(this.controlX2Property(), this.controlY2Property(), this.endXProperty(),
				this.endYProperty(), Color.GREY);

		this.controlLine1.setMouseTransparent(true);
		this.controlLine2.setMouseTransparent(true);
	}

	/**
	 * Function to init the control point
	 * @param a (Arrow)
	 */
	public void setControl(Arrow a) {
		this.control1 = new AnchorPoint(Color.GOLDENROD, this.controlX1Property(), this.controlY1Property(), a);
		this.control2 = new AnchorPoint(Color.GOLDENROD, this.controlX2Property(), this.controlY2Property(), a);

		initControl(control1);
		initControl(control2);
	}

	/**
	 * 
	 * @return the control line 1
	 */
	public BoundLine getLineControl1() {
		return this.controlLine1;
	}

	/**
	 * 
	 * @return the control line 2
	 */
	public BoundLine getLineControl2() {
		return this.controlLine2;
	}

	/**
	 * 
	 * @return the control point 1
	 */
	public AnchorPoint getControl1() {
		return this.control1;
	}

	/**
	 * 
	 * @return the control point 2
	 */
	public AnchorPoint getControl2() {
		return this.control2;
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
	 * @param c1   (Circle) : First circle to link to
	 * @param c2   (Circle) : Second circle to link to
	 * @param line (Line) : Line that will connect the 2 Circle
	 */
	public static void connect(Circle c1, Circle c2, CubicCurve line) {
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

	/**
	 * Function to set the interaction
	 * @param AP (AnchorPoint)
	 */
	private void initControl(AnchorPoint AP) {
		AP.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				AP.setCursor(Cursor.CLOSED_HAND);
			}
		});
		AP.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				AP.setCursor(Cursor.HAND);
			}
		});
		AP.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				AP.setCursor(Cursor.DEFAULT);
			}
		});
	}
}
