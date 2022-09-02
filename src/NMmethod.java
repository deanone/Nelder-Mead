import java.util.TreeMap;	// implementation of Map and SortedMap interfaces that maintains ascending order
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Map.Entry;
import javax.swing.JFrame;

/**
 * @author A. Salamanis
 * @version 0.1
 * @since 2019-09-05
 *
 * NM class: The class representing the Nelder-Mead algorithm.
 */
public class NMmethod {
	public static double Himmelblau(double x, double y) {
		return Math.pow(Math.pow(x, 2) + y - 11, 2) + Math.pow(x + Math.pow(y, 2) - 7, 2);
	}
	
	public static double Himmelblau(Point p) {
		int n = p.getNumOfValues();
		if (n != 2) {
			return -1.0;
		}
		else {
			return Math.pow(Math.pow(p.getValue(0), 2) + p.getValue(1) - 11, 2) + Math.pow(p.getValue(0) + Math.pow(p.getValue(1), 2) - 7, 2);
		}
	}

	/**
	 * Order the test points of the simplex according to the values of the function to be optimized.
	 * 
	 * @param sim the simplex
	 */
	public static void order(Simplex sim) {
		TreeMap<Double, Point> testPointsFuncValues = new TreeMap<Double, Point>();
		int numOfTestPoints = sim.getNumOfTestPoints();
		for (int testPointIndex = 0; testPointIndex < numOfTestPoints; testPointIndex++) {
			Point testPoint = sim.getPoint(testPointIndex);
			Double testPointFuncValue = Himmelblau(testPoint);
			testPointsFuncValues.put(testPointFuncValue, testPoint);
		}
		
		int i = 0;
		for (Entry<Double, Point> entry : testPointsFuncValues.entrySet()) {
			Point testPoint = entry.getValue();
			sim.setPoint(i, testPoint);
			i++;
		}
	}
	
	/**
	 * Calculates the centroid of all points of the simplex except of the x_{n+1}.
	 * 
	 * @param sim the simplex
	 * @return the centroid
	 */
	public static Point calcCentroid(Simplex sim) {
		int numOfTestPoints = sim.getNumOfTestPoints();
		int dimension = numOfTestPoints - 1;
		
		// the centroid point
		Point centroid  = new Point();
		centroid.setDimension(dimension);		
		for (int i = 0; i < dimension; i++) {
			double val = 0.0;
			for (int testPointIndex = 0; testPointIndex < numOfTestPoints; testPointIndex++) {
				val += sim.getPoint(testPointIndex).getValue(i);
			}
			val /= numOfTestPoints;
			centroid.addVal(val);
		}
		return centroid;
	}
	
	/**
	 * Subtracts the values of two points, thus generating a new point. (it could be an operator overloading function in C++).
	 * 
	 * @param p1 the first point
	 * @param p2 the second point
	 * @return the new point generated by the subtraction of the two points
	 */
	public static Point subtract(Point p1, Point p2) {
		int n1 = p1.getNumOfValues();
		int n2 = p2.getNumOfValues();
		Point diff = new Point();
		if (n1 == n2) {
			int dimension = n1;
			diff.setDimension(dimension);
			for (int valueIndex = 0; valueIndex < dimension; valueIndex++) {
				double val = p1.getValue(valueIndex) - p2.getValue(valueIndex);
				diff.addVal(val);
			}
		}
		return diff;
	}
	
	/**
	 * Multiplies the values of a point with a constant, thus generating a new point. (it could be an operator overloading function in C++).
	 * 
	 * @param p the point
	 * @param c the constant
	 * @return the new point generated by the multiplication of a point with a constant
	 */
	public static Point multiplyPointByConstant(Point p, double c) {
		int dimension = p.getNumOfValues();
		Point product = new Point();
		product.setDimension(dimension);
		for (int valueIndex = 0; valueIndex < dimension; valueIndex++) {
			double val = c * p.getValue(valueIndex);
			product.addVal(val);
		}
		return product;
	}
	
