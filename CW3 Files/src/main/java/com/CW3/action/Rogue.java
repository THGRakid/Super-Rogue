package com.CW3.action;

import com.CW3.game.Game;
import com.CW3.graph.Dungeon;
import com.CW3.graph.Site;

import java.util.*;

public class Rogue extends RoleAction{
    private Game game;
    private Dungeon dungeon;
    private int N;

    private ArrayList<Integer> corridorList;
    private ArrayList<Integer> connectedCorridorList;
    private HashMap<Integer, List<Integer>> neighbours;

    public Rogue(Game game) {
        this.game = game;
        this.dungeon = game.getDungeon();
        this.N = dungeon.size();
        this.neighbours = new HashMap<>();

        dungeon.addLegalNeighborsToGridNodes();
        initializeCorridorLists();
    }

    public Site move() {
        Site monster = game.getMonsterSite();
        Site rogue = game.getRogueSite();
        Site move = null;

        ArrayList<Site> validGridSites = getValidGridSites(rogue, monster);

        if (validGridSites.isEmpty()) {
            return rogue;
        }

        ArrayList<Integer> corridorDistances = calculateCorridorDistances(validGridSites, rogue, monster);

        boolean hasAccessibleCorridor = corridorDistances.stream().anyMatch(distance -> distance != Integer.MAX_VALUE);

        if (!hasAccessibleCorridor) {
            move = getFurthestSiteFromMonster(validGridSites, monster);
        } else {
            move = getClosestSiteToCorridor(validGridSites, corridorDistances);
        }

        return move;
    }

    public int calculateShortestPath(int startNodeId, int endNodeId) {
        if (startNodeId == endNodeId) {
            return 0;
        }

        HashMap<Integer, List<Integer>> neighbours = getNeighbours();
        Queue<Integer> queue = new LinkedList<>();
        HashMap<Integer, Integer> distances = new HashMap<>();

        queue.add(startNodeId);
        distances.put(startNodeId, 0);

        while (!queue.isEmpty()) {
            int currentNode = queue.poll();
            int currentDistance = distances.get(currentNode);

            for (int neighbor : neighbours.getOrDefault(currentNode, new ArrayList<>())) {
                if (!distances.containsKey(neighbor)) {
                    distances.put(neighbor, currentDistance + 1);
                    queue.add(neighbor);

                    if (neighbor == endNodeId) {
                        return distances.get(neighbor);
                    }
                }
            }
        }

        return Integer.MAX_VALUE; // 如果没有路径
    }


    public HashMap<Integer, List<Integer>> getNeighbours() {
        return neighbours;
    }

    public ArrayList<Integer> getConnectedCorridorList() {
        return new ArrayList<>(connectedCorridorList);
    }

    


    private void initializeCorridorLists() {
        corridorList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Site thisSite = new Site(i, j);
                if (dungeon.isCorridor(thisSite)) {
                    corridorList.add(dungeon.getNodeIDFromSite(thisSite));
                }
            }
        }

        connectedCorridorList = new ArrayList<>();
        for (int corridor : corridorList) {
            if (dungeon.ifCyc(corridor)) {
                connectedCorridorList.add(corridor);
            }
        }

        System.out.println(neighbours);
    }

    private ArrayList<Site> getValidGridSites(Site rogue, Site monster) {
        ArrayList<Site> gridSites = new ArrayList<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    if (calculateShortestPath(dungeon.getNodeIDFromSite(rogue), dungeon.getNodeIDFromSite(monster)) == 1) {
                        continue;
                    }
                }

                if (monster.i() == rogue.i() + dx && monster.j() == rogue.j() + dy) {
                    continue;
                }

                Site gridSite = new Site(rogue.i() + dx, rogue.j() + dy);
                if (dungeon.isLegalMove(rogue, gridSite)) {
                    gridSites.add(gridSite);
                }
            }
        }

        return gridSites;
    }

    private ArrayList<Integer> calculateCorridorDistances(ArrayList<Site> gridSites, Site rogue, Site monster) {
        ArrayList<Integer> corridorDistances = new ArrayList<>();

        for (Site gridSite : gridSites) {
            int gridSiteNodeID = dungeon.getNodeIDFromSite(gridSite);
            ArrayList<Integer> connectedCorridors = getSafeCorridors(rogue, monster);

            ArrayList<Integer> distancesToCorridors = calculateNodeDistances(gridSiteNodeID, connectedCorridors);

            if (distancesToCorridors.isEmpty()) {
                corridorDistances.add(Integer.MAX_VALUE);
            } else {
                corridorDistances.add(Collections.min(distancesToCorridors));
            }
        }

        return corridorDistances;
    }

    private ArrayList<Integer> getSafeCorridors(Site rogue, Site monster) {
        ArrayList<Integer> connectedCorridors = getConnectedCorridorList();

        connectedCorridors.removeIf(corridorNodeID -> {
            int monsterToCorridor = calculateShortestPath(dungeon.getNodeIDFromSite(monster), corridorNodeID);
            int rogueToCorridor = calculateShortestPath(dungeon.getNodeIDFromSite(rogue), corridorNodeID);
            return rogueToCorridor >= monsterToCorridor;
        });

        return connectedCorridors;
    }

    private Site getFurthestSiteFromMonster(ArrayList<Site> gridSites, Site monster) {
        ArrayList<Integer> distancesFromMonster = new ArrayList<>();
        int monsterNodeID = dungeon.getNodeIDFromSite(monster);

        for (Site gridSite : gridSites) {
            int gridSiteNodeID = dungeon.getNodeIDFromSite(gridSite);
            int distance = calculateShortestPath(gridSiteNodeID, monsterNodeID);
            distancesFromMonster.add(distance);
        }

        int maxDistance = Collections.max(distancesFromMonster);
        int indexOfMaxDistance = distancesFromMonster.indexOf(maxDistance);
        return gridSites.get(indexOfMaxDistance);
    }

    private Site getClosestSiteToCorridor(ArrayList<Site> gridSites, ArrayList<Integer> corridorDistances) {
        int minDistance = Collections.min(corridorDistances);
        int indexOfMinDistance = corridorDistances.indexOf(minDistance);
        return gridSites.get(indexOfMinDistance);
    }


    public ArrayList<Integer> calculateNodeDistances(int fromNodeID, ArrayList<Integer> toNodeIDs) {
        ArrayList<Integer> distances = new ArrayList<>();
        for (int toNodeID : toNodeIDs) {
            int distance = calculateShortestPath(fromNodeID, toNodeID);
            distances.add(distance);
        }
        return distances;
    }
}
