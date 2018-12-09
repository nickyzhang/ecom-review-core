package com.core.search.lucene.query;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.ConcurrentSkipListMap;

public class BooleanQueryTool {
    public static TopDocs search(String readPath, int fetchSize) throws Exception {
        Directory dir = FSDirectory.open(Paths.get(readPath));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
//        builder.add(new TermQuery(new Term("type","灾难")),BooleanClause.Occur.SHOULD);
        builder.add(new TermQuery(new Term("country","中国")),BooleanClause.Occur.SHOULD);
        builder.add(new TermQuery(new Term("type","喜剧")),BooleanClause.Occur.FILTER);

        BooleanQuery query = builder.build();
        long start = System.currentTimeMillis();
        TopDocs hits = searcher.search(query,fetchSize);
        long end = System.currentTimeMillis();

        System.err.println("搜索到 "+hits.totalHits+" 文档(共花费 "+(end - start)+" 毫秒) 与之匹配");
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);
            System.out.println(document.get("film") + " : "+document.get("director")+ " : "+document.get("country")+ " : "+document.get("type")+ " : "+document.get("area"));
        }
        reader.close();
        return hits;
    }

    public static void main(String[] args) {
        try {
            BooleanQueryTool.search("D:\\Index\\out\\test",10);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
