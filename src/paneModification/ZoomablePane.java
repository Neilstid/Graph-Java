package paneModification;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

public class ZoomablePane {
	public Group content = new Group();
	final double SCALE_DELTA = 1.1;
	
	public ZoomablePane() {
	}
	
	public void makeZoomablePane(Pane pane) {
		pane.setOnScroll((ScrollEvent event) -> {
			event.consume();
			if (event.getDeltaY() == 0) {
				return;
			}
			
			double zoomlvl = (event.getDeltaY() > 0) ? SCALE_DELTA : 1 / SCALE_DELTA;

			for(Object o:pane.getChildren()) {
				((Node) o).setScaleX(((Node) o).getScaleX() * zoomlvl);
				((Node) o).setScaleY(((Node) o).getScaleY() * zoomlvl);
			}

		});
	}
}
