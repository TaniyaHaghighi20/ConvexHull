package models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class QuickHull {
	// --------------attributes---------------
	private ArrayList<Point> externalPoints;
	// --------------constructor---------------

	public QuickHull() {
		super();
		externalPoints = new ArrayList<>();
	}
	// --------------methods---------------

	public void performQuickHull(ArrayList<Point> points) {
		externalPoints.clear();
		ArrayList<Point> minMax = findMaxMinX(points);
		Point minX = minMax.get(0);
		Point maxX = minMax.get(1);

		ArrayList<Point> down = new ArrayList<>();
		ArrayList<Point> up = new ArrayList<>();
		for (Point point : points) {
			if (determinant(minX, maxX, point) > 0) {// is right turn
				down.add(point);
			} else if (determinant(minX, maxX, point) < 0) {
				up.add(point);
			}
		}
		QuickHullRecursion(up, minX, maxX, true);
		QuickHullRecursion(down, minX, maxX, false);
		// sort by angle in the end
		externalPoints.add(minX);
		externalPoints.add(maxX);
		externalPoints = sortExternalPointsAccordingToDegree(externalPoints);
	}

	private void QuickHullRecursion(ArrayList<Point> points, Point minX, Point maxX, boolean isUp) {
		if (points.size() == 0) {
			return;
		}
		if (points.size() == 1) {
			externalPoints.add(points.get(0));
			return;
		}
		float area = areaOfTriangle(minX, maxX, points.get(0));
		Point biggest = points.get(0);
		for (Point point : points) {
			float tempArea = areaOfTriangle(minX, maxX, point);
			if (tempArea > area) {
				biggest = point;
				area = tempArea;
			}
		}
		externalPoints.add(biggest);
		ArrayList<Point> left = new ArrayList<>();
		ArrayList<Point> right = new ArrayList<>();
		for (Point point : points) {
			if (isUp) {
				if (determinant(minX, biggest, point) < 0) {
					left.add(point);
				}
				if (determinant(maxX, biggest, point) > 0) {
					right.add(point);
				}
			} else {
				if (determinant(biggest, minX, point) < 0) {
					left.add(point);
				}
				if (determinant(biggest, maxX, point) > 0) {
					right.add(point);
				}
			}

		}
		QuickHullRecursion(right, biggest, maxX, isUp);
		QuickHullRecursion(left, minX, biggest, isUp);

	}

	private float areaOfTriangle(Point A, Point B, Point C) {
		float area = (A.x * (B.y - C.y) + B.x * (C.y - A.y) + C.x * (A.y - B.y)) / 2.0f;
		return Math.abs(area);

	}

	private ArrayList<Point> findMaxMinX(ArrayList<Point> points) {
		ArrayList<Point> maxMin = new ArrayList<>();
		Point minX = points.get(0);
		Point MaxX = points.get(0);
		for (Point point : points) {
			if (point.getX() < minX.getX()) {
				minX = point;
			} else if (point.getX() > MaxX.getX()) {
				MaxX = point;
			} else if (point.getX() == minX.getX()) {
				if (point.getY() > minX.getY()) {
					minX = point;
				}
			} else if (point.getX() == MaxX.getX()) {
				if (point.getY() < MaxX.getY()) {
					MaxX = point;
				}
			}
		}
		maxMin.add(minX);
		maxMin.add(MaxX);
		return maxMin;
	}

	/**
	 * This method calculates determinant if return>0 its a right turn
	 *
	 * @param p1 first point of triangle
	 * @param p2 second point of triangle
	 * @param p  point that we want to check is inside the triangle or not
	 * @return the determinant
	 */
	private int determinant(Point p1, Point p2, Point p) {
		return ((p2.x - p1.x) * (p.y - p1.y)) - ((p2.y - p1.y) * ((p.x) - p1.x));
	}

	/**
	 * This method sort the external points according to their angle of the line
	 * that they make with the X axis ('line' means the line between the desired
	 * point and a point with the lowest Y) uses "finedExternalNodeForBlindSearch()"
	 *
	 * @return sorted list of external Points
	 */
	private ArrayList<Point> sortExternalPointsAccordingToDegree(ArrayList<Point> points) {
		ArrayList<Point> sortedPoints = sortPointsAccordingToY(points);
		Point pointMinY = sortedPoints.get(0);
		Collections.sort(sortedPoints, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				double y1 = o1.y - pointMinY.y;
				double y2 = o2.y - pointMinY.y;
				double x1 = o1.x - pointMinY.x;
				double x2 = o2.x - pointMinY.x;
				double polar1 = Math.atan2(y1, x1);
				double polar2 = Math.atan2(y2, x2);
				if (polar1 < polar2) {
					return 1;
				} else if (polar1 > polar2) {
					return -1;
				} else if (o1.y > 0) {
					if (o1.y > o2.y) {
						return 1;
					} else if (o1.y < o2.y) {
						return -1;
					}
				} else if (o1.y < 0) {
					if (o1.y < o2.y) {
						return 1;
					} else if (o1.y > o2.y) {
						return -1;
					}
				}
				return 0;
			}
		});
		return points;
	}

	/**
	 * This method sort the points list according to their Y from max Y to min Y
	 */
	private ArrayList<Point> sortPointsAccordingToY(ArrayList<Point> points) {
		Collections.sort(points, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				if (o1.y < o2.y) {
					return 1;
				} else if (o1.y > o2.y) {
					return -1;
				} else if (o1.x > o2.x) {
					return 1;
				} else if (o1.x < o2.x) {
					return -1;
				}
				return 0;
			}
		});
		return points;
	}

	// --------------getter&setters---------------
	public ArrayList<Point> getExternalPoints() {
		return externalPoints;
	}

	public void setExternalPoints(ArrayList<Point> externalPoints) {
		this.externalPoints = externalPoints;
	}

}
