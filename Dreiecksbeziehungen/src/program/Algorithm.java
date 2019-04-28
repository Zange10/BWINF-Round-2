package program;

import java.util.ArrayList;

import triangle.Triangle;

public class Algorithm {
	Triangle[] triangles;
	int distance;
	int leftest = 0, rightest = 0, highest = 0;
	
	Algorithm(Triangle[] triangles) {
		this.triangles = triangles;
	}
	
	public Triangle[] calculate() {
		double allFirstAngles = addSharpestAngles();
		if(allFirstAngles <= 180) {
			Semicircle sc = new Semicircle(triangles);
			triangles = sc.getTriangles();
			distance = 0;
			rightest = (int) sc.getRightest();
			leftest = (int) sc.getLeftest();
			highest = (int) sc.getHighest();
		} else {
			ArrayList<ArrayList<Triangle>> triangleGroups = calcGroups(triangles);
			Semicircle[] semicircles = new Semicircle[triangleGroups.size()];
			int t_counter = 0;
			double buffX = 0;
			for(int i = 0; i < triangleGroups.size(); i++) {
				semicircles[i] = new Semicircle(triangleGroups.get(i));
				if(semicircles[i].getHighest() > highest) highest = (int) semicircles[i].getHighest();
				if(i>0) {
					buffX -= semicircles[i].getLeftest();	// leftest is negative
					semicircles[i].move(buffX);
					semicircles[i-1].calcMinDistanceDiff(semicircles[i]);
					double diff = -(semicircles[i-1].calcMinDistanceDiff(semicircles[i]));
					buffX += diff;
					semicircles[i].move(diff);
				}
				buffX += semicircles[i].getRightest();
				Triangle[] group_triangles = semicircles[i].getTriangles();
				for(int j = 0; j < group_triangles.length; j++) {
					triangles[t_counter] = group_triangles[j];
					t_counter++;
				}
			}
			distance = (int) (buffX - semicircles[semicircles.length-1].getRightest());
			rightest = (int) buffX;
			leftest = (int) semicircles[0].getLeftest();
		}
		return triangles;
	}

	private double addSharpestAngles() {
		double result = 0;
		for(int i = 0; i < triangles.length; i++) {
			result += triangles[i].getAngles()[0];
		}
		return result;
	}

	private ArrayList<ArrayList<Triangle>> calcGroups(Triangle[] t) {
		sortByAngles(t);
		ArrayList<ArrayList<Triangle>> triangleGroups = new ArrayList<ArrayList<Triangle>>();
		for(int i = 0; i < t.length; ) {
			ArrayList<Triangle> tGroup = new ArrayList<Triangle>();
			double angle_counter = 0;
			while(angle_counter < 180 && i < t.length) {
				if(t[i] == null) i++;
				else {
					angle_counter += t[i].getAngles()[0];
					if(angle_counter < 180) {
						tGroup.add(t[i]);
						t[i] = null;
						i++;
					} else {
						angle_counter -= t[i].getAngles()[0];
						int tIndex = searchForAngle(180-angle_counter, t);
						while(tIndex >= 0) {
							tGroup.add(t[tIndex]);
							angle_counter += t[tIndex].getAngles()[0];
							t[tIndex] = null;
							tIndex = searchForAngle(180-angle_counter, t);
						}
						angle_counter += t[i].getAngles()[0];
					}
				}
			}
			triangleGroups.add(tGroup);
		}
		return triangleGroups;
	}
	
	private int searchForAngle(double angle, Triangle[] t) {
		for(int i = 0; i < t.length; i++) {
			if(t[i] != null && t[i].getAngles()[0] < angle) return i;
		}
		return -1;
	}
	
	private Triangle[] sortByAngles(Triangle[] t) {
		for(int i = 0; i < t.length - 1; i++) {
			for(int j = i + 1; j < t.length; j++) {
				if(t[i].getAngles()[0] < t[j].getAngles()[0]) {
					Triangle temp = t[i];
					t[i] = t[j];
					t[j] = temp;
				}
			}
		}
		return t;
	}
	
	public int getDistance() {
		return distance;
	}

	public int getLeftest() {
		return leftest;
	}

	public int getRightest() {
		return rightest;
	}

	public int getHighest() {
		return highest;
	}
}