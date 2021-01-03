package dfs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.Edge;
import graph.Vertex;

public class DFS {

	private Vertex root;

	private List<Vertex> vertexGraph;

	private List<Edge> edgeGraph;

	private List<Vertex> VertexOrder;
	
	private Map<Vertex, Vertex> previous;

	private boolean isOriented;
	
	private List<Vertex> discovered;

	public DFS(Vertex root, List<Vertex> vertex, List<Edge> edge, boolean isOrient) {
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
		this.discovered = new ArrayList<Vertex>();
		this.previous = new HashMap<Vertex, Vertex>();
		this.discovered.clear();
		this.VertexOrder.clear();

		previous.put(this.root, null);
		explore(this.root);
	}

	private  void explore(Vertex v) {
		this.discovered.add(v);
		this.VertexOrder.add(v);
		for(Edge e:v.linkToMe) {
			if(!isOriented && !discovered.contains(e.getSecondVertex(v))) {
				previous.put(e.getSecondVertex(v), v);
				explore(e.getSecondVertex(v));
			}else if(isOriented && e.getV1().equals(v) && !discovered.contains(e.getSecondVertex(v))) {
				previous.put(e.getSecondVertex(v), v);
				explore(e.getSecondVertex(v));
			}
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
