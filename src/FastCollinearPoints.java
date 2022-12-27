import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {

    private final LinkedList<LineSegment> lineSegments = new LinkedList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new IllegalArgumentException();
        for (Point point : points) {
            if (point == null)
                throw new IllegalArgumentException();
        }
        Point[] sorted = points.clone();
        Arrays.sort(sorted);
        for (int i = 0; i < sorted.length - 1; i++) {
            if (sorted[i].equals(sorted[i + 1]))
                throw new IllegalArgumentException();
        }
        for (int i = 0; i < sorted.length - 1; i++) {
            Point p = sorted[i];
            Point[] slopePoints = sorted.clone();
            Arrays.sort(slopePoints, p.slopeOrder());
            int pointSegmentCount = 2;
            double skipSlope = -0.0;
            for (int j = 0; j < slopePoints.length; j++) {

                // Skipping itself
                if (p.compareTo(slopePoints[j]) == 0) {
                    continue;
                }

                // Relying on stability of the merge sort:
                // - if p less than slopePoint[j] than p and slopePoints[j] already included in found segment
                // - if p less than slopePoint[j] than it should be at the beginning across the points with the same slope
                // as points were sorted by coordinates and only then sorted by slope (merge sort stability)
                if (p.compareTo(slopePoints[j]) > 0) {
                    skipSlope = p.slopeTo(slopePoints[j]);
                    continue;
                }

                if (j + 1 < slopePoints.length
                        && Double.compare(p.slopeTo(slopePoints[j]), p.slopeTo(slopePoints[j + 1])) == 0) {

                    // If slope was registered for skipping - then skipping it
                    if (Double.compare(p.slopeTo(slopePoints[j]), skipSlope) != 0) {
                        pointSegmentCount++;
                        skipSlope = -0.0;
                    }
                } else {
                    if (pointSegmentCount >= 4) {
                        LineSegment lineSegment = new LineSegment(p, slopePoints[j]);
                        lineSegments.add(lineSegment);
                    }
                    pointSegmentCount = 2;
                }
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[lineSegments.size()];
        lineSegments.toArray(segments);
        return segments;
    }
}
