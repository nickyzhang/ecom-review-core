package com.core.search.lucene.query;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class MultiFieldQueryParserTool {
    public static TopDocs search(String[] fields, String keyword, String readPath, int fetchSize) throws Exception {
        Directory dir = FSDirectory.open(Paths.get(readPath));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new StandardAnalyzer();
        MultiFieldQueryParser parser  = new MultiFieldQueryParser (fields,analyzer);
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
            String[] fields = {"facility","state"};
            MultiFieldQueryParserTool.search(fields,"Biological CA","D:\\Index\\out\\science\\",10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
