package com.core.common;

import java.util.List;

public class Page {
    String url;
    List<String> keywords;

    public Page(String url, List<String> keywords) {
        this.url = url;
        this.keywords = keywords;
    }
}
