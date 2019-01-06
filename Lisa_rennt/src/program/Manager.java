package program;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import window.Window;

public class Manager {
	Window window;
	Obstacle[] obstacles;
	Algorithm algo;
	int home_x;
	
	Manager() {
		window = new Window(1500,750);
		parseData("data/example1");
		window.drawObstacles(obstacles);
		home_x = window.getWidth()-250;
		window.drawHome(home_x, 25);
		algo = new Algorithm(obstacles);
		ArrayList<Integer> pathPoints = algo.caculate((window.getHeight())/2, home_x);
		if(pathPoints != null ) {
			for(int i = 0; i < pathPoints.size()/2-1; i++) {
				int x1 = pathPoints.get(i*2);
				int y1 = pathPoints.get(i*2+1);
				int x2 = pathPoints.get((i+1)*2);
				int y2 = pathPoints.get((i+1)*2+1);
				window.drawPath(x1, y1, x2, y2);
			}
		} else {
			int x1 = home_x;
			int y1 = window.getHeight()/2;
			int x2 = 0;
			int y2 = window.getHeight()/2;
			window.drawPath(x1, y1, x2, y2);
		}
		
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
					obstacles[i].addCorner(scanner.nextInt(), scanner.nextInt(), j);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("start");
		new Manager();
	}
}
