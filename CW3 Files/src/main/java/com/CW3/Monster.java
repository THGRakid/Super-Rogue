package com.CW3;

import java.util.List;

/**
 * 抓捕者
 */
public class Monster {
    private Game game;
    private Dungeon dungeon;
    private int N;

    public Monster(Game game) {
        this.game    = game;
        this.dungeon = game.getDungeon();
        this.N       = dungeon.size();
    }


    // TAKE A RANDOM LEGAL MOVE
    // YOUR TASK IS TO RE-IMPLEMENT THIS METHOD TO DO SOMETHING INTELLIGENT
    public Site move() {
        Site monster = game.getMonsterSite(); // 获取怪物的当前位置
        Site rogue = game.getRogueSite(); // 获取躲避者的位置

        // 创建图的实例
        Graph graph = new Graph();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Site current = new Site(i, j);
                if (dungeon.isLegalMove(monster, current)) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            Site next = new Site(i + dx, j + dy);
                            if (dungeon.isLegalMove(current, next)) {
                                graph.addEdge(current, next); // 添加合法的移动路径到图中
                            }
                        }
                    }
                }
            }
        }

        // 使用 BFS 查找怪物到躲避者的最短路径
        List<Site> shortestPath = graph.getShortestPath(monster, rogue);
        if (shortestPath != null && shortestPath.size() > 1) {
            return shortestPath.get(1); // 返回最短路径上的下一个位置作为移动目标
        } else {
            // 如果未找到有效路径，暂时先返回随机数值
            Site move = null;

            // take random legal move
            int n = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    Site site = new Site(i, j);
                    if (dungeon.isLegalMove(monster, site)) {
                        n++;
                        if (Math.random() <= 1.0 / n) move = site;
                    }
                }
            }
            return move;
        }
    }

}
