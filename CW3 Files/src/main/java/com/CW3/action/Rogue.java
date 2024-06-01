package com.CW3.action;

import com.CW3.game.Game;
import com.CW3.graph.Dungeon;
import com.CW3.graph.Site;

import java.util.ArrayList;
import java.util.Collections;

public class Rogue {
    private Game game;
    private Dungeon dungeon;
    private int N;

    public Rogue(Game game) {
        this.game    = game;
        this.dungeon = game.getDungeon();
        this.N       = dungeon.size();
    }
    //算一个点到其他的所有点的距离
    public ArrayList<Integer> computerThisNodetoOtherNode(int thisNodeID,ArrayList<Integer>otherNOdeID){
        ArrayList<Integer> ditances = new ArrayList<>();
        for (int otherNodeID:otherNOdeID){
            int dist = dungeon.dijsktra(thisNodeID,otherNodeID);
            ditances.add(dist);
        }
        return ditances;
    }

    // TAKE A RANDOM LEGAL MOVE
    // YOUR MAIN TASK IS TO RE-IMPLEMENT THIS METHOD TO DO SOMETHING INTELLIGENT
    public Site move() {
        Site monster = game.getMonsterSite();
        Site rogue = game.getRogueSite();
        Site move = null;

        //1.得到周围有效的九宫格,存储nodeid
        ArrayList<Site> gridSites = new ArrayList<>();
        for (int offsetI = -1; offsetI <= 1; offsetI++) {
            for (int offsetJ = -1; offsetJ <= 1; offsetJ++) {
                if (offsetI == 0 && offsetJ == 0) {
                    //判断怪物有没有堵着路，如果怪物就在旁边，就不能把怪物格子添加到储存的格子
                    if (dungeon.dijsktra(dungeon.getNodeidFromsite(rogue), dungeon.getNodeidFromsite(monster)) == 1) {
                        continue;
                    }
                }
                //怪物是否在九宫格
                if (monster.i() == rogue.i() + offsetI && monster.j() == rogue.j() + offsetJ) {
                    continue;

                }
                Site gridSite = new Site(rogue.i() + offsetI, rogue.j() + offsetJ);
                if (dungeon.isLegalMove(rogue, gridSite)) {
                    gridSites.add(gridSite);
                }

            }
        }

        //如果gridsite为0
        if (gridSites.size() == 0) {
            return rogue;
        }

        //2.计算有效的九宫格到走廊的最短距离
        ArrayList<Integer> corridorDistances = new ArrayList<>();
        for (Site gridSite : gridSites) {
            //计算他到走廊的距离-计算走到环中走廊的值
            int gridSiteNodeID = dungeon.getNodeidFromsite(gridSite);

            //得到环中走廊
            ArrayList<Integer> connectedCorrNodelist = dungeon.getConnectedCorridorNodeIDlist();
            //加限制，计算对于每一个环中走廊，谁先能到走廊，如果怪物能到，走廊不安全，剔除这个走廊
            for (int i = connectedCorrNodelist.size() - 1; i >= 0; i--) {
                int thisCorrNodeID = connectedCorrNodelist.get(i);
                int MonTOthisCorr = dungeon.dijsktra(dungeon.getNodeidFromsite(monster), thisCorrNodeID);
                int RogTOthisCorr = dungeon.dijsktra(dungeon.getNodeidFromsite(rogue), thisCorrNodeID);
                if (RogTOthisCorr >= MonTOthisCorr) {
                    //怪物能走到，这个走廊就不安全
                    connectedCorrNodelist.remove(i);
                }

            }

            ArrayList<Integer> distanceToEacheCorr = computerThisNodetoOtherNode(gridSiteNodeID, connectedCorrNodelist);
            //如果格子到环中走廊的距离为空
            if (distanceToEacheCorr.size() == 0) {
                corridorDistances.add(Integer.MAX_VALUE);
            } else {
                //挑选出最小距离作为走廊的距离
                int minDistance = Collections.min(distanceToEacheCorr);
                corridorDistances.add(minDistance);
            }
        }
        //判断走廊是否能走
        boolean isCorr = false;
        for (int corridorDistance : corridorDistances) {
            if (corridorDistance != Integer.MAX_VALUE) {
                isCorr = true;
                break;
            }
        }
        //2.1没有走廊，就走远离怪物的格子
        if (!isCorr) {
            //远离怪物的格子走
            ArrayList<Integer> distances = new ArrayList<>();
            for (Site gridSite : gridSites) {
                distances.add(dungeon.dijsktra(gridSite.i() * N + gridSite.j(), monster.i() * N + monster.j()));
            }
            //2.2找到离怪物最远的格子
            int maxDistance = Collections.max(distances);
            int maxDistanceIndex = distances.indexOf(maxDistance);
            move = gridSites.get(maxDistanceIndex);
            return move;
        } else {
            //3.找到离corr最近的
            int minDistance = Collections.min(corridorDistances);
            int minDistanceIndex = corridorDistances.indexOf(minDistance);
            move = gridSites.get(minDistanceIndex);

        }
        return move;

    }

}


