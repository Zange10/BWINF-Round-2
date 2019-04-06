package program;

import java.util.ArrayList;

public class Algorithm {
	ArrayList<ArrayList<Integer>> routes;	// multiple routes (one is correct)
	ArrayList<Double> distances;	// length of these routes
	Obstacle[] obstacles;	// all obstacles
	int[][][] all_paths;	// all paths (corner to corner)
	ArrayList<int[]> all_corners;	// all corners of every obstacle
	int[] home_pos;	// x- and y-position of Lisa's home
	int home_index;	// home is at last index
	double[] shortest_to_corner;	// shortest distance to each corner
	double[] shortest_time;	// times of result
	
	Algorithm(int[] home_pos, Obstacle[] obstacles, ArrayList<int[]> all_corners) {
		this.routes = new ArrayList<ArrayList<Integer>>();
		this.distances = new ArrayList<Double>();
		this.obstacles = obstacles;
		this.home_pos = home_pos;
		this.all_paths = new int[all_corners.size()+1][][];	// every corner (and home) gets own index
		this.home_index = all_corners.size();
		this.all_corners = all_corners;
		parseCorners();	// calculating all useful paths between corners
		this.shortest_to_corner = new double[all_corners.size()];
		for(int i = 0; i < shortest_to_corner.length; i++) {
			shortest_to_corner[i] = -1;	// must be changed
		}
		shortest_time = new double[2];
		shortest_time[0] = 0;
		shortest_time[1] = 0;
	}
	
	// calculating all useful paths between corners
	private void parseCorners() {
		int[][] home_paths = new int[all_corners.size()+1][4];	// all useful paths from home
		// calculating with all corners
		for(int i = 0; i < all_corners.size(); i++) {
			int x1 = home_pos[0];
			int y1 = home_pos[1];
			int x2 = all_corners.get(i)[0];
			int y2 = all_corners.get(i)[1];
			// calculating if path can be used
			if(!(intersectsWithAnyObstacle(x1, y1, x2, y2))) home_paths[i] = new int[]{x1, y1, x2, y2};
			else home_paths[i] = null;
		}
		// from home to road
		int home_x1 = home_pos[0];
		int home_y1 = home_pos[1];
		int home_x2 = 0;
		int home_y2 = (int) (home_pos[1] + home_pos[0]/Math.sqrt(3));	// fastest gradient: m = 1/(3^0.5)
		// calculating if path can be used
		if(!(intersectsWithAnyObstacle(home_x1, home_y1, home_x2, home_y2))) {
			home_paths[all_corners.size()] = new int[]{home_x1, home_y1, home_x2, home_y2};
		}
		else home_paths[all_corners.size()] = null;
		all_paths[home_index] = home_paths;
		
		// calculating useful paths from corners to corners
		for(int i = 0; i < all_corners.size(); i++) {
			int[][] new_paths = new int[all_corners.size()+1][4];	// all useful paths from this corner
			for(int j = 0; j < all_corners.size(); j++) {
				if(j != i) {
					int x1 = all_corners.get(i)[0];
					int y1 = all_corners.get(i)[1];
					int x2 = all_corners.get(j)[0];
					int y2 = all_corners.get(j)[1];
					// calculating if path can be used
					if(!(intersectsWithAnyObstacle(x1, y1, x2, y2))) new_paths[j] = new int[]{x1, y1, x2, y2};
					else new_paths[j] = null;
				} else {
					new_paths[j] = null;
				}
			}
			// calculating from corner to road
			int x1 = all_corners.get(i)[0];
			int y1 = all_corners.get(i)[1];
			int x2 = 0;
			int y2 = (int) (all_corners.get(i)[1] + all_corners.get(i)[0]/Math.sqrt(3));	// fastest gradient: m = /3^0.5
			// calculating if path can be used
			if(!(intersectsWithAnyObstacle(x1, y1, x2, y2))) new_paths[all_corners.size()] = new int[]{x1, y1, x2, y2};
			else new_paths[all_corners.size()] = null;
			all_paths[i] = new_paths;
		}
	}

