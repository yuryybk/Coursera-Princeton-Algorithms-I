public class DoubleComparison {

    public static void main(String[] args) {
        double a1 = 0.0;
        double b1 = -0.0;
        Double x1 = 0.0;
        Double y1 = -0.0;
        if (a1 == b1 && !x1.equals(y1)) {
            System.out.println("Different Double objects for the same values");
        }

        double a2 = Double.NaN;
        double b2 = Double.NaN;
        Double x2 = Double.NaN;
        Double y2 = Double.NaN;
        if (a2 != b2 && x2.equals(y2)) {
            System.out.println("Different double value-type values for the same values");
        }
    }
}
