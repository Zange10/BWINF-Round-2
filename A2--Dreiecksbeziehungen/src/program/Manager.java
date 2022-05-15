package program;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import window.Window;


public class Manager {
	
	Window window;
	int highest, rightest;
	Triangle[] triangles;
	Algorithm algo;
	
	Manager() {
		
		
		parseData("Data/dreiecke4.txt");
		
		algo = new Algorithm(triangles);
		triangles = algo.calculate();
		window = new Window(500, 200);

		this.highest = 600;
		this.rightest = algo.getRightest();

		window.SetNewWindowSize(algo.getLeftest(), algo.getRightest(), algo.getHighest());
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Distance: " + algo.getDistance());
		for(int i = 0; i < triangles.length; i++) {
			Color color;
			if(i%2 == 0) color = Color.BLACK;
			else color = Color.GRAY;
			window.drawTriangle(triangles[i].getXs(), triangles[i].getYs(), color);
		}
		
		window.drawDistance(algo.getDistance());
		System.out.println("finished");
	}
	
	// parsing userdata
	private void parseData(String path) {
		try {
			Scanner scanner = new Scanner(new File(path));
			int triangle_count = scanner.nextInt();	// the amount of obstacles
			triangles = new Triangle[triangle_count];
			for(int i = 0; i < triangle_count; i++) {
				scanner.nextInt();	// always 3
				int[] p = new int[6];
				for(int j = 0; j < 3; j++) {	// for three corners
					int x = scanner.nextInt();
					int y = scanner.nextInt();
					p[j*2] = x;
					p[j*2+1] = y;
					// getting rightest and highest corner
					if(x > rightest) rightest = x;
					if(y > highest) highest = y;
				}
				triangles[i] = new Triangle(p[0], p[1], p[2], p[3], p[4], p[5]);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println("start");
		new Manager();
		System.out.println(System.currentTimeMillis()-start);
	}
}
