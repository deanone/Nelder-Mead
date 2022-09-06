import java.util.ArrayList;

/**
 * @author A. Salamanis
 * @version 0.1
 * @since 2019-09-05
 *
 * Simplex class: The class representing the concept of a simplex.
 */
public class Simplex {
	private int numOfTestPoints; 
	private ArrayList<Point> testPoints;

	/**
	 * Constructor.
	 * 
	 * @param numOfTestPoints the number of test points
	 * @param rangeMin the left end of the interval from which the values of the test points are initialized
	 * @param rangeMax the right end of the interval from which the values of the test points are initialized
	 */
	public Simplex(int numOfTestPoints, double rangeMin, double rangeMax) {
		this.numOfTestPoints = numOfTestPoints;
		int dimension = this.numOfTestPoints - 1;
		testPoints = new ArrayList<Point>();
		for (int testPointIndex = 0; testPointIndex < this.numOfTestPoints; testPointIndex++) {
			Point p = new Point(dimension, rangeMin, rangeMax);
			testPoints.add(p);
		}
	}

	/**
	 * Getter for the number of test points.
	 * 
	 * @return the number of test points
	 */
	public int getNumOfTestPoints() {
		return numOfTestPoints;
	}
	
	/**
	 * Getter for a point at a specific index.
	 * 
	 * @param i the index of the point
	 * @return a point at a specific index
	 */
	public Point getPoint(int i) {
		return testPoints.get(i);
	}
	
	/**
	 * Setter of a point at a specific index.
	 * 
	 * @param i the index of the point
	 * @param p the point to be added
	 */
	public void setPoint(int i, Point p) {
		testPoints.set(i, p);
	}
	
	/**
	 * Getter of the set of all test points.
	 * 
	 * @return all test points
	 */
	public ArrayList<Point> getPoints() {
		return testPoints;
	}
}