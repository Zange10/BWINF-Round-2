package program;

import java.util.ArrayList;

import triangle.Side;
import triangle.Triangle;

public class Semicircle {
	double leftestX, rightestX, highest;
	int highestInd;
	Triangle[] triangles;
	int originX;
	
	public int getOriginX() {
		return originX;
	}

	Semicircle(Triangle[] allTriangles) {
		this.leftestX = 0;
		this.rightestX = 0;
		this.highest = 0;
		this.highestInd = 0;
		this.originX = 0;
		this.triangles = allTriangles.clone();
		this.triangles = makeSemicircle(triangles);
	}
	
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
		double startingAngle = 0;
		for(int i = 0; i < t.length; i++) {
			double angle = startingAngle;
			int x = 0, y = 0;
			double[] sidelengths = t[i].getSidelengths();
			double[] angles = t[i].getAngles();
			t[i].setPoint(0, 0, 0);
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
		// sort by side length -------------------
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
		
		// put in order with ping pong
		Triangle[] new_t = new Triangle[t.length];
		for(int i = 0; i < t.length; i++) {
			int j = (t.length-1)-i;
			if(j % 2 == 0) {
				new_t[j/2] = t[i];
			} else {
				new_t[t.length - (j+1)/2] = t[i];
			}
		}
		return new_t;
	}
	
	private int[] calcPosition(int startX, int startY, double angle, double sidelength) {
		int[] p = new int[2];
		p[0] = (int) (startX + sidelength*Math.cos(Math.toRadians(angle)));
		p[1] = (int) (startY + sidelength*Math.sin(Math.toRadians(angle)));
		return p;
	}
	
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
	
	public double calcMinDistanceDiff(Semicircle sc) {
		double difference = sc.getOriginX();
		Triangle[] testingTriangles = sc.getTriangles();
		
		for(int i = 0; i < triangles.length; i++) {
			Side[] sides = triangles[i].getSides();
			for(int j = 0; j < 3; j++) {
				System.out.print(sides[j].getX1() + " " + sides[j].getY1() + " " + sides[j].getX2() + " " + sides[j].getY2() + "   ");
			}
		}
		System.out.println("");
		
		
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
						System.out.println("--------- -----" + y1 + " " + y2 + " " + tY1 + " " + tY2);
						System.out.println("----------- " + x1 + " " + x2 + " " + tX1 + " " + tX2);
						
						if(y1 < tY1) {
							double buffDiff = tX1 - sides[j].calcX(tY1);
							System.out.println(1 + " " + tX1 + " " + sides[j].calcX(tY1));
							if(buffDiff < difference) difference = buffDiff;
						} else if (y1 > tY1){
							double buffDiff = testingSides[l].calcX(y1) - x1;
							System.out.println(2 + " " + testingSides[l].calcX(y1) + " " + x1);
							if(buffDiff < difference) difference = buffDiff;
						} else {
							double buffDiff = tX1 - x1;
							System.out.println(5 + " " + tX1 + " " + x1);
							if(buffDiff < difference) difference = buffDiff;
						}
						System.out.println(difference);

						if(y2 > tY2) {
							double buffDiff = tX2 - sides[j].calcX(tY2);
							System.out.println(3 + " " + tX2 + " " + sides[j].calcX(tY2));
							if(buffDiff < difference) difference = buffDiff;
						} else if(y2 < tY2) {
							double buffDiff = testingSides[l].calcX(y2) - x2;
							System.out.println(4 + " " + testingSides[l].calcX(y2) + " " + x2);
							if(buffDiff < difference) difference = buffDiff;
						} else {
							double buffDiff = tX1 - x1;
							System.out.println(6 + " " + tX2 + " " + x2);
							if(buffDiff < difference) difference = buffDiff;
						}
						System.out.println(difference);
						System.out.println("");
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
}
