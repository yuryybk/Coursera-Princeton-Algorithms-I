import java.util.Arrays;

public class BruteCollinearPoints {

    private final LineSegment[] lineSegments;
    private int numberOfSegments = 0;

    public BruteCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();
        for (Point point : points) {
            if (point == null)
                throw new IllegalArgumentException();
        }
        Point[] sortedPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(sortedPoints);
        for (int i = 0; i < sortedPoints.length - 1; i++) {
            if (sortedPoints[i].equals(sortedPoints[i + 1]))
                throw new IllegalArgumentException();
        }
        lineSegments = new LineSegment[points.length];
        for (int i1 = 0; i1 < sortedPoints.length; i1++) {
            for (int i2 = i1 + 1; i2 < sortedPoints.length; i2++) {
                for (int i3 = i2 + 1; i3 < sortedPoints.length; i3++) {
                    for (int i4 = i3 + 1; i4 < sortedPoints.length; i4++) {
                        Point p1 = sortedPoints[i1];
                        Point p2 = sortedPoints[i2];
                        Point p3 = sortedPoints[i3];
                        Point p4 = sortedPoints[i4];
                        if (p1.slopeTo(p2) == p2.slopeTo(p3)
                                && p2.slopeTo(p3) == p3.slopeTo(p4)) {
                            LineSegment lineSegment = new LineSegment(p1, p4);
                            lineSegments[numberOfSegments] = lineSegment;
                            numberOfSegments++;
                        }
                    }
                }
            }
        }
    }

    // finds all line segments containing 4 points
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the number of line segments
    public LineSegment[] segments() {
        return Arrays.copyOfRange(lineSegments, 0, numberOfSegments);
    }
}
