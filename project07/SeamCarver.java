/**
|-------------------------------------------------------------------------------
| SeamCarver.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Nov 24, 2021
| Compilation:  javac-algs4 SeamCarver.java
| Execution:    java-algs4 SeamCarver verticalseam.png
|
| This program implements a content-aware image resizing technique.
|
*/

import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.Arrays;

public class SeamCarver
{
    // instance variables
    private Picture picture;
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture)
    {
        if (picture == null)
            throw new IllegalArgumentException();
        
        this.picture = new Picture(picture);
    }
    
    // current picture
    public Picture picture()
    {
        return picture;
    }
    
    // width of current picture
    public int width()
    {
        return picture.width();
    }
    
    // height of current picture
    public int height()
    {
        return picture.height();
    }
    
    private double calculateEnergy(Color major, Color minor)
    {
        int red = major.getRed() - minor.getRed();
        int green = major.getGreen() - minor.getGreen();
        int blue = major.getBlue() - minor.getBlue();
        return Math.pow(red, 2) + Math.pow(green, 2) + Math.pow(blue, 2);
    }
    
    // energy of pixel at column x and row y
    public double energy(int x, int y)
    {
        if (x < 0 || x > width()-1 || y < 0 || y > height()-1)
            throw new IllegalArgumentException();
        if (x == 0 || x == width()-1 || y == 0 || y == height()-1)
            return 1000;
        
        Color right = picture.get(x+1, y);
        Color left = picture.get(x-1, y);
        Color up = picture.get(x, y-1);
        Color down = picture.get(x, y+1);
        
        return Math.pow((calculateEnergy(right, left) + calculateEnergy(down, up)), 0.5);
    }
    
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam()
    {
        Picture cloned = new Picture(picture);
        
        Picture transposed = new Picture(height(), width());
        for (int row = 0; row < width(); row++)
            for (int col = 0; col < height(); col++)
                transposed.set(col, row, picture.get(row, col));
        
        picture = transposed;
        int[] result = findVerticalSeam();
        picture = cloned;

        return result;
    }
    
    // sequence of indices for vertical seam
    public int[] findVerticalSeam()
    {
        int[] result = new int[height()];

        // handle edge cases where height() is 1, or width() is 1
        if (height() == 1)
        {
            double tinyEnergies[] = new double[width()];
            double lowestEnergy = 2000.0;
            int lowestCol = 0;
            for (int col = 0; col < width(); col++)
               tinyEnergies[col] = energy(col, 0); 
            for (int col = 0; col < width(); col++)
            {
                if (tinyEnergies[col] < lowestEnergy)
                {
                    lowestEnergy = tinyEnergies[col];
                    lowestCol = col;
                }
            }
            result[0] = lowestCol;
            return result;
        }
        
        if (width() == 1)
        {
            return result;
        }

        // initialize the energies array
        double energies[][] = new double[height()][width()];
        for (int row = 0; row < height(); row++)
            for (int col = 0; col < width(); col++)
                energies[row][col] = energy(col, row);

        // initialize the energyTo array
        double energyTo[][] = new double[height()][width()];
        for (int row = 0; row < height(); row++)
            for (int col = 0; col < width(); col++)
                energyTo[row][col] = energies[row][col];
        // sum up the first and last columns in energyTo
        for (int row = 1; row < height(); row++)
        {
            energyTo[row][0] += energyTo[row-1][0];
            energyTo[row][width()-1] += energyTo[row-1][width()-1];
        }
        
        // initialize the distTo array, set row 0 and row 1 to col values
        int distTo[][] = new int[height()][width()];
        for (int col = 0; col < width(); col++)
        {
            distTo[0][col] = col;
            distTo[1][col] = col;
        }
        // set the distTo array first col as 1, and the last col as width() - 2
        for (int row = 0; row < height(); row++)
        {
            distTo[row][0] = 1;
            distTo[row][width()-1] = width() - 2;
        }

        // calculate energy accumulations
        double findLowest[][] = new double[3][2];
        for (int row = 2; row < height(); row++)
        {
            for (int col = 1; col < width() - 1; col++)
            {
                findLowest[0][0] = energyTo[row-1][col-1];
                findLowest[0][1] = col-1;
                findLowest[1][0] = energyTo[row-1][col];
                findLowest[1][1] = col;
                findLowest[2][0] = energyTo[row-1][col+1];
                findLowest[2][1] = col+1;
                
                double lowestEnergy = 2000.0;
                double lowestCol = 0.0;
                for (int level = 0; level < 3; level++)
                {
                    if (findLowest[level][0] < lowestEnergy)
                    {
                        lowestEnergy = findLowest[level][0];
                        lowestCol = findLowest[level][1];
                    }
                }
                
                energyTo[row][col] += lowestEnergy;
                distTo[row][col] = (int) lowestCol;
            }
        }

        // determine the index of the bottom pixel with the lowest total energy
        double smallest = energyTo[height()-1][1];
        int index = 1;
        for (int col = 1; col < width() - 1; col++)
        {
            if (energyTo[height()-1][col] < smallest)
            {
                smallest = energyTo[height()-1][col];
                index = col;
            }
        }
        result[height()-1] = index;
        
        // extract the path of the smallest seam
        int pos = height()-2;
        int nextCol = index;
        for (int row = height() - 1; row > 0; row--)
        {
            nextCol = distTo[row][nextCol];
            result[pos] = nextCol;
            pos--;
        }
        
        return result;
    }
    
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam)
    {
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != width())
            throw new IllegalArgumentException();
        if (height() <= 1)
            throw new IllegalArgumentException();

        Picture transposed = new Picture(height(), width());
        for (int row = 0; row < width(); row++)
            for (int col = 0; col < height(); col++)
                transposed.set(col, row, picture.get(row, col));
        
        picture = transposed;
        removeVerticalSeam(seam);
        transposed = new Picture(picture);
        
        picture = new Picture(transposed.height(), transposed.width());
        for (int row = 0; row < transposed.width(); row++)
            for (int col = 0; col < transposed.height(); col++)
                picture.set(col, row, transposed.get(row, col));
    }
    
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)
    {
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != height())
            throw new IllegalArgumentException();
        if (width() <= 1)
            throw new IllegalArgumentException();

        int pos = 0;
        Picture sliced = new Picture(width()-1, height());
        for (int row = 0; row < height(); row++)
        {
            for (int col = 0; col < width()-1; col++)
            {
                if (col < seam[pos])
                    sliced.set(col, row, picture.get(col, row));
                else
                    sliced.set(col, row, picture.get(col+1, row));
            }
            pos++;
        }
        picture = sliced;
    }
    
    //  unit testing (optional)
    public static void main(String[] args)
    {
        Picture picture = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(picture);
        
        int[] result = sc.findVerticalSeam();
        System.out.println(Arrays.toString(result));
        
        result = sc.findHorizontalSeam();
        System.out.println(Arrays.toString(result));
    }
}

