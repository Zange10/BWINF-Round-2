package program;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import window.FileWindow;
import window.Window;

public class Manager {
	Window window;
	Obstacle[] obstacles;
	Algorithm algo;
	int[] home_pos;	// x and y position of Lisa's home
	ArrayList<int[]> all_corners;	// x and y position of every corner of every obstacle
	int highest, rightest;	// rightest and highest point of "image"
	
	Manager() {
		this.highest = 0;
		this.rightest = 0;
		this.all_corners = new ArrayList<int[]>();
		this.home_pos = new int[2];

//		FileWindow fcWindow = new FileWindow();
//		parseData(fcWindow.getPath());
		parseData("data/example3.txt");
//		parseData("data/lisarennt5.txt");
		
		algo = new Algorithm(home_pos, obstacles, all_corners);
		ArrayList<Integer> route = algo.caculate();
		
		// end of path is sometimes higher than highest obstacle
		if(route.get(route.size()-1) > highest) highest = route.get(route.size()-1);
		
		window = new Window(rightest+250, highest+50);	
		sleep(200);	// algorithm sometimes to fast for window to open
		window.drawObstacles(obstacles);
		window.drawHome(home_pos[0], home_pos[1]);
		

//		ArrayList<ArrayList<Integer>> paths = algo.getAllPaths();
//		if(paths != null) {
//			for(ArrayList<Integer> pathPoints : paths) {
//				for(int i = 0; i < pathPoints.size()/2-1; i++) {
//					int x1 = pathPoints.get(i*2);
//					int y1 = pathPoints.get(i*2+1);
//					int x2 = pathPoints.get((i+1)*2);
//					int y2 = pathPoints.get((i+1)*2+1);
//					window.drawPath(x1, y1, x2, y2, Color.CYAN);
//				}
//			}
//		} else {
//			int x1 = home_pos[0];
//			int y1 = home_pos[1];
//			int x2 = 0;
//			int y2 = window.getHeight()/2;
//			window.drawPath(x1, y1, x2, y2, Color.BLACK);
//		}

		
//		ArrayList<Integer> shortest = algo.getShortest();
//		for(int i = 0; i < shortest.size()/2-1; i++) {
//			int x1 = shortest.get(i*2);
//			int y1 = shortest.get(i*2+1);
//			int x2 = shortest.get((i+1)*2);
//			int y2 = shortest.get((i+1)*2+1);
//			window.drawPath(x1, y1, x2, y2, Color.RED);
//		}

		// drawing every path of the best route
		for(int i = 0; i < route.size()/2-1; i++) {
			int x1 = route.get(i*2);
			int y1 = route.get(i*2+1);
			int x2 = route.get((i+1)*2);
			int y2 = route.get((i+1)*2+1);
			window.drawPath(x1, y1, x2, y2, Color.GREEN);
		}
//		
		window.writeTimes(algo.getTimes(), home_pos[0], home_pos[1], 0, route.get(route.size()-1));
	}
	
	/**
	 * this method parses the data from the user's file:
	 * 1. getting amount of obstacles
	 * 2. getting amount of corners and their positions for every obstacle
	 * 3. getting home's position
	 */
	private void parseData(String path) {
		try {
			Scanner scanner = new Scanner(new File(path));
			int obst_count = scanner.nextInt();	// the amount of obstacles
			obstacles = new Obstacle[obst_count];
			for(int i = 0; i < obst_count; i++) {
				int corner_count = scanner.nextInt();
				
				obstacles[i] = new Obstacle(corner_count);
				for(int j = 0; j < corner_count; j++) {
					int x = scanner.nextInt();
					int y = scanner.nextInt();
					
					// getting rightest and highest corner
					if(x > rightest) rightest = x;
					if(y > highest) highest = y;
					obstacles[i].addCorner(x, y, j);
					all_corners.add(new int[] {x,y});
				}
			}

			home_pos[0] = scanner.nextInt();
			home_pos[1] = scanner.nextInt();
			if(home_pos[0] > rightest) rightest = home_pos[0];
			if(home_pos[1] > highest) highest = home_pos[1];
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Method to wait a specific time
	private void sleep(int millisec) {
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		try {
			new Manager();
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(new JFrame(), "Beim nächsten Mal bitte eine gültige Datei wählen.");
//		}
	}
}