	/**
	 * Adds the values of two points, thus generating a new point. (it could be an operator overloading function in C++).
	 * 
	 * @param p1 the first point
	 * @param p2 the second point
	 * @return the new point generated by the addition of the two points
	 */
	public static Point add(Point p1, Point p2) {
		int n1 = p1.getNumOfValues();
		int n2 = p2.getNumOfValues();
		Point sum = new Point();
		if (n1 == n2) {
			int dimension = n1;
			sum.setDimension(dimension);
			for (int valueIndex = 0; valueIndex < dimension; valueIndex++) {
				double val = p1.getValue(valueIndex) + p2.getValue(valueIndex);
				sum.addVal(val);
			}
		}
		return sum;
	}
	
	/**
	 * Computes reflected point.
	 * 
	 * @param centroid the centroid of the simplex
	 * @param lastPoint the last point of the simplex
	 * @return the reflected point
	 */
	public static Point reflection(Point centroid, Point lastPoint) {
		double alpha = 1.0;
		Point reflectedPoint = add(centroid, multiplyPointByConstant(subtract(centroid, lastPoint), alpha));
		return reflectedPoint;
	}
	
	/**
	 * Computes the expanded point.
	 * 
	 * @param centroid the centroid of the simplex
	 * @param reflectedPoint the reflected point
	 * @return the expanded point
	 */
	public static Point expansion(Point centroid, Point reflectedPoint) {
		double gamma = 2.0;
		Point expandedPoint = add(centroid, multiplyPointByConstant(subtract(reflectedPoint, centroid), gamma));    
		return expandedPoint;
	}
	
	/**
	 * Computes the contracted point.
	 * 
	 * @param centroid the centroid of the simplex
	 * @param point either the reflected point (for contacted point on the outside) or the last point of the simplex (for the contracted point on the inside)
	 * @return the contracted point
	 */
	public static Point contraction(Point centroid, Point point) {
		double rho = 0.5;
		Point contractedPoint =  add(centroid, multiplyPointByConstant(subtract(point, centroid), rho));
		return contractedPoint;
	}
	
	/**
	 * Replace all points of the simplex, but the best.
	 * 
	 * @param sim the simplex
	 * @param bestPoint the best point
	 * @param bestPointIndex the index of the best point
	 */
	public static void shrink(Simplex sim, Point bestPoint, int bestPointIndex) {
		double sigma = 0.5; 
		int numOfTestPoints = sim.getNumOfTestPoints();
		for (int testPointIndex = 0; testPointIndex < numOfTestPoints; testPointIndex++) {
			if (testPointIndex != bestPointIndex) {
				Point newPoint = add(bestPoint, multiplyPointByConstant(subtract(sim.getPoint(testPointIndex), bestPoint), sigma));
				sim.setPoint(testPointIndex, newPoint);
			}
		}
	}
	
	/**
	 * Computes the standard deviation of the function values at the points of the current simplex.
	 * 
	 * @param sim the current simplex
	 * @return the standard deviation of the function values at the points of the current simplex
	 */
	public static double computeSimplexStd(Simplex sim) {
		// Compute f values for the points of the Simplex
		ArrayList<Double> fvals = new ArrayList<Double>();
		int numOfTestPoints = sim.getNumOfTestPoints();
		for (int testPointIndex = 0; testPointIndex < numOfTestPoints; testPointIndex++) {
			double fval = Himmelblau(sim.getPoint(testPointIndex));
			fvals.add(fval);
		}
		
		// Compute the standard deviation of the f values
		double s = StatisticsCalculator.std(fvals);
		return s;
	}
	
	/**
	 * Evaluates the termination criteria of the method.
	 * 
	 * @param sim the simplex
	 * @param tolerance the tolerance against which the standard deviation of the function values of current simplex is compared 
	 * @return true if the evaluation criteria have been met, false otherwise
	 */
	public static boolean isTerminated(Simplex sim, double tolerance){
		boolean terminated = false;
		double simplexStd = computeSimplexStd(sim);
		if (simplexStd < tolerance) {
			terminated = true;
		}
		return terminated;		
	}
	
