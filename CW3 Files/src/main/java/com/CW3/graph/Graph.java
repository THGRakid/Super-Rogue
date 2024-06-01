package com.CW3.graph;

public interface Graph<V> {

    // return dimension of dungeon
    int size();

    // does v correspond to a corridor site?
    boolean isCorridor(Site v);

    // does v correspond to a room site?
    boolean isRoom(Site v);

    // does v correspond to a wall site?
    boolean isWall(Site v);

    // does v-w correspond to a legal move?
    boolean isLegalMove(Site v, Site w);

    // A method to get the nodeID under a corresponding site
    int getNodeIDFromSite(Site site);


    // Check if this point is a 3x3 grid and if it's valid, it's a neighbor
    void addLegalNeighborsToGridNodes();
}
