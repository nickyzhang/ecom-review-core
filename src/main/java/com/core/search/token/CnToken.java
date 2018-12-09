package com.core.search.token;

public class CnToken {
    String term; // 词
    int start; // 词开始位置
    int end; // 词结束位置
    int freq; // 词在语料库中出现的频率

    public CnToken(int vertexFrom, int vertexTo,String word) {
        this.start = vertexFrom;
        this.end = vertexTo;
        this.term = word;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }
}
