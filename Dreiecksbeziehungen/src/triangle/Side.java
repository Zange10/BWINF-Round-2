package triangle;

public class Side {
	double gradient;
	int x1, y1, x2, y2;
	double sidelength;
	double n;	// y-axis intersection
	
	Side(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		
		sortByX();
		calcYAxisIntersect();
		calcSideLengths();
	}

	private void calculateGradient() {
		// m = (x2-x1)/(y2-y1)
		this.gradient = (double)(y2-y1)/(double)(x2-x1);
	}
	
	private void calcYAxisIntersect() {
		// y = m*x+n
		// n = y - (m*x)
		this.n = y1 - (gradient * x1);
	}

	private void calcSideLengths() {
		// calculate sidelength with Pythagoras
		this.sidelength = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	public void sortByX() {
		if(!(x1 < x2)) {
			int buffX = x1;
			int buffY = y1;
			this.x1 = x2;
			this.y1 = y2;
			this.x2 = buffX;
			this.y2 = buffY;
		}
		calculateGradient();
	}
	
	public void sortByY() {
		if(!(y1 < y2)) {
			int buffX = x1;
			int buffY = y1;
			this.x1 = x2;
			this.y1 = y2;
			this.x2 = buffX;
			this.y2 = buffY;
		}
		calculateGradient();
	}
	
	public double calcX(int y) {
		// y = m*x+n
		// x = (y-n)/m
		double x = (y-n)/gradient;
		return x;
	}
	
	
	
	public double getGradient() {
		return gradient;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	public double getSidelength() {
		return sidelength;
	}
}
