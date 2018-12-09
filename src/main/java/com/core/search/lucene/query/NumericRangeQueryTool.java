package com.core.search.lucene.query;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.nio.file.Paths;

public class NumericRangeQueryTool {
    public static TopDocs search(String field, String low,String high,boolean includeLow,boolean includeHigh, String readPath, int fetchSize) throws Exception {
        Directory dir = FSDirectory.open(Paths.get(readPath));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        TermRangeQuery rangeQuery = new TermRangeQuery(field,new BytesRef(low.getBytes()),new BytesRef(high.getBytes()),includeLow,includeHigh);
        long start = System.currentTimeMillis();
        TopDocs hits = searcher.search(rangeQuery,fetchSize);
        long end = System.currentTimeMillis();

        System.err.println("搜索到 "+hits.totalHits+" 文档(共花费 "+(end - start)+" 毫秒) 与之匹配");
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);
            System.out.println(document.get("film") + " : "+document.get("director"));
        }
        reader.close();
        return hits;
    }

    public static void main(String[] args) {
        try {
            NumericRangeQueryTool.search("review","10000","200000",true,true,"D:\\Index\\out\\movie\\",10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
