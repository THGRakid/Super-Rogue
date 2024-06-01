package com.CW3.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Dungeon {
    private boolean[][] isRoom;        // is v-w a room site?
    private boolean[][] isCorridor;    // is v-w a corridor site?
    private int N;                     // dimension of dungeon
    public int offSetI;
    private int offSetJ;
    //保存所有走廊的nodeID
    private ArrayList<Integer> corridorNodeIDlist;
    //保存环状走廊nodeid
    private ArrayList<Integer> connectedCorridorNodeIDlist;

    private ArrayList<Integer> DfsPassedNodelist;

    private HashMap<Integer, ArrayList<Integer>> linjiebiaoMap;


    // initialize a new dungeon based on the given board
    public Dungeon(char[][] board) {
        N = board.length;
        linjiebiaoMap = new HashMap<>();
        isRoom = new boolean[N][N];
        isCorridor = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == '.') isRoom[i][j] = true;
                else if (board[i][j] == '+') isCorridor[i][j] = true;
            }
        }

        isGridlegalAddNeighbor();

        //遍历格子找到走廊存
        corridorNodeIDlist =new ArrayList<>();
        for (int i=0;i<N;i++){
            for (int j=0;j<N;j++){
                Site thisSite = new Site(i,j);
                if (isCorridor(thisSite)){
                    corridorNodeIDlist.add(getNodeidFromsite(thisSite));
                }
            }
        }
        //生成环状走廊的nodeidlist
        connectedCorridorNodeIDlist = new ArrayList<>();
        for (int corrNodeID:corridorNodeIDlist){
            //判断走廊是不是在环中
            if (ifCyc(corrNodeID)){
                connectedCorridorNodeIDlist.add(corrNodeID);
            }

        }

        System.out.println(linjiebiaoMap);//打印出中心点附近的邻居点
    }


    public void isGridlegalAddNeighbor(){ //看这个点是否是九宫格并且是否合法，合法就是邻居
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //遍历格内每一个点
                //得到中心点
                int nodeID = i * N + j;
                //遍历周围九宫格
                for (offSetI = -1; offSetI <= 1; offSetI++) {
                    for (offSetJ = -1; offSetJ <= 1; offSetJ++) {
                        if (offSetI == 0 && offSetJ == 0) {
                            continue;
                        }
                        //得到九宫格内的某个点的id
                        int gridNodeID = ((i + offSetI) * N) + (j + offSetJ);
                        //如果移动合法
                        Site center = new Site(i, j);//中心点
                        Site gridNode = new Site(i + offSetI, j + offSetJ);//中心点附近的某个点
                        //判断能否走过去
                        if (isLegalMove(center, gridNode)) {
                            //把邻居添加到邻接表里
                            if (linjiebiaoMap.containsKey(nodeID)) {
                                linjiebiaoMap.get(nodeID).add(gridNodeID);
                            } else {
                                linjiebiaoMap.put(nodeID, new ArrayList<>(Arrays.asList(gridNodeID)));
                            }


                        }

                    }
                }

            }


        }
    }


    //dfs递归的方法

    public boolean DFS(int nodeID,int parent,int startNodeID){
        if (DfsPassedNodelist.contains(nodeID) ){
            return false;
        }
        //没有就接着访问
        DfsPassedNodelist.add(nodeID);
        //递归遍历dfs相领节点
        for (int neighborNodeID:linjiebiaoMap.get(nodeID)){
            //判断邻居是不是startnodeid
            if (neighborNodeID == startNodeID && parent != startNodeID){
                return true;
            }
            //如果找到递归的值 需要接收一下
            if (DFS(neighborNodeID,nodeID,startNodeID)){
                return true;
            }
        }
        return false;
    }
    //用dfs判断能否回到自己来判断是否成为一个环
    public boolean ifCyc(int startNodeID){
        DfsPassedNodelist = new ArrayList<>();
        return DFS(startNodeID,startNodeID,startNodeID);
    }



    //返回之前，浅拷贝（int类型简答）
    public ArrayList<Integer> getCorridorNodeIDlist() {
        return (ArrayList<Integer>) corridorNodeIDlist.clone();
    }
    public ArrayList<Integer> getConnectedCorridorNodeIDlist() {
        return (ArrayList<Integer>)connectedCorridorNodeIDlist.clone();
    }





    public int dijsktra(int stratNodeID, int endNodeID) {
        //记录已经有点的集合，点的nodeid和原点到这个点的距离
        HashMap<Integer, Integer> distanceMap = new HashMap<>();
        HashMap<Integer, Integer> waitingMap = new HashMap<>();
        //初始化，将原点放进去
        waitingMap.put(stratNodeID, 0);
        //进入循环，直到waitingmap为空
        while (!waitingMap.isEmpty()) {
            //算出waitingmap中的最小值，nodeid distance
            int minNodeID = -1;
            int minDistance = Integer.MAX_VALUE;
            //下面这一步操作完就是都是最小值
            for (Map.Entry<Integer,Integer> entry:waitingMap.entrySet()) {
                int thisNodeID = entry.getKey();
                int thisNodeDistance = entry.getValue();
                //这点值比较是否比目前的最小值要小
                if (thisNodeDistance < minDistance) {
                    minDistance = thisNodeDistance;
                    minNodeID = thisNodeID;
                }
            }
            //将最短距离的node拿出来
            waitingMap.remove(minNodeID);
            //更新distance waitingmap
            distanceMap.put(minNodeID, minDistance);
            //看这个点能到的点，距离是否会更近
            for (int neighborNodeID:linjiebiaoMap.get(minNodeID)) {
                if (distanceMap.containsKey(neighborNodeID)) {
                    continue;
                }
                if (waitingMap.containsKey(neighborNodeID)) {
                    if (minDistance + 1 < waitingMap.get(neighborNodeID)) {
                        waitingMap.put(neighborNodeID, minDistance);
                    }
                } else {
                    //如果waitingmap里不含这个邻居
                    //报这个邻居加入到waitingmap中去
                    waitingMap.put(neighborNodeID, minDistance + 1);
                }
            }


        }
        //watingmap为空，跳出while
        //distancemap中有endNodeID
        if (distanceMap.containsKey(endNodeID)) {
            return distanceMap.get(endNodeID);
        }
        //说明startid无法走到endNodeid
        return -1;
    }



    //一个得到对应site下的nodeid的方法

    public int getNodeidFromsite(Site site){
        int i = site.i();
        int j = site.j();
        return i*N+j;
    }

    //计算nodeid到site

    public Site getsiteidFromnodeid(int nodeiID){
        int i = nodeiID/N;
        int j = nodeiID%N;
        Site siteFromnodeId = new Site(i,j);
        return siteFromnodeId;
    }



    // return dimension of dungeon
    public int size() {
        return N;
    }

    // does v correspond to a corridor site?
    public boolean isCorridor(Site v) {
        int i = v.i();
        int j = v.j();
        if (i < 0 || j < 0 || i >= N || j >= N) return false;
        return isCorridor[i][j];
    }

    // does v correspond to a room site?
    public boolean isRoom(Site v) {
        int i = v.i();
        int j = v.j();
        if (i < 0 || j < 0 || i >= N || j >= N) return false;
        return isRoom[i][j];
    }

    // does v correspond to a wall site?
    public boolean isWall(Site v) {
        return (!isRoom(v) && !isCorridor(v));
    }

    // does v-w correspond to a legal move?
    public boolean isLegalMove(Site v, Site w) {
        int i1 = v.i();
        int j1 = v.j();
        int i2 = w.i();
        int j2 = w.j();
        if (i1 < 0 || j1 < 0 || i1 >= N || j1 >= N) return false;
        if (i2 < 0 || j2 < 0 || i2 >= N || j2 >= N) return false;
        if (isWall(v) || isWall(w)) return false;
        if (Math.abs(i1 - i2) > 1) return false;
        if (Math.abs(j1 - j2) > 1) return false;
//判断两个地图连接的地方
        if (isRoom(v) && isRoom(w)) {
            if (v.i() == w.i() || v.j() == w.j()){
                return true;
            }else {
                if (isRoom(new Site(v.i(), w.j())) && isRoom(new Site(w.i(), v.j()))){
                    return true;
                }else {
                    return false;
                }
            }

        }

        if (i1 == i2) return true;
        if (j1 == j2) return true;

        return false;
    }
}



