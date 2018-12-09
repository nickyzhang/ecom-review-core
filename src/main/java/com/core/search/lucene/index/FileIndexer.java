package com.core.search.lucene.index;

import com.core.search.lucene.tools.BuildDocTools;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileIndexer {

    public void baseline(String indexPath) throws Exception {
        // Path是对路径进行封装的一个对象，可以被比较和被监控，包括文件路径和网络路径
        // paths: 可以根据文件路径和网络URL获取Path对象
        Path outpath = Paths.get(indexPath);
        Directory dir = FSDirectory.open(outpath);
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        config.setUseCompoundFile(false);
        IndexWriter writer = new IndexWriter(dir,config);
        List<Document> documentList = buildDocs();
        documentList.forEach((doc) -> {
            try {
                writer.addDocument(doc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 返回索引了多少个文件，有几个文件返回几个
        int num = writer.numDocs();
        System.out.println("文件个数: "+num);
        writer.close();
    }

    private static List<Document> buildDocs(){
        String readpath = "E:\\code\\ecom-review-core\\src\\main\\java\\com\\core\\search\\lucene\\index\\科学.txt";
        File file = new File(readpath);
        String[] fields = {"agency","center","centerStatus","facility","occupied","status","link","recordDate","lastUpdate","address","city","state","zip","country","contact","mailStop","phone"};
        int[] stored = {0,1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1};
        Charset charset = Charset.forName("UTF-8");
        return BuildDocTools.buildDocs(file,fields,stored,",",charset);
    }

    public static void main(String[] args) throws Exception {
        FileIndexer indexer = new FileIndexer();
        indexer.baseline("D:\\Index\\out\\science\\s2");
    }
}
