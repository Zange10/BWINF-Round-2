package program;

import java.util.ArrayList;

public class Algorithm {
	ArrayList<ArrayList<Integer>> routes = new ArrayList<ArrayList<Integer>>();
	Obstacle[] obstacles;
	ArrayList<int[]> all_corners;
	
	Algorithm(Obstacle[] obstacles, ArrayList<int[]> all_corners) {
		this.obstacles = obstacles;
		this.all_corners = all_corners;            
	}
	
	public ArrayList<ArrayList<Integer>> caculate(int[] home_pos) {
		ArrayList<Integer> routePoints = new ArrayList<Integer>();
		int lastX = home_pos[0];
		int lastY = home_pos[1];
		
		// first point at home
		routePoints.add(lastX);
		routePoints.add(lastY);
		
		calcAllPaths(all_corners, routePoints);
		
		return routes;
	}
	
	private void calcAllPaths(ArrayList<int[]> remaining_corners, ArrayList<Integer> routePoints) {
		for(int i = 0; i < remaining_corners.size(); i++) {
			ArrayList<int[]> buf_rem_corners = (ArrayList<int[]>) remaining_corners.clone();
			ArrayList<Integer> buf_pathPoints = (ArrayList<Integer>) routePoints.clone();
			int x = remaining_corners.get(i)[0];
			int y = remaining_corners.get(i)[1];
			buf_pathPoints.add(x);
			buf_pathPoints.add(y);
			buf_rem_corners.remove(i);
			calcAllPaths(buf_rem_corners, buf_pathPoints);
		}
		int x = 0;
		int y = routePoints.get(routePoints.size()-1);
		routePoints.add(x);
		routePoints.add(y);
		routes.add(routePoints);
		System.out.println(routes.size());
	}
	
	
//	private boolean canGoStraight(int max_X, int middle) {
//		for(Obstacle o : obstacles) {
//			int highest = o.getYCorners()[o.getHighestPointIndex()];
//			int lowest = o.getYCorners()[o.getLowestPointIndex()];
//			int leftest = o.getLeftestValue();
//			if(highest < middle && lowest > middle && leftest < max_X) return false;
//		}
//		return true;
//	}
//	
//	private Obstacle nextObstacle(int max_xValue, int y) {
//		int xValue = 0;	// rightest Value of rightest Obstacle on path
//		int index = 0;	// index of rightest Obstacle on path
//		for(int i = 0; i < obstacles.length; i++) {
//			int highest = obstacles[i].getYCorners()[obstacles[i].getHighestPointIndex()];
//			int lowest = obstacles[i].getYCorners()[obstacles[i].getLowestPointIndex()];
//			int xval_right = obstacles[i].getRightestValue();
//			int xval_left = obstacles[i].getLeftestValue();
//			
//			// is Obstacle on path
//			if(highest < y && lowest > y && xval_right > xValue && xval_left < max_xValue) {
//				xValue = xval_right;
//				index = i;
//			}
//		}
//		return obstacles[index];
//	}
//	
//	private Obstacle getObstacleOnPath(int x1, int y1, int x2, int y2) {
//		ArrayList<Obstacle> onPath = new ArrayList<Obstacle>();
//		int path_top, path_bottom, path_left, path_right;
//		
//		if(x1 < x2) {
//			path_left = x1;
//			path_right = x2;
//		} else {
//			path_left = x2;
//			path_right= x1;
//		}
//		if(y1 < y2) {
//			path_top = y1;
//			path_bottom = y2;
//		} else {
//			path_top = y2;
//			path_bottom = y1;
//		}
//		
//		for(Obstacle o : obstacles) {
//			int highest = o.getYCorners()[o.getHighestPointIndex()];
//			int lowest = o.getYCorners()[o.getLowestPointIndex()];
//			int leftest = o.getLeftestValue();
//			int rightest = o.getRightestValue();
//			
//			if(!(leftest > path_right && rightest < path_left &&
//					lowest < path_top && highest > path_bottom)) {
//				onPath.add(o);
//				// TODO: obstacle on path?
//				// TODO: which point and obstacle to choose
//			}
//		}
//		return null;
//	}
}
