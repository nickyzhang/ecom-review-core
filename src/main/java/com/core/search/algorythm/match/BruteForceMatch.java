package com.core.search.algorythm.match;

public class BruteForceMatch {
    public static int match(char[] pat, char[] text) {
        int m = pat.length;
        int n = text.length;
        for (int i = 0; i <= n - m; i++) {
            int j = 0;
            while (j < m && pat[j] == text[i+j]) {
                ++j;
            }
            if (j == m) return i;
        }
        return -1;
    }
}
