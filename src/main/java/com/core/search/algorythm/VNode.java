package com.core.search.algorythm;

public class VNode {
    int val; // 顶点值
    ArcNode first;// 指向单链表中第一个边信息

    public VNode() {}

    public VNode(int val, ArcNode first) {
        this.val = val;
        this.first = first;
    }
}
