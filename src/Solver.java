import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private static class SearchNode implements Comparable<SearchNode> {

        private final int moves;
        private final SearchNode previousNode;
        private final Board board;
        private final int manhattan;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.moves = moves;
            this.board = board;
            this.previousNode = previous;
            this.manhattan = board.manhattan();
        }

        private int priority() {
            return manhattan + moves;
        }

        @Override
        public int compareTo(SearchNode other) {
            int priorityThis = priority();
            int priorityOther = other.priority();
            return Integer.compare(priorityThis, priorityOther);
        }
    }

    private int moves = -1;
    private boolean isSolvable = false;
    private SearchNode solutionNode = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<SearchNode> goalPq = new MinPQ<>();
        goalPq.insert(new SearchNode(initial, 0, null));

        // Using twin priority queue to check if solution could be found after swapping non-"0" tiles
        // It should confirm that board is not solvable
        MinPQ<SearchNode> twinPq = new MinPQ<>();
        twinPq.insert(new SearchNode(initial.twin(), 0, null));

        while (!goalPq.min().board.isGoal() && !twinPq.min().board.isGoal()) {
            processNeighbours(goalPq);
            processNeighbours(twinPq);
        }

        SearchNode goalNode = goalPq.min();
        if (goalNode.board.isGoal()) {
            moves = goalNode.moves;
            isSolvable = true;
            solutionNode = goalNode;
        }
    }

    private void processNeighbours(MinPQ<SearchNode> pq) {
        SearchNode currNode = pq.delMin();
        for (Board neighbor : currNode.board.neighbors()) {
            if (currNode.previousNode != null && neighbor.equals(currNode.previousNode.board)) {
                continue;
            }
            SearchNode newNode = new SearchNode(neighbor, currNode.moves + 1, currNode);
            pq.insert(newNode);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()  {
        if (!isSolvable) {
            return null;
        }
        Stack<Board> solution = new Stack<>();
        SearchNode goalNode = solutionNode;
        while (goalNode != null) {
            solution.push(goalNode.board);
            goalNode = goalNode.previousNode;
        }
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {
        // Temporary empty
    }
}
