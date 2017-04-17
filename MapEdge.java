package roadgraph;

import geography.GeographicPoint;

/**
 * A class representing edges (streets) in a MapGraph
 * object.
 * 
 * @author T. Andrew Taton
 *
 */
public class MapEdge {
	private GeographicPoint start;
	private GeographicPoint end;
	private String roadName;
	private String roadType;
	private double length;
	
	/** MapEdge constructor.
	 * 
	 * @param start
	 * @param end
	 * @param roadName
	 * @param roadType
	 * @param length
	 */
	public MapEdge(GeographicPoint start, GeographicPoint end, String roadName, String roadType, double length){
		this.start = start;
		this.end = end;
		this.roadName = roadName;
		this.roadType = roadType;
		this.length = length;
	}
	
	public double getLength() {
		return length;
	}

}
