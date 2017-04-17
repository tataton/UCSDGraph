package roadgraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * A class which represents a graph of geographic locations
 * as nodes, connected by edges. Nodes are typically street
 * intersections, and edges are streets.
 * 
 * @author UCSD MOOC development team and T. Andrew Taton
 *
 */
public class MapGraph {
	private HashMap<GeographicPoint, MapNode> mapOfNodes;
	private int numEdges;
	
	/** 
	 * Constructor; creates a new, empty MapGraph. 
	 */
	public MapGraph() {
		mapOfNodes = new HashMap<GeographicPoint, MapNode>();
		numEdges = 0;
	}
	
	/**
	 * Gets the number of vertices (road intersections) in the graph.
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()	{
		return mapOfNodes.size();
	}
	
	/**
	 * Returns the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices() {
		return mapOfNodes.keySet();
	}
	
	/**
	 * Gets the number of road segments in the graph.
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges() {
		return numEdges;
	}

	/** Adds a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location) {
		if ((location == null) || (mapOfNodes.containsKey(location))) {
			return false;
		} else {
			mapOfNodes.put(location, new MapNode(location));
			return true;
		}
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {
		if (from == null || to == null) {
			throw new IllegalArgumentException("Null arguments not allowed.");
		} else if (!mapOfNodes.containsKey(from) || !mapOfNodes.containsKey(to)) {
			throw new IllegalArgumentException("Edge terminus not recognized.");
		} else if (length < 0) {
			throw new IllegalArgumentException("Length cannot be less than zero.");
		} else {
			MapNode fromNode = mapOfNodes.get(from);
			fromNode.addEdge(to, roadName, roadType, length);
			numEdges++;
		}
	}
	
	
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		if (start == null || goal == null) {
			throw new NullPointerException("Start and end points must not be null.");
		} else if (!mapOfNodes.containsKey(start) || !mapOfNodes.containsKey(goal)) {
			throw new IllegalArgumentException("Route terminus not recognized.");
		} else {
			// Initialize lists of destinations to visit and past locations visited.
			LinkedList<GeographicPoint> toVisit = new LinkedList<GeographicPoint>();
			HashSet<GeographicPoint> visited = new HashSet<GeographicPoint>();
			// Initialize map of locations and routes that connect those locations to origin.
			HashMap<GeographicPoint, LinkedList<GeographicPoint>> shortestRoutes = new HashMap<GeographicPoint, LinkedList<GeographicPoint>>();
			// Initialize search starting point.
			GeographicPoint curr = start;
			LinkedList<GeographicPoint> startRoute = new LinkedList<GeographicPoint>();
			startRoute.add(start);
			shortestRoutes.put(start, startRoute);
			visited.add(start);
			// Add one-hop destinations to toVisit list, and routes to them to shortestRoutes map.
			addDestinationRoutes(start, visited, shortestRoutes, toVisit);
			// Now we are ready to BFS.
			// As long as there are still locations to visit, and we haven't reached the goal,
			// we keep searching.
			while (toVisit.size() > 0 && !curr.equals(goal)) {
				curr = toVisit.pop();
				nodeSearched.accept(curr);
				visited.add(curr);
				addDestinationRoutes(curr, visited, shortestRoutes, toVisit);			
			}
			if (curr.equals(goal)) {
				return shortestRoutes.get(goal);
			} else {
				// No path was found.
				return null;
			}
		}
	}
	
	/** Private method for adding routes to upcoming destinations.
	 * 
	 * @param curr Current GeographicPoint location.
	 * @param visited List of visited GeographicPoint locations.
	 * @param shortestRoutes Map of destination keys and route values.
	 * @param toVisit List of future destinations to be visited.
	 */
	private void addDestinationRoutes(GeographicPoint current, HashSet<GeographicPoint> visited,
			HashMap<GeographicPoint, LinkedList<GeographicPoint>> shortestRoutes,
			LinkedList<GeographicPoint> toVisit) {
		for (GeographicPoint destination: mapOfNodes.get(current).getDestinations()) {
			if (!visited.contains(destination)) {
				LinkedList<GeographicPoint> newRoute = new LinkedList<GeographicPoint>(shortestRoutes.get(current));
				newRoute.add(destination);
				shortestRoutes.put(destination, newRoute);
				toVisit.add(destination);
			}
		}
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		if (start == null || goal == null) {
			throw new NullPointerException("Start and end points must not be null.");
		} else if (!mapOfNodes.containsKey(start) || !mapOfNodes.containsKey(goal)) {
			throw new IllegalArgumentException("Route terminus not recognized.");
		} else {
			// Initialize lists of destinations to visit, past locations visited
			// and best routes to those locations.
			PriorityQueue<MapBestDijkRoute> toVisit = new PriorityQueue<MapBestDijkRoute>();
			HashMap<GeographicPoint, MapBestDijkRoute> mapOfRoutes = new HashMap<GeographicPoint, MapBestDijkRoute>();
			// Initialize search starting point and node.
			GeographicPoint curr = start;
			LinkedList<GeographicPoint> startRoute = new LinkedList<GeographicPoint>();
			startRoute.add(start);
			mapOfRoutes.put(start, new MapBestDijkRoute(start, 0, startRoute));
			// Add one-hop destinations to toVisit list, and routes to them to shortestRoutes map.
			System.out.print("Dijkstra node searched: ");
			System.out.println(curr);
			int nodeCount = 1;
			addDijkstraRoutes(start, mapOfRoutes, toVisit);
			// Now we are ready to perform Dijkstra search.
			// As long as there are still locations to visit, and we haven't reached the goal,
			// we keep searching.
			while (toVisit.size() > 0 && !curr.equals(goal)) {
				curr = toVisit.poll().getDestination();
				nodeSearched.accept(curr);
				System.out.print("Dijkstra node searched: ");
				System.out.println(curr);
				nodeCount++;
				addDijkstraRoutes(curr, mapOfRoutes, toVisit);
			}
			if (curr.equals(goal)) {
				System.out.print("Total nodes searched with Dijkstra: ");
				System.out.println(nodeCount);
				return mapOfRoutes.get(goal).getRoute();
			} else {
				// No path was found.
				return null;
			}
		}
	}
	
