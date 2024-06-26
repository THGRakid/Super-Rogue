package com.CW3.graph;

/**
 * 标记一处地点
 */
public class Site {
    public static final Site DEFAULT_INITIAL_LOCATION = new Site(-1,-1);
    private int i;
    private int j;

    // initialize board from file
    public Site(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int i() { return i; }
    public int j() { return j; }

    // Manhattan distance between invoking Site and w
    // v 和 w 之间的最短直线距离
    public int manhattanTo(Site w) {
        Site v = this;
        int i1 = v.i();
        int j1 = v.j();
        int i2 = w.i();
        int j2 = w.j();
        return Math.abs(i1 - i2) + Math.abs(j1 - j2);
    }

    // does invoking site equal site w?
    public boolean equals(Site w) {
        return (manhattanTo(w) == 0);
    }

}
