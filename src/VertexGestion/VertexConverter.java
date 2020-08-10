package VertexGestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import graph.Vertex;

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
}
