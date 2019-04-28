package window;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Window extends JFrame{

	Container cp;
	int width, height;
	int leftmargin, sidemargin;
	
	public Window(int width, int height) {
		super();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.width = width;
		this.height = height;
		this.sidemargin = 100;
		setSize(width, height);
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (d.width - getSize().width) / 2;
	    int y = (d.height - getSize().height) / 2 - 17;
	    setLocation(x, y);
	    setTitle("Dreiecksbeziehungen");
	    setResizable(false);
	    setBackground(Color.WHITE);
	    cp = getContentPane();
	    cp.setLayout(null);
	    setVisible(true);
	    cp.setBackground(Color.WHITE);
	    
	    try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void drawTriangle(int[] xPoints, int[] yPoints, Color color) {
		Graphics g = getGraphics();
		g.setColor(color);
		for(int j = 0; j < xPoints.length; j++) {
			xPoints[j] = calcX(xPoints[j]);
			yPoints[j] = calcY(yPoints[j]);
		}
		g.fillPolygon(xPoints, yPoints, 3);
		g.setColor(Color.DARK_GRAY);
		g.drawPolygon(xPoints, yPoints, 3);
	}

	public void drawDistance(int distance) {
		Graphics g = getGraphics();
		g.setColor(Color.BLACK);
		for(int i = 0; i < 3; i++) {
			g.drawLine(calcX(0), calcY(-10+i), calcX(distance), calcY(-10+i));
		}
		for(int i = 0; i < 3; i++) {
			g.drawLine(calcX(-1+i), calcY(-3), calcX(-1+i), calcY(-15));
		}
		for(int i = 0; i < 3; i++) {
			g.drawLine(calcX(distance-1+i), calcY(-3), calcX(distance-1+i), calcY(-15));
		}
		
		g.setFont(new Font("TimesRoman", Font.BOLD, 30));
		g.drawString(String.valueOf(distance), calcX(distance/2), calcY(-50));
	}
	
	public void SetNewWindowSize(int minX, int maxX, int maxheight) {
		this.leftmargin = -minX;	// minX always negative
		this.width = maxX + leftmargin + 2*(sidemargin);
		this.height = maxheight + 2*(sidemargin);
		this.setSize(width, height);
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (d.width - getSize().width) / 2;
	    int y = (d.height - getSize().height) / 2 - 17;
	    this.setLocation(x, y);
		
	}
	
	// returns x-value calculated for output
	private int calcX(int x) {
		return leftmargin + sidemargin + x;
	}
	
	// returns y-value calculated for output
	private int calcY(int y) {
		return getHeight() - y - 100;
	}
}
