/**
|-------------------------------------------------------------------------------
| Percolation
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Oct 21, 2021
| Compilation:  javac-algs4 Percolation.java
| Execution:    java-algs4 Percolation
|
| This program models a percolation system.
|
*/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation
{
    private boolean[][] lattice;
    private int openSites;
    private int size;
    private WeightedQuickUnionUF sets;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if (n <= 0)
        {
            throw new IllegalArgumentException();
        }
        size = n+1;
        lattice = new boolean[size][size];
        openSites = 0;
        sets = new WeightedQuickUnionUF(size*size+2);
    }

    // opens the site(row, col) if it is not open already, and merges with
    // neighboring sites
    public void open(int row, int col)
    {
        validateIndices(row, col);
        if (!lattice[row][col]) {
            lattice[row][col] = true;
            openSites++;
        }
        int site = mapCoordinates(row, col);
        boolean up = true;
        boolean down = true;
        boolean left = true;
        boolean right = true;
        // check boundary conditions
        if (row == 1) {
            up = false;
            sets.union(0, mapCoordinates(row, col));
        }
        if (row == lattice.length-1) {
            down = false;
            sets.union(size*size+1, mapCoordinates(row, col));
        }
        if (col == 1)
            left = false;
        if (col == lattice[0].length-1)
            right = false;
        // perform opens and merges
        if (up && lattice[row-1][col]) {
            sets.union(site, mapCoordinates(row-1, col));
        }
        if (down && lattice[row+1][col]) {
            sets.union(site, mapCoordinates(row+1, col));
        }
        if (left && lattice[row][col-1]) {
            sets.union(site, mapCoordinates(row, col-1));
        }
        if (right && lattice[row][col+1]) {
            sets.union(site, mapCoordinates(row, col+1));
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        validateIndices(row, col);
        return lattice[row][col];
    }
    
    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        validateIndices(row, col);
        return sets.find(0) == sets.find(mapCoordinates(row, col));
    }
    
    // returns the number of open sites
    public int numberOfOpenSites()
    {
        return openSites;
    }
    
    // does the system percolate?
    public boolean percolates()
    {
        return sets.find(0) == sets.find(size*size+1);
    }
    
    private int mapCoordinates(int row, int col)
    {
        return col + lattice[0].length * row;
    }
    
    private void validateIndices(int row, int col)
    {
        if (row < 1 || row > lattice.length)
        {
            throw new IllegalArgumentException("row index out of bounds.");
        }
        else if (col < 1 || col > lattice[0].length)
        {
            throw new IllegalArgumentException("column index out of bounds.");
        }
    }
    
    // test client (optional)
    public static void main(String[] args)
    {
//        Percolation p = new Percolation(5);
//        System.out.println(p.sets.find(3));
//        System.out.println(p.sets.count());
    }
}


