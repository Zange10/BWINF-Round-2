package program;

import java.util.ArrayList;

public class Algorithm {
	ArrayList<ArrayList<Integer>> routes = new ArrayList<ArrayList<Integer>>();
	Obstacle[] obstacles;
	int[][][] all_paths;
	ArrayList<int[]> all_corners;
	int[] home_pos;
	int home_index;
	double shortest_distance;
	
	Algorithm(int[] home_pos, Obstacle[] obstacles, ArrayList<int[]> all_corners) {
		this.obstacles = obstacles;
		this.home_pos = home_pos;
		all_paths = new int[all_corners.size()+1][][];
		home_index = all_corners.size();
		this.all_corners = all_corners;
		parseCorners();
		this.shortest_distance = -1;
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
		int home_y2 = home_pos[1];
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
			int y2 = all_corners.get(i)[1];
			if(!(intersectsWithAnyObstacle(x1, y1, x2, y2))) new_paths[all_corners.size()] = new int[]{x1, y1, x2, y2};
			else new_paths[all_corners.size()] = null;
			all_paths[i] = new_paths;
		}
	}
	
	public ArrayList<ArrayList<Integer>> caculate() {
		ArrayList<Integer> routePoints = new ArrayList<Integer>();
		int lastX = home_pos[0];
		int lastY = home_pos[1];
		
		// first point at home
		routePoints.add(lastX);
		routePoints.add(lastY);
		
//		showAllPaths();
		
		calcAllPaths(home_index, all_paths, routePoints, 0);	
		System.out.println(routes.size());
		for(ArrayList<Integer> a : routes) {
			for(int i : a) {
				System.out.print(i + " ");
			}
			System.out.println("");
		}
		
		System.out.println(routes.size());
		return routes;
	}
	
	private void showAllPaths() {
		for(int[][] a : all_paths) {
			for(int[] p : a) {
				if(p != null) {
					ArrayList<Integer> points = new ArrayList<Integer>();
					for(int c : p) {
						points.add(c);
					}
					routes.add(points);
				}
			}
		}
	}
	
	private void calcAllPaths(int pos, int[][][] pos_paths, ArrayList<Integer> routePoints, double distance) {
		int[][][] rem_paths = copy3DArray(pos_paths);
		for(int i = 0; i < rem_paths[pos].length-1; i++) {
			if(pos == home_index) System.out.println(i + " -------------------------------");
			if(rem_paths[pos][i] != null) {
				ArrayList<Integer> buf_pathPoints = (ArrayList<Integer>) routePoints.clone();
				int x = rem_paths[pos][i][2];
				int y = rem_paths[pos][i][3];
				buf_pathPoints.add(x);
				buf_pathPoints.add(y);
				double new_distance = distance + calcDistance(buf_pathPoints);
				if(new_distance < shortest_distance || shortest_distance < 0) {
					rem_paths[pos][i] = null;
					if(pos < 3) rem_paths[i][pos] = null;
					calcAllPaths(i, rem_paths, buf_pathPoints, new_distance);
					rem_paths[pos][i] = copyArray(pos_paths[pos][i]);
					if(pos < 3) rem_paths[i][pos] = copyArray(pos_paths[i][pos]);
				}
			}
		}
		if(rem_paths[pos][rem_paths[pos].length-1] != null) {
			int x = 0;
			int y = rem_paths[pos][rem_paths[pos].length-1][3];
			routePoints.add(x);
			routePoints.add(y);
			distance += calcDistance(routePoints);
			if(distance < shortest_distance || shortest_distance < 0) {
				shortest_distance = distance;
				routes.add(routePoints);
				System.out.println(shortest_distance);
			}
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
}
