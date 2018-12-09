package com.core.common;

import java.util.ArrayList;
import java.util.List;

public class VGraph {
    List<VNode> nodeList;
    int[][] matrix;
    int nodeSize = 0, edgeSize = 0;
    int maxNodeNum;

    public VGraph(int maxNodeNum) {
        this.nodeList = new ArrayList<VNode>();
        this.maxNodeNum = maxNodeNum;
        for (int i = 0; i < maxNodeNum; i++) {
            for (int j = 0; j < maxNodeNum; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    // 添加顶点
    public void addNode(String label) {
        VNode node = new VNode(label);
        this.nodeList.add(node);
        this.nodeSize++;
    }


    // 批量添加顶点
    public void addNodes(String[] labels) {
        for (int i = 0; i < labels.length; i++) {
            addNode(labels[i]);
        }
    }

    // 为顶点添加边
    public void addEdge(VNode from, VNode to) {
        VEdge edge = new VEdge(from,to);
        from.toList.add(edge);
        to.fromList.add(edge);
        this.edgeSize++;
    }


    // 为顶点添加边
    public void addEdge(String from, String to) {
        VNode fromNode = getVNode(from);
        VNode toNode = getVNode(to);
        VEdge edge = new VEdge(fromNode,toNode);
        fromNode.toList.add(edge);
        toNode.fromList.add(edge);
        this.edgeSize++;
    }

    public VNode getVNode(String label) {
        for (int i = 0; i < this.nodeList.size(); i++) {
            if (this.nodeList.get(i).equals(label)) {
                return this.nodeList.get(i);
            }
        }
        return null;
    }

    public int getNodeSize() {

        return this.nodeSize;
    }

    public int getEdgeSize() {

        return this.edgeSize;
    }
}
