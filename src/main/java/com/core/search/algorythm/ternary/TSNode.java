package com.core.search.algorythm.ternary;

import com.core.search.algorythm.Data;

public class TSNode {
    Data data;
    TSNode lchild;
    TSNode mchild;
    TSNode rchild;
    char c;
    boolean end;

    public TSNode() {
    }

    public TSNode(char c) {
        this.c = c;
        this.end = false;
    }
}


