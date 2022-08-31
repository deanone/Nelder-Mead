import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JFrame;

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

	public static void Order(Simplex sim) {
		Map<Double, Point> m = new TreeMap<Double, Point>();
		int numOfPoints = sim.getNumOfPoints();
		for (int i = 0; i < numOfPoints; i++) {
			Point p = sim.getPoint(i);
			Double fval = Himmelblau(p);
			m.put(fval, p);
		}
		
		int i = 0;
		for (Entry<Double, Point> entry : m.entrySet()) {
			Point p = entry.getValue();
			sim.setPoint(i, p);
			i++;
		}
	}
	
	public static double mean(ArrayList<Double> vals) {
		double m = 0.0;
		for (int i = 0; i < vals.size(); i++) {
			m += vals.get(i);
		}
		m /= vals.size();
		return m;
	}
	
	public static double std(ArrayList<Double> vals) {
		double s = 0.0;
		double m = mean(vals);
		for (int i = 0; i < vals.size(); i++) {
			s += Math.pow(vals.get(i) - m, 2);
		}
		s /= (double)(vals.size() - 1);
		s = Math.sqrt(s);
		return s;
	}
	
	public static Point add(Point p1, Point p2) {
		int n1 = p1.getNumOfValues();
		int n2 = p2.getNumOfValues();
		Point sum = new Point();
		if (n1 == n2) {
			for (int i = 0; i < n1; i++) {
				double val = p1.getValue(i) + p2.getValue(i);
				sum.addVal(val);
			}
		}
		return sum;
	}
	
	public static Point subtract(Point p1, Point p2) {
		int n1 = p1.getNumOfValues();
		int n2 = p2.getNumOfValues();
		Point diff = new Point();
		if (n1 == n2) {
			for (int i = 0; i < n1; i++) {
				double val = p1.getValue(i) - p2.getValue(i);
				diff.addVal(val);
			}
		}
		return diff;
	}
	
	public static Point multiplyConstantByPoint(double c, Point p) {
		int n = p.getNumOfValues();
		Point product = new Point();
		for (int i = 0; i < n; i++) {
			double val = c * p.getValue(i);
			product.addVal(val);
		}
		return product;
	}
	
	public static Point calcCentroid(Simplex sim) {
		int numOfPoints = sim.getNumOfPoints();
		int n = sim.getN();
		Point centroid  = new Point();
		for (int i = 0; i < n; i++) {
			double val = 0.0;
			for (int j = 0; j < numOfPoints; j++) {
				val += sim.getPoint(j).getValue(i);
			}
			val /= numOfPoints;
			centroid.addVal(val);
		}
		return centroid;
	}
	
	public static Point reflection(Point centroid, Point lastPoint) {
		double alpha = 1.0;
		Point reflectedPoint = subtract(centroid, lastPoint);
		reflectedPoint = multiplyConstantByPoint(alpha, reflectedPoint);
		reflectedPoint = add(centroid, reflectedPoint);
		return reflectedPoint;
	}
	
	public static Point expansion(Point centroid, Point reflectedPoint) {
		double gamma = 2.0;
		Point expandedPoint = subtract(reflectedPoint, centroid);
		expandedPoint = multiplyConstantByPoint(gamma, expandedPoint);
		expandedPoint = add(centroid, expandedPoint);
		return expandedPoint;
	}
	
	public static Point contraction(Point centroid, Point lastPoint) {
		double rho = 0.5;
		Point contractedPoint = subtract(lastPoint, centroid);
		contractedPoint = multiplyConstantByPoint(rho, contractedPoint);
		contractedPoint = add(centroid, contractedPoint);
		return contractedPoint;
	}
	
	public static void shrink(Simplex sim, Point bestPoint, int bestPointId) {
		double sigma = 0.5; 
		int numOfPoints = sim.getNumOfPoints();
		for (int i = 0; i < numOfPoints; i++) {
			if (i != bestPointId) {
				Point newPoint = subtract(sim.getPoint(i), bestPoint);
				newPoint = multiplyConstantByPoint(sigma, newPoint);
				newPoint = add(newPoint, bestPoint);
				sim.setPoint(i, newPoint);
			}
		}
	}
	
	public static double computeSimplexStd(Simplex sim) {
		// Compute f values for the points of the Simplex
		ArrayList<Double> fvals = new ArrayList<Double>();
		int numOfPoints = sim.getNumOfPoints();
		for (int i = 0; i < numOfPoints; i++) {
			double fval = Himmelblau(sim.getPoint(i));
			fvals.add(fval);
		}
		
		// Compute standard deviation of the f values
		double s = std(fvals);
		return s;
	}
	
	public static boolean isTerminated(Simplex sim, double threshold){
		boolean terminated = false;
		double simplexStd = computeSimplexStd(sim);
		if (simplexStd < threshold) {
			terminated = true;
		}
		return terminated;		
	}
	
	public static void main(String[] args) {
		// Visualize
		JFrame frame = new JFrame("Running...");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		int delayTime = 1;
		double threshold = 10.0;
		int n = 2;
		int numOfPoints = n + 1;
		double rangeMin = 1.0;
		double rangeMax = 5.0;
		Simplex sim = new Simplex(n, rangeMin, rangeMax);
		
		int iterationIndex = 1;
		double simplexStd = 0.0;
		double minFval = 0.0;
		boolean terminated = isTerminated(sim, threshold);
		while (!terminated) {
			Order(sim);
			
			simplexStd = computeSimplexStd(sim);
			minFval = Himmelblau(sim.getPoint(numOfPoints - 1));
			System.out.printf("Iteration: %d", iterationIndex);
			System.out.printf(", Simplex standard deviation: %.2f ", simplexStd);
			System.out.printf(", Minimum value of function: %.2f ", minFval);
			System.out.println();
			iterationIndex += 1;
			
			// Calculate centroid
			Point centroid = calcCentroid(sim);
			
			// Calculate reflected point
			Point reflectedPoint = reflection(centroid, sim.getPoint(numOfPoints - 1));
			
			// Check reflected point
			double fBest = Himmelblau(sim.getPoint(0));
			double fReflectedPoint = Himmelblau(reflectedPoint);
			double fSecondWorst = Himmelblau(sim.getPoint(numOfPoints - 2));
			
			if (fReflectedPoint >= fBest && fReflectedPoint <= fSecondWorst) {
				sim.setPoint(numOfPoints - 1, reflectedPoint);
				terminated = isTerminated(sim, threshold);
			}
			else if (fReflectedPoint < fBest) {
				Point expandedPoint = expansion(centroid, reflectedPoint);
				double fExpandedPoint = Himmelblau(expandedPoint);
				if (fExpandedPoint < fReflectedPoint) {
					sim.setPoint(numOfPoints - 1, expandedPoint);
					terminated = isTerminated(sim, threshold);
				}
				else {
					sim.setPoint(numOfPoints - 1, reflectedPoint);
					terminated = isTerminated(sim, threshold);
				}
			}
			else {
				Point contractedPoint = contraction(centroid, sim.getPoint(numOfPoints - 1));
				double fContractedPoint = Himmelblau(contractedPoint);
				double fWorstPoint = Himmelblau(sim.getPoint(numOfPoints - 1));
				if (fContractedPoint < fWorstPoint) {
					sim.setPoint(numOfPoints - 1, contractedPoint);
					terminated = isTerminated(sim, threshold);
				}
				else {
					shrink(sim,  sim.getPoint(numOfPoints - 1), numOfPoints - 1);
				}
			}
			
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