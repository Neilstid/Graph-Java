package VertexGestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import graph.Graph;
import graph.Vertex;
import javafx.scene.layout.Pane;

/**
 * Class to convert Vertex into string and get giving a string a vertex
 * @author Neil Farmer
 *
 */
public class VertexConverter {

	//Dictionnary from String to vertex
	private HashMap<String, Vertex> StringVertex = new HashMap<String, Vertex>();
	
	//Constructor
	/**
	 * Constructor
	 * @param vList (List Vertex) : List of the vertex
	 */
	public VertexConverter(List<Vertex> vList) {
		for(Vertex vertex:vList) {
			StringVertex.put(vertex.getName(), vertex);
		}
	}
	
	/**
	 * 
	 * @return A list of String which are all the key of the hashmap
	 */
	public List<String> getListVertexKey() {
		List<String> toReturn = new ArrayList<String>();
		toReturn.addAll(StringVertex.keySet());
		return toReturn;
	}
	
	/**
	 * 
	 * @param s (String) : Key for the hashmap
	 * @return A vertex corresponding to the key entered
	 */
	public Vertex getVertex(String s) {
		return StringVertex.get(s);
	}
	
	/**
	 * Function to get the vertex with a given name
	 * @param p (Pane) :  Pane where the graph is
	 * @param name (String) : Name we search
	 * @return Vertex with the given name
	 */
	public static Vertex getVertex(Pane p, String name) {
		for(Vertex v:Graph.getListVertex(p)) {
			if(v.getName() == name) {
				return v;
			}
		}
		return null;		
	}
	
	/**
	 * Function to find a vertex with a giving name
	 * @param vertexList (List) : List of veretx
	 * @param name (String) :  name of the vertex we search for
	 * @return The vertex with the giing name
	 */
	public static Vertex getVertex(List<Vertex> vertexList, String name) {
		for(Vertex v:vertexList) {
			if(v.getName() == name) {
				return v;
			}
		}
		return null;		
	}
}
