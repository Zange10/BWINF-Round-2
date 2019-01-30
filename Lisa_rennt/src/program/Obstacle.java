package program;

public class Obstacle {
	int[] xCorners, yCorners, xCorners_win, yCorners_win;
	int corners;
	int[] highestPoint, lowestPoint, rightestPoint, leftestPoint;
	
	Obstacle(int corner_count) {
		xCorners = new int[corner_count];
		yCorners = new int[corner_count];
		xCorners_win = new int[corner_count];
		yCorners_win = new int[corner_count];
		highestPoint = new int[2];
		lowestPoint  = new int[2];
		rightestPoint = new int[2];
		leftestPoint = new int[2];
		corners = corner_count;
		
		highestPoint[1] = 10000;
		lowestPoint[1] = 0;
		rightestPoint[1] = 0;
		leftestPoint[1] = 10000;
	}
	
	public void addCorner(int x, int y, int index) {
		xCorners[index] = x;
		yCorners[index] = y;
		xCorners_win[index] = x;
		yCorners_win[index] = y;
		
		if(y < highestPoint[1]) {
			highestPoint[0] = index;
			highestPoint[1] = y;
		}
		
		if(y > lowestPoint[1]) {
			lowestPoint[0] = index;
			lowestPoint[1] = y;
		}
		
		if(x > rightestPoint[1]) {
			rightestPoint[0] = index;
			rightestPoint[1] = x;
		}
		
		if(x < leftestPoint[1]) {
			leftestPoint[0] = index;
			leftestPoint[1] = x;
		}
	}

	public int getCorners() {
		return corners;
	}

	public int[] getXCorners() {
		return xCorners;
	}

	public int[] getYCorners() {
		return yCorners;
	}
	
	public int[] getXCornersWin() {
		return xCorners_win;
	}

	public int[] getYCornersWin() {
		return yCorners_win;
	}
	
	public int getHighestPointIndex() {
		return highestPoint[0];
	}
	
	public int getLowestPointIndex() {
		return lowestPoint[0];
	}
	
	public int getRightestPointIndex() {
		return rightestPoint[0];
	}
	
	public int getRightestValue() {
		return rightestPoint[1];
	}
	
	public int getLeftestPointIndex() {
		return leftestPoint[0];
	}
	
	public int getLeftestValue() {
		return leftestPoint[1];
	}
}
