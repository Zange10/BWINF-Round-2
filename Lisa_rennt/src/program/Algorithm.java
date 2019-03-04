package program;

import java.util.ArrayList;

public class Algorithm {
	ArrayList<ArrayList<Integer>> routes;
	ArrayList<Double> distances;
	Obstacle[] obstacles;
	int[][][] all_paths;
	ArrayList<int[]> all_corners;
	int[] home_pos;
	int home_index;
	double shortest_distance;
	double[] shortest_to_corner;
	double[] shortest_time;
	
	Algorithm(int[] home_pos, Obstacle[] obstacles, ArrayList<int[]> all_corners) {
		this.routes = new ArrayList<ArrayList<Integer>>();
		this.distances = new ArrayList<Double>();
		this.obstacles = obstacles;
		this.home_pos = home_pos;
		this.all_paths = new int[all_corners.size()+1][][];
		this.home_index = all_corners.size();
		this.all_corners = all_corners;
		parseCorners();
		this.shortest_distance = -1;
		this.shortest_to_corner = new double[all_corners.size()];
		for(int i = 0; i < shortest_to_corner.length; i++) {
			shortest_to_corner[i] = -1;
		}
		shortest_time = new double[2];
		shortest_time[0] = 0;
		shortest_time[1] = 0;
	}
	
	private void parseCorners() {
		int[][] home_paths = new int[all_corners.size()+1][4];
		for(int i = 0; i < all_corners.size(); i++) {
			int x1 = home_pos[0];
			int y1 = home_pos[1];
			int x2 = all_corners.get(i)[0];
			int y2 = all_corners.get(i)[1];
			if(!(intersectsWithAnyObstacle(x1, y1, x2, y2))) home_paths[i] = new int[]{x1, y1, x2, y2};
			else home_paths[i] = null;
		}
		int home_x1 = home_pos[0];
		int home_y1 = home_pos[1];
		int home_x2 = 0;
		int home_y2 = (int) (home_pos[1] + home_pos[0]/Math.sqrt(3));	// fastest way: m = x/3^0.5
		if(!(intersectsWithAnyObstacle(home_x1, home_y1, home_x2, home_y2))) {
			home_paths[all_corners.size()] = new int[]{home_x1, home_y1, home_x2, home_y2};
		}
		else home_paths[all_corners.size()] = null;
		all_paths[home_index] = home_paths;
		
		for(int i = 0; i < all_corners.size(); i++) {
			int[][] new_paths = new int[all_corners.size()+1][4];
			for(int j = 0; j < all_corners.size(); j++) {
				if(j != i) {
					int x1 = all_corners.get(i)[0];
					int y1 = all_corners.get(i)[1];
					int x2 = all_corners.get(j)[0];
					int y2 = all_corners.get(j)[1];
					if(!(intersectsWithAnyObstacle(x1, y1, x2, y2))) new_paths[j] = new int[]{x1, y1, x2, y2};
					else new_paths[j] = null;
				} else {
					new_paths[j] = null;
				}
			}
			int x1 = all_corners.get(i)[0];
			int y1 = all_corners.get(i)[1];
			int x2 = 0;
			int y2 = (int) (all_corners.get(i)[1] + all_corners.get(i)[0]/Math.sqrt(3));	// fastest way: m = x/3^0.5
			if(!(intersectsWithAnyObstacle(x1, y1, x2, y2))) new_paths[all_corners.size()] = new int[]{x1, y1, x2, y2};
			else new_paths[all_corners.size()] = null;
			all_paths[i] = new_paths;
		}
	}
	
