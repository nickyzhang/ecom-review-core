package com.core.common;

import java.util.ArrayList;
import java.util.List;

public class VNode {
    String label;
    List<VEdge> fromList;
    List<VEdge> toList;

    public VNode(String label) {
        this.label = label;
        this.fromList = new ArrayList<VEdge>();
        this.toList = new ArrayList<VEdge>();
    }

}
