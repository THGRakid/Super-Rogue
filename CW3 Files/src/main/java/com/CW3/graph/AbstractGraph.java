package com.CW3.graph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * Greedy Algorithm: Dijkstra
     *
     * @param u starting point
     * @param v ending point
     * @return
     */
    public Integer dijkstra(Integer u, Integer v) {
        // Record the distance between two nodes
        HashMap<Integer, Integer> distanceToNodes = new HashMap<>();
        // Stores unprocessed nodes
        HashMap<Integer, Integer> nodesToProcess = new HashMap<>();

        // Initialize by putting the starting point u into the nodesToProcess and setting the distance to 0
        nodesToProcess.put(u, 0);

        // Jumps out of while loop when nodesToProcess is empty
        while (!nodesToProcess.isEmpty()) {
            // Initializes the minimum node site and minimum distance
            Integer minNodeID = -1;
            int minDistance = Integer.MAX_VALUE;
            // Traverse the nodesToProcess to find the node that is the shortest distance from the starting point
            for (Map.Entry<Integer, Integer> entry : nodesToProcess.entrySet()) {
                Integer thisNodeID = entry.getKey();
                int thisNodeDistance = entry.getValue();
                // Compare whether the distance of the current node is smaller than the minimum distance found earlier
                if (thisNodeDistance < minDistance) {
                    minDistance = thisNodeDistance;
                    minNodeID = thisNodeID;
                }
            }
            // Take the shortest distance node out
            nodesToProcess.remove(minNodeID);
            // Update distance
            distanceToNodes.put(minNodeID, minDistance);

            for (Integer neighborNodeID : neighbours.get(minNodeID)) {
                if (distanceToNodes.containsKey(neighborNodeID)) {
                    continue;
                }

                if (nodesToProcess.containsKey(neighborNodeID)) {
                    if (minDistance + 1 < nodesToProcess.get(neighborNodeID)) { // Assuming the edge weight is 1
                        nodesToProcess.put(neighborNodeID, minDistance + 1); // Update the distance
                    }
                } else {
                    nodesToProcess.put(neighborNodeID, minDistance + 1);
                }
            }
        }

        if (distanceToNodes.containsKey(v)) {

            return distanceToNodes.get(v);
        }
        return -1;
    }


    // return dimension of dungeon
    public int size() {
        return N;
    }

    // does v correspond to a corridor site?
    public boolean isCorridor(Site v) {
        int i = v.i();
        int j = v.j();
        if (i < 0 || j < 0 || i >= N || j >= N) return false;
        return isCorridor[i][j];
    }

    // does v correspond to a room site?
    public boolean isRoom(Site v) {
        int i = v.i();
        int j = v.j();
        if (i < 0 || j < 0 || i >= N || j >= N) return false;
        return isRoom[i][j];
    }

    // does v correspond to a wall site?
    public boolean isWall(Site v) {
        return (!isRoom(v) && !isCorridor(v));
    }

    // does v-w correspond to a legal move?
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
}
