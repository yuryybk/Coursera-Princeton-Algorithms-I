import java.util.ArrayList;
import java.util.Arrays;

public class Board {

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)

    private final int[][] tiles;

    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        this.tiles = copyTiles(tiles);
    }

    // string representation of this board
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(tiles.length);
        builder.append("\n");
        for (int[] tile : tiles) {
            for (int j = 0; j < tiles.length; j++) {
                builder.append(" ");
                builder.append(tile[j]);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int hamming = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (!(i == dimension() - 1 && j == dimension() - 1)
                        && tiles[i][j] != i * tiles.length + j + 1) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                int val = tiles[i][j];
                if (val != 0) {
                    int valRow = (val - 1) / tiles.length;
                    int valCol = (val - 1) - valRow * tiles.length;
                    manhattan += Math.abs(i - valRow) + Math.abs(j - valCol);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (!(y.getClass() == this.getClass())) {
           return false;
        }
        return Arrays.deepEquals(tiles, ((Board) y).tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbours = new ArrayList<>();
        int[] zeroPos = findZero();
        int zeroRow = zeroPos[0];
        int zeroCol = zeroPos[1];

        if (zeroRow - 1 >= 0) {
            Board newBoard = copy();
            newBoard.swap(zeroRow, zeroCol, zeroRow - 1, zeroCol);
            neighbours.add(newBoard);
        }
        if (zeroCol + 1 < tiles.length) {
            Board newBoard = copy();
            newBoard.swap(zeroRow, zeroCol, zeroRow, zeroCol + 1);
            neighbours.add(newBoard);
        }
        if (zeroRow + 1 < tiles.length) {
            Board newBoard = copy();
            newBoard.swap(zeroRow, zeroCol, zeroRow + 1, zeroCol);
            neighbours.add(newBoard);
        }
        if (zeroCol - 1 >= 0) {
            Board newBoard = copy();
            newBoard.swap(zeroRow, zeroCol, zeroRow, zeroCol - 1);
            neighbours.add(newBoard);
        }
        return neighbours;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twinBoard = copy();
        if (tiles[0][0] != 0 && tiles[0][1] != 0) {
            twinBoard.swap(0, 0, 0, 1);
        } else {
            twinBoard.swap(1, 0, 1, 1);
        }
        return twinBoard;
    }

    private int[][] copyTiles(int[][] srcTiles) {
        int[][] copyTiles = new int[srcTiles.length][srcTiles.length];
        for (int i = 0; i < srcTiles.length; i++) {
            copyTiles[i] = Arrays.copyOf(srcTiles[i], srcTiles[i].length);
        }
        return copyTiles;
    }

    private Board copy() {
        return new Board(tiles);
    }

    private void swap(int row1, int col1, int row2, int col2) {
        int temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
    }

    private int[] findZero() {
        int[] pos = new int[2];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0) {
                    pos[0] = i;
                    pos[1] = j;
                }
            }
        }
        return pos;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // Temporary empty
    }

}