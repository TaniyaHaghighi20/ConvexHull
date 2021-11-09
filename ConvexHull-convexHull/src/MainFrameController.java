import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import models.GrahamScan;
import models.MyLinkedList;
import models.QuickHull;

public class MainFrameController implements Comparator<Point>, Comparable<Point> {
	ArrayList<Point> points = new ArrayList<>();
	private MainFrame mainFrame = new MainFrame();
	private JPanel drawingPanel = mainFrame.getDrawingPanel();
	private JCheckBox showXY_chk = mainFrame.getChckbxShowXAnd();
	private JRadioButton blindSearch = mainFrame.getBlindSearch_btn();
	private JRadioButton drawLine = mainFrame.getDrawLine();
	// graham scan controller
	private JRadioButton grahamScanRadioButton = mainFrame.getGrahamSearch_btn();
	private GrahamScan grahamScan = new GrahamScan();
	// QuickHull controller
	private JRadioButton QuickHullRadioButton = mainFrame.getQuickHull_btn();
	private QuickHull quickHull = new QuickHull();

	public MainFrameController() {
		mainFrame.setVisible(true);
		putDots();
		showXYChkMouseClicked();
		drawRandomLineBtnMouseClicked();
		blindSearchBtnMouseClicked();
		performGrahamScan();
		performQuickHull();
	}

