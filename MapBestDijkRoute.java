package roadgraph;

import java.util.LinkedList;

import geography.GeographicPoint;

public class MapBestDijkRoute extends MapBestRoute implements Comparable<MapBestDijkRoute>{
	private GeographicPoint start;
	private GeographicPoint destination;
	private double distance;
	private LinkedList<GeographicPoint> route;
	
	public MapBestDijkRoute(GeographicPoint destination, double distance, LinkedList<GeographicPoint> route) {
		this.destination = destination;
		this.distance = distance;
		this.route = route;
	}
	
    public int compareTo(MapBestDijkRoute other) {
    	if (this.distance == other.distance) {
    		return 0;
    	} else if (this.distance > other.distance) {
    		return 1;
    	} else if (this.distance < other.distance) {
    		return -1;
    	} else {
    		return 0;
    	}
    }
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double newDistance) {
		this.distance = newDistance;
	}
	
	@Override
	public void setRoute(LinkedList<GeographicPoint> newRoute) {
		this.route = newRoute;
		newRoute.
	}
}