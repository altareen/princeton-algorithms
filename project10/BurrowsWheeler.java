/**
|-------------------------------------------------------------------------------
| BurrowsWheeler.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Dec 03, 2021
| Compilation:  javac-algs4 BurrowsWheeler.java
| Execution:    java-algs4 BurrowsWheeler - < abra.txt | java-algs4 BurrowsWheeler +
|
| This program implements the Burrows-Wheeler compression scheme.
|
*/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.Arrays;

public class BurrowsWheeler
{
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform()
    {
        String shifted = "";
        StringBuilder result = new StringBuilder();
        int first = 0;
 
        String suffix = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(suffix);
        for (int i = 0; i < suffix.length(); i++)
        {
            int pos = csa.index(i);
            if (pos == 0)
                first = i;
            pos--;
            if (pos < 0)
                pos += suffix.length();
            result.append(suffix.charAt(pos));
        }
        
        BinaryStdOut.write(first);
        BinaryStdOut.write(result.toString());
        BinaryStdOut.close();
    }
    
    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform()
    {
        StringBuilder result = new StringBuilder();
        int first = BinaryStdIn.readInt();
        String suffix = BinaryStdIn.readString();
        
        // implement the Key-Indexed Sort
        int N = suffix.length();
        int R = 256;
        char[] t = suffix.toCharArray();
        char[] aux = new char[N];
        int[] count = new int[R+1];
        int[] next = new int[N];
        int pos = 0;
        
        for (int i = 0; i < N; i++)
            count[t[i]+1]++;
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];
        for (int i = 0; i < N; i++)
        {
            int location = count[t[i]]++;
            aux[location] = t[i];
            next[location] = pos;
            pos++;
        }
        for (int i = 0; i < N; i++)
            t[i] = aux[i];
        
        int row = next[first];
        result.append(suffix.substring(row, row+1));
        for (int i = 1; i < next.length; i++)
        {
            row = next[row];
            result.append(suffix.substring(row, row+1));
        }
        
        BinaryStdOut.write(result.toString());
        BinaryStdOut.close();
    }
    
    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args)
    {
        if (args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();
    }
}

