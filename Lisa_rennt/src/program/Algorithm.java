package program;

import java.util.ArrayList;

public class Algorithm {
	Obstacle[] obstacles;
	
	Algorithm(Obstacle[] obstacles) {
		this.obstacles = obstacles;
	}
	
	public ArrayList<Integer> caculate(int middle, int homeX) {
		ArrayList<Integer> pathPoints = new ArrayList<Integer>();
		int lastX = homeX;
		int lastY = middle;
		
		// first point at home
		pathPoints.add(lastX);
		pathPoints.add(lastY);
		
		while(!canGoStraight(lastX, lastY)) {
			// finding first obstacle on current path
			Obstacle obst = nextObstacle(lastX, lastY);
			lastX = obst.getXCorners()[obst.getHighestPointIndex()];
			lastY = obst.getYCorners()[obst.getHighestPointIndex()];
			pathPoints.add(lastX);
			pathPoints.add(lastY);
		}
		// last part of path
		pathPoints.add(0);
		pathPoints.add(pathPoints.get(pathPoints.size()-2));	// last height
		
		return pathPoints;
	}
	
	private boolean canGoStraight(int max_X, int middle) {
		for(Obstacle o : obstacles) {
			int highest = o.getYCorners()[o.getHighestPointIndex()];
			int lowest = o.getYCorners()[o.getLowestPointIndex()];
			int leftest = o.getLeftestValue();
			if(highest < middle && lowest > middle && leftest < max_X) return false;
		}
		return true;
	}
	
	private Obstacle nextObstacle(int max_xValue, int y) {
		int xValue = 0;	// rightest Value of rightest Obstacle on path
		int index = 0;	// index of rightest Obstacle on path
		for(int i = 0; i < obstacles.length; i++) {
			int highest = obstacles[i].getYCorners()[obstacles[i].getHighestPointIndex()];
			int lowest = obstacles[i].getYCorners()[obstacles[i].getLowestPointIndex()];
			int xval_right = obstacles[i].getRightestValue();
			int xval_left = obstacles[i].getLeftestValue();
			
			// is Obstacle on path
			if(highest < y && lowest > y && xval_right > xValue && xval_left < max_xValue) {
				xValue = xval_right;
				index = i;
			}
		}
		return obstacles[index];
	}
	
	private Obstacle getObstacleOnPath(int x1, int y1, int x2, int y2) {
		ArrayList<Obstacle> onPath = new ArrayList<Obstacle>();
		int path_top, path_bottom, path_left, path_right;
		
		if(x1 < x2) {
			path_left = x1;
			path_right = x2;
		} else {
			path_left = x2;
			path_right= x1;
		}
		if(y1 < y2) {
			path_top = y1;
			path_bottom = y2;
		} else {
			path_top = y2;
			path_bottom = y1;
		}
		
		for(Obstacle o : obstacles) {
			int highest = o.getYCorners()[o.getHighestPointIndex()];
			int lowest = o.getYCorners()[o.getLowestPointIndex()];
			int leftest = o.getLeftestValue();
			int rightest = o.getRightestValue();
			
			if(!(leftest > path_right && rightest < path_left &&
					lowest < path_top && highest > path_bottom)) {
				onPath.add(o);
				// TODO: obstacle on path?
				// TODO: which point and obstacle to choose
			}
		}
		return null;
	}
}
