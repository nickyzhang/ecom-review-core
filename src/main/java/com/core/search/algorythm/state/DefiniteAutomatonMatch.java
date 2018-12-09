package com.core.search.algorythm.state;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.fst.ByteSequenceOutputs;

public class DefiniteAutomatonMatch {

    static final int CHAR_TABLE_SIZE = 256;

    public static int[][]  build(char[] pat) {
        int m = pat.length;
        int[][] table = new int[m+1][CHAR_TABLE_SIZE];

        for (int state = 0; state <= m; state++) {
            for (int c = 0; c < CHAR_TABLE_SIZE; c++) {
                table[state][c] = getNextState(pat,m,state,c);
            }
        }
        return table;
    }

    public static int getNextState(char[] pat, int m, int state, int c) {
        if (state < m && pat[state] == c) {
            return state + 1;
        }
        int i = 0;
        for (int ns = state; ns > 0; ns--) {
            if (pat[ns-1] == c)
            {
                for (; i < ns-1; i++) {
                    if (pat[i] != pat[state - ns + 1 + i]) break;
                }
                if (i == ns-1) {
                    return ns;
                }
            }
        }

        return 0;
    }

    public static int match(String text,String pat) {
        int m = pat.length();
        int n = text.length();
        char[] pats = pat.toCharArray();
        char[] texts = text.toCharArray();
        int[][] table = build(pats);
        int state = 0;
        int count = 0;
        for (int i = 0; i < n; i++) {
            state = table[state][texts[i]];
            if (state == m) {
                count++;
                System.out.println("第"+count+"次匹配成功,匹配位置: "+(i-m+1));
            }
        }
        return count;
    }

    public static void main(String[] args) {
//        String txt = "AABAACAADAABAABA";
//        String pat = "AABA";
//        DefiniteAutomatonMatch.match(txt,pat);

        byte[] b1 = "hello".getBytes();
        byte[] b2 = "helen".getBytes();
        BytesRef br1 = new BytesRef(b1);
        BytesRef br2 = new BytesRef(b2);
        BytesRef res = ByteSequenceOutputs.getSingleton().common(br1,br2);
        System.out.println(res.utf8ToString());
    }
}