	public static void main(String[] args) {
		// visualization
		JFrame frame = new JFrame("Executing the Nelder-Mead algorithm...");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		// create a new simplex
		int numOfTestPoints = 3;
		double rangeMin = -1.0;
		double rangeMax = 1.0;
		Simplex sim = new Simplex(numOfTestPoints, rangeMin, rangeMax);
		
		int delayTime = 1;
		double tolerance = 1.0;
		int iterationIndex = 1;
		double simplexStd = 0.0;
		double minFval = 0.0;
		boolean terminated = isTerminated(sim, tolerance);
		
		// iteration
		while (!terminated) {
			// the test points of the simplex according to the function values on them
			order(sim);
			
			simplexStd = computeSimplexStd(sim);
			minFval = Himmelblau(sim.getPoint(numOfTestPoints - 1));
			System.out.printf("Iteration: %d", iterationIndex);
			System.out.printf(", Simplex standard deviation: %.2f ", simplexStd);
			System.out.printf(", Minimum value of function: %.2f ", minFval);
			System.out.println();
			iterationIndex += 1;
			
			// Compute centroid
			Point centroid = calcCentroid(sim);
			
			// Compute reflected point
			Point reflectedPoint = reflection(centroid, sim.getPoint(numOfTestPoints - 1));
			
			// Check reflected point
			double fBest = Himmelblau(sim.getPoint(0));
			double fReflectedPoint = Himmelblau(reflectedPoint);
			double fSecondWorst = Himmelblau(sim.getPoint(numOfTestPoints - 2));
			if ((fReflectedPoint >= fBest) && (fReflectedPoint < fSecondWorst)) {
				sim.setPoint(numOfTestPoints - 1, reflectedPoint);
				terminated = isTerminated(sim, tolerance);
			}
			else if (fReflectedPoint < fBest) {
				Point expandedPoint = expansion(centroid, reflectedPoint);
				double fExpandedPoint = Himmelblau(expandedPoint);
				if (fExpandedPoint < fReflectedPoint) {
					sim.setPoint(numOfTestPoints - 1, expandedPoint);
					terminated = isTerminated(sim, tolerance);
				}
				else {
					sim.setPoint(numOfTestPoints - 1, reflectedPoint);
					terminated = isTerminated(sim, tolerance);
				}
			}
			else {
				
				double fWorstPoint = Himmelblau(sim.getPoint(numOfTestPoints - 1));
				if (fReflectedPoint < fWorstPoint) {
					Point contractedPoint = contraction(centroid, reflectedPoint);
					double fContractedPoint = Himmelblau(contractedPoint);
					if (fContractedPoint < fReflectedPoint)
					{
						sim.setPoint(numOfTestPoints - 1, contractedPoint);
						terminated = isTerminated(sim, tolerance);
					}
					else {
						shrink(sim,  sim.getPoint(numOfTestPoints - 1), numOfTestPoints - 1);
					}
				}
				else {
					Point contractedPoint = contraction(centroid, sim.getPoint(numOfTestPoints - 1));
					double fContractedPoint = Himmelblau(contractedPoint);
					if (fContractedPoint < fWorstPoint)
					{
						sim.setPoint(numOfTestPoints - 1, contractedPoint);
						terminated = isTerminated(sim, tolerance);
					}
					else {
						shrink(sim,  sim.getPoint(numOfTestPoints - 1), numOfTestPoints - 1);
					}
				}
			}
			
			// visualization
			PointDrawer pointsDrawer = new PointDrawer(sim.getPoints());
			frame.add(pointsDrawer);
			frame.repaint();
			frame.revalidate();
			try {
				TimeUnit.SECONDS.sleep(delayTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("The algorithm converged in %d iterations with minimum simplex stardard deviation %.2f and minimum value of function %.2f.", iterationIndex - 1, simplexStd, minFval);
	}
}