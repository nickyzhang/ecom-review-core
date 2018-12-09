package com.core.search.lucene.index;

import com.core.search.lucene.tools.BuildDocTools;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CommonIndexer {

    public void baseline() throws Exception {
        Path outpath = Paths.get("D:\\Index\\out\\compound");
        Directory dir = FSDirectory.open(outpath);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        MergePolicy mergePolicy = new TieredMergePolicy();
        config.setMergePolicy(mergePolicy);
        config.setUseCompoundFile(false);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(dir,config);
        List<Document> documentList = buildDocs();
        documentList.forEach((doc) -> {
            try {
                writer.addDocument(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        // 返回索引了多少个文件，有几个文件返回几个
        int num = writer.numDocs();
        System.out.println("文件个数: "+num);
        writer.close();
    }

    public void addDocs() {
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setUseCompoundFile(false);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        Path path = Paths.get("D:\\Index\\out\\test");
        Directory directory;
        try {
            directory = FSDirectory.open(path);
            IndexWriter writer = new IndexWriter(directory,config);
            Document doc1 = new Document();
            FieldType fieldType = new FieldType();
            fieldType.setStored(true);
            fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
            fieldType.setTokenized(true);
            fieldType.setStoreTermVectors(true);
            fieldType.setStoreTermVectorPositions(true);
            fieldType.setStoreTermVectorOffsets(true);
            doc1.add(new Field("title","hadoop is very good",fieldType));
            doc1.add(new Field("desc","spark and hadoop are cool",fieldType));
            writer.addDocument(doc1);

            Document doc2 = new Document();
            doc2.add(new Field("title","hadoop vs spark",fieldType));
            doc2.add(new Field("desc","cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc",fieldType));
            writer.addDocument(doc2);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Document> buildDocs(){
        String readpath = "E:\\code\\ecom-review-core\\src\\main\\java\\com\\core\\search\\lucene\\index\\nicky.txt";
        File file = new File(readpath);
        String[] fields = {"name","desc"};
        int[] stored = {1,1};
        Charset charset = Charset.forName("UTF-8");
        return BuildDocTools.buildDocs(file,fields,stored,"\t",charset);
    }


    public static void main(String[] args) throws Exception {
        CommonIndexer indexer = new CommonIndexer();
        indexer.addDocs();
    }

}
