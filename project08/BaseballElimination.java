/**
|-------------------------------------------------------------------------------
| BaseballElimination.java
|-------------------------------------------------------------------------------
|
| Author:       Alwin Tareen
| Created:      Nov 24, 2021
| Compilation:  javac-algs4 BaseballElimination.java
| Execution:    java-algs4 BaseballElimination teams4.txt
|
| This program determines which teams have been mathematically eliminated from
| winning their division.
|
*/

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class BaseballElimination
{
    // instance variables
    private int numberOfTeams;
    private List<String> teams;
    private int[][] divisionRecord;
    private int[][] gamesRemaining;
    private FlowNetwork graph;
    private FordFulkerson paths;
    private int combinations;
    private boolean isTrivial;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename)
    {
        In in = new In(filename);
        numberOfTeams = in.readInt();
        in.readLine();
        
        teams = new ArrayList<String>();
        divisionRecord = new int[numberOfTeams][3];
        gamesRemaining = new int[numberOfTeams][numberOfTeams];
        isTrivial = false;
        
        while (!in.isEmpty())
        {
            for (int i = 0; i < numberOfTeams; i++)
            {
                teams.add(in.readString());
                for (int j = 0; j < 3; j++)
                    divisionRecord[i][j] = in.readInt();
                for (int j = 0; j < numberOfTeams; j++)                
                    gamesRemaining[i][j] = in.readInt();
            }
        }
        // TODO
        //System.out.println(teams);
        //System.out.println(Arrays.deepToString(divisionRecord));
        //System.out.println(Arrays.deepToString(gamesRemaining));
    }
    
    // number of teams
    public int numberOfTeams()
    {
        return numberOfTeams;
    }
    
    // all teams
    public Iterable<String> teams()
    {
        return teams;
    }
    
    // number of wins for given team
    public int wins(String team)
    {
        if (!teams.contains(team))
            throw new IllegalArgumentException();
        return divisionRecord[teams.indexOf(team)][0];
    }
    
    // number of losses for given team
    public int losses(String team)
    {
        if (!teams.contains(team))
            throw new IllegalArgumentException();
        return divisionRecord[teams.indexOf(team)][1];
    }
    
    // number of remaining games for given team
    public int remaining(String team)
    {
        if (!teams.contains(team))
            throw new IllegalArgumentException();
        return divisionRecord[teams.indexOf(team)][2];
    }
    
    // number of remaining games between team1 and team2
    public int against(String team1, String team2)
    {
        if (!teams.contains(team1) || !teams.contains(team2))
            throw new IllegalArgumentException();
        return gamesRemaining[teams.indexOf(team1)][teams.indexOf(team2)];
    }
    
    private int determineCombinations(int n, int r)
    {
        if (n < r || n == 0)
            return 1;
        int numer = 1;
        int denom = 1;
        for (int i = r; i >= 1; i--)
        {
            numer = numer * n--;
            denom = denom * i;
        }
        return numer/denom;
    }
    
    private int[][] generateCombinations(int n, int qty)
    {
        int[][] result = new int[qty][2];
        int pos = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = i; j < n; j++)
            {
                if (i != j)
                {
                    result[pos][0] = i;
                    result[pos][1] = j;
                    pos++;
                }
            }
        }
        return result;
    }
    
    // is given team eliminated?
    public boolean isEliminated(String team)
    {
        if (!teams.contains(team))
            throw new IllegalArgumentException();
        
        boolean result = false;
        int num = teams.indexOf(team);

        // trivial elimination
        for (int i = 0; i < numberOfTeams; i++)
        {
            if (divisionRecord[num][0] + divisionRecord[num][2] < divisionRecord[i][0])
            {
                isTrivial = true;
                return true;
            }
        }
        
        // maxflow elimination: generate the FlowNetwork graph
        combinations = determineCombinations(numberOfTeams-1, 2);
        int[][] gameVertices = generateCombinations(numberOfTeams-1, combinations);
        
        graph = new FlowNetwork(combinations + numberOfTeams + 1);
        
        for (int i = 0; i < combinations; i++)
        {
            int gamesLeft = gamesRemaining[gameVertices[i][0]][gameVertices[i][1]];
            graph.addEdge(new FlowEdge(0, i+1, gamesLeft));
        }
        
        for (int i = 0; i < combinations; i++)
        {
            graph.addEdge(new FlowEdge(i+1, gameVertices[i][0] + combinations+1, Double.POSITIVE_INFINITY));
            graph.addEdge(new FlowEdge(i+1, gameVertices[i][1] + combinations+1, Double.POSITIVE_INFINITY));
        }
        
        int sinkVertex = combinations + numberOfTeams;
        for (int i = combinations+1; i < sinkVertex; i++)
        {
            int club = i - (combinations+1);
            int capacity = divisionRecord[num][0] + divisionRecord[num][2] - divisionRecord[club][0];
            graph.addEdge(new FlowEdge(i, sinkVertex, capacity));
        }
        
        // compute the maxflow and the mincut
        paths = new FordFulkerson(graph, 0, sinkVertex);
        paths.value();
        // TODO
        //System.out.println(graph.toString());
        
        Iterable<FlowEdge> edges = graph.adj(0);
        for (FlowEdge edge : edges)
            if (edge.flow() < edge.capacity())
                return true;
        
        return result;
    }
    
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team)
    {
        if (!teams.contains(team))
            throw new IllegalArgumentException();

        if (!isEliminated(team))
            return null;

        List<String> ballClubs = new ArrayList<String>();

        if (isTrivial)
        {
            // TODO: should retrieve the team with the most wins
            ballClubs.add(teams.get(0));
            isTrivial = false;
            return ballClubs;
        }
        
        int totalVertices = graph.V();
        int sinkVertex = totalVertices - 1;
        int teamVertex = totalVertices - numberOfTeams;
        
        for (int vertex = 0; vertex < totalVertices; vertex++)
        {
            if (paths.inCut(vertex) && vertex < sinkVertex && vertex >= teamVertex)
            {
                // TODO
                //System.out.println("vertex: " + vertex);
                ballClubs.add(teams.get(vertex-combinations-1));
            }
        }

        return ballClubs;
    }
    
    public static void main(String[] args)
    {
        BaseballElimination division = new BaseballElimination(args[0]);
        
        for (String team : division.teams())
        {
            if (division.isEliminated(team))
            {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            }
            else
                StdOut.println(team + " is not eliminated");
        }
    }
}

