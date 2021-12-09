/**
|-------------------------------------------------------------------------------
| Permutation.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Oct 25, 2021
| Compilation:  javac-algs4 Permutation.java
| Execution:    java-algs4 Permutation 3 < distinct.txt
|
| This program reads in a sequence of strings and displays k of them at random.
|
*/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation
{
    public static void main(String[] args)
    {
        int k = Integer.parseInt(args[0]);
        int n = 0;
        RandomizedQueue<String> stack = new RandomizedQueue<String>();
        
        while (!StdIn.isEmpty())
        {
            String word = StdIn.readString();
            stack.enqueue(word);
            n++;
        }
        
        for (int i = 0; i < k; i++)
        {
            StdOut.println(stack.dequeue());
            //StdOut.println(stack.sample());
        }
    }
}

