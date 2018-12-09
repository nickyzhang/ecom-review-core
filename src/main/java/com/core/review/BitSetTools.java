package com.core.review;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class BitSetTools {

    public static void main(String[] args) {

        List<Integer> list = new ArrayList<Integer>();
        list.add(11);list.add(22);list.add(33);list.add(44);list.add(55);

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) % 2 == 0) {
                list.remove(list.get(i));
            }
        }
//        Iterator<Integer> it = list.iterator();
//        while (it.hasNext())
//        {
//            Integer s = it.next();
//            if (s == 22)
//            {
//                it.remove();
//            }
//        }

    }

    public static boolean find(int target) {
        // 40亿数，放到一个很大的数组中，快速排序，然后采用二分查找，来判断这个数是否存在;

        BitSet bitSet = new BitSet(100000000);
        Random random = new Random();
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            bitSet.set(random.nextInt(Integer.MAX_VALUE));
        }
        return bitSet.get(target);
    }


    public static void find() {
        BitSet bitSet = new BitSet(100000000);
        Random random = new Random();
        for (int i = 0; i < 10000000; i++) {
            bitSet.set(random.nextInt(100000000));
        }

        for(int i = 0; i < 100000000; i++) {
            if (bitSet.get(i) == false) {
                System.out.println(i);
            }
        }
    }


    public static void find1() {
        BitSet bitSet = new BitSet(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            bitSet.set(random.nextInt(100));
        }

        for(int i = 0; i < 100; i++) {
            if (bitSet.get(i) == false) {
                System.out.println(i);
            }
        }
    }


}