	public ArrayList<Integer> caculate() {
		ArrayList<Integer> routePoints = new ArrayList<Integer>();
		int lastX = home_pos[0];
		int lastY = home_pos[1];
		
		// first point at home
		routePoints.add(lastX);
		routePoints.add(lastY);
		
		calcAllPaths(home_index, all_paths, routePoints, 0);
//		System.out.println("-----------------------------------------");
//		for(int i = 0; i < obstacles.length; i++) {
//			System.out.println(i + " " + obstacles[i].isOnPath(380, 208, 380, 228));
//		}
//		System.out.println("-----------------------------------------");
//		
//		return routes;
		
		
		
		ArrayList<Integer> final_route = new ArrayList<Integer>();
		double best_time = 0;
		for(int i = 0; i < distances.size(); i++) {
			double walk_vel = 15.0/3.6;	// walk velocity (15 km/h to ~4 m/s)
			double walk_time = distances.get(i)/walk_vel;	// time to walk distance at 15 km/h
			double bus_vel = 30.0/3.6;
			double bus_loc = routes.get(i).get(routes.get(i).size()-1);
			double bus_time = bus_loc/bus_vel;	// bus velocity (30 km/h to ~8 m/s)
			double time = bus_time-walk_time;
			if(time > best_time || i == 0) {
				best_time = time;
				final_route = routes.get(i);
				shortest_time[0] = time;
				shortest_time[1] = bus_time;
			}
//			System.out.println(best_time);
		}
		return final_route;
	}
	
	private void calcAllPaths(int pos, int[][][] pos_paths, ArrayList<Integer> routePoints, double distance) {
		int[][][] rem_paths = copy3DArray(pos_paths);
		if(rem_paths[pos][rem_paths[pos].length-1] == null) {	// if now path to road exists
			for(int i = 0; i < rem_paths[pos].length-1; i++) {
				if(rem_paths[pos][i] != null) {
					ArrayList<Integer> buf_pathPoints = (ArrayList<Integer>) routePoints.clone();
					int x = rem_paths[pos][i][2];
					int y = rem_paths[pos][i][3];
					buf_pathPoints.add(x);
					buf_pathPoints.add(y);
					double new_distance = distance + calcDistance(buf_pathPoints);
					if(new_distance < shortest_to_corner[i] || shortest_to_corner[i] < 0) {
						shortest_to_corner[i] = new_distance;
						rem_paths[pos][i] = null;
						if(pos < 3) rem_paths[i][pos] = null;
						calcAllPaths(i, rem_paths, buf_pathPoints, new_distance);
						rem_paths[pos][i] = copyArray(pos_paths[pos][i]);
						if(pos < 3) rem_paths[i][pos] = copyArray(pos_paths[i][pos]);
					}
				}
			}
		} else {
			int x = 0;
			int y = rem_paths[pos][rem_paths[pos].length-1][3];
			routePoints.add(x);
			routePoints.add(y);
			distance += calcDistance(routePoints);
			shortest_distance = distance;
			routes.add(routePoints);
			distances.add(distance);
		}
	}
	
	private boolean intersectsWithAnyObstacle(int x1, int y1, int x2, int y2) {
		boolean intersects = false;
		for(Obstacle o : obstacles) {
			if(o.isOnPath(x1, y1, x2, y2)) {
				intersects = true;
				break;
			}
		}
		return intersects;
	}
	
	private int[] copyArray(int[] a) {
		if(a != null) {
			int[] array = new int[a.length];
			for(int i = 0; i < a.length; i++) {
				array[i] = a[i];
			}
			return array;
		} else return null;
	}
	
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
	
	public ArrayList<ArrayList<Integer>> getAllPaths() {
		ArrayList<ArrayList<Integer>> allPaths = new ArrayList<ArrayList<Integer>>();
		for(int[][] a : all_paths) {
			for(int[] p : a) {
				if(p != null) {
					ArrayList<Integer> points = new ArrayList<Integer>();
					for(int c : p) {
						points.add(c);
					}
					allPaths.add(points);
				}
			}
		}
		return allPaths;
	}
	
	public ArrayList<Integer> getFastest() {
		ArrayList<Integer> route = new ArrayList<Integer>();
		int index = 0;
		double shortest = distances.get(0);
		for(int i = 0; i < distances.size(); i++) {
			if(distances.get(i) <= shortest) {
				shortest = distances.get(i);
				route = routes.get(i);
				index = i;
			}
		}
		
		double walk_vel = 15.0/3.6;	// walk velocity (15 km/h to ~4 m/s)
		double walk_time = distances.get(index)/walk_vel;	// time to walk distance at 15 km/h
		double bus_vel = 30.0/3.6;
		double bus_loc = routes.get(index).get(routes.get(index).size()-1);
		double bus_time = bus_loc/bus_vel;	// bus velocity (30 km/h to ~8 m/s)
		double time = bus_time-walk_time;
//		System.out.println("--- " + time);
		return route;
	}
	
	public double[] getTime() {
		return shortest_time;
	}
}
