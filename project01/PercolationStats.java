/**
|-------------------------------------------------------------------------------
| PercolationStats
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Oct 21, 2021
| Compilation:  javac-algs4 PercolationStats.java
| Execution:    java-algs4 PercolationStats 200 100
|
| This program performs a statistical analysis on the percolation experiments.
|
*/

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats
{
    private double[] outcomes;
    private int numTrials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if (n <= 0 || trials <= 0)
        {
            throw new IllegalArgumentException();
        }

        outcomes = new double[trials];
        numTrials = trials;
        
        for (int i = 0; i < trials; i++)
        {
            Percolation board = new Percolation(n);
            while (!board.percolates())
            {
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                if (!board.isOpen(row, col))
                    board.open(row, col);
            }
            outcomes[i] = 1.0 * board.numberOfOpenSites() / (n*n);
        }
    }

    // sample mean of percolation threshold
    public double mean()
    {
        return StdStats.mean(outcomes);
    }
    
    // sample standard deviation of percolation threshold
    public double stddev()
    {
        return StdStats.stddev(outcomes);
    }
    
    // low endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return mean() - 1.96 * stddev()/Math.sqrt(numTrials);
    }
    
    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return mean() + 1.96 * stddev()/Math.sqrt(numTrials);
    }
    
    // test client (see below)
    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}

