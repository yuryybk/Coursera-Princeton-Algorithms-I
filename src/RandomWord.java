import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {

    public static void main(String[] args) {
        double counter = 1;
        String resultWord = null;
        while(!StdIn.isEmpty()) {
            String word = StdIn.readString();
            if (StdRandom.bernoulli(1 / counter)) {
                resultWord = word;
            }
            counter++;
        }
        StdOut.println(resultWord);
    }
}
