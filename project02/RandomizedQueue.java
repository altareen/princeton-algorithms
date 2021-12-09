/**
|-------------------------------------------------------------------------------
| RandomizedQueue.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Oct 25, 2021
| Compilation:  javac-algs4 RandomizedQueue.java
| Execution:    java-algs4 RandomizedQueue
|
| This program implements a randomized queue data structure.
|
*/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item>
{
    private int size;
    private Node first;
    private Node last;
    
    private class Node
    {
        private Item item;
        private Node next;
    }

    // construct an empty randomized queue
    public RandomizedQueue()
    {
        size = 0;
        first = null;
        last = null;
    }

    // is the randomized queue empty?
    public boolean isEmpty()
    {
        return first == null;
    }
    
    // return the number of items on the randomized queue
    public int size()
    {
        return size;
    }
    
    // add the item
    public void enqueue(Item item)
    {
        if (item == null)
            throw new IllegalArgumentException();
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty())
            first = last;
        else
            oldlast.next = last;
        size++;
    }
    
    private Item removeFirst()
    {
        if (size == 0)
            return null;
        else
        {
            Node temp = first;
            first = first.next;
            size--;
            if (first == null)
                last = null;
            return temp.item;
        }
    }
    
    private Item removeLast()
    {
        if (size == 0)
            return null;
        else if (size == 1)
        {
            Node temp = first;
            first = last = null;
            size = 0;
            return temp.item;
        }
        else
        {
            Node current = first;
            for (int i = 0; i < size - 2; i++)
                current = current.next;
            Node temp = last;
            last = current;
            last.next = null;
            size--;
            return temp.item;
        }
    }
    
    // remove and return a random item
    public Item dequeue()
    {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");
        int limit = StdRandom.uniform(size);
        if (limit == 0)
            return removeFirst();
        else if (limit == size - 1)
            return removeLast();
        Node previous = first;
        for (int i = 1; i < limit; i++)
            previous = previous.next;
        Node current = previous.next;
        previous.next = current.next;
        size--;
        return current.item;
    }
    
    // return a random item (but do not remove it)
    public Item sample()
    {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");
        int limit = StdRandom.uniform(size);
        Node current = first;
        for (int i = 0; i < limit; i++)
            current = current.next;
        return current.item;
    }
    
    // return an independent iterator over items in random order
    public Iterator<Item> iterator()
    {
        return new ArrayIterator();
    }
    
    private class ArrayIterator implements Iterator<Item>
    {
        private Node current = first;
        
        public boolean hasNext()
        {
            // return (current != null && current != last);
            return current != null;
        }
        
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        
        public Item next()
        {
            if (!hasNext())
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    // unit testing (required)
    public static void main(String[] args)
    {
        RandomizedQueue<String> stack = new RandomizedQueue<String>();
        while (!StdIn.isEmpty())
        {
            String item = StdIn.readString();
            if (!item.equals("-"))
                stack.enqueue(item);
            else if (!stack.isEmpty())
                StdOut.print(stack.dequeue() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");
    }
}

