package program;

import java.util.ArrayList;

public class Semicircle {
	double leftest, rightest, highest;
	Triangle[] triangles;
	
	Semicircle(Triangle[] allTriangles) {
		this.leftest = 0;
		this.rightest = 0;
		this.highest = 0;
		this.triangles = allTriangles.clone();
		this.triangles = makeSemicircle(triangles);
	}
	
	Semicircle(ArrayList<Triangle> allTriangles) {
		this.leftest = 0;
		this.rightest = 0;
		this.highest = 0;
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
				 if(x < leftest) leftest = x;
				 if(x > rightest)rightest = x;
				 if(y > highest) highest = y;
//				 System.out.println(angle + " " + angles[j]);
				 angle += (180-angles[j]);
//				 System.out.println(angle + " " + angles[j]);
//				 System.out.println(p[0] + " " + p[1]+ " - " + angle);
			}
			startingAngle += angles[0];
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
					System.out.println(i + " " + t[j].getSidelengths()[1] + " " + t[j].getSidelengths()[2]);
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
				System.out.println(j/2);
			} else {
				new_t[t.length - (j+1)/2] = t[i];
				System.out.println(t.length - (j+1)/2);
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
		}
	}
	
	public Triangle[] getTriangles() {
		return triangles;
	}

	public double getLeftest() {
		return leftest;
	}

	public double getRightest() {
		return rightest;
	}

	public double getHighest() {
		return highest;
	}
}
