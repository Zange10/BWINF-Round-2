package program;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import window.Window;

public class Manager {
	Window window;
	Obstacle[] obstacles;
	Algorithm algo;
	int[] home_pos;
	ArrayList<int[]> all_corners;
	int highest, rightest;
	
	Manager() {
		highest = 0;
		rightest = 0;
		all_corners = new ArrayList<int[]>();
		home_pos = new int[2];
//		parseData("data/example4.txt");
		parseData("data/lisarennt3.txt");
		algo = new Algorithm(home_pos, obstacles, all_corners);
		ArrayList<Integer> route = algo.caculate();
		ArrayList<ArrayList<Integer>> paths = algo.getAllPaths();
		
		if(route.get(route.size()-1) > highest) highest = route.get(route.size()-1);
		window = new Window(rightest+250, highest+50);
		sleep(200);
		window.drawObstacles(obstacles);
		window.drawHome(home_pos[0], home_pos[1]);
		
		if(paths != null) {
			for(ArrayList<Integer> pathPoints : paths)
				for(int i = 0; i < pathPoints.size()/2-1; i++) {
					int x1 = pathPoints.get(i*2);
					int y1 = pathPoints.get(i*2+1);
					int x2 = pathPoints.get((i+1)*2);
					int y2 = pathPoints.get((i+1)*2+1);
					window.drawPath(x1, y1, x2, y2, Color.CYAN);
				}
		} else {
			int x1 = home_pos[0];
			int y1 = home_pos[1];
			int x2 = 0;
			int y2 = window.getHeight()/2;
			window.drawPath(x1, y1, x2, y2, Color.BLACK);
		}

		
		ArrayList<Integer> fastest = algo.getFastest();
		for(int i = 0; i < fastest.size()/2-1; i++) {
			int x1 = fastest.get(i*2);
			int y1 = fastest.get(i*2+1);
			int x2 = fastest.get((i+1)*2);
			int y2 = fastest.get((i+1)*2+1);
			window.drawPath(x1, y1, x2, y2, Color.RED);
		}

		for(int i = 0; i < route.size()/2-1; i++) {
			int x1 = route.get(i*2);
			int y1 = route.get(i*2+1);
			int x2 = route.get((i+1)*2);
			int y2 = route.get((i+1)*2+1);
			window.drawPath(x1, y1, x2, y2, Color.GREEN);
		}
		
		window.writeTime(algo.getTime(), home_pos[0], home_pos[1], 0, route.get(route.size()-1));
		
		System.out.println("finished");
	}
	
	private void parseData(String path) {
		try {
			Scanner scanner = new Scanner(new File(path));
			int obst_count = scanner.nextInt();
			obstacles = new Obstacle[obst_count];
			for(int i = 0; i < obst_count; i++) {
				int corner_count = scanner.nextInt();
				obstacles[i] = new Obstacle(corner_count);
				for(int j = 0; j < corner_count; j++) {
					int x = scanner.nextInt();
					int y = scanner.nextInt();
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
	
	private void sleep(int millisec) {
		try {
			Thread.sleep(millisec);
		} catch (InterruptedException e) {
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
