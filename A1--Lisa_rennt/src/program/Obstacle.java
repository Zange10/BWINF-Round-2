package program;

public class Obstacle {
	int[] xCorners, yCorners;	// x- or y-positions of every corner (for class Window)
	int[][] sides;	// x- and y-values of all sides and their individual index
	int corner_count;	// count of all corners
	int[] highestPoint, lowestPoint, rightestPoint, leftestPoint;	// x- and y-values of these points and their individual index
	
	Obstacle(int corner_count) {
		this.xCorners = new int[corner_count];
		this.yCorners = new int[corner_count];
		this.sides = new int[corner_count][5];
		this.highestPoint = new int[3];
		this.lowestPoint  = new int[3];
		this.rightestPoint = new int[3];
		this.leftestPoint = new int[3];
		this.corner_count = corner_count;
		
		this.highestPoint[1] = 0;	// must be changed
		this.lowestPoint[1] = 10000;	// must be changed
		this.rightestPoint[0] = 0;	// must be changed
		this.leftestPoint[0] = 10000;	// must be changed
	}
	
	// parses new values of new corner
	public void addCorner(int x, int y, int index) {
		xCorners[index] = x;
		yCorners[index] = y;
		
		// checking, if this point is higher, lower, ... than each other and if, then store
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
		
		// storing point with last point as new side
		// higher point first
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
		// if it is the last point, store this point with first point ("closing obstacle")
		// higher point first
		if(index >= corner_count-1) {
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
		// initialize path values -----------------------
		if(x1 < x2) {
			path_left = x1;
			path_right = x2;
		} else {
			path_left = x2;
			path_right = x1;
		}
		
		if(y1 > y2) {
			path_high = y1;
			path_low = y2;
		} else {
			path_high = y2;
			path_low = y1;
		}
		// is path even in range of obstacle -------------
		if(path_low > highestPoint[1] || path_high < lowestPoint[1] ||
				path_right < leftestPoint[0] || path_left > rightestPoint[0]) return false;
		// -----------------------------
		for(int i = 0; i < sides.length; i++) {
			// is path in range of side ---------
			boolean b1, b2, b3, b4;
			b1 = ((x1 < sides[i][0] && x1 < sides[i][2]) && (x2 < sides[i][0] && x2 < sides[i][2]));	// lefter
			b2 = ((x1 > sides[i][0] && x1 > sides[i][2]) && (x2 > sides[i][0] && x2 > sides[i][2]));	// righter
			b3 = ((y1 > sides[i][1] && y2 > sides[i][1]));	// higher
			b4 = ((y1 < sides[i][3] && y2 < sides[i][3]));	// lower
			if(b1 || b2 || b3 || b4) continue;
			
			// is one end of path is one corner of side -----------
			b1 = (sides[i][0] == x1 && sides[i][1] == y1);
			b2 = (sides[i][2] == x1 && sides[i][3] == y1);
			b3 = (sides[i][0] == x2 && sides[i][1] == y2);
			b4 = (sides[i][2] == x2 && sides[i][3] == y2);
			if(!(b1||b2||b3||b4)) {
				// crosses path this side ------------
				if(intersectsWithSide(x1, x2, y1, y2, i)) return true;
			} else {
				// the point, where path and side intersect (end and corner)
				int[] intersection = new int[2];
				if(b1 || b2) {
					intersection[0] = x1;
					intersection[1] = y1;
				} else {
					intersection[0] = x2;
					intersection[1] = y2;
				}
				
				int sec_index;	// index of second intersecting side
				
				// which end of the path intersects with which corner (of which side)
				if(i > 0) {
					if((sides[i-1][0] == intersection[0] && sides[i-1][1] == intersection[1]) ||
							(sides[i-1][2] == intersection[0] && sides[i-1][3] == intersection[1])) {
						sec_index = i-1;
					} else {
						if(i < corner_count-1) sec_index = i+1;
						else sec_index = 0;
					}
				} else {
					if((sides[corner_count-1][0] == intersection[0] && sides[corner_count-1][1] == intersection[1]) ||
							(sides[corner_count-1][2] == intersection[0] && sides[corner_count-1][3] == intersection[1])) {
						sec_index = corner_count-1;
					} else {
						if(i < corner_count-1) sec_index = i+1;
						else sec_index = 0;
					}
				}
				
				// the gradients of the path, the first side and the second side
				double gradient_path, gradient_first, gradient_second;
				
				// m = (y2-y1)/(x2-x1)
				gradient_path = (double)(y2 - y1) / (double)(x2 - x1);
				gradient_first = (double)(sides[i][3] - sides[i][1]) / (double)(sides[i][2] - sides[i][0]);
				gradient_second = (double)(sides[sec_index][3] - sides[sec_index][1]) / (double)(sides[sec_index][2] - sides[sec_index][0]);
				
				// angle to x-axis of the path, the first side and the second side
				// a ("alpha") = tan^-1 (m)
				double angle_path = Math.toDegrees(Math.atan(gradient_path));
				double angle_first = Math.toDegrees(Math.atan(gradient_first));
				double angle_second = Math.toDegrees(Math.atan(gradient_second));

				// is the intersection point the highest point
				boolean first_is_high = (intersection[0] == sides[i][0] && intersection[1] == sides[i][1]);
				boolean second_is_high = (intersection[0] == sides[sec_index][0] && intersection[1] == sides[sec_index][1]);
				boolean path_is_high = (y1 < intersection[1] || y2 < intersection[1]);

				// n = -(m*x-y) = y-m*x
				double n_path = y1-gradient_path*x1; // intersection with y-axis

				// has path same gradient as the first side -----------------
				if(gradient_path == gradient_first || (Double.isInfinite(gradient_path) && Double.isInfinite(gradient_first))) {
					// does path stops on side (if it does, another corner is there and Lisa should not be able to pass it)
					if((x1 > sides[i][0]) && (x1 < sides[i][2]) || (x1 < sides[i][0]) && (x1 > sides[i][2])) {
						return true;
					} else if((x2 > sides[i][0]) && (x2 < sides[i][2]) || (x2 < sides[i][0]) && (x2 > sides[i][2])) {
						return true;
					} else if((y1 > sides[i][1]) && (y1 < sides[i][3]) || (y1 < sides[i][1]) && (y1 > sides[i][3])) {
						return true;
					} else if((y2 > sides[i][1]) && (y2 < sides[i][3]) || (y2 < sides[i][1]) && (y2 > sides[i][3])) {
						return true;
					} else {
						// cannot cross this path, because identical or but goes further (does not cross side)
						continue;
					}
					
				// has path same gradient as the second side ---------------------
				} else if(gradient_path == gradient_second || (Double.isInfinite(gradient_path) && Double.isInfinite(gradient_second))) {
					// does path stops on side (if it does, another corner is there and Lisa should not be able to pass it)
					if((x1 > sides[sec_index][0]) && (x1 < sides[sec_index][2]) || (x1 < sides[sec_index][0]) && (x1 > sides[sec_index][2])) {
						return true;
					} else if((x2 > sides[sec_index][0]) && (x2 < sides[sec_index][2]) || (x2 < sides[sec_index][0]) && (x2 > sides[sec_index][2])) {
						return true;
					} else if((y1 > sides[sec_index][1]) && (y1 < sides[sec_index][3]) || (y1 < sides[sec_index][1]) && (y1 > sides[sec_index][3])) {
						return true;
					} else if((y2 > sides[sec_index][1]) && (y2 < sides[sec_index][3]) || (y2 < sides[sec_index][1]) && (y2 > sides[sec_index][3])) {
						return true;
					} else {
						// cannot cross this path, because identical, but goes further (does not cross side)
						continue;
					}
				}

				// angles between those straights (path, first side, second side)
				double ang_first_second, ang_path_first, ang_path_second;

				// calculating angles between these straights -------------------
				
				// if the first side is parallel to x-axis
				if(angle_first == 0) {
					boolean horizontal;
					int[] x_values = new int[]{sides[i][0], sides[i][2]};
					ang_first_second = calcZeroDegAngle(intersection, x_values, second_is_high, gradient_second, angle_second);
					ang_path_first = calcZeroDegAngle(intersection, x_values, path_is_high, gradient_path, angle_path);
					horizontal = (path_is_high == second_is_high);
					ang_path_second = calcAngle(angle_path, angle_second, horizontal);
					// if true, then path is either in obstacle or is going inwards (never fastest way)
					if(!(ang_path_first >= ang_first_second || ang_path_second >= ang_first_second)) return true;
					
				// if second side is parallel to x-axis
				} else if(angle_second == 0) {
					boolean horizontal;
					int[] x_values = new int[]{sides[sec_index][0], sides[sec_index][2]};
					ang_first_second = calcZeroDegAngle(intersection, x_values, first_is_high, gradient_first, angle_first);
					horizontal = (path_is_high == first_is_high);
					ang_path_first = calcAngle(angle_path, angle_first, horizontal);
					ang_path_second = calcZeroDegAngle(intersection, x_values, path_is_high, gradient_path, angle_path);
					// if true, then path is either in obstacle or is going inwards (never fastest way)
					if(!(ang_path_first >= ang_first_second || ang_path_second >= ang_first_second)) return true;
					
				// if path is parallel to x-axis
				} else if(gradient_path == 0) {
					// can never be part of fastest way (sides are "to the side")
					if(!(first_is_high == second_is_high)) return true;
				} else {
					ang_first_second = calcAngle(angle_first, angle_second, first_is_high == second_is_high);
					ang_path_first = calcAngle(angle_path, angle_first, path_is_high == first_is_high);
					ang_path_second = calcAngle(angle_path, angle_second, path_is_high == second_is_high);
					// if true, then path is either in obstacle or is going inwards (never fastest way)
					if(!(ang_path_first >= ang_first_second || ang_path_second >= ang_first_second)) return true;
				}
				
				// checking if path goes from corner to corner in obstacle (both are corners of obstacle)
				if(containsPoint(x1, y1) && containsPoint(x2, y2)) {
					// sending a probes from middle of the path downwards and checking how many sides they intersect
					// if they intersect an uneven number of sides, path is in obstacle
					if(Double.isFinite(gradient_path)) {
						int probe_x1, probe_x2, probe_y1, probe_y2;
						// x-positions of probes (both in the middle)
						probe_x1 = (x2-x1)/2 + x1;
						probe_x2 = probe_x1;
						// y-positions of probes (first: middle of path, second: 0   --> downwards)
						if(gradient_path != 0) {
							probe_y1 = (int)(gradient_path*probe_x1+n_path);
							probe_y2 = 0;
						} else {
							probe_y1 = y1;
							probe_y2 = 0;
						}
						double counter = 0;
						for(int j = 0; j < sides.length; j++) {
							if(!(probe_x1 == sides[j][0] || probe_x1 == sides[j][2])) {
								if(intersectsWithSide(probe_x1, probe_x2, probe_y1, probe_y2, j)) counter++;
							} else {
								if((probe_x1 == sides[j][0] && probe_y1 >= sides[j][1]) ||
										(probe_x1 == sides[j][2] && probe_y1 >= sides[j][3])) {
									counter += 0.5; // two sides at one corner --> adding up two count as one
								}
							}
						}
						if(counter%2 != 0) return true;
					}

					if(gradient_path != 0) {
						// sending a probes from middle of the path to the left and checking how many sides they intersect
						// if they intersect an uneven number of sides, path is in obstacle
						int probe_x1, probe_x2, probe_y1, probe_y2;
						// y-positions of probes (both in the middle)
						probe_y1 = (y2-y1)/2 + y1;
						probe_y2 = probe_y1;
						// y-positions of probes (first: middle of path, second: 0   --> to the left)
						if(Double.isFinite(gradient_path)) {
							probe_x1 = (int)((probe_y1-n_path)/gradient_path);
							probe_x2 = 0;
						} else {
							probe_x1 = x1;
							probe_x2 = 0;
						}
						double counter = 0;
						for(int j = 0; j < sides.length; j++) {
							if(!(probe_y1 == sides[j][1] || probe_y1 == sides[j][3])) {
								if(intersectsWithSide(probe_x1, probe_x2, probe_y1, probe_y2, j)) counter++;
							} else {
								if((probe_y1 == sides[j][1] && probe_x1 >= sides[j][0]) ||
										(probe_y1 == sides[j][3] && probe_x1 >= sides[j][2])) {
									counter += 0.5; // two sides at one corner --> adding up two count as one
								}
							}
						}
						if(counter%2 != 0) return true;
					}
				}
			}
		}
		return false;	// does not cross obstacle
	}
	
	// returns true if side intersects with given path -------------------------------------
	private boolean intersectsWithSide(int x1, int x2, int y1, int y2, int side_index) {
		
		double path_gradient, side_gradient;	// m = (y2-y1)/(x2-x1)
		path_gradient = (double)(y2 - y1) / (double)(x2 - x1);
		side_gradient = (double)(sides[side_index][3] - sides[side_index][1]) / (double)(sides[side_index][2] - sides[side_index][0]);
		double n_path, n_side; // n = y-m*x
		n_path = y1-path_gradient*x1; // intersection with y-axis
		n_side = sides[side_index][1]-side_gradient*sides[side_index][0]; // intersection with y-axis
		
		// calculate intersection ------------------
		if(side_gradient != path_gradient) {
			if(Double.isFinite(side_gradient) && Double.isFinite(path_gradient)) {
				// m1*x+n1 = m2*x+n2 	(m = gradient; n = intersection with y-axis)
				// <=> x = (n1-n2) / (m2-m1) --------------- y = m*x+n
				double intersection_x = (double) (n_path-n_side) / (double) (side_gradient-path_gradient);
				double intersection_y = (double) path_gradient * intersection_x + n_path;
				
				// calculate if path intersects with side -----------------------------------
				// intersection is not in range of used path- and side-values
				if((intersection_x > x1) == (intersection_x < x2) && (intersection_x > sides[side_index][0]) == (intersection_x < sides[side_index][2])) return true;
				if((int)intersection_x == x1) {
					if((int) intersection_y == y1) {
						if(((intersection_x > sides[side_index][0]) && (intersection_x < sides[side_index][2])) ||
								((intersection_x < sides[side_index][0]) && (intersection_x > sides[side_index][2]))) return true;
					}
				} else if((int)intersection_x == x2) {
					if((int) intersection_y == y2) {
						if(((intersection_x > sides[side_index][0]) == (intersection_x < sides[side_index][2])) ||
								((intersection_x < sides[side_index][0]) && (intersection_x > sides[side_index][2]))) return true;
					}
				} else if((int)intersection_x == sides[side_index][0]) {
					if((int) intersection_y == sides[side_index][1]) {
						if(((intersection_x > x1) == (intersection_x < x2)) ||
							((intersection_x < x1) && (intersection_x > x2))) return true;
					}
				} else if((int)intersection_x == sides[side_index][2]) {
					if((int) intersection_y == sides[side_index][3]) {
						if(((intersection_x > x1) == (intersection_x < x2)) ||
								((intersection_x < x1) && (intersection_x > x2))) return true;
					}
				}
			} else {
				if(Double.isInfinite(side_gradient)) {
					// crossees path side's x-value
					double intersection_x = sides[side_index][0];
					double path_inters_y = path_gradient*intersection_x+n_path;
					if((sides[side_index][1] > path_inters_y == sides[side_index][3] < path_inters_y) && (x1 > intersection_x == x2 < intersection_x)) return true;
				} else {
					// crossees side path's x-value
					double intersection_x = x1;
					double side_inters_y = side_gradient*intersection_x+n_side;
					if((y1 > side_inters_y == y2 < side_inters_y) && (sides[side_index][0] > intersection_x == sides[side_index][2] < intersection_x)) return true;
				}
			}
		} else {
			// if same gradient and going on each other --> "crosses"
			if(Double.isInfinite(path_gradient)) {
				if(x1 == sides[side_index][0]) return true;
			} else {
				if(n_path == n_side) return true;
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
					angle = 180-Math.abs(angle_line);	//	Math.abs(angle_first) = 0
				}
			} else {
				if(gradient_line > 0) {
					angle = 180-Math.abs(angle_line);
				} else {
					angle = 	Math.abs(angle_line);
				}
			}
		} else {
			if(line_is_high) {
				if(gradient_line > 0) {
					angle = 180-Math.abs(angle_line);
				} else {
					angle =		Math.abs(angle_line);
				}
			} else {
				if(gradient_line > 0) {
					angle =		Math.abs(angle_line);
				} else {
					angle = 180-Math.abs(angle_line);
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
	
	
	
	public boolean containsPoint(int x, int y) {
		for(int i = 0; i < xCorners.length; i++) {
			if(xCorners[i] == x && yCorners[i] == y) {
				return true;
			}
		}
		return false;
	}
	
	
	// Getters and Setters ----------------------------------------------------
	
	// returns the count of all corners
	public int getCorners() {
		return corner_count;
	}

	// returns x-positions of all corners
	public int[] getXCorners() {
		return xCorners;
	}

	// returns y-position of all corners
	public int[] getYCorners() {
		return yCorners;
	}
}
