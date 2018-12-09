package com.core.search.lucene.index;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RAMIndexer {
    public static void baseline() throws Exception{
        Directory dir = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setUseCompoundFile(false);
        IndexWriter writer = new IndexWriter(dir,config);
        List<Document> documentList = buildDocs();
        documentList.forEach((doc) -> {
            try {
                writer.addDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static List<Document> buildDocs(){
        String path = "E:\\code\\ecom-review-core\\src\\main\\java\\com\\core\\search\\lucene\\index\\film.txt";
        File file = new File(path);
        String[] fields = {"id","film","director","country","releaseTime","runTime","score","reviewNum","type","area"};
        List<Document> docList = new ArrayList<Document>();
        try {
            List<String> lines = FileUtils.readLines(file,"GBK");
            Document document = null;
            String[] words = null;
            for (String line : lines) {
                document = new Document();
                words = line.split("\t");
                for (int i = 0; i < words.length; i++) {
                    document.add(new TextField(fields[i],words[i],Field.Store.YES));
                }
                docList.add(document);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docList;
    }

    public static void main(String[] args) {
        try {
            RAMIndexer.baseline();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
