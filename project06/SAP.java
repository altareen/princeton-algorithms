/**
|-------------------------------------------------------------------------------
| SAP.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Nov 19, 2021
| Compilation:  javac-algs4 SAP.java
| Execution:    java-algs4 SAP digraph1.txt
|
| This program implements the shortest common ancestor algorithm.
|
*/

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;

public class SAP
{
    // instance variables
    private int edges;
    private int vertices;
    private Digraph G;
    

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G)
    {
        if (G == null)
            throw new IllegalArgumentException();
        
        this.G = new Digraph(G);
        
        // retrieve the number of vertices in the digraph
        vertices = G.V();
        edges = G.E();
    }
    
    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
        if (v < 0 || w < 0 || v > vertices || w > vertices)
            throw new IllegalArgumentException();
        
        int shortestPath = edges;
        int currentPath = 0;

        BreadthFirstDirectedPaths breV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths breW = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < vertices; i++)
        {
            if (breV.hasPathTo(i) && breW.hasPathTo(i))
            {
                currentPath = breV.distTo(i) + breW.distTo(i);
                if (currentPath < shortestPath)
                    shortestPath = currentPath;
            }
        }
        if (shortestPath == edges)
            shortestPath = -1;
        return shortestPath;
    }
    
    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w)
    {
        if (v < 0 || w < 0 || v > vertices || w > vertices)
            throw new IllegalArgumentException();

        int shortestPath = edges;
        int currentPath = 0;
        int commonAncestor = -1;

        BreadthFirstDirectedPaths breV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths breW = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < vertices; i++)
        {
            if (breV.hasPathTo(i) && breW.hasPathTo(i))
            {
                currentPath = breV.distTo(i) + breW.distTo(i);
                if (currentPath < shortestPath)
                {
                    shortestPath = currentPath;
                    commonAncestor = i;
                }
            }
        }
        return commonAncestor;
    }
    
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        if (v == null || w == null)
            throw new IllegalArgumentException();
        for (Integer item : v)
            if (item == null)
                throw new IllegalArgumentException();
        for (Integer item : w)
            if (item == null)
                throw new IllegalArgumentException();
        int count = 0;
        for (Integer num : v)
            count++;
        if (count == 0)
            return -1;
        count = 0;
        for (Integer num : w)
            count++;
        if (count == 0)
            return -1;

        int shortestPath = edges;
        int currentPath = 0;

        BreadthFirstDirectedPaths breV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths breW = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < vertices; i++)
        {
            if (breV.hasPathTo(i) && breW.hasPathTo(i))
            {
                currentPath = breV.distTo(i) + breW.distTo(i);
                if (currentPath < shortestPath)
                    shortestPath = currentPath;
            }
        }
        if (shortestPath == edges)
            shortestPath = -1;
        return shortestPath;
    }
    
    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        if (v == null || w == null)
            throw new IllegalArgumentException();
        for (Integer item : v)
            if (item == null)
                throw new IllegalArgumentException();
        for (Integer item : w)
            if (item == null)
                throw new IllegalArgumentException();
        int count = 0;
        for (Integer num : v)
            count++;
        if (count == 0)
            return -1;
        count = 0;
        for (Integer num : w)
            count++;
        if (count == 0)
            return -1;

        int shortestPath = edges;
        int currentPath = 0;
        int commonAncestor = -1;

        BreadthFirstDirectedPaths breV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths breW = new BreadthFirstDirectedPaths(G, w);

        for (int i = 0; i < vertices; i++)
        {
            if (breV.hasPathTo(i) && breW.hasPathTo(i))
            {
                currentPath = breV.distTo(i) + breW.distTo(i);
                if (currentPath < shortestPath)
                {
                    shortestPath = currentPath;
                    commonAncestor = i;
                }
            }
        }
        return commonAncestor;
    }
    
    // do unit testing of this class
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty())
        {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

