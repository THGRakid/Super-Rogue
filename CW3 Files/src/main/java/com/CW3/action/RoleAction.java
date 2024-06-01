package com.CW3.action;

import com.CW3.graph.Dungeon;
import com.CW3.graph.Site;

import java.util.ArrayList;
import java.util.List;

public abstract class RoleAction implements Action{
    private Dungeon dungeon;

    public RoleAction(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public RoleAction() {

    }

    /**
     * 获取给定位置的所有合法移动
     * @param site 当前的位置
     * @return 所有合法移动的位置列表
     */
    @Override
    public List<Site> getLegalMoves(Site site) {
        List<Site> legalMoves = new ArrayList<>();
        int[] directions = {-1, 0, 1};
        for (int di : directions) {
            for (int dj : directions) {
                if (di == 0 && dj == 0) continue;
                Site next = new Site(site.i() + di, site.j() + dj);
                if (dungeon.isLegalMove(site, next)) {
                    legalMoves.add(next);
                }
            }
        }
        return legalMoves;
    }



}
