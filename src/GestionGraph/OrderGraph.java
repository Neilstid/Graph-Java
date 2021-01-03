package GestionGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import graph.Graph;
import graph.Vertex;
import javafx.scene.layout.Pane;

public class OrderGraph {
	
	/**
	 * Functio to put order in the graph by changing place of vertex
	 * @param p (Pane) : Where the graph is display
	 */
	public static void order(Pane p) {
		List<Vertex> ListVertex = Graph.getListVertex(p);
		Map<Vertex, Integer> ListDegree = Graph.degree(ListVertex);
		List<Integer> circleRadius = new ArrayList<Integer>();
		
		List<Integer> list = new ArrayList<Integer>(ListDegree.values());		
		if(Collections.frequency(list, Collections.max(list)) == 1) {
			Vertex v = getElement(ListDegree, Collections.max(list)).get(0);
			v.move((int) ((p.getHeight()-50)/2),(int) ((p.getWidth()-50)/2));
			
			circleRadius.add(0);
			ListDegree.remove(v);
			ListDegree.put(v, Integer.MIN_VALUE);
		}else {
			circleRadius.add((int) ((Collections.frequency(list, Collections.max(list)) / list.size()) * Math.min((p.getHeight()-50)/2, ((p.getWidth()-50)/2))) );
		}
		
		for(int circleNum = 0; circleNum < list.stream().distinct().count(); circleNum++) {
			circleRadius.add( (int) (((Math.min((p.getHeight()-50)/2, (p.getWidth()-50)/2)) - circleRadius.get(0)) / list.stream().distinct().count()) * circleNum + circleRadius.get(0));
		}
		
		int count = 1;
		for(int Radius:circleRadius) {
			if(Radius == 0) {
				continue;
			}
			
			for(Vertex vMax:getElement(ListDegree, Collections.max(list))) {
				double angle = (double) count / (double) list.size() * 360.0;
				vMax.move( (int) ((Radius * Math.cos(angle*Math.PI/180.0)) + (Math.min((p.getHeight()-50)/2, (p.getWidth()-50)/2))) + 25, (int) ((Radius * Math.sin(angle*Math.PI/180.0)) + (Math.min((p.getHeight()-50)/2, (p.getWidth()-50)/2))) + 25);
				
				ListDegree.put(vMax, Integer.MIN_VALUE);
				count++;
			}
		}
	}
	
	/**
	 * Function to get all Vertex with a giving degree
	 * @param ListDegree (Map) : Map of vertex and degree
	 * @param degree (int) : degree that we search
	 * @return List of all vertex with the giving degree
	 */
	private static List<Vertex> getElement(Map<Vertex, Integer> ListDegree, int degree){
		List<Vertex> to_return = new ArrayList<Vertex>();
		for (Vertex key: ListDegree.keySet()) {
		    if(ListDegree.get(key) == degree) {
		    	to_return.add(key);
		    }
		}
		return to_return;
	}
}
