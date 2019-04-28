package triangle;

public class Triangle {
	int[][] pos;	// x- and y-values of all three points
	double[] sidelengths;
	double[] angles;
	Side[] sides;
	
	
	public Triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		this.pos = new int[3][2];
		this.sidelengths = new double[3];
		this.sides = new Side[3];
		this.angles = new double[3];
		this.pos[0][0] = x1;
		this.pos[0][1] = y1;
		this.pos[1][0] = x2;
		this.pos[1][1] = y2;
		this.pos[2][0] = x3;
		this.pos[2][1] = y3;
		
		calcSides();
		calcAngles();
		sortByAngle();
	}
	
	private void calcSides() {
		for(int i = 0; i < 3; i++) {
			sides[i] = new Side(pos[i][0], pos[i][1], pos[(i+1)%3][0], pos[(i+1)%3][1]);
//			System.out.println(pos[i][0] + " " + pos[i][1] + " " + pos[(i+1)%3][0] + " " + pos[(i+1)%3][1]);
			sidelengths[i] = sides[i].getSidelength();
		}
	}

	private void calcAngles() {
		// law of cosine
		/*
		 * a^2 = b^2 + b^2 - 2bc * cos(a)
		 * <=> a^2 - b^2 - c^2 = -2bc * cos(a)
		 * <=> cos(a) = (a^2 - b^2 - c^2)/(-2bc)
		 * <=> a ("alpha")  = arccos((a^2 - b^2 - c^2)/(-2bc))
		 */
		
		for(int i = 0; i < 3; i++) {
			double a = sidelengths[i%3];
			double b = sidelengths[(i+1)%3];
			double c = sidelengths[(i+2)%3];
			angles[i] = Math.toDegrees(Math.acos((a*a - b*b - c*c)/(-2*b*c)));
		}
	}
	
	private void sortByAngle() {
		int shAngInd = 0;	// sharpest Angle
		double shAng = angles[0];
		for(int i = 1; i < 3; i++) {
			if(angles[i] < shAng) {
				shAngInd = i;
				shAng = angles[i];
			}
		}
		
		int add = 3+shAngInd;
		int[][] buffpos = new int[3][2];
		double[] buffsidelengths = new double[3];
		double[] buffangles = new double[3];
		
		for(int i = 0; i < 3; i++) {
			buffpos[i][0] = pos[(i+add)%3][0];
			buffpos[i][1] = pos[(i+add)%3][1];
			buffsidelengths[i] = sidelengths[(i+add)%3];
			buffangles[i] = angles[(i+add)%3];
		}
		pos = buffpos;
		sidelengths = buffsidelengths;
		angles = buffangles;
	}
	

	public void updateSides() {
		calcSides();
	}
	
	
	public void setPoint(int index, int x, int y) {
		pos[index][0] = x;
		pos[index][1] = y;
	}
	
	public int[] getXs() {
		int[] xPoints = new int[3];
		for(int i = 0; i < 3; i++) {
			xPoints[i] = pos[i][0];
		}
		return xPoints;
	}
	
	public int[] getYs() {
		int[] yPoints = new int[3];
		for(int i = 0; i < 3; i++) {
			yPoints[i] = pos[i][1];
		}
		return yPoints;
	}
	
	public double[] getAngles() {
		return angles;
	}
	
	public double[] getSidelengths() {
		return sidelengths;
	}
	
	public Side[] getSides() {
		return sides;
	}
}
