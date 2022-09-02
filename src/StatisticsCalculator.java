import java.util.ArrayList;

/**
 * @author A. Salamanis
 * @version 0.1
 * @since 2019-09-05
 *
 * StatisticsCalculator class: The class containing the implementation of several commonly used statistics.
 */
public class StatisticsCalculator {
	
	/**
	 * Computes the mean (average) value of a set of values.
	 * 
	 * @param vals the set of values
	 * @return the mean (average) value
	 */
	public static double mean(ArrayList<Double> vals) {
		double m = 0.0;
		for (int i = 0; i < vals.size(); i++) {
			m += vals.get(i);
		}
		m /= vals.size();
		return m;
	}
	
	/**
	 * Computes the standard deviation value of a set of values.
	 * 
	 * @param vals the set of values
	 * @return the standard deviation value
	 */
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
}
