package com.core.search.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SolrTools {
    static final String SOLR_URL = "http://localhost:8080/solr";

    public static void main(String[] args) {
        distributeSearch();
    }

    private static void query1() {
        try {
            SolrClient client = new HttpSolrClient(SOLR_URL);
            SolrQuery query = new SolrQuery("*:*");
            query.addField("id");
            query.addField("film");
            query.addField("director");
            query.addField("type");
            query.addField("area");
            query.setSort("score",SolrQuery.ORDER.desc);
            query.setRows(10);
            QueryResponse response = client.query("movie",query);
            SolrDocumentList documents = response.getResults();
            for (SolrDocument doc : documents) {
                System.out.println(doc.getFieldValue("id")+" | "+doc.getFieldValue("film")+" | "+doc.getFieldValue("type")+" | "+doc.getFieldValue("area"));
            }
            client.close();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void query2() {
        try {
            String url = "http://localhost:8080/solr/movie";
            SolrClient client = new HttpSolrClient.Builder(url).build();
            Map<String, String> queryParamMap = new HashMap<String, String>();
            queryParamMap.put("q","*:*");
            queryParamMap.put("fl","id,film,type,area");
            queryParamMap.put("sort","score desc");
            MapSolrParams queryMap = new MapSolrParams(queryParamMap);
            QueryResponse response = client.query(queryMap);
            SolrDocumentList documents = response.getResults();
            for (SolrDocument doc : documents) {
                System.out.println(doc.getFieldValue("id")+" | "+doc.getFieldValue("film")+" | "+doc.getFieldValue("type")+" | "+doc.getFieldValue("area"));
            }
            client.close();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void distributeIndex() {
        String collection = "catalog";
        // zookeeper客户端连接zookeeper集群超时时间，默认10000
        int zkClientTimeout = 30000;
        // zookeeper服务端等待客户端连接成功的最大超时时间，默认10000
        int zkConnectTimeout = 30000;
        String zkHost = "192.168.99.151:2181,192.168.99.152:2181,192.168.99.153:2181";
        CloudSolrClient.Builder builder = new CloudSolrClient.Builder();
        builder.withZkHost(zkHost);
        builder.sendUpdatesOnlyToShardLeaders();
        CloudSolrClient client = builder.build();
        client.setDefaultCollection(collection);
        client.setZkClientTimeout(zkClientTimeout);
        client.setZkConnectTimeout(zkConnectTimeout);
        // 设置是否并行更新索引文档
        client.setParallelUpdates(Boolean.TRUE);
        client.setIdField("id");
        // 设置collection缓存时间,默认分钟
        client.setCollectionCacheTTl(2);

        SolrInputDocument doc = new SolrInputDocument();
        try {
            doc.addField("id","127083299842555905");
            doc.addField("catName","洗衣机");
            doc.addField("brand","海尔");
            doc.addField("productName","海尔 定频 滚筒");
            doc.addField("size"," ");
            doc.addField("color","梦幻紫");
            doc.addField("gender","未知");
            doc.addField("price","440115");
            doc.addField("review","226673");
            doc.addField("createTime","2015-12-01 19:59:18");
            doc.addField("updateTime","2018-12-01 19:59:18");
            doc.addField("instock","true");
            doc.addField("onsale","false");
            doc.addField("new","false");
            final UpdateResponse updateResponse = client.add(doc);
            client.commit();
            client.close();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void distributeSearch(){
        String collection = "ecom";
        String shard = "shard1";
        // zookeeper客户端连接zookeeper集群超时时间，默认10000
        int zkClientTimeout = 30000;
        // zookeeper服务端等待客户端连接成功的最大超时时间，默认10000
        int zkConnectTimeout = 30000;
        String zkHost = "192.168.99.151:2181,192.168.99.152:2181,192.168.99.153:2181";
        CloudSolrClient.Builder builder = new CloudSolrClient.Builder();
        builder.withZkHost(zkHost);
        CloudSolrClient client = builder.build();
        client.setDefaultCollection(collection);
        client.setZkClientTimeout(zkClientTimeout);
        client.setZkConnectTimeout(zkConnectTimeout);

        try {
            SolrQuery query = new SolrQuery("*:*");
            query.setRows(0);
            // 查询collection中所有数据
            query.set("collection",collection);

            QueryResponse response = client.query(query);
            SolrDocumentList documents = response.getResults();
            System.out.println("分布式查询结果，有 "+documents.getNumFound()+" 条记录");
            // 指定切片上进行查询
            query.set("shards",shard);
            response = client.query(query);
            documents = response.getResults();
            System.out.println("切片1上有 "+documents.getNumFound()+" 条记录");
            client.close();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void index() {
        try {
            String url = "http://localhost:8080/solr/movie";
            SolrClient client = new HttpSolrClient.Builder(url).build();

            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id",1040);
            doc.addField("film","阿凡达");
            doc.addField("director","詹姆斯·卡梅隆");
            doc.addField("country","美国");
            doc.addField("score","9.5");
            doc.addField("runTime","150");
            doc.addField("releaseDate","20");
            doc.addField("review","55500");
            doc.addField("type","科幻");
            doc.addField("area","欧美");
            final UpdateResponse updateResponse = client.add(doc);
            client.commit("movie");
            client.close();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

    }
    public void delete() {

    }
}
