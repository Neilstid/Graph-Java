package TabuSearchTSP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import HamiltonianCycle.ClothestPath;
import VertexGestion.VertexConverter;
import graph.Edge;
import graph.Vertex;
import javafx.scene.paint.Color;

public class TabuSearchTSP {

	private int numberIteration;

	private int TabuListSize;

	private List<Vertex> TabuListV1;
	
	private List<Vertex> TabuListV2;

	private List<Vertex> BestPath;

	private List<Vertex> LastPath;

	private List<Vertex> vertexGraph;

	private List<Edge> edgeGraph;

	private boolean isOriented;
	
	public List<List<Vertex>> histoBestPath;
	
	public int iterationReal;
	
	private int tabuElementType;

	/**
	 * Constructor
	 * @param iteration (int) : Number of iteration for the tabu search
	 * @param TabuListSize (int) : size of the tabu list
	 * @param vertexList (List) : List of vertex of the graph
	 * @param edgeList (List) : List of edge of the graph
	 * @param _isOriented (boolean)
	 * @param typeTabu (int) : Element put in tabu list
	 */
	public TabuSearchTSP(int iteration, int TabuListSize, List<Vertex> vertexList, List<Edge> edgeList, boolean _isOriented, int typeTabu) {
		this.numberIteration = iteration;
		this.isOriented = _isOriented;
		this.TabuListSize = TabuListSize;
		tabuElementType = typeTabu;

		this.TabuListV1 = new ArrayList<Vertex>();
		this.TabuListV2 = new ArrayList<Vertex>();
		
		this.LastPath = new ArrayList<Vertex>();
		this.BestPath = new ArrayList<Vertex>();
		this.histoBestPath = new ArrayList<List<Vertex>>();
		
		//Create copy of vertex and edge
		Map<Vertex, Vertex> LinkToPrevious = new HashMap<Vertex, Vertex>();
		this.vertexGraph = new ArrayList<Vertex>();
		for (Vertex v : vertexList) {
			Vertex newV = new Vertex(Color.GREEN, v.getName());
			LinkToPrevious.put(v, newV);
			vertexGraph.add(newV);
		}

		this.edgeGraph = new ArrayList<Edge>();
		for (Edge e : edgeList) {
			edgeGraph.add(new Edge(LinkToPrevious.get(e.getV1()), LinkToPrevious.get(e.getV2()), e.getWeight()));
		}
	}

	/**
	 * Function to run the algorithm
	 * @return The solution found
	 */
	public List<Vertex> runTabuSearch() {
		BestPath = convertPath(ClothestPath.findCycle(this.vertexGraph, this.edgeGraph, this.isOriented));
		LastPath = new ArrayList<Vertex>(BestPath);
		int BestEval = fit(BestPath);
		histoBestPath.add(new ArrayList<Vertex>(BestPath));

		int iteration_actuel = 0;
		while (iteration_actuel < this.numberIteration) {
			LastPath = newPath();
			
			if(LastPath.isEmpty()) {
				iterationReal = iteration_actuel;
				return BestPath;
			}

			if (fit(LastPath) < BestEval) {
				BestPath = new ArrayList<Vertex>(LastPath);
				BestEval = fit(LastPath);
				histoBestPath.add(new ArrayList<Vertex>(BestPath));
			}

			iteration_actuel++;
		}

		iterationReal = iteration_actuel;
		return BestPath;
	}

	/**
	 * Search a new path non-tabu
	 * @return the path found
	 */
	public List<Vertex> newPath() {
		List<Vertex> Pathchange = new ArrayList<Vertex>();
		List<Vertex> tempPath = new ArrayList<Vertex>();
		int BestEval = Integer.MAX_VALUE;
		Vertex changeVertexV1 = new Vertex();
		Vertex changeVertexV2 = new Vertex();

		for (int position = 0; position < this.LastPath.size() - 1; position++) {
			for (int changement = position+1; changement < this.LastPath.size(); changement++) {

				tempPath.clear();
				tempPath = new ArrayList<Vertex>(this.LastPath);
				if (!(this.TabuListV1.contains(tempPath.get(position)) || this.TabuListV1.contains(tempPath.get(changement)) || this.TabuListV2.contains(tempPath.get(position)) || this.TabuListV2.contains(tempPath.get(changement)))) {
					
					Collections.swap(tempPath, position, changement);
					if (this.fit(tempPath) < BestEval) {
						Pathchange.clear();
						Pathchange = new ArrayList<Vertex>(tempPath);
						changeVertexV1 = tempPath.get(position);
						changeVertexV2 = tempPath.get(changement);
					}
				}
			}
		}

		if(this.tabuElementType == 0) {
			this.TabuListV1.add(0, changeVertexV1);
		}else if(this.tabuElementType == 1) {
			this.TabuListV2.add(0, changeVertexV2);
		}else {
			this.TabuListV1.add(0, changeVertexV1);
			this.TabuListV2.add(0, changeVertexV2);
		}
		
		if (this.TabuListV1.size() > this.TabuListSize) {
			this.TabuListV1.remove(this.TabuListV1.size() - 1);
		}
		if (this.TabuListV2.size() > this.TabuListSize) {
			this.TabuListV2.remove(this.TabuListV2.size() - 1);
		}

		return Pathchange;
	}

	/**
	 * Function to get the fitness of a cycle
	 * @param to_eval (List) : Cycle to evaluate
	 * @return Fitness of the cycle
	 */
	public int fit(List<Vertex> to_eval) {
		if(to_eval.size() != this.vertexGraph.size()) {
			return Integer.MAX_VALUE;
		}
		
		int fitness = 0;
		for (int position = 0; position < to_eval.size(); position++) {
			int weight = Integer.MAX_VALUE;
			if (this.isOriented) {
				for (Edge e : to_eval.get(position).linkToMe) {
					if (e.getV1().equals(to_eval.get(position)) && e.getSecondVertex(to_eval.get(position))
							.equals(to_eval.get((position + 1) % (to_eval.size())))) {
						weight = e.getWeight();
					}
				}
			} else {
				for (Edge e : to_eval.get(position).linkToMe) {
					if (e.getSecondVertex(to_eval.get(position))
							.equals(to_eval.get((position + 1) % (to_eval.size())))) {
						weight = e.getWeight();
					}
				}
			}
			if (weight == Integer.MAX_VALUE) {
				return Integer.MAX_VALUE;
			}
			fitness += weight;
		}

		return fitness;
	}

	private List<Vertex> convertPath(List<Vertex> PathToTranslate) {
		List<Vertex> newPath = new ArrayList<Vertex>();
		for (Vertex v : PathToTranslate) {
			newPath.add(VertexConverter.getVertex(vertexGraph, v.getName()));
		}

		return newPath;
	}
}