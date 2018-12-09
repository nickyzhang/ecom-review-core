package com.core.search.token;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.*;

import java.io.IOException;
import java.io.StringReader;

public class TokenStreamTool {

    public static void main(String[] args) throws IOException {
        Analyzer analyzer1 = new StandardAnalyzer();
        Analyzer analyzer2 = new WhitespaceAnalyzer();
        Analyzer analyzer3 = new StopAnalyzer();

        String text = "hadoop is cool tool hive hadoop a hadoop cool";
        token(text,analyzer1);
        System.out.println("***************************************************");
//        token(text,analyzer2);
//        System.out.println("***************************************************");
//        token(text,analyzer3);
        String text2 = "美女是书籍计算机美女上海美女的红尘";
        Analyzer smartcn = new SmartChineseAnalyzer();
        token(text2,smartcn);
    }

    public static void token(String text, Analyzer analyzer) throws IOException {

        TokenStream stream = analyzer.tokenStream(null,new StringReader(text));

        // 相应词汇的内容
        CharTermAttribute ch = stream.addAttribute(CharTermAttribute.class);

        // 获取当前分词的类型
        TypeAttribute type = stream.addAttribute(TypeAttribute.class);

        // 获取词与词之间的位置增量
        PositionIncrementAttribute position = stream.addAttribute(PositionIncrementAttribute.class);

        // 获取各个单词之间的偏移量
        OffsetAttribute offset = stream.addAttribute(OffsetAttribute.class);

        // 词权重
        PayloadAttribute payload = stream.addAttribute(PayloadAttribute.class);

        // 词频信息
        TermFrequencyAttribute freq = stream.addAttribute(TermFrequencyAttribute.class);


        // 位置长度
        PositionLengthAttribute length = stream.addAttribute(PositionLengthAttribute.class);

        stream.reset();

        while (stream.incrementToken()) {
            System.out.println("词汇内容: "+ch+" | 分词类型: "+type.type()+" | 位置增量: "+position.getPositionIncrement()
            +" | 偏移量: 开始=> "+offset.startOffset()+" 结束=> "+offset.endOffset()+" | 权重信息: "+payload.getPayload()
            + " | 单词频率: "+freq.getTermFrequency()+" | 位置长度: "+length.getPositionLength());
        }
    }
}
