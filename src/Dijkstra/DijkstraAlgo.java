package Dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import javafx.scene.layout.Pane;

/**
 * 
 * @author Neil Farmer
 *
 */
public class DijkstraAlgo {

	//Vertex where start the algorithm
	private Vertex departure;

	//Vertex where end the algorithm
	private Vertex arrival;

	//true if the graph is oriented, else false
	private boolean isOriented;

	//Pane where the graph is display
	private Pane pane;

	//List of visited edge by the algorithm
	private List<Edge> EdgeVisited = new ArrayList<Edge>();

	//List of unvisited edge by the algorithm
	private List<Vertex> unVisited = new ArrayList<Vertex>();

	//Distance of the vertex from the vertex departure
	private Map<Vertex, Integer> Distance = new HashMap<>();

	//The vertex that preceed 
	private Map<Vertex, Vertex> Previous = new HashMap<>();

	//List of Vertex visited in order of when they were visited by the algorithm
	private List<Vertex> VertexVisitOrder = new ArrayList<Vertex>();

	//List of Edge visited in order of when they were used by the algorithm
	private List<Edge> EdgeVisitOrder = new ArrayList<Edge>();

	//Path from departure to arrival
	private List<Vertex> path;

	/**
	 * Constructor
	 * @param dep (Vertex) : Vertex where must start the algorithm
	 * @param arr (Vertex) : Vertex where the algorithm will stop
	 * @param Oriented (boolean) : true if the graph is oriented, else false
	 * @param p (Pane) : Pane where the graph is display
	 */
	public DijkstraAlgo(Vertex dep, Vertex arr, boolean Oriented, Pane p) {
		this.departure = dep;
		this.arrival = arr;
		this.isOriented = Oriented;
		this.pane = p;
		
		//run the algorithm
		this.solve();
	}

	/**
	 * Function to get all the edge visited
	 * @return List of visited edge by the algorithm
	 */
	public List<Edge> EdgeVisited() {
		Map<Vertex, Vertex> edgeDijkstra = new HashMap<>();
		edgeDijkstra = this.solve();

		for (Vertex v : edgeDijkstra.keySet()) {
			for (Edge e : v.linkToMe) {
				if (e.getV1().equals(edgeDijkstra.get(v)) || e.getV2().equals(edgeDijkstra.get(v))) {
					EdgeVisited.add(e);
				}
			}
		}
		return EdgeVisited;
	}

	/**
	 * Function to get all the edge visited ordered by when they have been visited
	 * @return List of visited edge by the algorithm ordered by when they have been visited
	 */
	public List<Edge> EdgeVisitedOrder() {
		List<Vertex> VertexOrder = new ArrayList<Vertex>(this.VertexVisitOrder);
		VertexOrder.remove(0);
		
		//clear if the list already exist
		this.EdgeVisitOrder.clear();

		//If the graph is non-oriented (The vertex can be either v1 or v2 of the edge)
		if (!this.isOriented) {
			for (Vertex v : VertexOrder) {
				for (Edge e : v.linkToMe) {
					if (e.getV1().equals(this.Previous.get(v)) || e.getV2().equals(this.Previous.get(v))) {
						this.EdgeVisitOrder.add(e);
					}
				}
			}
		}else { //if the graph is oriented
			for (Vertex v : VertexOrder) {
				for (Edge e : v.linkToMe) {
					if (e.getV1().equals(this.Previous.get(v))) {
						this.EdgeVisitOrder.add(e);
					}
				}
			}
		}
		return this.EdgeVisitOrder;
	}

	/**
	 * Function to get all the vertex visited ordered by when they have been visited
	 * @return List of visited vertex by the algorithm ordered by when they have been visited
	 */
	public List<Vertex> getVertexOrder() {
		return this.VertexVisitOrder;
	}

	/**
	 * Function to get the path from the departure to the arrival
	 * @return  path from the departure to the arrival
	 */
	public List<Vertex> VertexPath() {
		Vertex u = this.arrival;
		this.path = new ArrayList<Vertex>();

		while (u != null) {
			this.path.add(0, u);
			u = this.Previous.get(u);
		}

		return this.path;
	}

	/**
	 * Function to apply the Dijkstra's algorithm on the graph
	 * @return The list of vertex that preceed every vertex 
	 */
	public Map<Vertex, Vertex> solve() {

		this.unVisited = new ArrayList<Vertex>();
		this.Distance = new HashMap<>();
		this.Previous = new HashMap<>();
		Map<Vertex, Vertex> VisitedPrevious = new HashMap<Vertex, Vertex>();
		this.unVisited = Graph.getListVertex(pane);
		this.VertexVisitOrder.clear();

		//For each vertex we put an infinity distance from the vertex departure and put the previous vertex to null
		for (Vertex v : this.unVisited) {
			this.Distance.put(v, Integer.MAX_VALUE);
			this.Previous.put(v, null);
		}

		//We set the data for the vertex departure
		this.Distance.put(this.departure, 0);

		//While all the vertex have not been visited we run the algorithm
		while (!this.unVisited.isEmpty()) {
			//Search the vertex with the minimum distance from the departure that we know
			Vertex minVertex = null;
			int minValue = Integer.MAX_VALUE;
			for (Vertex v : this.unVisited) {
				if (minValue > this.Distance.get(v)) {
					minValue = this.Distance.get(v);
					minVertex = v;
				}
			}

			//If none vertex have been found (Disconnected graph for example, all vertex can't be found)
			if (minVertex == null) {
				return VisitedPrevious;
			}

			//We update our list
			VisitedPrevious.put(minVertex, Previous.get(minVertex));
			this.unVisited.remove(minVertex);
			this.VertexVisitOrder.add(minVertex);

			//If we have found the arrival
			if (this.arrival != null && minVertex.equals(this.arrival)) {
				return VisitedPrevious;
			}

			//Update the distance and the previous from the new vertex that we know
			if (this.isOriented) {
				for (Edge e : minVertex.linkToMe) {
					//update distance and the previous if it's needed
					if (!e.getV2().equals(minVertex)
							&& e.getWeight() + this.Distance.get(minVertex) < this.Distance.get(e.getV2())) {
						this.Distance.put(e.getV2(), e.getWeight() + this.Distance.get(minVertex)); 
						this.Previous.put(e.getV2(), minVertex);
					}
				}
			} else { //if the graph is oriented
				for (Edge e : minVertex.linkToMe) {
					if (!e.getV2().equals(minVertex)
							&& e.getWeight() + this.Distance.get(minVertex) < this.Distance.get(e.getV2())) {
						this.Distance.put(e.getV2(), e.getWeight() + this.Distance.get(minVertex));
						this.Previous.put(e.getV2(), minVertex);
					} else if (!e.getV1().equals(minVertex)
							&& e.getWeight() + this.Distance.get(minVertex) < this.Distance.get(e.getV1())) {
						this.Distance.put(e.getV1(), e.getWeight() + this.Distance.get(minVertex));
						this.Previous.put(e.getV1(), minVertex);
					}
				}
			}
		}

		return Previous;
	}
}
