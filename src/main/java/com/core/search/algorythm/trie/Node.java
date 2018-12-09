package com.core.search.algorythm.trie;

import com.core.search.algorythm.Data;
import org.apache.commons.collections.CollectionUtils;
import java.util.LinkedList;

public class Node {
    // 节点数据,如果是一个结尾词节点，data必然不为空
    Data data;
    boolean end;
    // 当前节点字符
    char c;
    // 是否是一个结尾单词节点
    LinkedList<Node> children;

    public Node() {
        this.end = Boolean.FALSE;
        this.children = new LinkedList<Node>();
    }

    public Node(char c) {
        this.end = Boolean.FALSE;
        this.c = c;
        this.children = new LinkedList<Node>();
    }

    // 根据当前节点查找子节点
    public Node findSubNode(char c) {
        if (CollectionUtils.isNotEmpty(children)) {
            for (Node child : children) {
                if (child.c == c) {
                    return child;
                }
            }
        }
        return null;
    }
}
