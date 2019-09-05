import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

public class NMmethod
{
	public static double Himmelblau(double x, double y)
	{
		return Math.pow(Math.pow(x, 2) + y - 11, 2) + Math.pow(x + Math.pow(y, 2) - 7, 2);
	}
	
	public static double Himmelblau(Point p)
	{
		int n = p.getNumOfValues();
		if (n != 2)
		{
			return -1.0;
		}
		else
		{
			return Math.pow(Math.pow(p.getValue(0), 2) + p.getValue(1) - 11, 2) + Math.pow(p.getValue(0) + Math.pow(p.getValue(1), 2) - 7, 2);
		}
	}

	public static void Order(Simplex sim)
	{
		Map<Double, Point> m = new TreeMap<Double, Point>();
		int numOfPoints = sim.getNumOfPoints();
		for (int i = 0; i < numOfPoints; i++)
		{
			Point p = sim.getPoint(i);
			Double fval = Himmelblau(p);
			m.put(fval, p);
		}
		
		int i = 0;
		for (Entry<Double, Point> entry : m.entrySet())
		{
			Point p = entry.getValue();
			sim.setPoint(i, p);
			i++;
		}
	}
	
	public static double mean(ArrayList<Double> vals)
	{
		double m = 0.0;
		for (int i = 0; i < vals.size(); i++)
		{
			m += vals.get(i);
		}
		m /= vals.size();
		return m;
	}
	
	public static double std(ArrayList<Double> vals)
	{
		double s = 0.0;
		double m = mean(vals);
		for (int i = 0; i < vals.size(); i++)
		{
			s += Math.pow(vals.get(i) - m, 2);
		}
		s /= (double)(vals.size() - 1);
		s = Math.sqrt(s);
		return s;
	}
	
	public static Point add(Point p1, Point p2)
	{
		int n1 = p1.getNumOfValues();
		int n2 = p2.getNumOfValues();
		Point sum = new Point();
		if (n1 == n2)
		{
			for (int i = 0; i < n1; i++)
			{
				double val = p1.getValue(i) + p2.getValue(i);
				sum.addVal(val);
			}
		}
		return sum;
	}
	
	public static Point subtract(Point p1, Point p2)
	{
		int n1 = p1.getNumOfValues();
		int n2 = p2.getNumOfValues();
		Point diff = new Point();
		if (n1 == n2)
		{
			for (int i = 0; i < n1; i++)
			{
				double val = p1.getValue(i) - p2.getValue(i);
				diff.addVal(val);
			}
		}
		return diff;
	}
	
	public static Point multiplyConstantByPoint(double c, Point p)
	{
		int n = p.getNumOfValues();
		Point product = new Point();
		for (int i = 0; i < n; i++)
		{
			double val = c * p.getValue(i);
			product.addVal(val);
		}
		return product;
	}
	
	public static Point calcCentroid(Simplex sim)
	{
		int numOfPoints = sim.getNumOfPoints();
		int n = sim.getN();
		Point centroid  = new Point();
		for (int i = 0; i < n ; i++)
		{
			double val = 0.0;
			for (int j = 0; j < numOfPoints; j++)
			{
				val += sim.getPoint(i).getValue(j);
			}
			val /= numOfPoints;
			centroid.addVal(val);
		}
		return centroid;
	}
	
	public static Point reflection(Point centroid, Point lastPoint)
	{
		double alpha = 1.0;
		Point reflectedPoint = subtract(centroid, lastPoint);
		reflectedPoint = multiplyConstantByPoint(alpha, reflectedPoint);
		reflectedPoint = add(centroid, reflectedPoint);
		return reflectedPoint;
	}
	
	public static Point expansion(Point centroid, Point reflectedPoint)
	{
		double gamma = 2.0;
		Point expandedPoint = subtract(reflectedPoint, centroid);
		expandedPoint = multiplyConstantByPoint(gamma, expandedPoint);
		expandedPoint = add(centroid, expandedPoint);
		return expandedPoint;
	}
	
	public static Point contraction(Point centroid, Point lastPoint)
	{
		double rho = 0.5;
		Point contractedPoint = subtract(lastPoint, centroid);
		contractedPoint = multiplyConstantByPoint(rho, contractedPoint);
		contractedPoint = add(centroid, contractedPoint);
		return contractedPoint;
	}
	
	public static void shrink(Simplex sim, Point bestPoint, int bestPointId)
	{
		double sigma = 0.5; 
		int numOfPoints = sim.getNumOfPoints();
		for (int i = 0; i < numOfPoints; i++)
		{
			if (i != bestPointId)
			{
				Point newPoint = subtract(sim.getPoint(i), bestPoint);
				newPoint = multiplyConstantByPoint(sigma, newPoint);
				newPoint = add(newPoint, bestPoint);
				sim.setPoint(i, newPoint);
			}
		}
	}
	
	public static boolean isTerminated(Simplex sim, double threshold)
	{
		boolean terminated = false;
		
		// Calculate f values for the points of the Simplex
		ArrayList<Double> fvals = new ArrayList<Double>();
		int numOfPoints = sim.getNumOfPoints();
		for (int i = 0; i < numOfPoints; i++)
		{
			double fval = Himmelblau(sim.getPoint(i));
			fvals.add(fval);
		}
		
		// Calculate standard deviation of the f values
		double s = std(fvals);
		if (s < threshold)
		{
			terminated = true;
		}
		return terminated;		
	}
	
	public static void main(String[] args)
	{
		double threshold = 10.0;
		int n = 2;
		int numOfPoints = n + 1;
		double rangeMin = 1.0;
		double rangeMax = 10.0;
		Simplex sim = new Simplex(n, rangeMin, rangeMax);
		
		boolean terminated = isTerminated(sim, threshold);
		while (!terminated)
		{
			Order(sim);
			
			// Calculate centroid
			Point centroid = calcCentroid(sim);
			
			// Calculate reflected point
			Point reflectedPoint = reflection(centroid, sim.getPoint(numOfPoints - 1));
			
			// Check reflected point
			double fBest = Himmelblau(sim.getPoint(0));
			double fReflectedPoint = Himmelblau(reflectedPoint);
			double fSecondWorst = Himmelblau(sim.getPoint(numOfPoints - 2));
			
			if (fReflectedPoint >= fBest && fReflectedPoint <= fSecondWorst)
			{
				sim.setPoint(numOfPoints - 1, reflectedPoint);
				terminated = isTerminated(sim, threshold);
			}
			else if (fReflectedPoint < fBest)
			{
				Point expandedPoint = expansion(centroid, reflectedPoint);
				double fExpandedPoint = Himmelblau(expandedPoint);
				if (fExpandedPoint < fReflectedPoint)
				{
					sim.setPoint(numOfPoints - 1, expandedPoint);
					terminated = isTerminated(sim, threshold);
				}
				else
				{
					sim.setPoint(numOfPoints - 1, reflectedPoint);
					terminated = isTerminated(sim, threshold);
				}
			}
			else
			{
				Point contractedPoint = contraction(centroid, sim.getPoint(numOfPoints - 1));
				double fContractedPoint = Himmelblau(contractedPoint);
				double fWorstPoint = Himmelblau(sim.getPoint(numOfPoints - 1));
				if (fContractedPoint < fWorstPoint)
				{
					sim.setPoint(numOfPoints - 1, contractedPoint);
					terminated = isTerminated(sim, threshold);
				}
			}
			shrink(sim,  sim.getPoint(numOfPoints - 1), numOfPoints - 1);
		}		
	}
}
