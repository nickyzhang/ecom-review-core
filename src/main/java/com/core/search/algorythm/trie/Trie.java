package com.core.search.algorythm.trie;

import com.core.search.algorythm.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Trie {
    private Node root = null;
    public Trie(Node root) {
        this.root = root;
    }
    public void insert(String word) {
        if (root == null) {
            root = new Node();
        }
        Node current = root;
        Node child = null;
        int len = word.length();
        for (int i = 0; i < len; i++) {
            char c = word.charAt(i);
            child = current.findSubNode(c);
            if (child != null) {
                current = child;
            } else {
                child = new Node(c);
                current.children.add(child);
                current = current.findSubNode(c);
            }

            if (i == (len - 1)) {
                if (child.data == null) {
                    child.data = new Data(word, null, 1);
                } else {
                    child.data.frequency += 1;
                }
                child.end = Boolean.TRUE;
            }
        }
    }

    public boolean search(String word) {
        if (StringUtils.isBlank(word)) {
            return Boolean.FALSE;
        }
        Node current = root;
        for (int i = 0; i < word.length(); i++) {
            if (current == null) {
                return Boolean.FALSE;
            }
            current = current.findSubNode(word.charAt(i));
        }
        return current.end;
    }

    public void delete(String word) {
        if (search(word) == false) return;
        Node current = root;
        Node child = null;
        for (char c : word.toCharArray()) {
            child = current.findSubNode(c);
            if (child.data.frequency == 1) {
                current.children.remove(child);
                return;
            } else {
                child.data.frequency --;
                current = child;
            }
        }
        current.end = false;
    }

    public int freq(String word) {
        Node node = find(word);
        return node != null ? node.data.frequency : 0;
    }

    public boolean contains(String word) {

        return this.find(word) != null;

    }

    public Node find(String word) {
        Node current = root;
        return find(word,0,current);
    }

    public Node find(String word,int pos,Node node) {
        int len = word.length();
        if (pos > (len - 1)) return null;
        Node current = node;
        final char c = word.charAt(pos);
        Node child = current.findSubNode(c);
        if (child != null) {
            if (pos == (len - 1)) {
                if (child.data != null && StringUtils.isNoneBlank(child.data.word) ) {
                    return child;
                }
            }
            return find(word,++pos,child);
        }
        return null;
    }
}
