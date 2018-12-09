package com.core.common;

public class VEdge {
    VNode fromNode;
    VNode endNode;
    int weight;

    public VEdge(VNode fromNode, VNode endNode) {
        this.fromNode = fromNode;
        this.endNode = endNode;
        weight = 1;
    }

    public VEdge(VNode fromNode, VNode endNode, int weight) {
        this.fromNode = fromNode;
        this.endNode = endNode;
        this.weight = weight;
    }
}
