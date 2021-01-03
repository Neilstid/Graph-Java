package branchAndBound;

import graph.Vertex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.Edge;
import graph.Graph;

public class Little {

	//Number of vertex in the graph
	private int nbVertex;

	//The best path fond
	private int final_path[];

	//List of visited node
	private boolean visited[];

	//Best evaluation 
	private int final_res = Integer.MAX_VALUE;
	
	//All leaf that have upgrade the path found
	private List<List<String>> histo_path;
	
	//Best path (with name of vertex)
	private List<String> best_path;
	
	//List of all edge in graph
	private List<Edge> edgeList;
	
	//List of all vertex in the graph
	private List<Vertex> vertexList;
	
	//Matrix of the graph
	private int[][] matrix;

	/**
	 * Contructor
	 * @param vertex0 (List) :  List of vertex in graph
	 * @param edge0 (List) : List of edge in the graph
	 * @param _isOriented (boolean) : true if the graoh is oriented else false
	 */
	public Little(List<Vertex> vertex0, List<Edge> edge0, boolean _isOriented) {

		vertexList = new ArrayList<Vertex>();
		edgeList = new ArrayList<Edge>();
		
		this.nbVertex = vertex0.size();
		this.final_path = new int[this.nbVertex + 1];
		this.visited = new boolean[this.nbVertex];
		this.histo_path = new ArrayList<List<String>>();
		this.best_path = new ArrayList<String>();

		Map<Vertex, Vertex> LinkToPrevious = new HashMap<Vertex, Vertex>();

		// create copy of the graph

		// create copy of vertex
		for (Vertex v : vertex0) {
			Vertex newV = new Vertex(v.getColor(), v.getName());
			LinkToPrevious.put(v, newV);
			vertexList.add(newV);
		}

		// create copy of edge
		for (Edge e : edge0) {
			Edge newE = new Edge(LinkToPrevious.get(e.getV1()), LinkToPrevious.get(e.getV2()), e.getWeight());
			edgeList.add(newE);
		}

		this.matrix = Graph.getMatrix(vertexList, edgeList, _isOriented);
	}

	/**
	 * 
	 * @return The historic of path
	 */
	public List<List<String>> getHisto_path(){
		return this.histo_path;
	}
	
	/**
	 * 
	 * @return The best path
	 */
	public List<String> getBest_path(){
		return this.best_path;
	}
	
	/**
	 * Function to copy a path to the final path
	 * @param curr_path path to copy
	 */
	private void copyToFinal(int curr_path[]) {
		this.best_path.clear();
		for (int i = 0; i < this.nbVertex; i++) {
			this.best_path.add(this.vertexList.get(curr_path[i]).getName());
			this.final_path[i] = curr_path[i];
		}
		
		this.histo_path.add(new ArrayList<String>(this.best_path));
		final_path[this.nbVertex] = curr_path[0];
	}

	// Function to find the minimum edge cost
	// having an end at the vertex i
	private int firstMin(int adj[][], int i) {
		int min = Integer.MAX_VALUE;
		for (int k = 0; k < this.nbVertex; k++)
			if (adj[i][k] < min && i != k)
				min = adj[i][k];
		return min;
	}

	// function to find the second minimum edge cost
	// having an end at the vertex i
	private int secondMin(int adj[][], int i) {
		int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
		for (int j = 0; j < this.nbVertex; j++) {
			if (i == j)
				continue;

			if (adj[i][j] <= first) {
				second = first;
				first = adj[i][j];
			} else if (adj[i][j] <= second && adj[i][j] != first)
				second = adj[i][j];
		}
		return second;
	}

	// function that takes as arguments:
	// curr_bound -> lower bound of the root node
	// curr_weight-> stores the weight of the path so far
	// level-> current level while moving in the search
	// space tree
	// curr_path[] -> where the solution is being stored which
	// would later be copied to final_path[]
	private void TSPRec(int adj[][], int curr_bound, int curr_weight, int level, int curr_path[]) {
		
		// base case is when we have reached level N which
		// means we have covered all the nodes once
		if (level == this.nbVertex) {
			// check if there is an edge from last vertex in
			// path back to the first vertex
			if (adj[curr_path[level - 1]][curr_path[0]] != 0) {
				// curr_res has the total weight of the
				// solution we got
				int curr_res = curr_weight + adj[curr_path[level - 1]][curr_path[0]];

				// Update final result and final path if
				// current result is better.
				if (curr_res < final_res) {
					copyToFinal(curr_path);
					final_res = curr_res;
				}
			}
			return;
		}

		// for any other level iterate for all vertices to
		// build the search space tree recursively
		for (int i = 0; i < this.nbVertex; i++) {
			// Consider next vertex if it is not same (diagonal
			// entry in adjacency matrix and not visited
			// already)
			if (adj[curr_path[level - 1]][i] != 0 && visited[i] == false) {
				int temp = curr_bound;
				curr_weight += adj[curr_path[level - 1]][i];

				// different computation of curr_bound for
				// level 2 from the other levels
				if (level == 1)
					curr_bound -= ((firstMin(adj, curr_path[level - 1]) + firstMin(adj, i)) / 2);
				else
					curr_bound -= ((secondMin(adj, curr_path[level - 1]) + firstMin(adj, i)) / 2);

				// curr_bound + curr_weight is the actual lower bound
				// for the node that we have arrived on
				// If current lower bound < final_res, we need to explore
				// the node further
				if (curr_bound + curr_weight < final_res) {
					curr_path[level] = i;
					visited[i] = true;

					// call TSPRec for the next level
					TSPRec(adj, curr_bound, curr_weight, level + 1, curr_path);
				}

				// Else we have to prune the node by resetting
				// all changes to curr_weight and curr_bound
				curr_weight -= adj[curr_path[level - 1]][i];
				curr_bound = temp;

				// Also reset the visited array
				Arrays.fill(visited, false);
				for (int j = 0; j <= level - 1; j++)
					visited[curr_path[j]] = true;
			}
		}
	}

	// This function sets up final_path[]
	private void TSP(int adj[][]) {
		int curr_path[] = new int[this.nbVertex + 1];

		// Calculate initial lower bound for the root node
		// using the formula 1/2 * (sum of first min +
		// second min) for all edges.
		// Also initialize the curr_path and visited array
		int curr_bound = 0;
		Arrays.fill(curr_path, -1);
		Arrays.fill(visited, false);

		// Compute initial bound
		for (int i = 0; i < this.nbVertex; i++)
			curr_bound += (firstMin(adj, i) + secondMin(adj, i));

		// Rounding off the lower bound to an integer
		curr_bound = (curr_bound == 1) ? curr_bound / 2 + 1 : curr_bound / 2;

		// We start at vertex 1 so the first vertex
		// in curr_path[] is 0
		visited[0] = true;
		curr_path[0] = 0;

		// Call to TSPRec for curr_weight equal to
		// 0 and level 1
		TSPRec(adj, curr_bound, 0, 1, curr_path);
	}
	
	/**
	 * Function to run the algorithm
	 */
	public void runAlgo() {
        TSP(matrix); 
	}
}
