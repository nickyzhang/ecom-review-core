package com.core.thread.lock;

import org.apache.lucene.util.BitSet;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.SparseFixedBitSet;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionTool {
    final java.util.concurrent.locks.Lock lock = new ReentrantLock();
    final Condition notFull  = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[100];
    int putptr, takeptr, count;

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)
                notFull.await();
            items[putptr] = x;
            if (++putptr == items.length) putptr = 0;
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0)
                notEmpty.await();
            Object x = items[takeptr];
            if (++takeptr == items.length) takeptr = 0;
            --count;
            notFull.signal();
            return x;
        } finally {
            lock.unlock();
        }
    }

    static class Man {
        int age;

        public Man(int age) {
            this.age = age;
        }
    }

    static Unsafe getUnsafe() throws Throwable {
        Class<?> unsafeClass = Unsafe.class;
        for (Field f : unsafeClass.getDeclaredFields()) {
            if ("theUnsafe".equals(f.getName())) {
                f.setAccessible(true);
                return (Unsafe) f.get(null);
            }
        }
        throw new IllegalAccessException("no declared field: theUnsafe");
    }

    public static void main(String[] args) throws Throwable {
//        Unsafe unsafe = getUnsafe();
//        int[] x = new int[100];
//        int scale = unsafe.arrayIndexScale(short[].class);
//        System.out.println(scale);
        SparseFixedBitSet liveDocs = new SparseFixedBitSet(4);
        liveDocs.set(2);
        liveDocs.set(3);
        System.out.println(liveDocs.get(0));
        System.out.println(liveDocs.get(1));
        System.out.println(liveDocs.get(2));
        System.out.println(liveDocs.get(3));
        System.out.println(liveDocs.get(4));
    }
}
