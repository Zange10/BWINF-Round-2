package program;

import java.util.ArrayList;

import triangle.Side;
import triangle.Triangle;

public class Semicircle {
	double leftestX, rightestX, highest;
	int highestInd;
	Triangle[] triangles;
	int originX;

	// only one semicircle overall
	Semicircle(Triangle[] allTriangles) {
		this.leftestX = 0;
		this.rightestX = 0;
		this.highest = 0;
		this.highestInd = 0;
		this.originX = 0;
		this.triangles = allTriangles.clone();
		this.triangles = makeSemicircle(triangles);
	}
	
	// multiple semicircles overall
	Semicircle(ArrayList<Triangle> allTriangles) {
		this.leftestX = 0;
		this.rightestX = 0;
		this.highest = 0;
		this.highestInd = 0;
		this.triangles = new Triangle[allTriangles.size()];
		allTriangles.toArray(this.triangles);
		this.triangles = putTrianglesInOrder(triangles);
		this.triangles = makeSemicircle(triangles);
	}
	
	private Triangle[] makeSemicircle(Triangle[] t) {
		double startingAngle = 0;	// rotates with each triangle
		for(int i = 0; i < t.length; i++) {
			double angle = startingAngle;
			int x = 0, y = 0;
			double[] sidelengths = t[i].getSidelengths();
			double[] angles = t[i].getAngles();
			t[i].setPoint(0, 0, 0);	// point where every triangle connects
			for(int j = 1; j < 3; j++) {
				 int[] p = calcPosition(x, y, angle, sidelengths[(j+1)%3]);
				 x = p[0];
				 y = p[1];
				 t[i].setPoint(j, x, y);
				 if(x < leftestX) leftestX = x;
				 if(x > rightestX)rightestX = x;
				 if(y > highest) {
					 highest = y;
					 highestInd = i;
				 }
				 angle += (180-angles[j]);
			}
			startingAngle += angles[0];
		}
		
		for(int i = 0; i < t.length; i++) {
			t[i].updateSides();
		}
		return t;
	}
	
	private Triangle[] putTrianglesInOrder(Triangle[] t) {
		// sort by side length (simple sorting algorithm)-------------------
		double longest;
		for(int i = 0; i < t.length - 1; i++) {
			double side1 = t[i].getSidelengths()[1];
			double side2 = t[i].getSidelengths()[2];
			if(side1 > side2) longest = side1;
			else longest = side2;
			for(int j = i + 1; j < t.length; j++) {
				side1 = t[j].getSidelengths()[1];
				side2 = t[j].getSidelengths()[2];
				if(side1 > longest || side2 > longest) {
					if(side1 > side2) longest = side1;
					else longest = side2;
					Triangle temp = t[i];
					t[i] = t[j];
					t[j] = temp;
				}
			}
		}
		
		// put in order with ping pong (triangles with shortest sidelengths - triangles with longest sidelengths - triangles with shortest sidelengths)
		Triangle[] new_t = new Triangle[t.length];
		for(int i = 0; i < t.length; i++) {
			int j = (t.length-1)-i;
			if(j % 2 == 0) {
				new_t[j/2] = t[i]; // on the front of the array
			} else {
				new_t[t.length - (j+1)/2] = t[i];	// on the back of the array
			}
		}
		return new_t;
	}
	
	private int[] calcPosition(int startX, int startY, double angle, double sidelength) {
		int[] p = new int[2];
		// x = x0 + sidelength*cos(a)
		p[0] = (int) (startX + sidelength*Math.cos(Math.toRadians(angle)));
		// y = y0 + sidelength*sin(a)
		p[1] = (int) (startY + sidelength*Math.sin(Math.toRadians(angle)));
		return p;
	}
	
	// move every point of every triangle by difference
	public void move(double difference) {
		for(int i = 0; i < triangles.length; i++) {
			for(int j = 0; j < 3; j++) {
				int x = (int) (triangles[i].getXs()[j] + difference);
				int y = triangles[i].getYs()[j];
				triangles[i].setPoint(j, x, y);
			}
			triangles[i].updateSides();
		}
		this.originX = (int) difference;
	}
	
	// calculating difference of minimum distance to semicircle sc and current position
	public double calcMinDistanceDiff(Semicircle sc) {
		double difference = sc.getOriginX();
		Triangle[] testingTriangles = sc.getTriangles();
		
		// every triangle that "hides" behind the highest point of the semicircle can be ignored
		
		for(int i = 0; i <= highestInd; i++) {
			Side[] sides = triangles[i].getSides();
			for(int j = 0; j < 3; j++) {
				
				for(int k = sc.getHighestInd(); k < sc.getTriangles().length; k++) {
					Side[] testingSides = testingTriangles[k].getSides();
					
					for(int l = 0; l < 3; l++) {
						sides[j].sortByY();
						testingSides[l].sortByY();
	
						int x1 = sides[j].getX1(), x2 = sides[j].getX2(), tX1 = testingSides[l].getX1(), tX2 = testingSides[l].getX2();
						int y1 = sides[j].getY1(), y2 = sides[j].getY2(), tY1 = testingSides[l].getY1(), tY2 = testingSides[l].getY2();
						
						boolean b1, b2;
						b1 = ((y1 > tY2 && y2 > tY2));	// too high
						b2 = ((y1 < tY1 && y2 < tY1));	// too low
						if(b1 || b2) continue;
						
						// finding closest point between sides
						
						if(y1 < tY1) {
							double buffDiff = tX1 - sides[j].calcX(tY1);
							if(buffDiff < difference) difference = buffDiff;
						} else if (y1 > tY1){
							double buffDiff = testingSides[l].calcX(y1) - x1;
							if(buffDiff < difference) difference = buffDiff;
						} else {
							double buffDiff = tX1 - x1;
							if(buffDiff < difference) difference = buffDiff;
						}
	
						if(y2 > tY2) {
							double buffDiff = tX2 - sides[j].calcX(tY2);
							if(buffDiff < difference) difference = buffDiff;
						} else if(y2 < tY2) {
							double buffDiff = testingSides[l].calcX(y2) - x2;
							if(buffDiff < difference) difference = buffDiff;
						} else {
							double buffDiff = tX1 - x1;
							if(buffDiff < difference) difference = buffDiff;
						}
					}
				}
			}
		}
		return difference;
	}
	
	public Triangle[] getTriangles() {
		return triangles;
	}

	public double getLeftest() {
		return leftestX;
	}

	public double getRightest() {
		return rightestX;
	}

	public double getHighest() {
		return highest;
	}
	
	public int getHighestInd() {
		return highestInd;
	}
	
	public int getOriginX() {
		return originX;
	}
}
