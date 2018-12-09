package com.core.collection;

import java.util.*;

public class BitSetTools {
    // 需要维持bitset的long类型数组，即存储一个bitset，需要多少个long来支撑
    private long[] words;
    // 当前words数组的容量
    private transient int wordsInUse = 0;
    private transient boolean sizeIsSticky = false;

    public BitSetTools(long[] words) {
        this.words = words;
        this.wordsInUse = words.length;
    }

    public BitSetTools() {
        initWords(64);
        sizeIsSticky = false;
    }

    private void initWords(int nbits) {
        words = new long[wordIndex(nbits-1) + 1];
    }

    private int wordIndex(int bitIndex) {
        // 计算当前索引在数组word哪一个位置上，所以 由 bitIndex / 64
        return bitIndex >> 6;
    }

    // 说白了就是将1向左移动 bitIndex(要存储的数)位，然后计算后的值存储到long数组中
    // 如果计算的位置相同，则后面
    public void set(int bitIndex) {
        // 计算在数组中哪一个位置上
        int wordIndex = wordIndex(bitIndex);
        // 检查是否需要扩容
        grow(wordIndex);
        // 将索引位置上的数和2^bitIndex做和运算，取最大的数存储，因为同一个位置，可能有很多数据存储，比如1,64之内的数据，都存储到了数组第一个位置
        words[wordIndex] |= (1 << bitIndex);
    }

    public boolean get(int bitIndex ) {
        // 计算在数组中哪一个位置上
        int wordIndex = wordIndex(bitIndex);
        // 要获取的值计算的索引值不能大于数组当前最大容量
        // 当前指定元素的索引位置的值和2^bitIndex做&运算，结果肯定是最小值，如果不大于0，就表示存在；否则是0，表示不存在
        return ((wordIndex < wordsInUse) && (words[wordIndex] & (1 << bitIndex)) != 0);
    }

    public void grow(int wordIndex) {
        // 索引递增
        int wordsRequired = wordIndex + 1;
        // 当前words数组最大容量 小于需要的容量，则需需要扩容
        if (wordsInUse < wordsRequired) {
            // 扩容
            ensureCapacity(wordsRequired);
            // 更新wordInUse为words数组最大容量
            wordsInUse = wordsRequired;
        }
    }

    public void ensureCapacity(int wordsRequired) {
        if (words.length < wordsRequired) {
            int newSize = Math.max(2 * words.length, wordsRequired);
            words = Arrays.copyOf(words,newSize);
            sizeIsSticky = false;
        }
    }

    public static void main(String[] args) {
//        BitSet bitSetTools = new BitSet();
//        bitSetTools.set(5);
//        bitSetTools.set(10000);
//        bitSetTools.set(65);
//        System.out.println(bitSetTools.get(5));
//        System.out.println(bitSetTools.get(11));
//        System.out.println(bitSetTools.get(6));
//        System.out.println(bitSetTools.get(65));

        List<Integer> list = new ArrayList<Integer>();
        list.add(null);
        list.add(null);
        System.out.println(list.size());
        Set<String> set = new HashSet<>();
        set.add(null);
        set.add(null);
        set.add("cool");
        set.add(null);
        System.out.println(set.size());

        Map<String, String> map = new HashMap<>();
        map.put(null, null);
        map.put("qq", null);
        map.put("wc", null);
        map.put(null, "cool");

        System.out.println(map.size());
    }

}
