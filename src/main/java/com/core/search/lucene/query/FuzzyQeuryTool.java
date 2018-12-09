package com.core.search.lucene.query;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class FuzzyQeuryTool {
    public static TopDocs search(String field, String keyword, String readPath, int fetchSize) throws Exception {
        Directory dir = FSDirectory.open(Paths.get(readPath));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        Term term = new Term(field,keyword);
        Query query = new FuzzyQuery(term);
        long start = System.currentTimeMillis();
        TopDocs hits = searcher.search(query,fetchSize);
        long end = System.currentTimeMillis();

        System.err.println("搜索到 "+hits.totalHits+" 文档(共花费 "+(end - start)+" 毫秒) 与之匹配` "+keyword+"`");
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);
            System.out.println(document.get("film") + " : "+document.get("director"));
        }
        reader.close();
        return hits;
    }

    public static void main(String[] args) {
        try {
            QueryParserTool.search("film","the","D:\\Index\\out\\test",10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
