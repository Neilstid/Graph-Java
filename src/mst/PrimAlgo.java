package mst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import javafx.scene.layout.Pane;

public class PrimAlgo {
	// Vertex where start the algorithm
	private Vertex departure;

	// Pane where the graph is display
	private Pane pane;
	
	//List of Vertex visited in order of when they were visited by the algorithm
	private List<Vertex> VertexVisitOrder = new ArrayList<Vertex>();

	// List of visited edge by the algorithm
	private List<Edge> EdgeVisited = new ArrayList<Edge>();

	// List of unvisited edge by the algorithm
	private List<Vertex> unVisited = new ArrayList<Vertex>();

	// Distance of the vertex from the vertex departure
	private Map<Vertex, Integer> Distance = new HashMap<>();

	// The vertex that preceed
	private Map<Vertex, Vertex> Previous = new HashMap<>();

	// List of Edge visited in order of when they were used by the algorithm
	private List<Edge> EdgeVisitOrder = new ArrayList<Edge>();

	public PrimAlgo(Vertex dep, Pane p) {
		this.departure = dep;
		this.pane = p;

		// run the algorithm
		this.solve();
	}

	/**
	 * Function to get all the edge visited
	 * 
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
	 * 
	 * @return List of visited edge by the algorithm ordered by when they have been
	 *         visited
	 */
	public List<Edge> EdgeVisitedOrder() {
		List<Vertex> VertexOrder = new ArrayList<Vertex>(this.VertexVisitOrder);
		VertexOrder.remove(0);
		// clear if the list already exist
		this.EdgeVisitOrder.clear();

		for (Vertex v : VertexOrder) {
			for (Edge e : v.linkToMe) {
				if (e.getV1().equals(this.Previous.get(v)) || e.getV2().equals(this.Previous.get(v))) {
					this.EdgeVisitOrder.add(e);
				}
			}
		}
		return this.EdgeVisitOrder;
	}

	/**
	 * Function to run the prim's algo
	 * @return The mst found
	 */
	public Map<Vertex, Vertex> solve() {

		this.unVisited = new ArrayList<Vertex>();
		this.Distance = new HashMap<>();
		this.Previous = new HashMap<>();
		Map<Vertex, Vertex> VisitedPrevious = new HashMap<Vertex, Vertex>();
		this.unVisited = Graph.getListVertex(pane);
		this.VertexVisitOrder.clear();

		// For each vertex we put an infinity distance from the vertex departure and put
		// the previous vertex to null
		for (Vertex v : this.unVisited) {
			this.Distance.put(v, Integer.MAX_VALUE);
			this.Previous.put(v, null);
		}

		// We set the data for the vertex departure
		this.Distance.put(this.departure, 0);

		// While all the vertex have not been visited we run the algorithm
		while (!this.unVisited.isEmpty()) {
			// Search the vertex with the minimum distance from the departure that we know
			Vertex minVertex = null;
			int minValue = Integer.MAX_VALUE;
			for (Vertex v : this.unVisited) {
				if (minValue > this.Distance.get(v)) {
					minValue = this.Distance.get(v);
					minVertex = v;
				}
			}

			// If none vertex have been found (Disconnected graph for example, all vertex
			// can't be found)
			if (minVertex == null) {
				return VisitedPrevious;
			}

			// We update our list
			VisitedPrevious.put(minVertex, Previous.get(minVertex));
			this.unVisited.remove(minVertex);
			this.VertexVisitOrder.add(minVertex);

				for (Edge e : minVertex.linkToMe) {
					if (!e.getV2().equals(minVertex)
							&& e.getWeight() < this.Distance.get(e.getV2())&& unVisited.contains(e.getV2())) {
						this.Distance.put(e.getV2(), e.getWeight());
						this.Previous.put(e.getV2(), minVertex);
					} else if (!e.getV1().equals(minVertex)
							&& e.getWeight() < this.Distance.get(e.getV1()) && unVisited.contains(e.getV1())) {
						this.Distance.put(e.getV1(), e.getWeight());
						this.Previous.put(e.getV1(), minVertex);
					}
				}
			}
		return Previous;
	}
}