	private void performQuickHull() {
		QuickHullRadioButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showAllPoints();
				quickHull.performQuickHull(points);
				ArrayList<Point> externalPoints = quickHull.getExternalPoints();
				Graphics g = drawingPanel.getGraphics();
				g.setColor(Color.blue);
				for (int i = 0; i < externalPoints.size() - 1; i++) {
					g.drawLine(externalPoints.get(i).x, externalPoints.get(i).y, externalPoints.get(i + 1).x,
							externalPoints.get(i + 1).y);
				}
				g.drawLine(externalPoints.get(0).x, externalPoints.get(0).y,
						externalPoints.get(externalPoints.size() - 1).x,
						externalPoints.get(externalPoints.size() - 1).y);
			}
		});
	}

	private void performGrahamScan() {
		grahamScanRadioButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showAllPoints();
				grahamScan.PerformGrahamScan(points);
				MyLinkedList myLinkedList = grahamScan.getPointsLists();
				MyLinkedList.Node head = myLinkedList.head;
				if (head != null && head.next != null) {
					Graphics g = drawingPanel.getGraphics();
					g.setColor(Color.green);
					MyLinkedList.Node curr = myLinkedList.head;
					while (!curr.next.equals(myLinkedList.head)) {
						g.drawLine(curr.getKey(), curr.getData(), curr.next.getKey(), curr.next.getData());
						curr = curr.next;
					}
					g.drawLine(head.getKey(), head.getData(), head.previous.getKey(), head.previous.getData());
				}
			}
		});
	}

	/**
	 * This method put dots on the screen by clicking it uses "showingSinglePanel()"
	 * and "drawingPanel.mouseClick()"
	 */
	private void putDots() {
		drawingPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				points.add(arg0.getPoint());
				showSinglePoint(arg0.getPoint());
				blindSearchBtnMouseClicked();
				showXYChkMouseClicked();
			}
		});
	}

	/**
	 * This method shows the coordinate x and y for each dot near it
	 */
	private void showXY() {
		String coordinate;
		Graphics g = drawingPanel.getGraphics();
		for (Point p : points) {
			coordinate = "(" + p.x + "," + p.y + ")";
			g.drawString(coordinate, p.x - 10, p.y - 10);
		}
	}

	/**
	 * check box mouse click listener it uses "showAllPoints()"
	 */
	private void showXYChkMouseClicked() {
		showXY_chk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// if the checkBox was checked
				if (showXY_chk.isSelected()) {
					showXY();
				} else {
					// remove the coordinates from screen
					showAllPoints();
				}
			}
		});
	}

	/**
	 * This method add a single point to the screen and add it to the points list
	 *
	 * @param p a single point
	 */
	private void showSinglePoint(Point p) {
		Graphics g = drawingPanel.getGraphics();
		g.setColor(Color.black);
		g.fillOval(p.x, p.y, 10, 10);
	}

	/**
	 * This method sort the points list according to their X from mon X to max X
	 */
	private void sortPointsAccordingToX() {
		Collections.sort(points, new Comparator<Point>() {
			@Override
			public int compare(Point o1, Point o2) {
				if (o1.x > o2.x) {
					return 1;
				} else if (o1.x < o2.x) {
					return -1;
				}
				return 0;
			}
		});
	}

	/**
	 * This method draw line between points from the min x to the max x
	 */
	private void drawRandomLine() {
		showAllPoints();
		sortPointsAccordingToX();
		Graphics g = drawingPanel.getGraphics();
		g.setColor(Color.red);
		for (int i = 0; i < points.size() - 1; i++) {
			g.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
		}
		g.drawLine(points.get(0).x, points.get(0).y, points.get(points.size() - 1).x, points.get(points.size() - 1).y);
		// showPointsForBlind();

	}

	/**
	 * drawLine mouseClicked
	 */
	private void drawRandomLineBtnMouseClicked() {
		drawLine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				drawRandomLine();
			}
		});
	}

	/**
	 * This method find the external node of points with brute force using
	 * "determinant()"
	 *
	 * @return list of external nodes
	 */
	private ArrayList<Point> findExternalNodeForBlindSearch() {
		ArrayList<Point> externalPoints = new ArrayList<>();
		boolean internal;
		for (Point p : points) {
			internal = false;
			triangleLoop: for (int i = 0; i < points.size(); i++) {
				for (int j = 0; j < points.size(); j++) {
					for (int k = 0; k < points.size(); k++) {
						// calculate det for the first and second nodes of triangle
						int detFirstAndSec = determinant(points.get(i), points.get(j), p);
						// calculate det for the second and third nodes of triangle
						int detSecAndThird = determinant(points.get(j), points.get(k), p);
						// calculate det for the third and first nodes of triangle
						int detThirdAndFirst = determinant(points.get(k), points.get(i), p);
						internal = isInternal(detFirstAndSec, detSecAndThird, detThirdAndFirst);
						// if the point exists at least in a triangle its internal node and the
						// 'triangle for' breaks
						if (internal) {
							break triangleLoop;
						}

					}
				}
			}
			// if all possible triangles checks for a point and the point doesn't exist at
			// least in one of them it is external node
			if (!internal) {
				// add point to external node list
				externalPoints.add(p);
			}
		}
		return externalPoints;

	}

	/**
	 * This method calculates determinant
	 *
	 * @param p1 first point of triangle
	 * @param p2 second point of triangle
	 * @param p  point that we want to check is Ù‡Ù� inside the triangle or not
	 * @return the determinant
	 */
	private int determinant(Point p1, Point p2, Point p) {
		return ((p2.x - p1.x) * (p.y - p1.y)) - ((p2.y - p1.y) * ((p.x) - p1.x));
	}

	/**
	 * This method checks the node is internal or not, if all determinant are less
	 * than 0 then the point is internal and return true
	 *
	 * @param n1 first det
	 * @param n2 second det
	 * @param n3 third det
	 * @return true if it wa internal
	 */
	private boolean isInternal(int n1, int n2, int n3) {
		return (n1 < 0 && n2 < 0 && n3 < 0);
	}

	/**
	 * This method sort the points list according to their Y from max Y to min Y
	 */
	private void sortPointsAccordingToY() {
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
	}

	/**
	 * This method sort the external points according to their angle of the line
	 * that they make with the X axis ('line' means the line between the desired
	 * point and a point with the lowest Y) uses "finedExternalNodeForBlindSearch()"
	 *
	 * @return sorted list of external Points
	 */
	private ArrayList<Point> sortExternalPointsAccordingToDegree() {
		ArrayList<Point> externalPoints = findExternalNodeForBlindSearch();
		sortPointsAccordingToY();
		Point pointMinY = points.get(0);
		Collections.sort(externalPoints, new Comparator<Point>() {
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
		return externalPoints;
	}

	/**
	 * blindSearchBtn MouseClicked()
	 */
	private void blindSearchBtnMouseClicked() {
		blindSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				drawingBlindLines();
			}
		});
	}

	/**
	 * This method show all points when the screen needs to repaint points
	 */
	private void showAllPoints() {
		Graphics g = drawingPanel.getGraphics();
		g.setColor(Color.white);
		g.fillRect(drawingPanel.getX(), drawingPanel.getY(), drawingPanel.getWidth(), drawingPanel.getHeight());
		g.setColor(Color.black);
		for (Point p : points) {
			g.fillOval(p.x, p.y, 10, 10);
		}
	}

	/**
	 * This method draws line between points using
	 * "sortExternalPointsAccordingToDegree()"
	 */
	private void drawingBlindLines() {
		showAllPoints();
		ArrayList<Point> externalPoints = sortExternalPointsAccordingToDegree();
		Graphics g = drawingPanel.getGraphics();
		g.setColor(Color.red);
		for (int i = 0; i < externalPoints.size() - 1; i++) {
			g.drawLine(externalPoints.get(i).x, externalPoints.get(i).y, externalPoints.get(i + 1).x,
					externalPoints.get(i + 1).y);
		}
		g.drawLine(externalPoints.get(0).x, externalPoints.get(0).y, externalPoints.get(externalPoints.size() - 1).x,
				externalPoints.get(externalPoints.size() - 1).y);
	}

	@Override
	public int compare(Point o1, Point o2) {
		if (o1.x > o2.x) {
			return 1;
		} else if (o1.x < o2.x) {
			return -1;
		}
		return 0;
	}

	@Override
	public int compareTo(Point o) {
		return 0;
	}
}
