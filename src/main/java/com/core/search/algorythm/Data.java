package com.core.search.algorythm;

public class Data {
    public String word; // 单词
    public String character; // 词性
    public int frequency; // 出现频率

    public Data() { }

    public Data(String word, String character, int frequency) {
        this.word = word;
        this.character = character;
        this.frequency = frequency;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
