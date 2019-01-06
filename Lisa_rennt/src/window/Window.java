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
	
	public Window(int width, int height) {
		super();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.width = width;
		this.height = height;
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
		g.fillRect(0, 0, 200, getHeight());
		// road (markers)
		int buffer = 0;
		g.setColor(Color.WHITE);
		while(buffer <= getHeight()) {
			int marklength = 100;
			g.fillRect(85, buffer, 30, marklength);
			buffer += marklength*2;
		}
	}
	
	public void drawHome(int x, int diameter) {
		Graphics g = getGraphics();
		g.setColor(Color.BLACK);
		g.drawOval(x+200-diameter/2, height/2-diameter/2 + 20, diameter, diameter);
		g.setColor(Color.RED);
		g.fillOval(x+200-diameter/2, height/2-diameter/2 + 20, diameter, diameter);
	}
	
	public void drawObstacles(Obstacle[] obstacles) {
		Graphics g = getGraphics();
		g.setColor(Color.BLACK);
		for(int i = 0; i < obstacles.length; i++) {
			int corners = obstacles[i].getCorners();
			int[] xPoints = obstacles[i].getXCornersWin();
			int[] yPoints = obstacles[i].getYCornersWin();
			g.fillPolygon(xPoints, yPoints, corners);
		}
	}
	

	public void drawPath(int x1, int y1, int x2, int y2) {
		Graphics g = getGraphics();
		g.setColor(Color.BLUE);
		g.drawLine(x1+200, y1+20, x2+200, y2+20);
	}
}
