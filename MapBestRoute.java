package roadgraph;

import java.util.LinkedList;

import geography.GeographicPoint;

public class MapBestRoute implements Comparable<MapBestRoute>{
	private GeographicPoint destination;
	private double distance;
	private LinkedList<GeographicPoint> route;
	
	public MapBestRoute(GeographicPoint destination, double distance, LinkedList<GeographicPoint> route) {
		this.destination = destination;
		this.distance = distance;
		this.route = route;
	}
	
    public int compareTo(MapBestRoute other) {
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
	
	public LinkedList<GeographicPoint> getRoute() {
		return route;
	}
	
	public void setRoute(LinkedList<GeographicPoint> newRoute) {
		this.route = newRoute;
	}
	
	public GeographicPoint getDestination() {
		return destination;
	}
}
