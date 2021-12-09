/**
|-------------------------------------------------------------------------------
| KdTree.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Nov 13, 2021
| Compilation:  javac-algs4 KdTree.java
| Execution:    java-algs4 KdTree
|
| This program represents a Binary Search Tree with 2-dimensional keys.
|
*/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;
import java.util.List;

public class KdTree
{
    // instance variables
    private int size;
    private Node root;
    
    // inner class
    private static class Node
    {
        // instance variables
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;
        
        public Node(Point2D p, RectHV rect, Node lb, Node rt)
        {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }
    
    // constructor
    public KdTree()
    {
        size = 0;
        root = new Node(null, new RectHV(0.0, 0.0, 1.0, 1.0), null, null);
    }
    
    public String toString()
    {
        String result = "";
        return display(root, result);
    }
    
    private String display(Node node, String result)
    {
        if (node == null)
            return result;
        result = display(node.lb, result);
        result += node.p.toString() + "\n";
        result += node.rect.toString() + "\n\n";
        result = display(node.rt, result);
        return result;
    }
    
    // is the tree empty?
    public boolean isEmpty()
    {
        return size == 0;
    }
    
    // number of points in the tree
    public int size()
    {
        return size;
    }
    
    // add the point to the tree (if it is not already in the tree)
    public void insert(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException();
        if (contains(p))
            return;
        if (root.p == null)
        {
            root.p = p;
            root.rect = new RectHV(0.0, 0.0, 1.0, 1.0);
            size++;
            return;
        }
        root = put(root, p, true);
    }

    private Node put(Node node, Point2D p, boolean xcompare)
    {
        if (xcompare)
        {
            if (p.x() < node.p.x())
            {
                if (node.lb == null)
                {
                    node.lb = new Node(p, new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax()), null, null);
                    size++;
                }
                else                
                    node.lb = put(node.lb, p, false);
            }
            else if (p.x() >= node.p.x())
            {
                if (node.rt == null)
                {
                    node.rt = new Node(p, new RectHV(node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax()), null, null);
                    size++;
                }
                else
                    node.rt = put(node.rt, p, false);
            }
        }
        else
        {
            if (p.y() < node.p.y())
            {
                if (node.lb == null)
                {
                    node.lb = new Node(p, new RectHV(node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y()), null, null);
                    size++;
                }
                else
                    node.lb = put(node.lb, p, true);
            }
            else if (p.y() >= node.p.y())
            {
                if (node.rt == null)
                {
                    node.rt = new Node(p, new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax()), null, null);
                    size++;
                }
                else
                    node.rt = put(node.rt, p, true);
            }
        }
        return node;
    }
    
    // does the tree contain point p?
    public boolean contains(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException();
        if (root.p == null)
            return false;
        if (root.p == p)
            return true;
        return get(root, p, false) != null;
    }
    
    private Point2D get(Node node, Point2D p, boolean xcompare)
    {
        if (node == null)
            return null;
        if (xcompare)
        {
            if (p.x() < node.p.x())
                return get(node.lb, p, false);
            else if (p.x() > node.p.x())
                return get(node.rt, p, false);
            else if (p.equals(node.p))
                return node.p;
            else
                return null;
        }
        else
        {
            if (p.y() < node.p.y())
                return get(node.lb, p, true);
            else if (p.y() > node.p.y())
                return get(node.rt, p, true);
            else if (p.equals(node.p))
                return node.p;
            else
                return null;
        }
    }
    
    // draw all points to standard draw(optional)
    public void draw()
    {
    
    }
    
    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null)
            throw new IllegalArgumentException();
        List<Point2D> enclosed = new ArrayList<Point2D>();
        select(root, rect, enclosed);
        return enclosed;
    }
    
    private void select(Node node, RectHV rect, List<Point2D> enclosed)
    {
        if (node == null)
            return;
        if (rect.contains(node.p))
            enclosed.add(node.p);
        if (node.lb != null && rect.intersects(node.lb.rect))
            select(node.lb, rect, enclosed);
        if (node.rt != null && rect.intersects(node.rt.rect))
            select(node.rt, rect, enclosed);
    }
    
    // a nearest neighbor in the tree to point p; null if the tree is empty
    public Point2D nearest(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException();
        if (root.p == null)
            return null;
        Point2D minimum = root.p;
        minimum = choose(root, p, minimum);
        return minimum;
    }

    private Point2D choose(Node node, Point2D p, Point2D minimum)
    {
        if (node == null)
            return minimum;
        if (p.distanceSquaredTo(node.p) < p.distanceSquaredTo(minimum))
        {
            minimum = node.p;
        }
        
        Node otherBranch = null;
        if (node.lb != null && node.rt != null && node.lb.p.distanceSquaredTo(p) < node.rt.p.distanceSquaredTo(p))
        {
            minimum = choose(node.lb, p, minimum);
            otherBranch = node.rt;
        }
        else
        {
            minimum = choose(node.rt, p, minimum);
            otherBranch = node.lb;
        }
        
        if (otherBranch != null && otherBranch.rect.distanceSquaredTo(p) < p.distanceSquaredTo(minimum))
            minimum = choose(otherBranch, p, minimum);
        return minimum;
    }
    
    // unit testing of the methods(optional)
    public static void main(String[] args)
    {
        /*KdTree kt = new KdTree();
        kt.insert(new Point2D(0.7, 0.2));
        kt.insert(new Point2D(0.5, 0.4));
        kt.insert(new Point2D(0.2, 0.3));
        kt.insert(new Point2D(0.4, 0.7));
        kt.insert(new Point2D(0.9, 0.6));
        System.out.println(kt);*/
        
        // range
        /*KdTree kt = new KdTree();
        kt.insert(new Point2D(0.75, 0.375));
        kt.insert(new Point2D(0.125, 0.875));
        kt.insert(new Point2D(1.0, 1.0));
        kt.insert(new Point2D(0.375, 0.5));
        kt.insert(new Point2D(0.625, 0.25));*/
        
        //System.out.println(kt);
        //System.out.println(kt.range(new RectHV(0.5, 0.125, 0.875, 0.625)));
        
        // nearest
        KdTree kt = new KdTree();
        kt.insert(new Point2D(0.7, 0.2));
        kt.insert(new Point2D(0.5, 0.4));
        kt.insert(new Point2D(0.2, 0.3));
        kt.insert(new Point2D(0.4, 0.7));
        kt.insert(new Point2D(0.9, 0.6));
        
        System.out.println(kt);
        System.out.println(kt.nearest(new Point2D(0.39, 0.665)));
        System.out.println(kt.nearest(new Point2D(0.151, 0.569)));
        System.out.println(kt.nearest(new Point2D(0.729, 0.993)));
        System.out.println(kt.nearest(new Point2D(0.385, 0.068)));
    }
}

