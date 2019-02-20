package program;

import java.util.ArrayList;

public class Algorithm {
	ArrayList<ArrayList<Integer>> routes = new ArrayList<ArrayList<Integer>>();
	Obstacle[] obstacles;
	int[][][] all_paths;
	ArrayList<int[]> all_corners;
	int[] home_pos;
	int home_index;
	
	Algorithm(int[] home_pos, Obstacle[] obstacles, ArrayList<int[]> all_corners) {
		this.obstacles = obstacles;
		this.home_pos = home_pos;
		all_paths = new int[all_corners.size()+1][][];
		home_index = all_corners.size();
		this.all_corners = all_corners;
		parseCorners();          
	}
	
	private void parseCorners() {
		int[][] home_paths = new int[all_corners.size()+1][4];
		for(int i = 0; i < all_corners.size(); i++) {
			int x1 = home_pos[0];
			int y1 = home_pos[1];
			int x2 = all_corners.get(i)[0];
			int y2 = all_corners.get(i)[1];
			for(Obstacle o : obstacles) {
				if(!(o.isOnPath(x1, y1, x2, y2))) home_paths[i] = new int[]{x1, y1, x2, y2};
				else home_paths[i] = null;
				System.out.println((o.isOnPath(x1, y1, x2, y2)));
			}
//			home_paths[i] = new int[]{x1, y1, x2, y2};
		}
		int home_x1 = home_pos[0];
		int home_y1 = home_pos[1];
		int home_x2 = 0;
		int home_y2 = home_pos[1];
		for(Obstacle o : obstacles) {
			if(!(o.isOnPath(home_x1, home_y1, home_x2, home_y2))) {
				home_paths[all_corners.size()] = new int[]{home_x1, home_y1, home_x2, home_y2};
			}
			else home_paths[all_corners.size()] = null;
//			home_paths[all_corners.size()] = new int[]{home_x1, home_y1, home_x2, home_y2};
		}
//		home_paths[all_corners.size()] = new int[]{home_x1, home_y1, home_x2, home_y2};
//		for(int[] j : home_paths) {
//			if(j != null) {for(int c : j) {
//				System.out.print(c + " ");
//			}}
//			System.out.println("");
//		}
//		System.out.println("---");
		all_paths[home_index] = home_paths;
		for(int i = 0; i < all_corners.size(); i++) {
			int[][] new_paths = new int[all_corners.size()+1][4];
			for(int j = 0; j < all_corners.size(); j++) {
				if(j != i) {
					int x1 = all_corners.get(i)[0];
					int y1 = all_corners.get(i)[1];
					int x2 = all_corners.get(j)[0];
					int y2 = all_corners.get(j)[1];
//					System.out.println(j + " " + x1 + " " + y1 + " " + x2 + " " + y2);
					for(Obstacle o : obstacles) {
						if(!(o.isOnPath(x1, y1, x2, y2))) new_paths[j] = new int[]{x1, y1, x2, y2};
						else new_paths[j] = null;
					}
//					new_paths[j] = new int[]{x1, y1, x2, y2};
					
				} else {
//					System.out.println(i);
					new_paths[j] = null;
				}
			}
			int x1 = all_corners.get(i)[0];
			int y1 = all_corners.get(i)[1];
			int x2 = 0;
			int y2 = all_corners.get(i)[1];
//			System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);
			for(Obstacle o : obstacles) {
				if(!(o.isOnPath(x1, y1, x2, y2))) new_paths[all_corners.size()] = new int[]{x1, y1, x2, y2};
				else new_paths[all_corners.size()] = null;
			}
//			new_paths[all_corners.size()] = new int[]{x1, y1, x2, y2};
//				for(int[] j : new_paths) {
//					for(int c : j) {
//						System.out.print(c + " ");
//					}
//					System.out.println("");
//				}
//				System.out.println("---");
			all_paths[i] = new_paths;
		}
//		System.out.println("---");
//		for(int[][] i : all_paths) {
//			for(int[] j : i) {
//				if(j != null) {
//				for(int c : j) {
//					System.out.print(c + " ");
//				}
//				}
//				System.out.println("");
//			}
//			System.out.println("---");
//		}
		System.out.println("---");
//		System.out.println(obstacles[0].isOnPath(633, 189, 535, 410));
		System.out.println(obstacles[0].isOnPath(350, 100, 500, 180));
	}
	
	public ArrayList<ArrayList<Integer>> caculate() {
		ArrayList<Integer> routePoints = new ArrayList<Integer>();
		int lastX = home_pos[0];
		int lastY = home_pos[1];
		
		// first point at home
		routePoints.add(lastX);
		routePoints.add(lastY);
		calcAllPaths(home_index, all_paths, routePoints);
//		System.out.println("\n---\n" + routes.size());
//		for(ArrayList<Integer> i : routes) {
//			for(int a : i) {
//				System.out.print(a + " ");
//			}
//			System.out.println("");
//		}
		return routes;
	}
	
	private void calcAllPaths(int pos, int[][][] pos_paths, ArrayList<Integer> routePoints) {
		int[][][] rem_paths = pos_paths.clone();
		for(int i = 0; i < rem_paths[pos].length-1; i++) {
			if(rem_paths[pos][i] != null) {
//				System.out.println(pos + " " + i);
//				if(pos < 3) System.out.println(pos + " " + i + " -- " + all_corners.get(pos)[1] + " " + all_corners.get(i)[1]);
				ArrayList<Integer> buf_pathPoints = (ArrayList<Integer>) routePoints.clone();
				int x = rem_paths[pos][i][2];
				int y = rem_paths[pos][i][3];
//				System.out.println(x + ", " + y);
				buf_pathPoints.add(x);
				buf_pathPoints.add(y);
				rem_paths[pos][i] = null;
				if(pos < 3) rem_paths[i][pos] = null;
				calcAllPaths(i, rem_paths, buf_pathPoints);
			}
		}
		if(pos_paths[pos][pos_paths[pos].length-1] != null) {
			int x = 0;
			int y = pos_paths[pos][pos_paths[pos].length-1][3];
			routePoints.add(x);
			routePoints.add(y);
			routes.add(routePoints);
		}
	}
}
