package com.core.search.algorythm.match;

public class RobinKarpMatch {
    public static int match(char[] pat, char[] text) {
        int m = pat.length;
        int n = text.length;
        char[] temp = null;
        for (int i = 0; i <= n-m+1; i++) {
            temp = new char[m];
            int v = 0;
            for (int j = i; j < i+m; j++ ) {
                temp[v++] = text[j];
            }
            if (bkdrhash(String.valueOf(temp)) == bkdrhash(String.valueOf(pat))) return i;
        }
        return -1;
    }


    public static int bkdrhash(String str) {
        final int seed = 131;

        int hash = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = hash * seed + (int)str.charAt(i);
        }

        return hash & 0x7FFFFFFF;
    }

    public static int elfhash(String str) {
        int hash = 0;
        int x = 0;

        for (int i = 0; i < str.length(); i++) {
            hash = (hash << 4) + (int)str.charAt(i);

            if ((x & hash & 0xF0000000L) != 0) {
                hash ^= x >> 24;
                hash &= ~x;
            }
        }

        return hash & 0x7FFFFFFF;
    }

    public static void main(String[] args) {
        String s1="wonderful";
        String s2="ful";
        int pos = RobinKarpMatch.match(s2.toCharArray(),s1.toCharArray());
        System.out.println(pos);

    }
}
