package window;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import program.Obstacle;

public class Window extends JFrame{

	Container cp;
	int width, height;
	int roadwidth;
	
	public Window(int width, int height) {
		super();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.width = width;
		this.height = height;
		this.roadwidth = 100;
		setSize(width, height);
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (d.width - getSize().width) / 2;
	    int y = (d.height - getSize().height) / 2 - 17;
	    setLocation(x, y);
	    setTitle("Lisa");
	    setResizable(false);
	    cp = getContentPane();
	    cp.setLayout(null);
	    setVisible(true);
	    cp.setBackground(Color.WHITE);
	}

	public void paint(Graphics g) {
		// road (asphalt)
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, roadwidth, getHeight());
		// road (markers)
		int buffer = 0;
		g.setColor(Color.WHITE);
		while(buffer <= getHeight()) {
			int marklength = 50;
			g.fillRect(40, buffer, 15, marklength);
			buffer += marklength*2;
		}
	}
	
	public void drawHome(int x, int y) {
		Graphics g = getGraphics();
		g.setColor(Color.BLACK);
		g.drawOval(calcX(x), calcY(y), 10, 10);
		g.setColor(Color.RED);
		g.fillOval(calcX(x), calcY(y), 10, 10);
	}
	
	public void drawObstacles(Obstacle[] obstacles) {
		Graphics g = getGraphics();
		g.setColor(Color.BLACK);
		for(int i = 0; i < obstacles.length; i++) {
			int corners = obstacles[i].getCorners();
			int[] xPoints = obstacles[i].getXCorners();
			int[] yPoints = obstacles[i].getYCorners();
			for(int j = 0; j < xPoints.length; j++) {
				xPoints[j] = calcX(xPoints[j]);
				yPoints[j] = calcY(yPoints[j]);
			}
			g.fillPolygon(xPoints, yPoints, corners);
		}
	}

	public void drawPath(int x1, int y1, int x2, int y2) {
		Graphics g = getGraphics();
		g.setColor(Color.BLUE);
		g.drawLine(calcX(x1), calcY(y1), calcX(x2), calcY(y2));
	}
	
	private int calcX(int x) {
		return roadwidth + x;
	}
	
	private int calcY(int y) {
		return getHeight() - y;
	}
}
