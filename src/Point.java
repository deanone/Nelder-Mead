import java.util.ArrayList;
import java.util.Random;

public class Point
{
	public ArrayList<Double> vals;

	public Point()
	{
		vals = new ArrayList<Double>();
	}
	
	public Point(int n, double rangeMin, double rangeMax)
	{
		vals = new ArrayList<Double>();
		for (int i = 0; i < n; i++)
		{
			Random r = new Random();
			double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
			vals.add(randomValue);
		}
	}
	
	public void addVal(double val)
	{
		vals.add(val);
	}
	
	public int getNumOfValues()
	{
		return vals.size();
	}
	
	public ArrayList<Double> getValues()
	{
		return vals;
	}
	
	public double getValue(int i)
	{
		return vals.get(i);
	}
}
