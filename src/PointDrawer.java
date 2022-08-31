import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JPanel;

public class PointDrawer extends JPanel {
	ArrayList<Point> points;
	
	public PointDrawer(ArrayList<Point> points) {
		this.points = new ArrayList<Point>();
		this.points = points;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2dObject = (Graphics2D) g;		
		for (int i = 0; i < points.size(); i++) {
			ArrayList<Double> vals = points.get(i).getValues();
			double x = vals.get(0) * 100;
			double y = vals.get(1) * 100;			
			Rectangle2D.Double rect = new Rectangle2D.Double(x, y, 5, 5);
			g2dObject.setColor(new Color(255, 0, 0));
			g2dObject.draw(rect);
			g2dObject.fill(rect);
		}
	}
}