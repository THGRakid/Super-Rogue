package com.CW3.GraphTest;

public class Edge {
    public int u; // Starting vertex of the edge
    public int v; // Ending vertex of the edge
    /** Construct an edge for (u, v) */
    public Edge(int u, int v) {
        this.u = u;
        this.v = v;
    }
    public boolean equals(Object o) {
        return u == ((Edge)o).u && v == ((Edge)o).v;
    }
}