	/** Private method for adding/updating routes to upcoming destinations in Dijkstra search.
	 * 
	 * @param current Current GeographicPoint location.
	 * @param mapOfRoutes Map of destinations to optimized routes (so far).
	 * @param toVisit PriorityQueue of future route destinations (sorted by distance).
	 */
	private void addDijkstraRoutes(GeographicPoint current, 
			HashMap<GeographicPoint, MapBestDijkRoute> mapOfRoutes, PriorityQueue<MapBestDijkRoute> toVisit) {
		for (GeographicPoint destination: mapOfNodes.get(current).getDestinations()) {
			// Get ready to compare route to destination, and its length, to what we've got
			// already.
			LinkedList<GeographicPoint> proposedRoute = 
					new LinkedList<GeographicPoint>(mapOfRoutes.get(current).getRoute());
			proposedRoute.add(destination);
			double proposedDistance = mapOfRoutes.get(current).getDistance() + 
					mapOfNodes.get(current).getEdge(destination).getLength();
			if (mapOfRoutes.containsKey(destination)) {
				// Route map already contains this destination; we only need to
				// do work if this route is better than the one on record.
				MapBestDijkRoute routeToCheck = mapOfRoutes.get(destination);
				if (proposedDistance < routeToCheck.getDistance()) {
					// New route is better. Replace everything, and add the destination
					// to the toVisit list (because, even though we've been there before,
					// we have to recalculate all the downstream routes and distances.
					routeToCheck.setDistance(proposedDistance);
					routeToCheck.setRoute(proposedRoute);
					if (!toVisit.contains(routeToCheck)) {
						toVisit.add(routeToCheck);
					}
					// Otherwise, we do nothing. Either destination has already been
					// visited via a shorter route, or it is scheduled to be. No else.
				}
			} else {
				// This is a brand new destination. We need to create new entries in
				// mapOfRoutes and toVisit.
				MapBestDijkRoute routeToAdd = new MapBestDijkRoute(destination, proposedDistance, proposedRoute);
				mapOfRoutes.put(destination, routeToAdd);
				toVisit.add(routeToAdd);
			}
		}
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		if (start == null || goal == null) {
			throw new NullPointerException("Start and end points must not be null.");
		} else if (!mapOfNodes.containsKey(start) || !mapOfNodes.containsKey(goal)) {
			throw new IllegalArgumentException("Route terminus not recognized.");
		} else {
			// Initialize lists of destinations to visit, past locations visited
			// and best routes to those locations.
			PriorityQueue<MapBestAStarRoute> toVisit = new PriorityQueue<MapBestAStarRoute>();
			HashMap<GeographicPoint, MapBestAStarRoute> mapOfRoutes = new HashMap<GeographicPoint, MapBestAStarRoute>();
			// Initialize search starting point and node.
			GeographicPoint curr = start;
			LinkedList<GeographicPoint> startRoute = new LinkedList<GeographicPoint>();
			startRoute.add(start);
			mapOfRoutes.put(start, new MapBestAStarRoute(start, 0, startRoute, start.distance(goal)));
			// Add one-hop destinations to toVisit list, and routes to them to shortestRoutes map.
			System.out.print("AStar node searched: ");
			System.out.println(curr);
			int nodeCount = 1;
			addAStarRoutes(start, mapOfRoutes, toVisit, goal);
			// Now we are ready to perform Dijkstra search.
			// As long as there are still locations to visit, and we haven't reached the goal,
			// we keep searching.
			while (toVisit.size() > 0 && !curr.equals(goal)) {
				curr = toVisit.poll().getDestination();
				nodeSearched.accept(curr);
				System.out.print("AStar node searched: ");
				System.out.println(curr);
				nodeCount++;
				addAStarRoutes(curr, mapOfRoutes, toVisit, goal);
			}
			if (curr.equals(goal)) {
				System.out.print("Total nodes searched with AStar: ");
				System.out.println(nodeCount);
				return mapOfRoutes.get(goal).getRoute();
			} else {
				// No path was found.
				return null;
			}
		}
	}
	