	// calculating best route and its needed time
	public ArrayList<Integer> caculate() {
		// every point the route gets to
		ArrayList<Integer> routePoints = new ArrayList<Integer>();
		int lastX = home_pos[0];
		int lastY = home_pos[1];
		
		// first point at home
		routePoints.add(lastX);
		routePoints.add(lastY);
		
		// calculating all routes with calculated paths
		calcAllRoutes(home_index, all_paths, routePoints, 0);		
		
		
		ArrayList<Integer> final_route = new ArrayList<Integer>();
		double best_time = 0;
		// find best route of all routes
		for(int i = 0; i < routes.size(); i++) {
			double walk_vel = 15.0/3.6;	// walk velocity (15 km/h to ~4 m/s)
			double walk_time = distances.get(i)/walk_vel;	// time to walk distance at 15 km/h
			double bus_vel = 30.0/3.6;
			double bus_loc = routes.get(i).get(routes.get(i).size()-1);
			double bus_time = bus_loc/bus_vel;	// bus velocity (30 km/h to ~8 m/s)
			double time = bus_time-walk_time;
			if(time > best_time || i == 0) {
				best_time = time;
				final_route = routes.get(i);
				shortest_time[0] = time;	// starting time
				shortest_time[1] = bus_time;	// destination time
			}
		}
		return final_route;
	}
	
	/**
	 * @param pos: current position (index of current corner)
	 * @param pos_paths: all currently unused paths
	 * @param routePoints: points of current route
	 * @param distance: the distance of current route
	 */
	private void calcAllRoutes(int pos, int[][][] pos_paths, ArrayList<Integer> routePoints, double distance) {
		int[][][] rem_paths = copy3DArray(pos_paths);	// all unused paths
		// if there is a direct path to the road, no additional paths to other corners needed
		if(rem_paths[pos][rem_paths[pos].length-1] == null) {	// no direct path to the road
			// check all possibilities for going further (to next corners)
			for(int i = 0; i < rem_paths[pos].length-1; i++) {
				if(rem_paths[pos][i] != null) {	// if path exists
					// adding new path to calculation
					ArrayList<Integer> buf_pathPoints = (ArrayList<Integer>) routePoints.clone();
					int x = rem_paths[pos][i][2];
					int y = rem_paths[pos][i][3];
					buf_pathPoints.add(x);
					buf_pathPoints.add(y);
					double new_distance = distance + calcDistance(buf_pathPoints);
					// if there was no shorter path to next corner
					if(new_distance < shortest_to_corner[i] || shortest_to_corner[i] < 0) {
						shortest_to_corner[i] = new_distance;
						// make path "used"
						rem_paths[pos][i] = null;
						if(pos < 3) rem_paths[i][pos] = null;
						// go from next corner
						calcAllRoutes(i, rem_paths, buf_pathPoints, new_distance);
						rem_paths[pos][i] = copyArray(pos_paths[pos][i]);
						if(pos < 3) rem_paths[i][pos] = copyArray(pos_paths[i][pos]);
					}
				}
			}
		} else {
			// path to road
			int x = 0;
			int y = rem_paths[pos][rem_paths[pos].length-1][3];	// y-position of ending of path to road
			routePoints.add(x);
			routePoints.add(y);
			distance += calcDistance(routePoints);	// resulting distance
			routes.add(routePoints);	// new possible route
			distances.add(distance);	// distance of this route
		}
	}
	
	// checks if path intersects with any obstacle
	private boolean intersectsWithAnyObstacle(int x1, int y1, int x2, int y2) {
		for(Obstacle o : obstacles) {
			if(o.isOnPath(x1, y1, x2, y2)) {
				return true;
			}
		}
		return false;
	}
	
	// returns a copy of an array
	private int[] copyArray(int[] a) {
		if(a != null) {
			int[] array = new int[a.length];
			for(int i = 0; i < a.length; i++) {
				array[i] = a[i];
			}
			return array;
		} else return null;
	}
	
	// returns a copy of an three-dimensional array
	private int[][][] copy3DArray(int[][][] a) {
		int[][][] array = new int[a.length][][];
		for(int i = 0; i < a.length; i++) {
			array[i] = new int[a[i].length][];
			for(int j = 0; j < a[i].length; j++) {
				if(a[i][j] != null) {
					array[i][j] = new int[a[i][j].length];
					for(int k = 0; k < a[i][j].length; k++) {
						array[i][j][k] = a[i][j][k];
					}
				} else array[i][j] = null;
			}
		}
		return array;
	}
	
	// calculates with Pythagoras
	private double calcDistance(ArrayList<Integer> buf_pathPoints) {
		int size = buf_pathPoints.size();
		double a = buf_pathPoints.get(size-4) - buf_pathPoints.get(size-2);
		double b = buf_pathPoints.get(size-3) - buf_pathPoints.get(size-1);
		
		return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));	// a^2 + b^2 = c^2
	}
	
	// returns start and destination time
	public double[] getTimes() {
		return shortest_time;
	}
}
