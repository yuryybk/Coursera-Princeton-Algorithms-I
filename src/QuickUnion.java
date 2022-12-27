import java.util.Arrays;

public class QuickUnion {

    private int[] roots;
    private int[] treeSize;
    private int[] maxNums;

    QuickUnion(int n) {
        roots = new int[n];
        treeSize = new int[n];
        maxNums = new int[n];
        Arrays.setAll(maxNums, val -> val);
    }

    public void union(int idx1, int idx2) {
        int root1 = findRoot(idx1);
        int root2 = findRoot(idx2);
        if (treeSize[root1] > treeSize[root2]) {
            roots[root2] = root1;
            treeSize[root1] += treeSize[root2];
        } else {
            roots[root1] = root2;
            treeSize[root2] += treeSize[root1];
        }

        if (maxNums[root1] > maxNums[root2]) {
            maxNums[root2] = maxNums[root1];
        } else {
            maxNums[root1] = maxNums[root2];
        }
    }

    private boolean isConnected(int idx1, int idx2) {
        int root1 = findRoot(idx1);
        int root2 = findRoot(idx2);
        return root1 == root2;
    }

    private int findRoot(int idx) {
        int root = idx;
        while (roots[root] != root) {
            root = roots[root];
        }
        return root;
    }

}
