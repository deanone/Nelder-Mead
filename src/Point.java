import java.util.ArrayList;
import java.util.Random;

/**
 * @author A. Salamanis
 * @version 0.1
 * @since 2019-09-05
 *
 * Point class: The class representing the concept of a point.
 */
public class Point {
	private int dimension;
	private ArrayList<Double> vals;
	
	/**
	 * Default constructor.
	 */
	public Point() {
		dimension = -1;
		vals = new ArrayList<Double>();
	}
	
	/**
	 * Constructor.
	 * 
	 * @param dimension the dimension of the point
	 * @param rangeMin the left end of the interval from which the values of the point are initialized
	 * @param rangeMax the right end of the interval from which the values of the point are initialized
	 */
	public Point(int dimension, double rangeMin, double rangeMax) {
		this.dimension = dimension;
		vals = new ArrayList<Double>();
		for (int valueIndex = 0; valueIndex < dimension; valueIndex++) {
			Random r = new Random();
			double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
			vals.add(randomValue);
		}
	}
	
	/**
	 * Adds a value to the point.
	 * 
	 * @param val the value to be added
	 */
	public void addVal(double val) {
		vals.add(val);
	}
	
	/**
	 * Getter of the number of values of the point.
	 * 
	 * @return the number of values
	 */
	public int getNumOfValues() {
		return vals.size();
	}
	
	/**
	 * Getter of the dimension of the point.
	 * 
	 * @return the dimension of the point
	 */
	public int getDimension() {
		return dimension;
	}
	
	/**
	 * Getter of the values of the point.
	 * 
	 * @return the values of the point.
	 */
	public ArrayList<Double> getValues() {
		return vals;
	}
	
	/**
	 * Getter of a value at a specific index.
	 * 
	 * @param i the index of the value
	 * @return the value
	 */
	public double getValue(int i) {
		return vals.get(i);
	}
}
