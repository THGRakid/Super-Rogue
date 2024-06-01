package com.CW3.graph;

import java.util.ArrayList;

public class Dungeon extends AbstractGraph<Site>{

    //Save the Node IDs of all corridors
    private ArrayList<Integer> corridorNodeIDLists;
    //Save the Node IDs of all ring corridors
    private ArrayList<Integer> connectedCorridorNodeIDLists;

    private ArrayList<Integer> visitedNodeList;



    // Initialize a new dungeon based on the given board
    public Dungeon(char[][] board) {
        super(board);

        addLegalNeighborsToGridNodes();

        // Traverse the grid to find corridors
        corridorNodeIDLists =new ArrayList<>();
        for (int i=0;i<N;i++){
            for (int j=0;j<N;j++){
                Site thisSite = new Site(i,j);
                if (isCorridor(thisSite)){
                    corridorNodeIDLists.add(getNodeIDFromSite(thisSite));
                }
            }
        }
        // Generate a nodeIDList for the circular corridor
        connectedCorridorNodeIDLists = new ArrayList<>();
        for (int corrNodeID:corridorNodeIDLists){
            // Determine whether the corridor is in the ring
            if (ifCyc(corrNodeID)){
                connectedCorridorNodeIDLists.add(corrNodeID);
            }

        }

        // Print the neighbor points near the center point
        System.out.println(neighbours);
    }


    // DFS recursive method
    private boolean detectCycleDFS(int nodeID, int parent, int startNodeID) {
        if (visitedNodeList.contains(nodeID)) {
            return false;
        }

        visitedNodeList.add(nodeID);

        for (int neighborNodeID : neighbours.get(nodeID)) {
            if (neighborNodeID == startNodeID && parent != startNodeID) {
                return true;
            }

            if (detectCycleDFS(neighborNodeID, nodeID, startNodeID)) {
                return true;
            }
        }
        return false;
    }
    // Use DFS to determine if it can return to itself to check if it forms a loop
    public boolean ifCyc(int startNodeID){
        visitedNodeList = new ArrayList<>();
        return detectCycleDFS(startNodeID,startNodeID,startNodeID);
    }


}



