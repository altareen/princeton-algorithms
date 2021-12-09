/**
|-------------------------------------------------------------------------------
| PointSET.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Nov 13, 2021
| Compilation:  javac-algs4 PointSET.java
| Execution:    java-algs4 PointSET
|
| This program represents a set of points in the unit square.
|
*/

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET
{
    // instance variables
    private TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET()
    {
        points = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty()
    {
        return points.isEmpty();
    }

    // number of points in the set
    public int size()
    {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException(); 
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException();         
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw()
    {
        for (Point2D point : points)
            point.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect)
    {
        if (rect == null)
            throw new IllegalArgumentException();     
        List<Point2D> enclosed = new ArrayList<Point2D>();
        for (Point2D point : points)
        {
            if (rect.contains(point))
                enclosed.add(point);
        }
        return enclosed;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p)
    {
        if (p == null)
            throw new IllegalArgumentException(); 
        if (isEmpty())
            return null;
        
        Point2D result = points.first();
        double distance = result.distanceSquaredTo(p);
        for (Point2D point : points)
        {
            if (point.distanceSquaredTo(p) < distance)
            {
                result = point;
                distance = point.distanceSquaredTo(p);
            }
        }
        return result;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args)
    {
        
    }
}

