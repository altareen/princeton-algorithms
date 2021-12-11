/**
|-------------------------------------------------------------------------------
| MoveToFront.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Dec 03, 2021
| Compilation:  javac-algs4 MoveToFront.java
| Execution:    java-algs4 MoveToFront - < abra.txt | java-algs4 edu.princeton.cs.algs4.HexDump 16
|               java-algs4 MoveToFront - < abra.txt | java-algs4 MoveToFront +
|
| This program implements move-to-front encoding and decoding.
|
*/

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.List;
import java.util.ArrayList;

public class MoveToFront
{
    // apply move-to-front encoding, reading from standard input and writing to
    // standard output
    public static void encode()
    {
        List<Character> letters = new ArrayList<Character>(256);
        for (int i = 0; i < 256; i++)
            letters.add((char) i);        
        
        while(!BinaryStdIn.isEmpty())
        {
            char c = BinaryStdIn.readChar();
            int position = letters.indexOf(c);
            BinaryStdOut.write((char) position);
            letters.remove(position);
            letters.add(0, c);
        }
        BinaryStdOut.close();
    }
    
    // apply move-to-front decoding, reading from standard input and writing to
    // standard output
    public static void decode()
    {
        List<Character> letters = new ArrayList<Character>(256);
        for (int i = 0; i < 256; i++)
            letters.add((char) i);        
        
        while(!BinaryStdIn.isEmpty())
        {
            int i = BinaryStdIn.readChar();
            int position = letters.get(i);
            BinaryStdOut.write((char) position);
            letters.remove(i);
            letters.add(0, (char) position);
        }
        BinaryStdOut.close();
    }
    
    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args)
    {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
    }
}

