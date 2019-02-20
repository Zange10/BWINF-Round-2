package program;

public class Obstacle {
	int[] xCorners, yCorners; //, xCorners_win, yCorners_win;
	int[][] sides;
	int corners;
	int[] highestPoint, lowestPoint, rightestPoint, leftestPoint;
	
	Obstacle(int corner_count) {
		xCorners = new int[corner_count];
		yCorners = new int[corner_count];
		sides = new int[corner_count][4];
//		xCorners_win = new int[corner_count];
//		yCorners_win = new int[corner_count];
		highestPoint = new int[3];
		lowestPoint  = new int[3];
		rightestPoint = new int[3];
		leftestPoint = new int[3];
		corners = corner_count;
		
		highestPoint[1] = 0;
		lowestPoint[1] = 10000;
		rightestPoint[0] = 0;
		leftestPoint[0] = 10000;
	}
	
	public void addCorner(int x, int y, int index) {
		xCorners[index] = x;
		yCorners[index] = y;
//		xCorners_win[index] = x;
//		yCorners_win[index] = y;
		
		if(y > highestPoint[1]) {
			highestPoint[0] = x;
			highestPoint[1] = y;
			highestPoint[2] = index;
		}
		
		if(y < lowestPoint[1]) {
			lowestPoint[0] = x;
			lowestPoint[1] = y;
			lowestPoint[2] = index;
		}
		
		if(x > rightestPoint[0]) {
			rightestPoint[0] = x;
			rightestPoint[1] = y;
			rightestPoint[2] = index;
		}
		
		if(x < leftestPoint[0]) {
			leftestPoint[0] = x;
			leftestPoint[1] = y;
			leftestPoint[2] = index;
		}
		// highest is first
		if(index > 0) {
			if(yCorners[index-1] > y) {
				sides[index-1][0] = xCorners[index-1];
				sides[index-1][1] = yCorners[index-1];
				sides[index-1][2] = x;
				sides[index-1][3] = y;
			} else {
				sides[index-1][0] = x;
				sides[index-1][1] = y;
				sides[index-1][2] = xCorners[index-1];
				sides[index-1][3] = yCorners[index-1];
			}
		} 
		if(index >= corners-1) {
			if(yCorners[0] > y) {
				sides[index][0] = xCorners[0];
				sides[index][1] = yCorners[0];
				sides[index][2] = x;
				sides[index][3] = y;
			} else {
				sides[index][0] = x;
				sides[index][1] = y;
				sides[index][2] = xCorners[0];
				sides[index][3] = yCorners[0];
			}
		}
	}
	
	public boolean isOnPath(int x1, int y1, int x2, int y2) {
		int path_high, path_low, path_left, path_right;
		boolean first_is_left;
		// initialize path values -----------------------
		if(x1 < x2) {
			path_left = x1;
			path_right = x2;
			first_is_left = true;
		} else {
			path_left = x2;
			path_right = x1;
			first_is_left = false;
		}
		
		if(y1 > y2) {
			path_high = y1;
			path_low = y2;
		} else {
			path_high = y2;
			path_low = y1;
		}
//		System.out.println(path_low + " " + highestPoint[1] + " - " + path_high + " " + lowestPoint[1]);
//		System.out.println(path_right +" " + leftestPoint[0] + " - " + path_left + " " + rightestPoint[0]);
//		System.out.println((path_low > highestPoint[1]) + " " + (path_high < lowestPoint[1]) + " " +
//				(path_right < leftestPoint[0]) + " " + (path_left > rightestPoint[0]));
		// Path is near the obstacles --------------------
		if(path_low >= highestPoint[1] || path_high <= lowestPoint[1] ||
				path_right <= leftestPoint[0] || path_left >= rightestPoint[0]) return false;
		
		
		// TODO: fix code below (gradient infinity; no path not shown)
		
		
		// -----------------------------
		for(int[] side : sides) {
			System.out.println(side[0] + " " + side[1] + " " + side[2] + " " + side[3]);
			// high and low values of side
			int xval_high_side = side[0];
			int y_high = side[1];
			int xval_low_side = side[2];
			int y_low = side[3];
			double path_gradient;
			if(first_is_left) path_gradient = (double)(y2 - y1) / (double)(x2 - x1);
			else path_gradient = (double)(y1 - y2) / (double)(x1 - x2);
			double n_path = -(path_gradient*x1-y1); // intersection with y-axis
			System.out.println(path_gradient + " --- " + n_path);
			if(path_gradient != 0) {
				// values at high and low of side
				// y = m*x+n <=> x = (y-n)/m
				// y is y_high or y_low; n is path_low; m is path_gradient
				int xval_high = (int) Math.round((y_high-n_path)/path_gradient);
				int xval_low = (int) Math.round((y_low-n_path)/path_gradient);
				System.out.println(xval_high + "\t" + xval_low);
				System.out.println(xval_high_side + "\t" + xval_low_side);
				boolean isRightOfSide_high, isRightOfSide_low;
				if(path_gradient > 0) {
					isRightOfSide_high = (xval_high > xval_high_side);
					isRightOfSide_low = (xval_low > xval_low_side);
				} else {
					isRightOfSide_high = (xval_high >= xval_high_side);
					isRightOfSide_low = (xval_low >= xval_low_side);
				}
				
				if(isRightOfSide_high != isRightOfSide_low) return true;
			} else {
				// path_high = path_low
				boolean isBelowSide_high = (path_high < y_high);
				boolean isAboveSide_low = (path_high > y_low);
				
				if(isBelowSide_high && isAboveSide_low) {
					return true;
				}
			}
		}
		return false;
	}

	
	
	// Getters and Setters ----------------------------------------------------
	
	public int getCorners() {
		return corners;
	}

	public int[] getXCorners() {
		return xCorners;
	}

	public int[] getYCorners() {
		return yCorners;
	}
	
//	public int[] getXCornersWin() {
//		return xCorners_win;
//	}
//
//	public int[] getYCornersWin() {
//		return yCorners_win;
//	}
	
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
