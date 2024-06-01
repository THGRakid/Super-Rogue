package com.CW3.graph;


import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractGraph<V> implements Graph<V> {
    protected boolean[][] isRoom;               // is v-w a room site?
    protected boolean[][] isCorridor;           // is v-w a corridor site?
    protected int N;                            // dimension of dungeon
    protected HashMap<Integer, ArrayList<Integer>> neighbours; // Adjacency lists
    private ArrayList<Integer> corridorList;
    private ArrayList<Integer> connectedCorridorList;
    private ArrayList<Integer> visitedNodeList;


    public AbstractGraph(char[][] board) {
        N = board.length;
        neighbours = new HashMap<>();
        isRoom = new boolean[N][N];
        isCorridor = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == '.') isRoom[i][j] = true;
                else if (board[i][j] == '+') isCorridor[i][j] = true;
            }
        }
    }

    // return dimension of dungeon
    @Override
    public int size() {
        return N;
    }

    // does v correspond to a corridor site?
    @Override
    public boolean isCorridor(Site v) {
        int i = v.i();
        int j = v.j();
        if (i < 0 || j < 0 || i >= N || j >= N) return false;
        return isCorridor[i][j];
    }

    // does v correspond to a room site?
    @Override
    public boolean isRoom(Site v) {
        int i = v.i();
        int j = v.j();
        if (i < 0 || j < 0 || i >= N || j >= N) return false;
        return isRoom[i][j];
    }

    // does v correspond to a wall site?
    @Override
    public boolean isWall(Site v) {
        return (!isRoom(v) && !isCorridor(v));
    }

    // does v-w correspond to a legal move?
    @Override
    public boolean isLegalMove(Site v, Site w) {
        int i1 = v.i();
        int j1 = v.j();
        int i2 = w.i();
        int j2 = w.j();
        if (i1 < 0 || j1 < 0 || i1 >= N || j1 >= N) return false;
        if (i2 < 0 || j2 < 0 || i2 >= N || j2 >= N) return false;
        if (isWall(v) || isWall(w)) return false;
        if (Math.abs(i1 - i2) > 1) return false;
        if (Math.abs(j1 - j2) > 1) return false;
        if (isRoom(v) && isRoom(w)) return true;
        if (i1 == i2) return true;
        if (j1 == j2) return true;

        return false;
    }

    // A method to get the nodeID under a corresponding site
    @Override
    public int getNodeIDFromSite(Site site){
        int i = site.i();
        int j = site.j();
        return i*N+j;
    }

    
    // Check if this point is a 3x3 grid and if it's valid, it's a neighbor
    @Override
    public void addLegalNeighborsToGridNodes(){
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // Iterate through each point within the grid
                // Get the center point
                int nodeID = i * N + j;
                // Traverse the surrounding 3x3 grid
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (dx == 0 && dy == 0) {
                            continue;
                        }
                        // Get the id of a point within the 3x3 grid
                        int centerNeighborID = ((i + dx) * N) + (j + dy);
                        // If the move is valid
                        Site center = new Site(i, j);
                        Site centerNeighbor = new Site(i + dx, j + dy);
                        // Determine if it's possible to walk there
                        if (isLegalMove(center, centerNeighbor)) {
                            neighbours.computeIfAbsent(nodeID, k -> new ArrayList<>()).add(centerNeighborID);
                        }
                    }
                }

            }
        }
    }
}
