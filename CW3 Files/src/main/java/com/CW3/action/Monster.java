package com.CW3.action;

import com.CW3.game.Game;
import com.CW3.graph.Dungeon;
import com.CW3.graph.Site;

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
        // 获取怪物当前的位置
        Site monster = game.getMonsterSite();
        // 获取盗贼当前的位置
        Site rogue = game.getRogueSite();

        // 使用BFS（广度优先搜索）来找到从怪物到盗贼的最短路径
        // 创建一个队列来存储待访问的节点
        Queue<Site> queue = new LinkedList<>();
        // 创建一个二维布尔数组来标记已经访问过的节点
        boolean[][] visited = new boolean[N][N];
        // 创建一个二维Site数组来记录到达每个节点的上一个节点（用于重构路径）
        Site[][] edgeTo = new Site[N][N];

        // 将怪物位置加入队列，并标记为已访问
        queue.add(monster);
        visited[monster.i()][monster.j()] = true;

        // 当队列不为空时，继续搜索
        while (!queue.isEmpty()) {
            // 从队列中取出一个节点作为当前节点
            Site current = queue.poll();
            // 如果当前节点是盗贼的位置，则搜索结束
            if (current.equals(rogue)) break;

            // 遍历当前节点的所有合法移动
            for (Site neighbor : getLegalMoves(current)) {
                // 如果该邻居节点尚未被访问过
                if (!visited[neighbor.i()][neighbor.j()]) {
                    // 将邻居节点加入队列
                    queue.add(neighbor);
                    // 标记邻居节点为已访问
                    visited[neighbor.i()][neighbor.j()] = true;
                    // 记录到达邻居节点的上一个节点是当前节点
                    edgeTo[neighbor.i()][neighbor.j()] = current;
                }
            }
        }

        // 重构路径
        // 如果盗贼的位置没有被访问过，说明没有路径，返回怪物位置
        if (!visited[rogue.i()][rogue.j()]) return monster; // No path found

        // 从盗贼位置开始，沿着edgeTo数组回溯到怪物位置
        Site step = rogue;
        // 当当前节点不是怪物且上一个节点不是怪物时（防止无限循环）
        while (!step.equals(monster) && edgeTo[step.i()][step.j()] != monster) {
            // 将当前节点设置为上一个节点
            step = edgeTo[step.i()][step.j()];
        }

        // 返回路径上的倒数第二个节点（通常是盗贼应该移动到的位置）
        // 注意：这里返回的是盗贼应该移动到的位置，而不是整个路径
        return step;
    }


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
