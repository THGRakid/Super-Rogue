package com.CW3;

import java.util.ArrayList;
import java.util.List;

public class Rogue {
    private Game game;
    private Dungeon dungeon;
    private int N;

    public Rogue(Game game) {
        this.game = game;
        this.dungeon = game.getDungeon();
        this.N = dungeon.size();
    }

    /**
     * 获取盗贼的下一个移动位置
     * 通过最大化与怪物的距离来选择移动位置
     * @return 下一个移动位置
     */
    public Site move() {
        Site monster = game.getMonsterSite();
        Site rogue = game.getRogueSite();

        List<Site> legalMoves = getLegalMoves(rogue);
        Site bestMove = rogue;
        int maxDistance = monster.manhattanTo(rogue);

        for (Site move : legalMoves) {
            int distance = monster.manhattanTo(move);
            if (distance > maxDistance) {
                maxDistance = distance;
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * 获取给定位置的所有合法移动
     * @param site 当前的位置
     * @return 所有合法移动的位置列表
     */
    private List<Site> getLegalMoves(Site site) {
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
