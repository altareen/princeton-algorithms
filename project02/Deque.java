/**
|-------------------------------------------------------------------------------
| Deque.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Oct 25, 2021
| Compilation:  javac-algs4 Deque.java
| Execution:    java-algs4 Deque
|
| This program implements a double-ended queue data structure.
|
*/

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>
{
    private int size;
    private Node first;
    private Node last;
    
    private class Node
    {
        private Item item;
        private Node next;
        
        public Node(Item element)
        {
            item = element;
        }
    }

    // construct an empty deque
    public Deque()
    {
        size = 0;
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty()
    {
        return first == null;
    }
    
    // return the number of items on the deque
    public int size()
    {
        return size;
    }
    
    // add the item to the front
    public void addFirst(Item item)
    {
        if (item == null)
            throw new IllegalArgumentException();    
        Node current = new Node(item);
        current.next = first;
        first = current;
        size++;
        if (last == null)
            last = first;
    }

    // add the item to the back
    public void addLast(Item item)
    {
        if (item == null)
            throw new IllegalArgumentException();    
        Node current = new Node(item);
        if (last == null)
            first = last = current;
        else
        {
            last.next = current;
            last = last.next;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst()
    {
        if (isEmpty())
            throw new NoSuchElementException();
        if (size == 0)
            return null;
        Node temp = first;
        first = first.next;
        size--;
        if (first == null)
            last = null;
        return temp.item;
    }

    // remove and return the item from the back
    public Item removeLast()
    {
        if (isEmpty())
            throw new NoSuchElementException();
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

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item>
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
        Deque<String> stack = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) stack.addLast(item);
            else if (!stack.isEmpty()) StdOut.print(stack.removeLast() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");
    }
}

