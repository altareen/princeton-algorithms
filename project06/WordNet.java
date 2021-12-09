/**
|-------------------------------------------------------------------------------
| WordNet.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Nov 19, 2021
| Compilation:  javac-algs4 WordNet.java
| Execution:    java-algs4 WordNet
|
| This program groups words into sets of synonyms called synsets and hypernyms.
|
*/

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class WordNet
{
    // instance variables
    private HashMap<Integer, ArrayList<String>> corpus;
    private Digraph lexicon;
    private HashSet<String> distinctWords;
    private SAP relations;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();
        
        // create the mapping for the vertex & word cluster
        corpus = new HashMap<Integer, ArrayList<String>>();

        // read in the synset data and place it in the corpus
        In syndata = new In(synsets);
        String line = "";
        while (line != null)
        {
            line = syndata.readLine();
            if (line != null)
            {
                String[] fields = line.split(",");
                String[] cluster = fields[1].split(" ");
                ArrayList<String> words = new ArrayList<String>(Arrays.asList(cluster));
                int vertex = Integer.parseInt(fields[0]);
                corpus.put(vertex, words);
            }
        }
        //System.out.println(corpus.size());
        syndata.close();
        
        // create the digraph for the WordNet data
        lexicon = new Digraph(corpus.size());
        
        // read in the hypernym data and place it in the digraph
        In hyperdata = new In(hypernyms);
        line = "";
        while (line != null)
        {
            line = hyperdata.readLine();
            if (line != null)
            {
                String[] fields = line.split(",");
                for (int i = 1; i < fields.length; i++)
                {   
                    int vertex = Integer.parseInt(fields[0]);
                    int hypernym = Integer.parseInt(fields[i]);
                    lexicon.addEdge(vertex, hypernym);
                }
            }
        }
        hyperdata.close();
        
        // create the SAP graph
        relations = new SAP(lexicon);
        
        // verify that the graph is a proper DAG
        Topological top = new Topological(lexicon);
        if (!top.hasOrder())
            throw new IllegalArgumentException();
        int count = 0;
        for (int i = 0; i < lexicon.V(); i++)
            if (lexicon.outdegree(i) == 0 && lexicon.indegree(i) > 0)
                count++;
        if (count > 1)
            throw new IllegalArgumentException();
        
        // place all the words from the corpus into a set of distinct words
        distinctWords = new HashSet<String>();
        for (ArrayList<String> cluster: corpus.values())
            for (String item: cluster)
                    distinctWords.add(item);
    }
    
    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        return distinctWords;
    }
    
    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        if (word == null)
            throw new IllegalArgumentException();
    
        return distinctWords.contains(word);
    }
    
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        if (!distinctWords.contains(nounA))
            throw new IllegalArgumentException();
        if (!distinctWords.contains(nounB))
            throw new IllegalArgumentException();
        
        int v = 0;
        int w = 0;
        
        for (Entry<Integer, ArrayList<String>> entry : corpus.entrySet())
        {
            if (entry.getValue().contains(nounA))
            {
                v = entry.getKey();
                break;
            }
        }
        for (Entry<Integer, ArrayList<String>> entry : corpus.entrySet())
        {
            if (entry.getValue().contains(nounB))
            {
                w = entry.getKey();
                break;
            }
        }
        
        return relations.length(v, w);
    }
    
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        if (!distinctWords.contains(nounA))
            throw new IllegalArgumentException();
        if (!distinctWords.contains(nounB))
            throw new IllegalArgumentException();
        
        int v = 0;
        int w = 0;
        
        for (Entry<Integer, ArrayList<String>> entry : corpus.entrySet())
        {
            if (entry.getValue().contains(nounA))
            {
                v = entry.getKey();
                break;
            }
        }
        for (Entry<Integer, ArrayList<String>> entry : corpus.entrySet())
        {
            if (entry.getValue().contains(nounB))
            {
                w = entry.getKey();
                break;
            }
        }        
        
        String result = "";
        ArrayList<String> outcome = corpus.get(relations.ancestor(v, w));
        for (int i = 0; i < outcome.size(); i++)
        {
            result += outcome.get(i);
            if (i < outcome.size() - 1)
                result += " ";
        }
        return result;
    }
    
    // do unit testing of this class
    public static void main(String[] args)
    {
        //WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        //System.out.println(wordnet.nouns());
    }
}

