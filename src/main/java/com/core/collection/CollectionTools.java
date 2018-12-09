package com.core.collection;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Predicate;

public class CollectionTools {
    public static void main(String[] args) {
//        Integer[] array = {10,11,199,200,45,33,11,893,234,199};
//        List<Integer> origins = new ArrayList<>();
//        origins.add(10);origins.add(11);origins.add(199);origins.add(200);origins.add(45);
//        origins.add(33);origins.add(11);origins.add(893);origins.add(234);origins.add(199);
//        origins.add(20);origins.add(21);origins.add(299);origins.add(400);origins.add(41);
//        origins.add(38);origins.add(12);origins.add(23);origins.add(214);origins.add(100);
//
//        Spliterator<Integer> s1 = origins.spliterator();
//        Spliterator<Integer> s2 = s1.trySplit();
//        Spliterator<Integer> s3 = s1.trySplit();
//        Spliterator<Integer> s4 = s2.trySplit();
//        List<Spliterator<Integer>> sits = new ArrayList<>();
//        sits.add(s1); sits.add(s2);sits.add(s3);sits.add(s4);
//        ExecutorService service = Executors.newFixedThreadPool(4);
//        for (int i = 0; i < 4; i++) {
//            Task task = new Task(sits.get(i));
//            service.execute(task);
//        }

        final LongAdder adder = new LongAdder();
        adder.add(1);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                adder.add(10);
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {

                adder.add(20);
            }
        });

        t1.start();
        t2.start();
    }

    static class Task implements Runnable {
        Spliterator<Integer> sit;

        public Task() {
        }

        public Task(Spliterator<Integer> sit) {
            this.sit = sit;
        }

        public Spliterator<Integer> getSit() {
            return sit;
        }

        public void setSit(Spliterator<Integer> sit) {
            this.sit = sit;
        }

        @Override
        public void run() {
            sit.forEachRemaining((element) -> {
                System.out.println(Thread.currentThread().getName()+" : "+element);
            });

        }
    }

}
