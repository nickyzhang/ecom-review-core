package com.core.search.lucene.index.combine;

import com.core.search.lucene.tools.BuildDocTools;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MovieIndexer {

    public static void baseline(int indexNum) throws Exception {
        Path outpath = Paths.get("D:\\Index\\out\\compound");
        Directory dir = MMapDirectory.open(outpath);
        Analyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setUseCompoundFile(false);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(dir,config);
        List<Document> documentList = buildDocs();


        ExecutorService service = Executors.newFixedThreadPool(indexNum);
        int batch = (int)(documentList.size() / Double.valueOf(indexNum));
        for (int i = 0; i < indexNum; i++) {
            Runnable command = new Task(documentList.subList(i,i+batch-1),writer,config);
            i = i * batch;
            service.execute(command);
        }

        Thread.sleep(10000);
        // 返回索引了多少个文件，有几个文件返回几个
        int num = writer.numDocs();
        System.out.println("文件个数: "+num);
//        writer.close();
    }

    private static List<Document> buildDocs(){
        String readpath = "E:\\code\\ecom-review-core\\src\\main\\java\\com\\core\\search\\lucene\\index\\film.txt";
        File file = new File(readpath);
        String[] fields = {"id","film","director","country","releaseDate","runTime","score","review","type","area"};
        int[] stored = {1,1,1,1,1,1,1,1,1,1};
        Charset charset = Charset.forName("GBK");
        return BuildDocTools.buildDocs(file,fields,stored,"\t",charset);
    }

    public static void main(String[] args) throws Exception{
        MovieIndexer.baseline(4);
    }
}
