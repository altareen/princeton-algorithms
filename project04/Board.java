/**
|-------------------------------------------------------------------------------
| Board.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Nov 09, 2021
| Compilation:  javac-algs4 Board.java
| Execution:    java-algs4 Board puzzle04.txt
|
| This program implements an n-by-n board for the Eight Tile Sliding Puzzle.
|
*/

import edu.princeton.cs.algs4.In;
//import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Stack;

public class Board
{
    // instance variables
    private int n;
    private int[][] tiles;
    
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles)
    {
        n = tiles.length;
        
        // initialize board
        this.tiles = new int[n][n];
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                this.tiles[row][col] = tiles[row][col];
    }
    
    private Board(Board b)
    {
        n = b.tiles.length;
        
        // initialize board
        tiles = new int[n][n];
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                tiles[row][col] = b.tiles[row][col];
    }
    
    // string representation of this board
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    // board dimension n
    public int dimension()
    {
        return n;
    }
    
    private int[][] generateGoal()
    {
        // initialize goal
        int count = 1;
        int[][] goal = new int[n][n];
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
            {
                goal[row][col] = count;
                count++;
            }
        goal[n-1][n-1] = 0;
        return goal;
    }
    
    // number of tiles out of place
    public int hamming()
    {
        int count = 0;
        int[][] goal = generateGoal();
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
            {
                if (!(row == n-1 && col == n-1) && tiles[row][col] != goal[row][col])
                    count++;
            }
        return count;
    }
    
    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        int count = 0;
        int target = 0;
        int row = 0;
        int col = 0;
        int[][] goal = generateGoal();
        for (row = 0; row < n; row++)
        {
            for (col = 0; col < n; col++)
            {
                target = goal[row][col];
                for (int brow = 0; brow < n; brow++)
                {
                    for (int bcol = 0; bcol < n; bcol++)
                    {
                        if (target != 0 && target == tiles[brow][bcol])
                        {
                            count += Math.abs(brow - row);
                            count += Math.abs(bcol - col);
                        }
                    }
                }
            }
        }
        return count;
    }
    
    // is this board the goal board?
    public boolean isGoal()
    {
        int[][] goal = generateGoal();
        return Arrays.deepEquals(tiles, goal);
    }
    
    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == null || !(y instanceof Board))
            return false;
        else if (this == y)
            return true;

        final Board other = (Board) y;
        return Arrays.deepEquals(this.tiles, other.tiles);
    }
    
    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        Stack<Board> result = new Stack<Board>();
        
        int posrow = 0;
        int poscol = 0;
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                if (tiles[row][col] == 0)
                {
                    posrow = row;
                    poscol = col;
                }
        
        if (posrow > 0)
        {
            Board b = new Board(this);
            b.tiles[posrow][poscol] = b.tiles[posrow-1][poscol];
            b.tiles[posrow-1][poscol] = 0;
            result.push(b);
        }
        if (posrow < n-1)
        {
            Board b = new Board(this);
            b.tiles[posrow][poscol] = b.tiles[posrow+1][poscol];
            b.tiles[posrow+1][poscol] = 0;
            result.push(b);
        }
        if (poscol > 0)
        {
            Board b = new Board(this);
            b.tiles[posrow][poscol] = b.tiles[posrow][poscol-1];
            b.tiles[posrow][poscol-1] = 0;
            result.push(b);
        }
        if (poscol < n-1)
        {
            Board b = new Board(this);
            b.tiles[posrow][poscol] = b.tiles[posrow][poscol+1];
            b.tiles[posrow][poscol+1] = 0;
            result.push(b);
        }
        return result;
    }
    
    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        Board b = new Board(this);
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                if (b.tiles[row][col] != 0 && b.tiles[row+1][col] != 0)
                {
                    int upper = b.tiles[row][col];
                    int lower = b.tiles[row+1][col];
                    b.tiles[row][col] = lower;
                    b.tiles[row+1][col] = upper;
                    return b;
                }
        return b;
    }
    
    // unit testing (not graded)
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
        
        //System.out.println(initial);
        //System.out.println(initial.manhattan());
        //System.out.println(initial.equals(initial));
    }
}

