package com.core.search.link;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class PageRank {
    // 阻尼系数
    static final double DECAY_FACTOR = 0.85;
    int m; //最大迭代次数
    double cv;// 迭代收敛值
    int pageNum; // 网页总数
    PageGraph graph; // page 有向图

    public PageRank(PageGraph graph) {
        this.m = 50;
        this.cv = 0.0001;
        this.graph = graph;
        this.pageNum = this.graph.pageNum;
    }

    public void rank(){
        // 将图中没有出链的情况的顶点修改为对所有顶点出链
        linkOutAll();
        // 为每一个页面初始化PR值
        Map<Page,Double> ranks = new HashMap<>();
        this.graph.pageList.forEach((page -> ranks.put(page,(1.0/this.pageNum))));

        // 计算 (1-d) / N
        double decayVal = (1 - DECAY_FACTOR) / this.pageNum;


        boolean COMPLETED = Boolean.FALSE;
        for (int i = 0; i < m; i++) {
            double change = 0.0;
            for (int v = 0; v < this.pageNum; v++) {
                Page current = this.graph.pageList.get(v);
                double rank = 0.0;
                // 计算 阻尼系数 * 转移矩阵
                for (int w = 0; w < this.pageNum; w++) {
                    // 获取其他page
                    Page p = this.graph.pageList.get(w);
                    // 获取其他page的链出页
                    List<Page> linkOutPageList = this.graph.getLinkedOutPageList(p);
                    // 其他page的链出页是否包含当前正在被访问的页
                    if (CollectionUtils.isNotEmpty(linkOutPageList) && linkOutPageList.contains(current)) {
                        rank += DECAY_FACTOR * (ranks.get(p) / this.graph.getLinkedOutPageList(p).size());
                    }
                }

                rank += decayVal;
                // 计算这一次迭代当前页的rank的的差值，用于判断还要继续迭代
                change += Math.abs(ranks.get(current) - rank);
                // 将当前的页的默认rank更新
                ranks.put(current,rank);
            }
            System.out.println("第 "+i+" 次迭代,各个页rank值情况如下：");
            ranks.forEach((k,v) -> {
                System.out.println(k.url + " => "+v);
            });
            if (change < this.cv) {
                COMPLETED = Boolean.TRUE;
                break;
            }
        }
        if (COMPLETED) {
            System.out.println("迭代完成，终止迭代");
        } else {
            System.out.println("迭代失败");
        }
    }

    // 将图中没有出链的情况的顶点修改为对所有顶点出链
    public void linkOutAll() {
        for(Page from : this.graph.pageList) {
            if (CollectionUtils.isEmpty(this.graph.getLinkedOutPageList(from))) {
                for(Page to : this.graph.pageList) {
                    this.graph.addEdge(from,to);
                }
            }
        }
    }

    public static void main(String[] args) {
        PageGraph graph = new PageGraph(5);
        List<Page> pageList = buildPageList("E:\\code\\ecom-review-core\\src\\main\\java\\com\\core\\search\\link\\page.txt");
        graph.addVertex(pageList);
        graph.addEdge(0,1);
        graph.addEdge(0,2);
        graph.addEdge(0,3);
        graph.addEdge(0,4);
        graph.addEdge(1,0);
        graph.addEdge(1,3);
        graph.addEdge(2,0);
        graph.addEdge(2,3);
        graph.addEdge(3,2);
        graph.addEdge(4,0);
        graph.addEdge(4,2);
        PageRank rank = new PageRank(graph);
        rank.rank();

    }

    public static List<Page> buildPageList(String filepath) {
        File file = FileUtils.getFile(filepath);
        List<String> lines = null;
        try {
            lines = FileUtils.readLines(file, Charset.forName("UTF-8"));
            List<Page> pageList = new ArrayList<>();
            for (String line : lines) {
                String[] fields = StringUtils.split(line,' ');
                String[] keywords = StringUtils.split(fields[1],",");
                Page page = new Page(fields[0], Arrays.asList(keywords));
                pageList.add(page);
            }
            return pageList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
