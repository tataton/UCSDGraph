package roadgraph;

import java.util.LinkedList;

import geography.GeographicPoint;

public class MapBestAStarRoute implements Comparable<MapBestAStarRoute> {
	private GeographicPoint destination;
	private double distance;
	private LinkedList<GeographicPoint> route;
	private double remaining;
	
	public MapBestAStarRoute(GeographicPoint destination, double distance, LinkedList<GeographicPoint> route, double remaining) {
		this.destination = destination;
		this.distance = distance;
		this.route = route;
		this.remaining = remaining;
	}
	
    public int compareTo(MapBestAStarRoute other) {
    	if (this.distance + this.remaining == other.distance + other.remaining) {
    		return 0;
    	} else if (this.distance + this.remaining > other.distance + other.remaining) {
    		return 1;
    	} else if (this.distance + this.remaining < other.distance + other.remaining) {
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
