package com.core.search.lucene.index;

import com.core.search.lucene.tools.BuildDocTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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

public class MovieIndexer {

    public void baseline() throws Exception {
        Path outpath = Paths.get("D:\\Index\\out\\test");
        Directory dir = MMapDirectory.open(outpath);
        Analyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
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

    public static List<Document> buildDocs(){
        String readpath = "E:\\code\\ecom-review-core\\src\\main\\java\\com\\core\\search\\lucene\\index\\film.txt";
        File file = new File(readpath);
        String[] fields = {"id","film","director","country","releaseDate","runTime","score","review","type","area"};
        int[] stored = {1,1,1,0,1,1,1,1,0,0};
        Charset charset = Charset.forName("GBK");
        return BuildDocTools.buildDocs(file,fields,stored,"\t",charset);
    }

    public static List<BuildDocTools.Film> buildFilmList(){
        String readpath = "E:\\code\\ecom-review-core\\src\\main\\java\\com\\core\\search\\lucene\\index\\film.txt";
        File file = new File(readpath);
        String[] fields = {"id","film","director","country","releaseDate","runTime","score","review","type","area"};
        int[] stored = {1,1,1,0,1,1,1,1,0,0};
        Charset charset = Charset.forName("GBK");
        return BuildDocTools.buildFilmList(file,fields,stored,"\t",charset);
    }

    public static void buildJSONFile(String writePath) throws Exception {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String conents = gson.toJson(buildFilmList());
        FileUtils.write(new File(writePath),conents,Charset.forName("UTF-8"));
    }

    public void addDocs() {

    }

    public void updateDocs() {
        Analyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setUseCompoundFile(false);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        Path path = Paths.get("D:\\Index\\out\\test");
        Directory directory;
        try {
            directory = FSDirectory.open(path);
            IndexWriter writer = new IndexWriter(directory,config);
            Document doc = new Document();
            doc.add(new TextField("id","1031", Field.Store.YES));
            doc.add(new TextField("film", "战狼", Field.Store.YES));
            doc.add(new TextField("director", "吴京", Field.Store.YES));
            doc.add(new TextField("country", "中国", Field.Store.YES));
            doc.add(new TextField("releaseDate", "2017-05", Field.Store.YES));
            doc.add(new TextField("runTime", "120", Field.Store.YES));
            doc.add(new TextField("score", "9.5", Field.Store.YES));
            doc.add(new TextField("review", "1012454", Field.Store.YES));
            doc.add(new TextField("type", "战争", Field.Store.YES));
            doc.add(new TextField("area", "大陆", Field.Store.YES));
            writer.addDocument(doc);

            doc = new Document();
            doc.add(new TextField("id","1031", Field.Store.YES));
            doc.add(new TextField("film", "战狼", Field.Store.YES));
            doc.add(new TextField("director", "吴京", Field.Store.YES));
            doc.add(new TextField("country", "中国", Field.Store.YES));
            doc.add(new TextField("releaseDate", "2017-05", Field.Store.YES));
            doc.add(new TextField("runTime", "120", Field.Store.YES));
            doc.add(new TextField("score", "9.5", Field.Store.YES));
            doc.add(new TextField("review", "1012454", Field.Store.YES));
            doc.add(new TextField("type", "战争", Field.Store.YES));
            doc.add(new TextField("area", "大陆", Field.Store.YES));
            writer.addDocument(doc);
            writer.close();
            System.out.println("更新完成!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteDocs(String field, String text) {
        Analyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setUseCompoundFile(false);
        Path path = Paths.get("D:\\Index\\out\\compound");
        Directory directory;
        try {
            directory = FSDirectory.open(path);
            IndexWriter writer = new IndexWriter(directory,config);
            writer.deleteDocuments(new Term(field,text));
            writer.commit();
            writer.close();
            System.out.println("删除完成!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        MovieIndexer indexer = new MovieIndexer();
        indexer.buildJSONFile("D:\\Index\\film.json");
    }

}
