package com.core.search.link;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class HitsTool {
    // 保存Page关系的有向图
    PageGraph graph;
    // 最大迭代次数
    int m;
    //误差值，用于收敛判断
    double delta = 0.0001;
    // 当前所有page的初始authority值和hub值数组
    Map<Page,Double> hub;
    Map<Page,Double> authority;
    Map<Page,Double> temp;
    public HitsTool(PageGraph graph) {
        this.m = 50;
        this.delta = 0.0001;
        this.graph = graph;
        this.hub = new HashMap<>();
        this.authority = new HashMap<>();
        for (Page page : this.graph.pageList) {
            this.authority.put(page,1.0);
            this.hub.put(page,1.0);
        }
    }

    public void hits() {
        boolean FINISHED = Boolean.FALSE;
        if (this.graph == null) return;
        for (int i = 0; i < m; i++) {
            // 每一轮迭代的变化值，这个值如果小于指定的迭代终止值，则停止迭代
            double change = 0.0;
            // 标准值
            double norm = 0.0;
            temp = new HashMap<>();
            for (Map.Entry<Page,Double> entry : authority.entrySet()) {
                temp.put(entry.getKey(),entry.getValue());
            }
            for (Page page : this.graph.pageList) {
                double av = 0.0;
                for (Page in : this.graph.getLinkedInPageList(page)) {
                    av += this.hub.get(in);
                }
                this.authority.put(page,av);
                // 计算每一个页面计算的authority值的平方
                norm += Math.pow(this.authority.get(page),2);
            }
            // 标准化
            norm = Math.sqrt(norm);
            // 计算迭代权值差标准差
            for (Page page : this.graph.pageList) {
                this.authority.put(page,this.authority.get(page)/norm);
                change += Math.abs(temp.get(page) - this.authority.get(page));
            }


            // 计算每个页面的hub值
            norm = 0.0;
            temp = new HashMap<>();
            for (Map.Entry<Page,Double> entry : hub.entrySet()) {
                temp.put(entry.getKey(),entry.getValue());
            }
            for (Page page : this.graph.pageList) {
                double hv = 0.0;
                for (Page out : this.graph.getLinkedOutPageList(page)) {
                    hv += this.authority.get(out);
                }
                this.hub.put(page,hv);
                // 计算每一个页面计算的authority值的平方
                norm += Math.pow(this.hub.get(page),2);
            }
            // 标准化
            norm = Math.sqrt(norm);

            for (Page page : this.graph.pageList) {
                // 更新page的hub值
                this.hub.put(page,this.hub.get(page)/norm);
                // 计算迭代终止条件
                change += Math.abs(temp.get(page) - this.hub.get(page));
            }

            if (change < delta) {
                FINISHED = Boolean.TRUE;
                break;
            }
        }
        if (FINISHED) {
            System.out.println("迭代完成!!!");
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
        HitsTool hitsTool = new HitsTool(graph);
        hitsTool.hits();

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
