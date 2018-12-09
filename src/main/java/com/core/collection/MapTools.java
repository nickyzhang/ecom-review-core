package com.core.collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.LongAdder;

public class MapTools {
    public static void main(String[] args) {

        TreeMap<Integer,String> results = new TreeMap<>();
        results.put(385,"385");
        results.put(961,"961");
        results.put(769,"769");
        results.put(1409,"1409");
        results.put(1153,"1153");
        results.put(1665,"1665");
        results.put(193,"193");
        results.put(577,"577");
        results.put(65,"65");
        results.put(321,"321");
        results.put(1089,"1089");
        results.put(449,"449");
        results.put(705,"705");
        results.put(897,"897");
        results.put(1217,"1217");
        results.put(1473,"1473");
        results.put(1729,"1729");
        results.put(1857,"1857");
        results.put(1601,"1601");
        results.put(1281,"1281");
        results.put(1345,"1345");
        results.put(1537,"1537");
        results.put(1793,"1793");
        results.put(1025,"1025");
        results.put(129,"129");
        results.put(833,"833");
        results.put(513,"513");
        results.put(257,"257");
        results.put(1,"1");
        results.put(641,"641");

        ConcurrentSkipListMap<Integer,String> sm = new ConcurrentSkipListMap<>();
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int key = random.nextInt(1000);
            sm.put(key, "S---"+key);
            if (key % 10 == 0)
                sm.remove(key);
        }
    }

}