	private void addAStarRoutes(GeographicPoint current, 
			HashMap<GeographicPoint, MapBestAStarRoute> mapOfRoutes, 
			PriorityQueue<MapBestAStarRoute> toVisit, GeographicPoint goal) {
		for (GeographicPoint destination: mapOfNodes.get(current).getDestinations()) {
			// Get ready to compare route to destination, and its length, to what we've got
			// already.
			LinkedList<GeographicPoint> proposedRoute = 
					new LinkedList<GeographicPoint>(mapOfRoutes.get(current).getRoute());
			proposedRoute.add(destination);
			double proposedDistance = mapOfRoutes.get(current).getDistance() + 
					mapOfNodes.get(current).getEdge(destination).getLength();
			if (mapOfRoutes.containsKey(destination)) {
				// Route map already contains this destination; we only need to
				// do work if this route is better than the one on record.
				MapBestAStarRoute routeToCheck = mapOfRoutes.get(destination);
				if (proposedDistance < routeToCheck.getDistance()) {
					// New route is better. Replace everything, and add the destination
					// to the toVisit list (because, even though we've been there before,
					// we have to recalculate all the downstream routes and distances.
					routeToCheck.setDistance(proposedDistance);
					routeToCheck.setRoute(proposedRoute);
					if (!toVisit.contains(routeToCheck)) {
						toVisit.add(routeToCheck);
					}
					// Otherwise, we do nothing. Either destination has already been
					// visited via a shorter route, or it is scheduled to be. No else.
				}
			} else {
				// This is a brand new destination. We need to create new entries in
				// mapOfRoutes and toVisit.
				MapBestAStarRoute routeToAdd = new MapBestAStarRoute(destination, proposedDistance, proposedRoute, destination.distance(goal));
				mapOfRoutes.put(destination, routeToAdd);
				toVisit.add(routeToAdd);
			}
		}
	}
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		/*
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		*/
		
		
		/* Use this code in Week 3 End of Week Quiz */
		/*
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
		*/
		
		
	}
	
}
