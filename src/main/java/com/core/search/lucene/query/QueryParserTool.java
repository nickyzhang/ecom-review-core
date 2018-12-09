package com.core.search.lucene.query;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.nio.file.Paths;

// QueryParser子类对单个域查询时创建查询query
public class QueryParserTool {
    public static TopDocs search(String field, String keyword, String readPath,int fetchSize) throws Exception {
        Directory dir = FSDirectory.open(Paths.get(readPath));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser parser  = new QueryParser(field,analyzer);
        Query query = parser.parse(keyword);
        long start = System.currentTimeMillis();
        TopDocs hits = searcher.search(query,fetchSize);
        long end = System.currentTimeMillis();

        System.err.println("搜索到 "+hits.totalHits+" 文档(共花费 "+(end - start)+" 毫秒) 与之匹配` "+keyword+"`");
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);
            System.out.println(document.get("agency") + " : "+document.get("address"));
        }
        reader.close();
        return hits;
    }

    public static void main(String[] args) {
        try {
            QueryParserTool.search("score","[7.0 TO 8.0]","D:\\Index\\out\\test",10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
