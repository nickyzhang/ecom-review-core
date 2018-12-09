package com.core.search.algorythm.match;

public class BoyerMooreMatch {

    // 计算坏字符算法
    public static int[] buildBCTable(String pattern) {
        int tableSize = 256;
        int[] bcTable = new int[tableSize];
        int pLen = pattern.length();
        for (int i = 0; i < tableSize; i++) {
            // 坏字符表中，默认位移模式长度的数字
            bcTable[i] = pLen;
        }
        // 保存每一个字符的ASCII码值,方便文本坏字符去查找有没有这个字符
        for (int i = 0; i < pLen; i++) {
            int k = pattern.charAt(i);
            bcTable[k] = pLen - 1 - i;
        }
        return bcTable;
    }
    // 计算好后缀算法
//    public static int[] buildGoodSuffixTable(String pattern) {
//        int pLen = pattern.length();
//        int[] goodSuffixTable = new int[pLen];
//        int lastPrefixPosition = pLen;
//        for (int i = pLen - 1; i > 0; i--) {
//
//        }
//    }
    public static int[] build_good_table(String pattern) {
        int pLen = pattern.length();
        int[] good_table = new int[pLen];
        int lastPrefixPosition = pLen;

        for (int i = pLen - 1; i >= 0; --i) {
            if (isPrefix(pattern, i + 1)) {
                lastPrefixPosition = i + 1;
            }
            good_table[pLen - 1 - i] = lastPrefixPosition - i + pLen - 1;
        }

        for (int i = 0; i < pLen - 1; ++i) {
            int slen = suffixLength(pattern, i);
            good_table[slen] = pLen - 1 - i + slen;
        }
        return good_table;
    }
    /**
     * 前缀匹配
     */
    private static boolean isPrefix(String pattern, int p) {
        int patternLength = pattern.length();
        for (int i = p, j = 0; i < patternLength; ++i, ++j) {
            if (pattern.charAt(i) != pattern.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 后缀匹配
     */
    private static int suffixLength(String pattern, int p) {
        int pLen = pattern.length();
        int len = 0;
        for (int i = p, j = pLen - 1; i >= 0 && pattern.charAt(i) == pattern.charAt(j); i--, j--) {
            len += 1;
        }
        return len;
    }
    public static void main(String[] args) {
        int[] x = build_good_table("EXAMPLE");
        System.out.println();
    }
}
