package com.CW3;


import java.util.*;

/**
 * 当设计图的数据结构时，通常会选择邻接表（adjacency list）作为表示图的一种方式。邻接表是一种用于表示图的数据结构，它将每个顶点的邻接顶点列表存储在一个集合中。下面是对邻接表的简要解释：
 *
 * Map<Site, List> adjacencyList;：在这里，我们使用一个 Map 来存储邻接表。其中，键（key）是表示一个顶点（或位置）的 Site 对象，值（value）是一个列表，用于存储与该顶点相邻的其他顶点。
 *
 * public void addEdge(Site source, Site destination)：addEdge 方法用于向图中添加一条边。在邻接表中，每个顶点对应一个键，其对应的值是一个列表，表示与该顶点相邻的其他顶点。因此，通过添加边的操作，我们向图的邻接表中添加了两个顶点之间的连接关系。
 *
 * public List getShortestPath(Site start, Site end)：通过邻接表，我们可以很方便地查找某个顶点的邻接顶点列表。这对于实现算法非常有用，比如在 BFS（广度优先搜索）中，我们需要查找每个顶点的邻接顶点，以便按层次遍历图。
 *
 * 通过使用邻接表，我们可以高效地表示图，并且能够在许多图算法中方便地查找顶点的邻接顶点，进行遍历以及查找最短路径等操作。
 */
public class Graph {
    private Map<Site, List<Site>> adjacencyList; // 使用邻接表表示图的结构

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    // 添加边，表示两个位置之间存在连通关系
    //如果 adjacencyList 中存在以 source 为键的条目，则返回该键对应的值；否则，使用提供的 lambda 表达式 k -> new ArrayList<>()
    //新建一个 ArrayList 对象，并将其与 source 关联；最后，将 destination 添加到该 ArrayList 中。
    public void addEdge(Site source, Site destination) {
        adjacencyList.computeIfAbsent(source, k -> new ArrayList<>()).add(destination);
        adjacencyList.computeIfAbsent(destination, k -> new ArrayList<>()).add(source);
    }

    // 使用 BFS 算法查找从起始位置到目标位置的最短路径
    public List<Site> getShortestPath(Site start, Site end) {
        Map<Site, Site> parentMap = new HashMap<>(); // 记录每个位置的父节点，用于回溯构造最短路径
        Queue<Site> queue = new LinkedList<>(); // 使用队列进行 BFS 遍历
        Set<Site> visited = new HashSet<>(); // 记录已经访问过的位置

        queue.add(start); // 将起始位置加入队列
        visited.add(start); // 标记起始位置为已访问

        // 开始 BFS 遍历
        while (!queue.isEmpty()) {
            Site current = queue.poll(); // 取出队首位置

            if (current.equals(end)) {
                // 找到目标位置，根据 parentMap 回溯构造最短路径
                List<Site> path = new ArrayList<>();
                Site temp = end;
                while (!temp.equals(start)) {
                    path.add(temp);
                    temp = parentMap.get(temp); // 获取当前位置的父节点
                }
                path.add(start);
                Collections.reverse(path); // 翻转路径列表，得到起始位置到目标位置的最短路径
                return path;
            }

            List<Site> neighbors = adjacencyList.get(current); // 获取当前位置的相邻位置列表
            if (neighbors != null) {
                for (Site neighbor : neighbors) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor); // 标记相邻位置为已访问
                        parentMap.put(neighbor, current); // 记录当前位置为相邻位置的父节点
                        queue.add(neighbor); // 将相邻位置加入队列，继续 BFS 遍历
                    }
                }
            }
        }

        return null; // 未找到路径，返回空
    }
}