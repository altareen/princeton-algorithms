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
// import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item>
{
    private static final int INIT_CAPACITY = 8;
    private Item[] elements;
    private int low;
    private int high;
    private int size;

    // construct an empty deque
    public Deque()
    {
        elements = (Item[]) new Object[INIT_CAPACITY];
        // low = INIT_CAPACITY / 2;
        // high = INIT_CAPACITY / 2;
        low = 0;
        high = 0;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty()
    {
        return size == 0 && low == high;
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
        if (size == elements.length)
            resize(2 * elements.length);
        // low = dec(low);
        // elements[low] = item;
        for (int i = size - 1; i >= 0; i--)
            elements[i+1] = elements[i];
        elements[0] = item;
        size++;
    }

    // add the item to the back
    public void addLast(Item item)
    {
        if (item == null)
            throw new IllegalArgumentException();
        size++;
        if (size == elements.length)
            resize(2 * elements.length);
        // high = inc(high);
        high++;
        elements[high] = item;
    }

    // remove and return the item from the front
    public Item removeFirst()
    {
        if (isEmpty())
            throw new NoSuchElementException();
        // Item item = elements[low];
        // elements[low] = null;
        // low = inc(low);
        Item item = elements[0];
        for (int i = 1; i < size; i++)
            elements[i-1] = elements[i];        
        size--;
        if (size > 0 && size == elements.length/4)
            resize(elements.length/2);
        return item;
    }

    // remove and return the item from the back
    public Item removeLast()
    {
        if (isEmpty())
            throw new NoSuchElementException();
        Item item = elements[high];
        elements[high] = null;
        // high = dec(high);
        high--;
        size--;
        if (size > 0 && size == elements.length/4)
            resize(elements.length/2);
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator()
    {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item>
    {
        private int i;

        public ArrayIterator() {
            i = low;
        }

        public boolean hasNext() {
            int next = i;
            next++;
            return elements[next] != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return elements[i++];
        }
    }

    // resize the underlying array holding the elements
    private void resize(int capacity)
    {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++)
        {
            copy[i] = elements[low];
            low = (low + 1) % elements.length;
        }
        
        low = 0;
        high = elements.length;
        elements = copy;
    }

    // increment operation to accommodate wrapping around the end
    private int inc(int i)
    {
        return (i == elements.length - 1) ? 0 : i+1;
    }
    
    // decrement operation to accommodate wrapping around the beginning
    private int dec(int i)
    {
        return (i == 0) ? elements.length - 1 : i-1;
    }

//    public String toString()
//    {
//        return Arrays.toString(elements);
//    }

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
//        StdOut.println(stack.toString());
    }
}


