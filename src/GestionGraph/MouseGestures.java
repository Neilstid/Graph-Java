package GestionGraph;

import application.MainInterface_Controller;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

/**
 * 
 * @author Neil Farmer
 *
 */
public class MouseGestures {

	double orgSceneX, orgSceneY;
	
	double orgTranslateX, orgTranslateY;

	/**
	 * Make a node draggable
	 * @param node (Node) : Node that will be draggable
	 */
	public void makeDraggable(Node node) {
		node.setOnMousePressed(circleOnMousePressedEventHandler);
		node.setOnMouseDragged(circleOnMouseDraggedEventHandler);
	}

	/**
	 * Make a node draggable
	 * @param node (Node) : Node that will be draggable
	 * @param app (MainInterface_Controller) : Link to the main interface
	 */
	public void makeDraggableText(Node node, MainInterface_Controller app) {
		enableDragText(app, node);
	}

	EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {

			orgSceneX = t.getSceneX();
			orgSceneY = t.getSceneY();

			if (t.getSource() instanceof Circle) {

				Circle p = ((Circle) (t.getSource()));

				orgTranslateX = p.getCenterX();
				orgTranslateY = p.getCenterY();

			} else {

				Node p = ((Node) (t.getSource()));

				orgTranslateX = p.getTranslateX();
				orgTranslateY = p.getTranslateY();

			}
		}
	};

	EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		@Override
		public void handle(MouseEvent t) {

			double offsetX = t.getSceneX() - orgSceneX;
			double offsetY = t.getSceneY() - orgSceneY;

			double newTranslateX = orgTranslateX + offsetX;
			double newTranslateY = orgTranslateY + offsetY;

			if (t.getSource() instanceof Circle) {

				Circle p = ((Circle) (t.getSource()));

				p.setCenterX(newTranslateX);
				p.setCenterY(newTranslateY);

			} else {

				Node p = ((Node) (t.getSource()));

				p.setTranslateX(newTranslateX);
				p.setTranslateY(newTranslateY);

			}

		}
	};

	private void enableDragText(MainInterface_Controller app, Node node) {
		node.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0) {
					app.Overlay.setCursor(Cursor.CLOSED_HAND);

					orgSceneX = mouseEvent.getSceneX();
					orgSceneY = mouseEvent.getSceneY();

					if (mouseEvent.getSource() instanceof Circle) {

						Circle p = ((Circle) (mouseEvent.getSource()));

						orgTranslateX = p.getCenterX();
						orgTranslateY = p.getCenterY();

					} else {

						Node p = ((Node) (mouseEvent.getSource()));

						orgTranslateX = p.getTranslateX();
						orgTranslateY = p.getTranslateY();

					}
				}
			}
		});
		node.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0) {
					app.Overlay.setCursor(Cursor.OPEN_HAND);
				}
			}
		});
		node.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0) {
					double offsetX = mouseEvent.getSceneX() - orgSceneX;
					double offsetY = mouseEvent.getSceneY() - orgSceneY;

					double newTranslateX = orgTranslateX + offsetX;
					double newTranslateY = orgTranslateY + offsetY;

					if (mouseEvent.getSource() instanceof Circle) {

						Circle p = ((Circle) (mouseEvent.getSource()));

						p.setCenterX(newTranslateX);
						p.setCenterY(newTranslateY);

					} else {

						Node p = ((Node) (mouseEvent.getSource()));

						p.setTranslateX(newTranslateX);
						p.setTranslateY(newTranslateY);

					}
				}
			}
		});
		node.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0) {
					app.Overlay.setCursor(Cursor.OPEN_HAND);
				}
			}
		});
		node.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (app.userElementSelected == 0) {
					app.Overlay.setCursor(Cursor.DEFAULT);
				}
			}
		});
	}
}
