package bfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.Edge;
import graph.Vertex;

public class BFS {

	private Vertex root;

	private List<Vertex> vertexGraph;

	private List<Edge> edgeGraph;

	private List<Vertex> VertexOrder;
	
	private Map<Vertex, Vertex> previous;

	private boolean isOriented;

	public BFS(Vertex root, List<Vertex> vertex, List<Edge> edge, boolean isOrient) {
		Map<Vertex, Vertex> LinkToPrevious = new HashMap<Vertex, Vertex>();
		this.edgeGraph = new ArrayList<Edge>();
		this.vertexGraph = new ArrayList<Vertex>();
		this.VertexOrder = new ArrayList<Vertex>();
		this.isOriented = isOrient;

		// create copy of the graph

		// create copy of vertex
		for (Vertex v : vertex) {
			Vertex newV = new Vertex(v.getColor(), v.getName());
			LinkToPrevious.put(v, newV);
			vertexGraph.add(newV);
		}

		// create copy of edge
		for (Edge e : edge) {
			Edge newE = new Edge(LinkToPrevious.get(e.getV1()), LinkToPrevious.get(e.getV2()), e.getWeight());
			edgeGraph.add(newE);
		}

		this.root = getVertex(root);
	}

	public void solve() {
		List<Vertex> discovered = new ArrayList<Vertex>();
		List<Vertex> queue = new ArrayList<Vertex>();
		this.previous = new HashMap<Vertex, Vertex>();

		queue.add(queue.size(), this.root);
		discovered.add(this.root);
		previous.put(this.root, null);

		while (!queue.isEmpty()) {

			this.VertexOrder.add(queue.get(0));

			if (!this.isOriented) {
				for (Edge e : queue.get(0).linkToMe) {
					if (!discovered.contains(e.getSecondVertex(queue.get(0)))) {
						queue.add(queue.size(), e.getSecondVertex(queue.get(0)));
						discovered.add(e.getSecondVertex(queue.get(0)));
						previous.put(e.getSecondVertex(queue.get(0)), queue.get(0));
					}
				}
			} else {
				for (Edge e : queue.get(0).linkToMe) {
					if (!discovered.contains(e.getV2())) {
						queue.add(queue.size(), e.getV2());
						discovered.add(e.getV2());
						previous.put(e.getV2(), queue.get(0));
					}
				}
			}

			queue.remove(0);
		}
	}

	private Vertex getVertex(Vertex v) {
		for (Vertex vGraph : this.vertexGraph) {
			if (vGraph.getName().equals(v.getName())) {
				return vGraph;
			}
		}
		return null;
	}
	
	public List<Vertex> getVertexOrder() {
		return this.VertexOrder;
	}
	
	public Map<Vertex, Vertex> getPreviousVertex(){
		return this.previous;
	}
}
