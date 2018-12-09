package com.core.search.lucene.index.combine;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import java.io.IOException;
import java.util.List;

public class Task implements Runnable {

    List<Document> documents;

    IndexWriter writer;

    IndexWriterConfig config;

    public Task() {
    }

    public Task(List<Document> documents, IndexWriter writer, IndexWriterConfig config) {
        this.documents = documents;
        this.writer = writer;
        this.config = config;
    }

    @Override
    public void run() {
        System.out.println("Current Thread: " + Thread.currentThread());
        try {
            for (Document doc : documents) {
                writer.addDocument(doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public IndexWriter getWriter() {
        return writer;
    }

    public void setWriter(IndexWriter writer) {
        this.writer = writer;
    }

    public IndexWriterConfig getConfig() {
        return config;
    }

    public void setConfig(IndexWriterConfig config) {
        this.config = config;
    }
}
