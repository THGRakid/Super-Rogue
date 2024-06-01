package com.CW3.graph;

import java.util.ArrayList;
import java.util.Arrays;

public class Dungeon extends AbstractGraph<Site>{

    //保存所有走廊的nodeID
    private ArrayList<Integer> corridorNodeIDlist;
    //保存环状走廊nodeid
    private ArrayList<Integer> connectedCorridorNodeIDlist;

    private ArrayList<Integer> dfsPassedNodelist;



    // initialize a new dungeon based on the given board
    public Dungeon(char[][] board) {
        super(board);

        isNeighborValid();

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

        System.out.println(neighbours);//打印出中心点附近的邻居点
    }


    public void isNeighborValid(){ //看这个点是否是九宫格并且是否合法，合法就是邻居
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //遍历格内每一个点
                //得到中心点
                int nodeID = i * N + j;
                //遍历周围九宫格
                for (int offSetI = -1; offSetI <= 1; offSetI++) {
                    for (int offSetJ = -1; offSetJ <= 1; offSetJ++) {
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
                            if (neighbours.containsKey(nodeID)) {
                                neighbours.get(nodeID).add(gridNodeID);
                            } else {
                                neighbours.put(nodeID, new ArrayList<>(Arrays.asList(gridNodeID)));
                            }
                        }
                    }
                }

            }
        }
    }


    //dfs递归的方法

    public boolean DFS(int nodeID,int parent,int startNodeID){
        if (dfsPassedNodelist.contains(nodeID) ){
            return false;
        }
        //没有就接着访问
        dfsPassedNodelist.add(nodeID);
        //递归遍历dfs相领节点
        for (int neighborNodeID:neighbours.get(nodeID)){
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
        dfsPassedNodelist = new ArrayList<>();
        return DFS(startNodeID,startNodeID,startNodeID);
    }



    //返回之前，浅拷贝（int类型简答）
    public ArrayList<Integer> getCorridorNodeIDlist() {
        return (ArrayList<Integer>) corridorNodeIDlist.clone();
    }
    public ArrayList<Integer> getConnectedCorridorNodeIDlist() {
        return (ArrayList<Integer>)connectedCorridorNodeIDlist.clone();
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


}



