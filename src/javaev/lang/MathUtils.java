package javaev.lang;

import java.awt.Point;

public class MathUtils {
	public static double getRadius(double width, double height) {
		double radians = Math.atan2(width, height);
		return width / Math.sin(radians) / 2;// w/2r=sin<a
	}

	public static Point rotate(double theta, double x, double y) {
		return rotate(theta, 0, 0, x, y);
	}

	public static Point rotate(double theta, double xc, double yc, double x, double y) {
		double sint = Math.sin(theta);
		double cost = Math.cos(theta);
		double xr = (x - xc) * cost - (y - yc) * sint + xc;
		double yr = (x - xc) * sint + (y - yc) * sint + yc;
		Point point = new Point();
		point.setLocation(xr, yr);
		return point;
	}

	public static Point rotate(double theta, Point point) {
		if (null != point) {
			return rotate(theta, point.getX(), point.getY());
		}
		return null;
	}

	public static Point rotate(double theta, Point pointAnchor, Point point) {
		if (null != point) {
			return rotate(theta, pointAnchor.getX(), pointAnchor.getY(), point.getX(), point.getY());
		}
		return null;
	}
}
