import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final WeightedQuickUnionUF wquf;

    // Using to address backwash issue with the wrong isFull() check
    private final WeightedQuickUnionUF wqufBackwash;

    private final boolean[][] grid;
    private int numberOfOpenSites;
    private final int n;
    private final int lastVirtualIdx;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        wquf = new WeightedQuickUnionUF(n*n + 2);
        wqufBackwash = new WeightedQuickUnionUF(n*n + 1);
        grid = new boolean[n][n];

        // 0 is for top common virtual site, last is for bottom
        lastVirtualIdx = n*n + 1;
        this.n = n;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
        int gridRow = gridIdx(row);
        int gridCol = gridIdx(col);
        if (!grid[gridRow][gridCol]) {
            numberOfOpenSites++;
            grid[gridRow][gridCol] = true;
            connectWithUp(gridRow, gridCol);
            connectWithLeft(gridRow, gridCol);
            connectWithRight(gridRow, gridCol);
            connectWithBottom(gridRow, gridCol);
        }
    }

    private void connectWithUp(int gridRow, int gridCol) {
        if (gridRow - 1 < 0) {
            wquf.union(0, uIdx(gridRow, gridCol));
            wqufBackwash.union(0, uIdx(gridRow, gridCol));
        } else if (grid[gridRow - 1][gridCol]) {
            wquf.union(uIdx(gridRow, gridCol), uIdx(gridRow - 1, gridCol));
            wqufBackwash.union(uIdx(gridRow, gridCol), uIdx(gridRow - 1, gridCol));
        }
    }

    private void connectWithLeft(int gridRow, int gridCol) {
        if (gridCol - 1 >= 0 && grid[gridRow][gridCol -1]) {
            wquf.union(uIdx(gridRow, gridCol), uIdx(gridRow, gridCol -1));
            wqufBackwash.union(uIdx(gridRow, gridCol), uIdx(gridRow, gridCol -1));
        }
    }

    private void connectWithRight(int gridRow, int gridCol) {
        if (gridCol + 1 < n && (grid[gridRow][gridCol + 1])) {
            wquf.union(uIdx(gridRow, gridCol), uIdx(gridRow, gridCol + 1));
            wqufBackwash.union(uIdx(gridRow, gridCol), uIdx(gridRow, gridCol + 1));
        }
    }

    private void connectWithBottom(int gridRow, int gridCol) {
        if (gridRow + 1 >= n) {
            wquf.union(lastVirtualIdx, uIdx(gridRow, gridCol));
        } else if (grid[gridRow + 1][gridCol]) {
            wquf.union(uIdx(gridRow, gridCol), uIdx(gridRow + 1, gridCol));
            wqufBackwash.union(uIdx(gridRow, gridCol), uIdx(gridRow + 1, gridCol));
        }
    }

    // Get union index
    private int uIdx(int gridRow, int gridCol) {
        return gridRow * n + gridCol + 1;
    }

    // Get grid index
    private int gridIdx(int idx) {
        return idx - 1;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
        return grid[gridIdx(row)][gridIdx(col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
        return wqufBackwash.find(0) == wqufBackwash.find(uIdx(gridIdx(row), gridIdx(col)));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return wquf.find(0) == wquf.find(lastVirtualIdx);
    }
}
