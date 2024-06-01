package com.CW3.action;

import com.CW3.graph.Site;

import java.util.List;

public interface Action {
    public List<Site> getLegalMoves(Site site);
}
