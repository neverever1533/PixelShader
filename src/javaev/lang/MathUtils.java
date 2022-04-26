package javaev.lang;

import java.awt.Point;

public class MathUtils {
	public static double getRadius(double width, double height) {
		double radians = Math.atan2(width, height);
		return width / Math.sin(radians) / 2;// w/2r=sin<a
	}

	public static Point rotate(double theta, double x, double y) {
		double sint = Math.sin(theta);
		double cost = Math.cos(theta);
		double xp = x * cost - y * sint;
		double yp = x * sint + y * sint;
		Point point = new Point();
		point.setLocation(xp, yp);
		return point;
	}

	public static Point rotate(double theta, Point point) {
		if (null != point) {
			return rotate(theta, point.getX(), point.getY());
		}
		return null;
	}
}
