package HamiltonianCycle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.Edge;
import graph.Graph;
import graph.Vertex;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class ClothestPath {

	/**
	 * Function to return an hamiltonian cycle if the cycle exist
	 * 
	 * @param vertexList (List) : List of Vertex
	 * @param edgeList (List) : List of edge
	 * @param isOriented (boolean) : true if the graph is oriented, else false
	 * @return The hamiltonian cycle
	 */
	public static List<Vertex> findCycle(List<Vertex> vertexList, List<Edge> edgeList, boolean isOriented) {
		Map<Vertex, Vertex> LinkToPrevious = new HashMap<Vertex, Vertex>();
		List<Vertex> vertexGraph = new ArrayList<Vertex>();
		
		//Create a copy of vertex and edge
		for (Vertex v : vertexList) {
			Vertex newV = new Vertex(Color.GREEN, v.getName());
			LinkToPrevious.put(v, newV);
			vertexGraph.add(newV);
		}

		List<Edge> edgeGraph = new ArrayList<Edge>();
		for (Edge e : edgeList) {
			edgeGraph.add(new Edge(LinkToPrevious.get(e.getV1()), LinkToPrevious.get(e.getV2()), e.getWeight()));
		}

		int taille = vertexGraph.size();

		//Add an edge of the vertex with the smallest degree to tha path
		List<Vertex> Path = new ArrayList<Vertex>();
		List<Vertex> OrderDegree = Graph.vertexOrderDegree(vertexGraph);
		Path.add(OrderDegree.get(OrderDegree.size() - 1).linkToMe.get(0).getV1());
		Path.add(OrderDegree.get(OrderDegree.size() - 1).linkToMe.get(0).getV2());

		//if the graph is oriented
		if (isOriented) {
			//Apply dijkstra to add vertex to the cycle
			List<Vertex> result = DijkstraModified(vertexGraph, edgeGraph, Path, Path.get(1), Path.get(0), isOriented);
			
			//Remove the start and end of the path found
			result.remove(Path.get(1));
			result.remove(Path.get(0));

			if (result.isEmpty()) {
				return null;
			}

			Path.addAll(Path.indexOf(Path.get(1)) + 1, result);
		}

		List<Vertex> vertexDijkstra = new ArrayList<Vertex>(vertexGraph);
		List<Edge> edgeDijkstra = new ArrayList<Edge>(edgeGraph);

		while (Path.size() < taille) {
			List<Edge> tempEdge = new ArrayList<Edge>(edgeGraph);
			for (Edge e : edgeGraph) {
				if (Path.contains(e.getV1()) && Path.contains(e.getV2())) {
					e.RemoveFomVertex();
					tempEdge.remove(e);
				}
			}
			edgeGraph = tempEdge;

			Vertex departure = null;
			Vertex arrival = null;
			int TotalDegree = 0;
			for (int val = 0; val < Path.size(); val++) {
				if (Path.get(val).linkToMe.size() > 0 && Path.get((val + 1) % Path.size()).linkToMe.size() > 0
						&& TotalDegree < Path.get(val).linkToMe.size()
								+ Path.get((val + 1) % Path.size()).linkToMe.size()) {
					if (!isOriented) {
						departure = Path.get(val);
						arrival = Path.get((val + 1) % Path.size());
						TotalDegree = Path.get(val).linkToMe.size() + Path.get((val + 1) % Path.size()).linkToMe.size();

					} else {
						for (Edge e1 : Path.get(val).linkToMe) {
							if (e1.getV1().equals(Path.get(val))) {
								departure = Path.get(val);
								break;
							}
						}
						for (Edge e2 : Path.get((val + 1) % Path.size()).linkToMe) {
							if (e2.getV2().equals(Path.get((val + 1) % Path.size()))) {
								arrival = Path.get((val + 1) % Path.size());
								break;
							}
						}
						if (arrival != null && departure != null) {
							break;
						} else {
							arrival = null;
							departure = null;
						}
					}

				} else if (Path.get(val).linkToMe.size() == 0) {
					vertexGraph.remove(Path.get(val));
				}
			}

			vertexDijkstra.clear();
			edgeDijkstra.clear();
			vertexDijkstra = new ArrayList<Vertex>(vertexGraph);
			edgeDijkstra = new ArrayList<Edge>(edgeGraph);

			for (Vertex v : Path) {
				if (vertexGraph.contains(v) && (v != departure && v != arrival)) {
					vertexDijkstra.remove(v);
					for (Edge e : v.linkToMe) {
						edgeDijkstra.remove(e);
					}
				}
			}

			List<Vertex> result = DijkstraModified(vertexDijkstra, edgeDijkstra, Path, departure, arrival, isOriented);
			result.remove(departure);
			result.remove(arrival);

			if (result.isEmpty()) {
				break;
			}

			Path.addAll(Path.indexOf(departure) + 1, result);
		}
		return Path;
	}
	
	/**
	 * Function to return an hamiltonian cycle if the cycle exist
	 * 
	 * @param pane       (Pane) : Pane where the graph is display
	 * @param isOriented (boolean) : true if the graph is oriented, else false
	 * @return The hamiltonian cycle
	 */
	public static List<Vertex> findCycle(Pane pane, boolean isOriented) {
		Map<Vertex, Vertex> LinkToPrevious = new HashMap<Vertex, Vertex>();
		List<Vertex> vertexGraph = new ArrayList<Vertex>();
		for (Vertex v : Graph.getListVertex(pane)) {
			Vertex newV = new Vertex(Color.GREEN, v.getName());
			LinkToPrevious.put(v, newV);
			vertexGraph.add(newV);
		}

		List<Edge> edgeGraph = new ArrayList<Edge>();
		for (Edge e : Graph.getListEdge(pane)) {
			edgeGraph.add(new Edge(LinkToPrevious.get(e.getV1()), LinkToPrevious.get(e.getV2()), e.getWeight()));
		}

		int taille = vertexGraph.size();

		List<Vertex> Path = new ArrayList<Vertex>();
		List<Vertex> OrderDeree = Graph.vertexOrderDegree(vertexGraph);
		Path.add(OrderDeree.get(OrderDeree.size() - 1).linkToMe.get(0).getV1());
		Path.add(OrderDeree.get(OrderDeree.size() - 1).linkToMe.get(0).getV2());

		if (isOriented) {
			List<Vertex> result = DijkstraModified(vertexGraph, edgeGraph, Path, Path.get(1), Path.get(0), isOriented);
			result.remove(Path.get(1));
			result.remove(Path.get(0));

			if (result.isEmpty()) {
				return null;
			}

			Path.addAll(Path.indexOf(Path.get(1)) + 1, result);
		}

		List<Vertex> vertexDijkstra = new ArrayList<Vertex>(vertexGraph);
		List<Edge> edgeDijkstra = new ArrayList<Edge>(edgeGraph);

		while (Path.size() < taille) {
			List<Edge> tempEdge = new ArrayList<Edge>(edgeGraph);
			for (Edge e : edgeGraph) {
				if (Path.contains(e.getV1()) && Path.contains(e.getV2())) {
					e.RemoveFomVertex();
					tempEdge.remove(e);
				}
			}
			edgeGraph = tempEdge;

			Vertex departure = null;
			Vertex arrival = null;
			int TotalDegree = 0;
			for (int val = 0; val < Path.size(); val++) {
				if (Path.get(val).linkToMe.size() > 0 && Path.get((val + 1) % Path.size()).linkToMe.size() > 0
						&& TotalDegree < Path.get(val).linkToMe.size()
								+ Path.get((val + 1) % Path.size()).linkToMe.size()) {
					if (!isOriented) {
						departure = Path.get(val);
						arrival = Path.get((val + 1) % Path.size());
						TotalDegree = Path.get(val).linkToMe.size() + Path.get((val + 1) % Path.size()).linkToMe.size();

					} else {
						for (Edge e1 : Path.get(val).linkToMe) {
							if (e1.getV1().equals(Path.get(val))) {
								departure = Path.get(val);
								break;
							}
						}
						for (Edge e2 : Path.get((val + 1) % Path.size()).linkToMe) {
							if (e2.getV2().equals(Path.get((val + 1) % Path.size()))) {
								arrival = Path.get((val + 1) % Path.size());
								break;
							}
						}
						if (arrival != null && departure != null) {
							break;
						} else {
							arrival = null;
							departure = null;
						}
					}

				} else if (Path.get(val).linkToMe.size() == 0) {
					vertexGraph.remove(Path.get(val));
				}
			}

			vertexDijkstra.clear();
			edgeDijkstra.clear();
			vertexDijkstra = new ArrayList<Vertex>(vertexGraph);
			edgeDijkstra = new ArrayList<Edge>(edgeGraph);

			for (Vertex v : Path) {
				if (vertexGraph.contains(v) && (v != departure && v != arrival)) {
					vertexDijkstra.remove(v);
					for (Edge e : v.linkToMe) {
						edgeDijkstra.remove(e);
					}
				}
			}

			List<Vertex> result = DijkstraModified(vertexDijkstra, edgeDijkstra, Path, departure, arrival, isOriented);
			result.remove(departure);
			result.remove(arrival);

			if (result.isEmpty()) {
				break;
			}

			Path.addAll(Path.indexOf(departure) + 1, result);
		}
		return Path;
	}

	//A modified dijkstra to find path between two vertex of the cycle, passing by none vertex in the actual cycle
	private static List<Vertex> DijkstraModified(List<Vertex> vertexGraph, List<Edge> edgeGraph, List<Vertex> Path,
			Vertex departure, Vertex arrival, boolean isOriented) {
		List<Vertex> unVisited = new ArrayList<Vertex>(vertexGraph);
		Map<Vertex, Integer> Distance = new HashMap<>();
		Map<Vertex, Vertex> Previous = new HashMap<>();
		Map<Vertex, Vertex> VisitedPrevious = new HashMap<Vertex, Vertex>();

		// For each vertex we put an infinity distance from the vertex departure and put
		// the previous vertex to null
		for (Vertex v : vertexGraph) {
			Distance.put(v, Integer.MAX_VALUE);
			Previous.put(v, null);
		}

		// We set the data for the vertex departure
		Distance.put(departure, 0);

		Vertex minVertex = null;
		// While all the vertex have not been visited we run the algorithm
		while (minVertex != arrival) {
			// Search the vertex with the minimum distance from the departure that we know
			minVertex = null;
			int minValue = Integer.MAX_VALUE;
			for (Vertex v : unVisited) {
				if (minValue > Distance.get(v)) {
					minValue = Distance.get(v);
					minVertex = v;
				}
			}

			// If none vertex have been found (Disconnected graph for example, all vertex
			// can't be found)
			if (minVertex == null) {
				break;
			}

			// We update our list
			VisitedPrevious.put(minVertex, Previous.get(minVertex));
			unVisited.remove(minVertex);

			// Update the distance and the previous from the new vertex that we know
			if (isOriented) {
				for (Edge e : minVertex.linkToMe) {
					if (edgeGraph.contains(e)) {
						if (!e.getV2().equals(minVertex)
								&& e.getWeight() + Distance.get(minVertex) < Distance.get(e.getV2())) {
							Distance.put(e.getV2(), e.getWeight() + Distance.get(minVertex));
							Previous.put(e.getV2(), minVertex);
						}
					}
				}
			} else {
				for (Edge e : minVertex.linkToMe) {
					if (edgeGraph.contains(e)) {
						Vertex v = e.getSecondVertex(minVertex);
						if (e.getWeight() + Distance.get(minVertex) < Distance.get(v)) {
							Distance.put(v, e.getWeight() + Distance.get(minVertex));
							Previous.put(v, minVertex);
						}
					}
				}
			}
		}

		Vertex u = arrival;
		List<Vertex> p = new ArrayList<Vertex>();

		while (u != null) {
			p.add(0, u);
			u = Previous.get(u);
		}

		return p;
	}
}
