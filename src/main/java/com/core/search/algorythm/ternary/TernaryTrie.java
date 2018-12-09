package com.core.search.algorythm.ternary;

import com.core.search.algorythm.Data;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TernaryTrie {

    TSNode root;

    public TernaryTrie(TSNode root) {
        this.root = root;
    }

    public TSNode getTSNode(String word) {
        if (StringUtils.isBlank(word)) return null;
        int len = word.length();
        if (len < 1) return null;
        TSNode current = root;
        int index = 0;
        while (true) {
            final char c = word.charAt(index);
            if (current == null) return null;
            if (c < current.c) {
                current = current.lchild;
            } else if (c > current.c) {
                current = current.rchild;
            } else {
                if (++index == len) {
                    if (current.end) {
                        return current;
                    }
                }
                current = current.mchild;
            }
        }
    }

    public String matchLong(String prefix) {
        if (StringUtils.isBlank(prefix)) return null;
        int len = prefix.length();
        TSNode current = root;
        int index = 0;
        int maxLength = 0;
        while (true) {
            final char c = prefix.charAt(index);
            if (c < current.c) {
                current = current.lchild;
            } else if (c > current.c) {
                current = current.rchild;
            } else {
                if (index == (len - 1) && current.end) {

                }

                current = current.mchild;
            }
        }
    }

//    public List<String> segment(String text) {
//        List<String> words = new ArrayList<String>();
//        int len = text.length();
//        int i = 0;
//        while (i < len) {
//            String word = matchLong(text,i);
//            if (word != null) {
//                i += word.length();
//                words.add(word);
//            } else {
//                // 没有找到匹配的词，就按照单字切分
//                word = text.substring(i,i+1);
//                words.add(word);
//                ++i;
//            }
//        }
//        return words;
//    }
    public void addTSNode(String word, char rv) {
        // 全部转化为小写
        String text = word.toLowerCase().trim();
        if (root == null) {
            root = new TSNode(rv);
        }
        addTSNode(text,word,0,root);
    }

    public void addTSNode(String text, String word,int pos, TSNode node) {
        TSNode current = node;
        final char c = text.charAt(pos);
        if (c < current.c) {
            if (current.lchild == null) {
                current.lchild = new TSNode(c);
            }
            addTSNode(text,word,pos,current.lchild);
        } else if (c > current.c) {
            if (current.rchild == null) {
                current.rchild = new TSNode(c);
            }
            addTSNode(text,word,pos,current.rchild);
        } else {
            ++pos;
            if (pos == word.length()) {
                current.end = Boolean.TRUE;
                if (current.data == null) {
                    current.data = new Data(word,null,1);
                } else {
                    current.data.frequency += 1;
                }
            } else {
                if (current.mchild == null) {
                    current.mchild = new TSNode(c);
                }
                addTSNode(text,word,pos,current.mchild);
            }
        }
    }

    public boolean contain(String word){
        TSNode node = this.getTSNode(word);
        if (node == null) {
            return false;
        }
        System.out.println(node.data.word+" 出现了 "+ node.data.frequency+"次");
        return true;
    }

    public static void main(String[] args) {
        TernaryTrie trie = new TernaryTrie(null);
//        String[] dict = {"大","大学","大学生","活","活动","中","中心","生活","心"};
        String[] dict = {"he","cool","hello","apple","example","center","zoo","orange","hello","check"};
        Arrays.sort(dict);
        for (int i = 0; i < dict.length; i++) {
            trie.addTSNode(dict[i],dict[dict.length/2].charAt(0));
        }
        trie.contain("hello");


    }
}
