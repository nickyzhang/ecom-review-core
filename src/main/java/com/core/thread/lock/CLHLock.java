package com.core.thread.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CLHLock {
    class CLHNode {
        // 如果为false,表示所已经释放;true表示正在获取锁或者已经获取锁
        boolean locked = false;
    }
    // 因为有多个线程会竞争排队资格，所以有并发情况，因此使用原子变量
    AtomicReference<CLHNode> tail;
    ThreadLocal<CLHNode> current;
    ThreadLocal<CLHNode> prev;
    public CLHLock() {
        this.current = new ThreadLocal<>();
        this.current.set(new CLHNode());
        this.tail = new AtomicReference<>(new CLHNode());
        this.prev = new ThreadLocal<>();
    }

    public void lock() {
        // 获取当前线程的节点
        CLHNode node = this.current.get();
        // 将locked属性置为true,表示正在等待获取锁
        node.locked = true;
        // 将这个节点移到锁的尾部，让其排队，并且返回之前的尾部节点(其他线程的)，将作为当前线程节点的前驱节点
        CLHNode tail = this.tail.getAndSet(node);
        this.prev.set(tail);
        // 只要前驱节点(虚拟)没有释放锁，则一直不断自旋
        while (this.prev.get().locked) {

        }
    }

    public void unlock() {
        // 获取当前释放锁的线程的节点
        CLHNode node = this.current.get();
        // 将locked属性置为false,表示锁已经释放
        node.locked = false;

        // 将前驱节点置为当前线程节点，这样就是释放了当前线程节点，而且还可以保证其余线程自旋的时候获取到锁，不会影响。
        // 不这样的话，我们只能保证其余线程可以获取到锁，但是内存却有很多对象没有释放。
        CLHNode front = this.prev.get();
        this.current.set(front);
    }

    public static void compare (String s, int[] arr) {
        int tmp = Integer.parseInt(s);
        if (arr[0] == 0 && arr[1] == 0) {
            arr[0] = tmp;
            arr[1] = tmp;
        } else if (arr[0] > tmp) {
            arr[0] = tmp;
        } else if (arr[1] < tmp) {
            arr[1] = tmp;
        }
    }
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("100");
        list.add("200");
        list.add("50");
        list.add("120");
        list.add("90");
        int[] arr  = {0,0};
        for (String s : list ) {
            compare(s,arr);
        }
        System.out.println(arr[0]+" --- " +arr[1]);
    }
}
