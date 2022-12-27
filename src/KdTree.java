import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private static class KdTreeNode {

        RectHV rect;
        Point2D point;
        KdTreeNode left;
        KdTreeNode right;

        KdTreeNode(Point2D point) {
            this.point = point;
        }
    }

    private KdTreeNode root = null;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        if (!contains(p)) {
            if (root == null) {
                root = new KdTreeNode(p);
                root.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
                size++;
            } else {
                traverseInsert(root, p, true);
            }
        }
    }

    private void traverseInsert(KdTreeNode currentNode, Point2D point, boolean verticalSplit) {
        if (currentNode != null) {
            if (compareKdPoints(currentNode.point, point, verticalSplit) == 1) {
                if (currentNode.left == null) {
                    currentNode.left = new KdTreeNode(point);
                    currentNode.left.rect = divide(currentNode.rect, currentNode.point, true, verticalSplit);
                    size++;
                } else {
                    traverseInsert(currentNode.left, point, !verticalSplit);
                }
            } else {
                if (currentNode.right == null) {
                    currentNode.right = new KdTreeNode(point);
                    currentNode.right.rect = divide(currentNode.rect, currentNode.point, false, verticalSplit);
                    size++;
                } else {
                    traverseInsert(currentNode.right, point, !verticalSplit);
                }
            }
        }
    }

    private RectHV divide(RectHV rect, Point2D point, boolean isLeftNode, boolean isVerticalSplit) {
        if (isVerticalSplit) {
            if (isLeftNode) return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            else return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            if (isLeftNode) return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
            else return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return searchKdTreeNode(root, p, true) != null;
    }

    private KdTreeNode searchKdTreeNode(KdTreeNode currentNode, Point2D point, boolean verticalSplit) {
        if (currentNode == null) {
            return null;
        }

        if (currentNode.point.equals(point)) {
            return currentNode;
        }

        if (compareKdPoints(currentNode.point, point, verticalSplit) == 1) {
            return searchKdTreeNode(currentNode.left, point, !verticalSplit);
        } else {
            return searchKdTreeNode(currentNode.right, point, !verticalSplit);
        }
    }

    private int compareKdPoints(Point2D point1, Point2D point2, boolean verticalSplit) {
        if (verticalSplit) {
            return Double.compare(point1.x(), point2.x());
        } else {
            return Double.compare(point1.y(), point2.y());
        }
    }

    // draw all points to standard draw
    public void draw() {
        drawTraverse(root, true);
    }

    private void drawTraverse(KdTreeNode current, boolean verticalSplit) {

        if (current == null) {
            return;
        }

        if (verticalSplit) {
            drawLine(current.point.x(),  current.rect.ymin(), current.point.x(), current.rect.ymax());
        } else {
            drawLine(current.rect.xmin(), current.point.y(), current.rect.xmax(), current.point.y());
        }

        drawPoint(current.point);
        drawTraverse(current.left, !verticalSplit);
        drawTraverse(current.right, !verticalSplit);
    }

    private void drawPoint(Point2D point) {
        // Use only for testing as it doesn't pass automatic tests
        // StdDraw.setPenRadius(0.02);
        // StdDraw.setPenColor(Color.BLACK);

        StdDraw.point(point.x(), point.y());
    }

    private void drawLine(double x1, double y1, double x2, double y2) {
        /*
        Use only for testing as it doesn't pass automatic tests
        if (verticalSplit) {
            StdDraw.setPenColor(Color.RED);
        } else {
            StdDraw.setPenColor(Color.BLUE);
        }
        StdDraw.setPenRadius(0.01);
        */
        StdDraw.line(x1, y1, x2, y2);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        List<Point2D> rangePoints = new ArrayList<>();
        rangeTraverse(root, rect, rangePoints, true);
        return rangePoints;
    }

    private void rangeTraverse(KdTreeNode currentNode, RectHV rect, List<Point2D> points, boolean isVerticalSplit) {
        if (currentNode == null) {
            return;
        }
        if (rect.contains(currentNode.point)) {
            points.add(currentNode.point);
        }
        if (isVerticalSplit) {
            double x = currentNode.point.x();
            if (x < rect.xmin()) {
                rangeTraverse(currentNode.right, rect, points, false);
            } else if (x > rect.xmax()) {
                rangeTraverse(currentNode.left, rect, points, false);
            } else {
                rangeTraverse(currentNode.left, rect, points, false);
                rangeTraverse(currentNode.right, rect, points, false);
            }
        } else {
            double y = currentNode.point.y();
            if (y < rect.ymin()) {
                rangeTraverse(currentNode.right, rect, points, true);
            } else if (y > rect.ymax()) {
                rangeTraverse(currentNode.left, rect, points, true);
            } else {
                rangeTraverse(currentNode.left, rect, points, true);
                rangeTraverse(currentNode.right, rect, points, true);
            }
        }

    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        return nearestTraverse(root, p, root.point, true);
    }

    private Point2D nearestTraverse(
            KdTreeNode node,
            Point2D p,
            Point2D nearest,
            boolean isVerticalSplit) {

        if (node == null) {
            return nearest;
        }

        Point2D nodePoint = node.point;
        if (p.distanceSquaredTo(nodePoint) < p.distanceSquaredTo(nearest)) {
            nearest = nodePoint;
        }

        if (compareKdPoints(p, nodePoint, isVerticalSplit) == -1) {
            nearest = nearestTraverse(node.left, p, nearest, !isVerticalSplit);
            if (node.right != null && nearest.distanceSquaredTo(p) > node.right.rect.distanceSquaredTo(p)) {
                nearest = nearestTraverse(node.right, p, nearest, !isVerticalSplit);
            }
        } else {
            nearest = nearestTraverse(node.right, p, nearest, !isVerticalSplit);
            if (node.left != null && nearest.distanceSquaredTo(p) > node.left.rect.distanceSquaredTo(p)) {
                nearest = nearestTraverse(node.left, p, nearest, !isVerticalSplit);
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        // unit testing of the methods (optional)
    }
}
