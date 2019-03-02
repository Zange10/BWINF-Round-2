package window;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
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
	    setBackground(Color.GRAY);
	    cp = getContentPane();
	    cp.setLayout(null);
	    setVisible(true);
	    cp.setBackground(Color.GRAY);
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
		g.drawOval(calcX(x)-5, calcY(y)-5, 10, 10);
		g.setColor(Color.RED);
		g.fillOval(calcX(x)-5, calcY(y)-5, 10, 10);
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

	public void drawPath(int x1, int y1, int x2, int y2, Color color) {
		Graphics g = getGraphics();
		g.setColor(color);
		g.drawLine(calcX(x1), calcY(y1), calcX(x2), calcY(y2));
	}
	
	public void writeTime(double[] times, int x1, int y1, int x2, int y2) {
		Graphics g = getGraphics();
		g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		g.drawString(parseTime(times[0]) + "", calcX(x1)+5, calcY(y1)-5);
		g.drawString(parseTime(times[1]) + "", calcX(x2)+5, calcY(y2)-5);
	}
	
	private String parseTime(double time) {
		int hour = 7;
		int minute = 30;
		int second = 0;
		int millisecond = 0;
		while(time >= 3600 || time <= -3600) {
			if(time > 0) {
				hour++;
				time -= 3600;
			} else {
				hour--;
				time += 3600;
			}
		}
		while(time >= 60 || time <= -60) {
			if(time > 0) {
				minute++;
				time -= 60;
			} else {
				minute--;
				time += 60;
			}
		}
		if(time >= 0) {
			second = (int) time;
			time -= second;	// works, because second is floored
			millisecond = (int) (time*1000);
		} else {
			second = 59 + (int) time;	// (int) is always floored; needs to be other way round
			time -= (int) time;	// every digit behind the dot is not cleared
			if(time < 0) millisecond = 1000 + (int) (time*1000);
		}
		
		String s_hour, s_minute, s_second, s_millisecond;
		if(hour == 0) s_hour = "00:";
		else if(hour < 10) s_hour = "0" + hour + ":";
		else s_hour = hour + ":";
		if(minute == 0) s_minute = "00:";
		else if(minute < 10) s_minute = "0" + minute + ":";
		else s_minute = minute + ":";
		if(second == 0) s_second = "00.";
		else if(second < 10) s_second = "0" + second + ".";
		else s_second = second + ".";
		
		if(millisecond == 0) s_millisecond = "000";
		else if(millisecond < 10) s_millisecond = "00" + millisecond;
		else if(millisecond < 100) s_millisecond = "0" + millisecond;
		else s_millisecond = millisecond + "";
		
		return s_hour + s_minute + s_second + s_millisecond;
	}
	
	private int calcX(int x) {
		return roadwidth + x;
	}
	
	private int calcY(int y) {
		return getHeight() - y;
	}
}
