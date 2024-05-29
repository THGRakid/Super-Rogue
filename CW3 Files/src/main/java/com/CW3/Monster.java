package com.CW3;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Monster {
    private Game game;
    private Dungeon dungeon;
    private int N;

    public Monster(Game game) {
        this.game = game;
        this.dungeon = game.getDungeon();
        this.N = dungeon.size();
    }

    /**
     * 获取怪物的下一个移动位置
     * 使用广度优先搜索（BFS）找到最短路径
     * @return 下一个移动位置
     */
    public Site move() {
        Site monster = game.getMonsterSite();
        Site rogue = game.getRogueSite();

        // BFS to find the shortest path
        Queue<Site> queue = new LinkedList<>();
        boolean[][] visited = new boolean[N][N];
        Site[][] edgeTo = new Site[N][N];

        queue.add(monster);
        visited[monster.i()][monster.j()] = true;

        while (!queue.isEmpty()) {
            Site current = queue.poll();
            if (current.equals(rogue)) break;

            for (Site neighbor : getLegalMoves(current)) {
                if (!visited[neighbor.i()][neighbor.j()]) {
                    queue.add(neighbor);
                    visited[neighbor.i()][neighbor.j()] = true;
                    edgeTo[neighbor.i()][neighbor.j()] = current;
                }
            }
        }

        // Reconstruct path
        if (!visited[rogue.i()][rogue.j()]) return monster; // No path found
        Site step = rogue;
        while (!step.equals(monster) && edgeTo[step.i()][step.j()] != monster) {
            step = edgeTo[step.i()][step.j()];
        }
        return step;
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
