/**
 * @author A. Salamanis
 * @version 0.1
 * @since 2019-09-05
 *
 * FunctionsForOptimization class: The class containing the implementation of test optimization functions.
 */
public class FunctionsForOptimization {
	public static double Himmelblau(Point p) {
		int n = p.getNumOfValues();
		if (n != 2) {
			return -1.0;
		} else {
			double x = p.getValue(0);
			double y = p.getValue(1);
			return Math.pow(Math.pow(x, 2) + y - 11, 2) + Math.pow(x + Math.pow(y, 2) - 7, 2);
		}
	}
	
	public static double Booth(Point p) {
		int n = p.getNumOfValues();
		if (n != 2) {
			return -1.0;
		} else {
			double x = p.getValue(0);
			double y = p.getValue(1);
			return Math.pow(x + (2 * y) - 7, 2) + Math.pow((2 * x) + y - 5, 2);
		}
	}
}
