import java.util.ArrayList;

public class Simplex
{
	ArrayList<Point> testPoints;
	int n;
	
	public Simplex(int n, double rangeMin, double rangeMax) {
		testPoints = new ArrayList<Point>();
		this.n = n;
		for (int i = 0; i < this.n + 1; i++) {
			Point p = new Point(this.n, rangeMin, rangeMax);
			testPoints.add(p);
		}
	}
	
	public int getN() {
		return this.n;
	}
	
	public int getNumOfPoints() {
		return testPoints.size();
	}
	
	public Point getPoint(int i) {
		return testPoints.get(i);
	}
	
	public void setPoint(int i, Point p) {
		testPoints.set(i, p);
	}
	
	public ArrayList<Point> getPoints() {
		return testPoints;
	}
}