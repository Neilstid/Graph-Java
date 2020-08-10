package GestionGraph;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

/**
 * Class to draw the head of arrow
 * @author Neil Farmer
 *
 */
public class Arrow extends Polygon {

        public double rotate;
        
        public float t;
        
        private Line curve;
        
        public Rotate rz;

        //Constructor
        /**
         * Constructor
         * @param curve (Line) : Line where the triangle should end
         * @param t (float)
         * @param col (Color)
         */
        public Arrow(Line curve, float t, Color col) {
            super(new double[] { 0, 0, 7, 15, -7, 15 });
            this.curve = curve;
            this.t = t;
            init(col);
        }

        //Constructor
        /**
         * Constructor
         * @param curve (Line) : Line where the triangle should end
         * @param t (float)
         * @param col (Color) : Color of the Arrow
         * @param arg0 (double) : shape of the polygon
         */
        public Arrow(BoundLine curve, float t, Color col,double... arg0) {
            super(arg0);
            this.curve = curve;
            this.t = t;
            init(col);
        }
        
        /**
         * Constructor
         */
        public Arrow() {
        	super(new double[] { 0, 0, 7, 15, -7, 15 });
        }
        
        /**
         * Function to initialize the arrow
         * @param col (Color) : Color of the arrow
         */
        private void init(Color col) {
        	//Set the color
            setFill(col.deriveColor(0, 1, 1, 0.5));

            //define the rotation
            rz = new Rotate();
            {
                rz.setAxis(Rotate.Z_AXIS);
            }
            getTransforms().addAll(rz);

            update();
        }

        /**
         * Function to update the position and rotation of the arrow
         */
        public void update() {
            double size = Math.max(curve.getBoundsInLocal().getWidth(), curve.getBoundsInLocal().getHeight());
            double scale = size / 4d;

            Point2D ori = eval(curve, t);
            Point2D tan = evalDt(curve, t).normalize().multiply(scale);

            setTranslateX(ori.getX());
            setTranslateY(ori.getY());

            double angle = Math.atan2( tan.getY(), tan.getX());

            angle = Math.toDegrees(angle);

            double offset = -90;
            if( t > 0.5)
                offset = +90;

            rz.setAngle(angle + offset);

        }

          private Point2D eval(Line c, float t){
              Point2D p=new Point2D(Math.pow(1-t,3)*c.getStartX()+
                      3*t*Math.pow(1-t,2)*c.getStartX()+
                      3*(1-t)*t*t*c.getEndX()+
                      Math.pow(t, 3)*c.getEndX(),
                      Math.pow(1-t,3)*c.getStartY()+
                      3*t*Math.pow(1-t, 2)*c.getStartY()+
                      3*(1-t)*t*t*c.getEndY()+
                      Math.pow(t, 3)*c.getEndY());
              return p;
          }

          private Point2D evalDt(Line c, float t){
              Point2D p=new Point2D(-3*Math.pow(1-t,2)*c.getStartX()+
                      3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getStartX()+
                      3*((1-t)*2*t-t*t)*c.getEndX()+
                      3*Math.pow(t, 2)*c.getEndX(),
                      -3*Math.pow(1-t,2)*c.getStartY()+
                      3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getStartY()+
                      3*((1-t)*2*t-t*t)*c.getEndY()+
                      3*Math.pow(t, 2)*c.getEndY());
              return p;
          }
    }