package models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GrahamScan {
	// ----------------Attributes-----------------
	private MyLinkedList pointsLists;
	// ----------------methods--------------------

	private MyLinkedList ConvertToLinkedList(ArrayList<Point> points) {
		for (Point point : points) {
			pointsLists.insert((int) point.getX(), (int) point.getY());
		}
		return pointsLists;
	}

	public void PerformGrahamScan(ArrayList<Point> points) {
		pointsLists = new MyLinkedList();
		// sort by angle
		ArrayList<Point> sortedPoints = sortExternalPointsAccordingToDegree(points);
		// convert arrayList to LinkedList
		ConvertToLinkedList(sortedPoints);
		// perform graham scan
		MyLinkedList.Node curr = pointsLists.head;
		while (!curr.next.equals(pointsLists.head)) {
			Point p1 = new Point(curr.getKey(), curr.getData());
			Point p2 = new Point(curr.next.getKey(), curr.next.getData());
			Point p3 = new Point(curr.next.next.getKey(), curr.next.next.getData());
			if (determinant(p1, p2, p3) > 0) {
				curr.next.next.previous = curr;
				curr.next = curr.next.next;

				if (curr != pointsLists.head) {
					curr = curr.previous;
				}
			} else {
				curr = curr.next;
			}
		}
	}

	/**
	 * This method calculates determinant
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

	public MyLinkedList getPointsLists() {
		return pointsLists;
	}

	public void setPointsLists(MyLinkedList pointsLists) {
		this.pointsLists = pointsLists;
	}

}
