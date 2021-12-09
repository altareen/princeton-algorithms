/**
|-------------------------------------------------------------------------------
| Solver.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Nov 09, 2021
| Compilation:  javac-algs4 Solver.java
| Execution:    java-algs4 Solver puzzle04.txt
|
| This program implements an A* search to solve the Eight Tile Sliding Puzzle.
|
*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;
import java.util.ArrayDeque;
import java.util.Deque;

public class Solver
{
    // instance variables
    private Board initial;
    private MinPQ<SearchNode> queue;
    private SearchNode goalNode;
    private boolean outcome;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        // check for corner cases
        if (initial == null)
            throw new IllegalArgumentException("argument to Solver constructor is null");
        
        this.initial = initial;
        queue = new MinPQ<SearchNode>(ranker());
        queue.insert(new SearchNode(this.initial, 0, null));
        SearchNode removed = queue.delMin();
        
        outcome = true; // twin
        Board commence = initial.twin(); // twin
        MinPQ<SearchNode> parallel = new MinPQ<SearchNode>(ranker()); // twin
        parallel.insert(new SearchNode(commence, 0, null)); // twin
        SearchNode deleted = parallel.delMin(); // twin        
        
        while (!removed.getBoard().isGoal() && !deleted.getBoard().isGoal())
        {
            Iterable<Board> elements = removed.getBoard().neighbors();
            for (Board board: elements)
            {
                SearchNode grandparent = removed.getPrevious();
                if (grandparent == null || !grandparent.getBoard().equals(board))
                    queue.insert(new SearchNode(board, removed.getMoves()+1, removed));
            }
            removed = queue.delMin();
            
            // twin
            Iterable<Board> items = deleted.getBoard().neighbors();
            for (Board board: items)
            {
                SearchNode grandparent = deleted.getPrevious();
                if (grandparent == null || !grandparent.getBoard().equals(board))
                    parallel.insert(new SearchNode(board, deleted.getMoves()+1, deleted));
            }
            deleted = parallel.delMin();
            
            if (deleted.getBoard().isGoal())
                outcome = false;
        }
        goalNode = removed;
    }
    
    private Comparator<SearchNode> ranker()
    {
        return new Ranker();
    }
    
    private class Ranker implements Comparator<SearchNode>
    {
        public int compare(SearchNode one, SearchNode two)
        {
            if (one.getPriority() < two.getPriority())
                return -1;
            else if (one.getPriority() > two.getPriority())
                return 1;
            else
                return 0;
        }
    }
    
    private class SearchNode
    {
        // instance variables
        private Board board;
        private int moves;
        private int priority;
        private SearchNode previous;
        
        public SearchNode(Board board, int moves, SearchNode previous)
        {
            this.board = board;
            this.moves = moves;
            this.priority = board.manhattan() + moves;
            this.previous = previous;
        }
        
        public Board getBoard()
        {
            return board;
        }
        
        public int getMoves()
        {
            return moves;
        }
        
        public int getPriority()
        {
            return priority;
        }
        
        public SearchNode getPrevious()
        {
            return previous;
        }
    }
    
    // is the initial board solvable? (see below)
    public boolean isSolvable()
    {
        return outcome;
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()
    {
        if (!isSolvable())
            return -1;
        return goalNode.getMoves();
    }
    
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()
    {
        if (!isSolvable())
            return null;
        
        Deque<Board> sequence = new ArrayDeque<Board>();
        SearchNode current = goalNode;
        sequence.addFirst(current.getBoard());
        while (!initial.equals(current.getBoard()))
        {
            current = current.getPrevious();
            sequence.addFirst(current.getBoard());
        }
        return sequence;
    }
    
    // test client (see below) 
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

