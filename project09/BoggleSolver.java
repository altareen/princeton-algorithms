/**
|-------------------------------------------------------------------------------
| BoggleSolver.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Nov 29, 2021
| Compilation:  javac-algs4 BoggleSolver.java
| Execution:    java-algs4 BoggleSolver dictionary-algs4.txt board4x4.txt
|
| This program determines all valid dictionary words in a Boggle board.
|
*/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BoggleSolver
{
    // instance variables
    private TrieSET dictWords;
    private Set<String> validWords;
    private boolean[][] marked;
    private char[][] layout;

    // Initializes the data structure using the given array of strings as the
    // dictionary. You can assume each word in the dictionary contains only the
    // uppercase letters A through Z.
    public BoggleSolver(String[] dictionary)
    {
        dictWords = new TrieSET();
        for (String item : dictionary)
            if (item.length() >= 3)
                dictWords.add(item);
        
        validWords = new HashSet<String>();
    }
    
    private void depthFirstSearch(int row, int col, String word)
    {
        marked[row][col] = true;
        
        // handle the edge case with the Qu tile
        char letter = layout[row][col];
        if (letter == 'Q')
        {
            word += "QU";
        }
        else
            word += letter;

        // cease the DFS search when the next Node is null
        if (word.length() > 2)
        {
            TrieSET.Node node = dictWords.prefixQuery(word);

            if (node == null)
            {
                marked[row][col] = false;
                return;
            }
        }

        // place the current word into the results set, if valid
        if (word.length() > 2 && !validWords.contains(word) && dictWords.contains(word))
            validWords.add(word);

        // upper left
        if (row-1 >= 0 && col-1 >= 0 && !marked[row-1][col-1])
            depthFirstSearch(row-1, col-1, word);

        // up
        if (row-1 >= 0 && !marked[row-1][col])
            depthFirstSearch(row-1, col, word);

        // upper right
        if (row-1 >= 0 && col+1 < layout[0].length && !marked[row-1][col+1])
            depthFirstSearch(row-1, col+1, word);

        // left
        if (col-1 >= 0 && !marked[row][col-1])
            depthFirstSearch(row, col-1, word);

        // right
        if (col+1 < layout[0].length && !marked[row][col+1])
            depthFirstSearch(row, col+1, word);

        // down left
        if (row+1 < layout.length && col-1 >= 0 && !marked[row+1][col-1])
            depthFirstSearch(row+1, col-1, word);

        // down
        if (row+1 < layout.length && !marked[row+1][col])
            depthFirstSearch(row+1, col, word);

        // down right
        if (row+1 < layout.length && col+1 < layout[0].length && !marked[row+1][col+1])
            depthFirstSearch(row+1, col+1, word);

        marked[row][col] = false;
    }
    
    // Returns the set of all valid words in the given Boggle board, as an
    // Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
        int rows = board.rows();
        int cols = board.cols();
        
        marked = new boolean[rows][cols];
        layout = new char[rows][cols];
        
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++)
                layout[row][col] = board.getLetter(row, col);

        // clear out validWords
        validWords.clear();
        
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                String word = "";
                depthFirstSearch(row, col, word);
            }
        }
        
        return validWords;
    }
    
    // Returns the score of the given word if it is in the dictionary, zero
    // otherwise. You can assume the word contains only the uppercase letters
    // A through Z.
    public int scoreOf(String word)
    {
        int points = 0;
        if (!dictWords.contains(word))
            return points;
        
        int length = word.length();
        if (length == 3 || length == 4)
            points = 1;
        else if (length == 5)
            points = 2;
        else if (length == 6)
            points = 3;
        else if (length == 7)
            points = 5;
        else if (length >= 8)
            points = 11;
        
        return points;
    }
    
    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}

