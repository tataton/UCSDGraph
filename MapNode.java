package roadgraph;

import java.util.HashMap;
import java.util.Set;

import geography.GeographicPoint;

/**
 * A class representing nodes (intersections) in a MapGraph
 * object.
 * 
 * @author T. Andrew Taton
 *
 */
public class MapNode {
	private GeographicPoint location;
	private HashMap<GeographicPoint, MapEdge> destinations;
	
	/** Constructor; creates a MapNode object at the passed location.
	 * Currently, this location property isn't used.
	 * 
	 * @param location Location of MapNode.
	 */
	public MapNode(GeographicPoint location) {
		this.location = location;
		destinations = new HashMap<GeographicPoint, MapEdge>();
	}
	
	/** Adds an edge to the MapNode.
	 * 
	 * @param to Destination location of edge.
	 * @param roadName Name of road (from map data).
	 * @param roadType Type of road (from map data).
	 * @param length Length of road (from map data).
	 */
	public void addEdge(GeographicPoint to, String roadName, String roadType, double length) {
		destinations.put(to, new MapEdge(this.location, to, roadName, roadType, length));
	}
	
	/** Public getter for location.
	 * 
	 * @return MapNode's GeographicPoint location.
	 */
	public GeographicPoint getLocation() {
		return location;
	}
	
	/** Retrieves destinations of all outgoing edges from this MapNode.
	 * 
	 * @return Set of destination GeographicPoint locations.
	 */
	public Set<GeographicPoint> getDestinations() {
		return destinations.keySet();
	}
	
	public MapEdge getEdge(GeographicPoint destination) {
		return destinations.get(destination);
	}

}
