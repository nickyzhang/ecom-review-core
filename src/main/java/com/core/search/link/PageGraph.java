package com.core.search.link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageGraph {
    List<Page> pageList = null;
    int pageNum = 0;
    int edges;
    int[][] matrix;

    public PageGraph(int pageNum) {
        this.pageNum = pageNum;
        this.edges = 0;
        this.matrix = new int[this.pageNum][this.pageNum];
        this.pageList = new ArrayList<Page>(this.pageNum);
        for (int i = 0; i < pageNum; i++) {
            for (int j = 0; j < pageNum; j++) {
                matrix[i][j] = 0;
            }
        }
    }

    public void addVertex(String url, String keyword){
        List<String> keywordList = Arrays.asList(keyword.split(","));
        Page page = new Page(url,keywordList);
        this.pageList.add(page);
    }

    public void addVertex(List<Page> pageList){
        for (Page page : pageList) {
            this.pageList.add(page);
        }
    }

    // 添加两个顶点的边
    public void addEdge(int from, int to) {
        // 将对应矩阵中位置元素置为1，即表示两个顶点有边
        this.matrix[from][to] = 1;
        this.edges++;
    }

    // 添加两个顶点的边
    public void addEdge(Page src, Page dest) {
        int from = this.pageList.indexOf(src);
        int to = this.pageList.indexOf(dest);
        // 将对应矩阵中位置元素置为0，即表示两个顶点没有边
        this.matrix[from][to] = 1;
        this.edges++;
    }

    // 删除两个顶点的边
    public void deleteEdge(int from, int to) {
        // 将对应矩阵中位置元素置为0，即表示两个顶点没有边
        this.matrix[from][to] = 0;
        // 边数减少
        this.edges--;
    }

    // 获取有向图中顶点出度相关的顶点，即邻接点
    public List<Page> getLinkedOutPageList(Page page) {
        int v = this.pageList.indexOf(page);
        if (v == -1) return null;
        List<Page> targets = new ArrayList<Page>();
        for (int i = 0; i < pageNum; i++) {
            if (matrix[v][i] == 1) {
                targets.add(this.pageList.get(i));
            }
        }
        return targets;
    }

    // 获取有向图中顶点入度相关的顶点，即邻接点
    public List<Page> getLinkedInPageList(Page page) {
        int v = this.pageList.indexOf(page);
        if (v == -1) return null;
        List<Page> targets = new ArrayList<Page>();
        for (int i = 0; i < pageNum; i++) {
            if (matrix[i][v] == 1) {
                targets.add(this.pageList.get(i));
            }
        }
        return targets;
    }

    public int getVertexSize() {

        return this.pageList.size();
    }

    public int getEdgeSize() {

        return this.edges;
    }
}
