package com.core.search.lucene.index.combine;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class CombineIndexer {
    public void build() throws Exception{
        Directory out1 = FSDirectory.open(Paths.get("D:\\Index\\combine\\out1"));
        Directory out2 = FSDirectory.open(Paths.get("D:\\Index\\combine\\out2"));
        Directory out3 = FSDirectory.open(Paths.get("D:\\Index\\combine\\out3"));

        Directory out = FSDirectory.open(Paths.get("D:\\Index\\combine\\out"));

        Analyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        IndexWriter writer = new IndexWriter(out,config);
        //传入各自的Diretory或者IndexReader进行合并
        writer.addIndexes(out1,out2,out3);
        writer.commit();//提交索引
        writer.close();
        System.out.println("合并索引完毕.........");
    }
}
