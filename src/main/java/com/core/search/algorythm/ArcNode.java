package com.core.search.algorythm;

public class ArcNode {
    int adjvex; // 邻接顶点，即弧尾指向的顶点
    ArcNode next; // 下一个边信息

    public ArcNode() {
    }

    public ArcNode(int adjvex, ArcNode next) {
        this.adjvex = adjvex;
        this.next = next;
    }
}
