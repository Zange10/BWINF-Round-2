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
//		if(path_low >= highestPoint[1] || path_high <= lowestPoint[1] ||
//				path_right <= leftestPoint[0] || path_left >= rightestPoint[0]) return false;
		if(path_low > highestPoint[1] || path_high < lowestPoint[1] ||
				path_right < leftestPoint[0] || path_left > rightestPoint[0]) return false;
		// -----------------------------
		for(int i = 0; i < sides.length; i++) {
			// is path in range of side
			boolean b1, b2, b3, b4;
			b1 = ((x1 < sides[i][0] && x1 < sides[i][2]) && (x2 < sides[i][0] && x2 < sides[i][2]));	// lefter
			b2 = ((x1 > sides[i][0] && x1 > sides[i][2]) && (x2 > sides[i][0] && x2 > sides[i][2]));	// righter
			b3 = ((y1 > sides[i][1] && y2 > sides[i][1]));	// higher
			b4 = ((y1 < sides[i][3] && y2 < sides[i][3]));	// lower
			if(b1 || b2 || b3 || b4) continue;
			
			// high and low values of side
//			int xval_high_side = sides[i][0];
//			int y_high = sides[i][1];
//			int xval_low_side = sides[i][2];
//			int y_low = sides[i][3];
			
			b1 = (sides[i][0] == x1 && sides[i][1] == y1);
			b2 = (sides[i][2] == x1 && sides[i][3] == y1);
			b3 = (sides[i][0] == x2 && sides[i][1] == y2);
			b4 = (sides[i][2] == x2 && sides[i][3] == y2);
			if((b1 && b4) || (b2 && b3)) return false;
			else if(!(b1||b2||b3||b4)) {
				double path_gradient, side_gradient;
				path_gradient = (double)(y2 - y1) / (double)(x2 - x1);
				side_gradient = (double)(sides[i][3] - sides[i][1]) / (double)(sides[i][2] - sides[i][0]);
				double n_path, n_side;
				n_path = -(path_gradient*x1-y1); // intersection with y-axis
				n_side = -(side_gradient*sides[i][0]-sides[i][1]);
				
				// calculate intersection
				// m1*x+n1 = m2*x+n2 	(m = gradient; n = intersection with y-axis)
				// <=> x = (n1-n2) / (m2-m1)
				if(side_gradient != path_gradient) {
					if(Double.isFinite(side_gradient) && Double.isFinite(path_gradient)) {
						double intersection_x = (double) (n_path-n_side) / (double) (side_gradient-path_gradient);
						double intersection_y = (double) path_gradient * intersection_x + n_path;
						if((intersection_x > x1) == (intersection_x < x2) && (intersection_x > sides[i][0]) == (intersection_x < sides[i][2])) return true;
						if(side_gradient != path_gradient) {
							if((int)intersection_x == x1) {
								if((int) intersection_y == y1) {
									if(((intersection_x > sides[i][0]) && (intersection_x < sides[i][2])) ||
											((intersection_x < sides[i][0]) && (intersection_x > sides[i][2]))) return true;
								}
							} else if((int)intersection_x == x2) {
								if((int) intersection_y == y2) {
									if(((intersection_x > sides[i][0]) == (intersection_x < sides[i][2])) ||
											((intersection_x < sides[i][0]) && (intersection_x > sides[i][2]))) return true;
								}
							} else if((int)intersection_x == sides[i][0]) {
								if((int) intersection_y == sides[i][1]) {
									if(((intersection_x > x1) == (intersection_x < x2)) ||
										((intersection_x < x1) && (intersection_x > x2))) return true;
								}
							} else if((int)intersection_x == sides[i][2]) {
								if((int) intersection_y == sides[i][3]) {
									if(((intersection_x > x1) == (intersection_x < x2)) ||
											((intersection_x < x1) && (intersection_x > x2))) return true;
								}
							}
						}
					} else {
						if(Double.isInfinite(side_gradient)) {
							double intersection_x = sides[i][0];
							double path_inters_y = path_gradient*intersection_x+n_path;
							if(sides[i][1] > path_inters_y == sides[i][3] < path_inters_y) return true;
						} else {
							double intersection_x = x1;
							double side_inters_y = side_gradient*intersection_x+n_side;
							if(y1 > side_inters_y == y2 < side_inters_y) return true;
						}
					}
				} else {
					if(n_path == n_side) return true;
				}
			} else {
				// intersection point
				int[] intersection = new int[2];
				if(b1 || b2) {
					intersection[0] = x1;
					intersection[1] = y1;
				} else {
					intersection[0] = x2;
					intersection[1] = y2;
				}
				
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

				// check if second side is same as path
				b1 = (sides[sec_index][0] == x1 && sides[sec_index][1] == y1);
				b2 = (sides[sec_index][2] == x1 && sides[sec_index][3] == y1);
				b3 = (sides[sec_index][0] == x2 && sides[sec_index][1] == y2);
				b4 = (sides[sec_index][2] == x2 && sides[sec_index][3] == y2);
				if((b1 && b4) || (b2 && b3)) continue;
				
				
				double gradient_path, gradient_first, gradient_second;
				
				gradient_path = (double)(y2 - y1) / (double)(x2 - x1);
				gradient_first = (double)(sides[i][3] - sides[i][1]) / (double)(sides[i][2] - sides[i][0]);
				gradient_second = (double)(sides[sec_index][3] - sides[sec_index][1]) / (double)(sides[sec_index][2] - sides[sec_index][0]);

				// (Double.isInfinite(gradient_path) && Double.isInfinite(gradient_first) --> -Infinity and Infinity are equal
				if(gradient_path == gradient_first || (Double.isInfinite(gradient_path) && Double.isInfinite(gradient_first))) {
					// if it stops on path
					if((x1 > sides[i][0]) && (x1 < sides[i][2]) || (x1 < sides[i][0]) && (x1 > sides[i][2])) {
						return true;
					} else if((x2 > sides[i][0]) && (x2 < sides[i][2]) || (x2 < sides[i][0]) && (x2 > sides[i][2])) {
						return true;
					} else if((y1 > sides[i][1]) && (y1 < sides[i][3]) || (y1 < sides[i][1]) && (y1 > sides[i][3])) {
						return true;
					} else if((y2 > sides[i][1]) && (y2 < sides[i][3]) || (y2 < sides[i][1]) && (y2 > sides[i][3])) {
						return true;
					} else {
						// cannot collide with this path, because identical, but goes further
						continue;
					}
					// (Double.isInfinite(gradient_path) && Double.isInfinite(gradient_first) --> -Infinity and Infinity are equal
				} else if(gradient_path == gradient_second || (Double.isInfinite(gradient_path) && Double.isInfinite(gradient_second))) {
					// stops on path
					if((x1 > sides[sec_index][0]) && (x1 < sides[sec_index][2]) || (x1 < sides[sec_index][0]) && (x1 > sides[sec_index][2])) {
						return true;
					} else if((x2 > sides[sec_index][0]) && (x2 < sides[sec_index][2]) || (x2 < sides[sec_index][0]) && (x2 > sides[sec_index][2])) {
						return true;
					} else if((y1 > sides[sec_index][1]) && (y1 < sides[sec_index][3]) || (y1 < sides[sec_index][1]) && (y1 > sides[sec_index][3])) {
						return true;
					} else if((y2 > sides[sec_index][1]) && (y2 < sides[sec_index][3]) || (y2 < sides[sec_index][1]) && (y2 > sides[sec_index][3])) {
						return true;
					} else {
						// cannot collide with this path, because identical, but goes further
						continue;
					}
				}
				
				// angle to x-axis
				double angle_path = Math.toDegrees(Math.atan(gradient_path));
				double angle_first = Math.toDegrees(Math.atan(gradient_first));
				double angle_second = Math.toDegrees(Math.atan(gradient_second));
				
				// calculation

				// is the intersection point the highest point
				boolean first_is_high = (intersection[0] == sides[i][0] && intersection[1] == sides[i][1]);
				boolean second_is_high = (intersection[0] == sides[sec_index][0] && intersection[1] == sides[sec_index][1]);
				boolean path_is_high = (y1 < intersection[1] || y2 < intersection[1]);
				
				if(angle_first == 0) {
					double ang_first_second, ang_path_first, ang_path_second;
					boolean horizontal;
					int[] x_values = new int[]{sides[i][0], sides[i][2]};
					ang_first_second = calcZeroDegAngle(intersection, x_values, second_is_high, gradient_second, angle_second);
					ang_path_first = calcZeroDegAngle(intersection, x_values, path_is_high, gradient_path, angle_path);
					horizontal = (path_is_high == second_is_high);
					ang_path_second = calcAngle(angle_path, angle_second, horizontal);
					if(!(ang_path_first >= ang_first_second || ang_path_second >= ang_first_second)) return true;
					
				} else if(angle_second == 0) {
					double ang_first_second, ang_path_first, ang_path_second;
					boolean horizontal;
					int[] x_values = new int[]{sides[sec_index][0], sides[sec_index][2]};
					ang_first_second = calcZeroDegAngle(intersection, x_values, first_is_high, gradient_first, angle_first);
					horizontal = (path_is_high == first_is_high);
					ang_path_first = calcAngle(angle_path, angle_first, horizontal);
					ang_path_second = calcZeroDegAngle(intersection, x_values, path_is_high, gradient_path, angle_path);
					if(!(ang_path_first >= ang_first_second || ang_path_second >= ang_first_second)) return true;
					
				} else if(gradient_path == 0) {
					// can be still in obstacle
					if(!(first_is_high == second_is_high)) return true;
				} else {
					double ang_first_second, ang_path_first, ang_path_second;
					ang_first_second = calcAngle(angle_first, angle_second, first_is_high == second_is_high);
					ang_path_first = calcAngle(angle_path, angle_first, path_is_high == first_is_high);
					ang_path_second = calcAngle(angle_path, angle_second, path_is_high == second_is_high);
					
					if(!(ang_path_first >= ang_first_second || ang_path_second >= ang_first_second)) return true;
				}
			}
		}
		return false;
	}

	private double calcZeroDegAngle(int[] intersection, int[] x_values, boolean line_is_high, double gradient_line, double angle_line) {
		double angle;
		if(x_values[0] < intersection[0] || x_values[1] < intersection[0]) {
			if(line_is_high) {
				if(gradient_line > 0) {
					angle = 	Math.abs(angle_line);	// (Math.abs(angle_first) + Math.abs(angle_second))
				} else {
					angle =180-	Math.abs(angle_line);	//	Math.abs(angle_first) = 0
				}
			} else {
				if(gradient_line > 0) {
					angle =180-	Math.abs(angle_line);
				} else {
					angle = 	Math.abs(angle_line);
				}
			}
		} else {
			if(line_is_high) {
				if(gradient_line > 0) {
					angle =180-	Math.abs(angle_line);
				} else {
					angle =		Math.abs(angle_line);
				}
			} else {
				if(gradient_line > 0) {
					angle =		Math.abs(angle_line);
				} else {
					angle =180-	Math.abs(angle_line);
				}
			}
		}
		return angle;
	}
	
	
	private double calcAngle(double a1, double a2, boolean long_angle) {
		double angle;
		if((a1 == Math.abs(a1)) == (a2 == Math.abs(a2))) {
			if(!long_angle) {
				angle =	180-Math.abs(Math.abs(a1) - Math.abs(a2));
			} else {
				angle = 	Math.abs(Math.abs(a1) - Math.abs(a2));
			}
		} else {
			if(!long_angle) {
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
