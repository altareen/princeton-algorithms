/**
|-------------------------------------------------------------------------------
| CircularSuffixArray.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Dec 03, 2021
| Compilation:  javac-algs4 CircularSuffixArray.java
| Execution:    java-algs4 CircularSuffixArray
|
| This program implements the abstraction of a sorted array of the n circular
| suffixes of a string of length n.
|
*/

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class CircularSuffixArray
{
    private String suffix;
    private TreeMap<String, Integer> ordered;
    private int[] rows;

    // circular suffix array of s
    public CircularSuffixArray(String s)
    {
        if (s == null)
            throw new IllegalArgumentException();
        
        suffix = s;
        rows = new int[suffix.length()];
        ordered = new TreeMap<String, Integer>();
        String rotated = suffix;

        for (int i = 0; i < suffix.length(); i++)
        {
            ordered.put(rotated, i);
            rotated = rotated.substring(1) + rotated.substring(0, 1);
        }

        int i = 0;
        for (Integer rank : ordered.values())
        {
            rows[i] = rank;
            i++;
        }
    }
    
    // length of s
    public int length()
    {
        return suffix.length();
    }
    
    // returns index of ith sorted suffix
    public int index(int i)
    {
        if (i < 0 || i > length()-1)
            throw new IllegalArgumentException();
        return rows[i];
    }
    
    // unit testing (required)
    public static void main(String[] args)
    {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        System.out.println(csa.index(11));
    }
}

