package roadgraph;

import java.util.LinkedList;

import geography.GeographicPoint;

public abstract class MapBestRoute {
	private GeographicPoint start;
	private GeographicPoint routeEnd;
	private GeographicPoint destination;
	private LinkedList<MapEdge> route;
	
//	public MapBestRoute(GeographicPoint destination, double distance, LinkedList<GeographicPoint> route) {
//		this.destination = destination;
//		this.route = route;
//	}
	
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
