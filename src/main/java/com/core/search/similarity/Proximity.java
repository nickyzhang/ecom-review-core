package com.core.search.similarity;

public class Proximity {

    public static void lcs(String s1, String s2) {
        // 初始化二维数组
        int num[][] = new int[s1.length()+1][s2.length()+1];

        for(int i = 1; i < s1.length(); i++) {
            for (int j = 1; j < s2.length(); j++) {
                if (s1.charAt(i-1) ==  s2.charAt(j-1)) {
                    num[i][j] = 1 + num[i-1][j-1];
                } else {
                    num[i][j] = Math.max(num[i-1][j],num[i][j-1]);
                }
            }
        }
        int pos1 = s1.length();
        int pos2 = s2.length();
        StringBuilder sb = new StringBuilder();
        while(pos1 != 0 && pos2 != 0){
            if (s1.charAt(pos1-1) == s2.charAt(pos2-1)) {
                sb.append(s1.charAt(pos1-1));
                pos1--;
                pos2--;
            } else if (num[pos1][pos2-1] > num[pos1-1][pos2]) {
                pos2--;
            } else {
                pos1--;
            }
        }
        sb.reverse();
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        String s1 = "我爱北京天门";
        String s2 = "我爱天安门";
        Proximity.lcs(s1,s2);
    }
}
