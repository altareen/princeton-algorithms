/**
|-------------------------------------------------------------------------------
| Outcast.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Nov 19, 2021
| Compilation:  javac-algs4 Outcast.java
| Execution:    java-algs4 Outcast synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt
|
| This program determines which noun is least related to the others.
|
*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast
{
    // instance variables
    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet)
    {
        this.wordnet = wordnet;
    }
    
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns)
    {
        int maximum = 0;
        String maxWord = nouns[0];
        for (int i = 0; i < nouns.length; i++)
        {
            int current = 0;
            for (int j = 0; j < nouns.length; j++ )
            {
                if (i != j)
                    current += wordnet.distance(nouns[i], nouns[j]);
            }
            if (current > maximum)
            {
                maximum = current;
                maxWord = nouns[i];
            }
        }
        return maxWord;
    }
    
    public static void main(String[] args)
    {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
            for (int t = 2; t < args.length; t++)
            {
                In in = new In(args[t]);
                String[] nouns = in.readAllStrings();
                StdOut.println(args[t] + ": " + outcast.outcast(nouns));
            }
    }
}

