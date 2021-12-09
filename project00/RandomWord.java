/**
|-------------------------------------------------------------------------------
| RandomWord
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Oct 20, 2021
| Compilation:  javac-algs4 RandomWord.java
| Execution:    java-algs4 RandomWord
|
| This program reads in a sequence of words, and displays one of them at random.
|
*/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord
{
    public static void main(String[] args)
    {
        String current = "";
        String champion = "";
        int count = 0;
        double probability = 0.0;
        
        while (!StdIn.isEmpty())
        {
            current = StdIn.readString();
            count++;
            probability = 1.0/count;
            
            if (StdRandom.bernoulli(probability))
            {
                champion = current;
            }
        }
        
        System.out.println(champion);
    }
}

