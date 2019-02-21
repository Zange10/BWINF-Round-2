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
//		boolean first_is_left;
		// initialize path values -----------------------
		if(x1 < x2) {
			path_left = x1;
			path_right = x2;
//			first_is_left = true;
		} else {
			path_left = x2;
			path_right = x1;
//			first_is_left = false;
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
		System.out.println(highestPoint[1] + " " + lowestPoint[1]);
		if(path_low >= highestPoint[1] || path_high <= lowestPoint[1] ||
				path_right <= leftestPoint[0] || path_left >= rightestPoint[0]) return false;
		
		
		// TODO: fix code below (gradient infinity)
		
		
		// -----------------------------
		for(int i = 0; i < sides.length; i++) {
//			System.out.println(sides[i][0] + " " + sides[i][1] + " " + sides[i][2] + " " + sides[i][3]);
			// high and low values of side
			int xval_high_side = sides[i][0];
			int y_high = sides[i][1];
			int xval_low_side = sides[i][2];
			int y_low = sides[i][3];
			
			boolean b1, b2, b3, b4;
			b1 = (sides[i][0] == x1 && sides[i][1] == y1);
			b2 = (sides[i][2] == x1 && sides[i][3] == y1);
			b3 = (sides[i][0] == x2 && sides[i][1] == y2);
			b4 = (sides[i][2] == x2 && sides[i][3] == y2);
			
			if(!(b1||b2||b3||b4)) {
				double path_gradient;
//				if(first_is_left) path_gradient = (double)(y2 - y1) / (double)(x2 - x1);
//				else path_gradient = (double)(y1 - y2) / (double)(x1 - x2);
				path_gradient = (double)(y2 - y1) / (double)(x2 - x1);
				double n_path = -(path_gradient*x1-y1); // intersection with y-axis
//				System.out.println(path_gradient + " --- " + n_path);
				if(path_gradient != 0) {
					// values at high and low of side
					// y = m*x+n <=> x = (y-n)/m
					// y is y_high or y_low; n is path_low; m is path_gradient
					int xval_high = (int) Math.round((y_high-n_path)/path_gradient);
					int xval_low = (int) Math.round((y_low-n_path)/path_gradient);
//					System.out.println(xval_high + "\t" + xval_low);
//					System.out.println(xval_high_side + "\t" + xval_low_side);
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
				
				
				
			} else {
				
				int[] intersection = new int[2];
				if(b1 || b2) {
					intersection[0] = x1;
					intersection[1] = y1;
				} else {
					intersection[0] = x2;
					intersection[1] = y2;
				}
				
//				System.out.println(intersection[0] + " " + intersection[1]);
				
				int sec_index;	// index of second intersecting side
				
				if(i > 0) {
					if((sides[i-1][0] == intersection[0] && sides[i-1][1] == intersection[1]) ||
							(sides[i-1][2] == intersection[0] && sides[i-1][3] == intersection[1])) {
						sec_index = i-1;
					} else {
						if(i < corners-1) sec_index = i+1;
						else sec_index = 0;
					}
				} else {
					if((sides[corners-1][0] == intersection[0] && sides[corners-1][1] == intersection[1]) ||
							(sides[corners-1][2] == intersection[0] && sides[corners-1][3] == intersection[1])) {
						sec_index = corners-1;
					} else {
						if(i < corners-1) sec_index = i+1;
						else sec_index = 0;
					}
				}
				
				double gradient_path, gradient_first, gradient_second;
				
				gradient_path = (double)(y2 - y1) / (double)(x2 - x1);
				gradient_first = (double)(sides[i][3] - sides[i][1]) / (double)(sides[i][2] - sides[i][0]);
				gradient_second = (double)(sides[sec_index][3] - sides[sec_index][1]) / (double)(sides[sec_index][2] - sides[sec_index][0]);
				
				
				
				// angle to x-axis
				double angle_path = Math.toDegrees(Math.atan(gradient_path));
//				if(angle_path < 0) angle_path = 180 + angle_path;
				
				double angle_first = Math.toDegrees(Math.atan(gradient_first));
//				if(angle_first < 0) angle_first = 180 + angle_first;

				double angle_second = Math.toDegrees(Math.atan(gradient_second));
//				if(angle_second < 0) angle_second = 180 + angle_second;
				
				System.out.print(sides[i][0] + " " + sides[i][1] + " " + sides[i][2] + " " + sides[i][3] + " (" + i + ") - ");
				System.out.println(sides[sec_index][0] + " " + sides[sec_index][1] + " " + sides[sec_index][2] + " " + sides[sec_index][3] + " (" + sec_index + ")");
				System.out.println(" G " + gradient_path + " " + gradient_first + " " + gradient_second);
				System.out.println(" A " + angle_path + " " + angle_first + " " + angle_second);
				
				// calculation
				boolean horizontal;	// is obstacle to left/right
				boolean first_high = (intersection[0] == sides[i][0] && intersection[1] == sides[i][1]);
				boolean second_high = (intersection[0] == sides[sec_index][0] && intersection[1] == sides[sec_index][1]);
				horizontal = (first_high == second_high);
				System.out.println(horizontal);
				
				double ang_first_second, ang_path_first, ang_path_second;
				ang_first_second = calcAngle(angle_first, angle_second, horizontal);
				ang_path_first = calcAngle(angle_path, angle_first, horizontal);
				ang_path_second = calcAngle(angle_path, angle_second, horizontal);
				
				System.out.println(ang_first_second + " " + ang_path_first + " " + ang_path_second);
				
				if(!(ang_path_first >= ang_first_second || ang_path_second >= ang_first_second)) return true;
			}
		}
		return false;
	}

	
	private double calcAngle(double a1, double a2, boolean horizontal) {
		double angle;
		if((a1 == Math.abs(a1)) == (a2 == Math.abs(a2))) {
			angle = Math.abs(Math.abs(a1) - Math.abs(a2));
		} else {
			if(!horizontal) {
				angle =		(Math.abs(a1) + Math.abs(a2));
			} else {
				angle = 180-(Math.abs(a1) + Math.abs(a2));
			}
		}
		return angle;
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
