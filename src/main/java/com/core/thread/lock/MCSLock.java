package com.core.thread.lock;

import java.util.concurrent.atomic.AtomicReference;

public class MCSLock {
    class MCSNode{
        // true：已经获取锁或者准备获取 false: 对应的线程没有持有锁或者释放了锁
        volatile boolean locked = false;
        // 指向下一个节点
        MCSNode next;
    }

    // 当前线程节点变量
    ThreadLocal<MCSNode> current = new ThreadLocal<>();

    // 链表尾部节点
    AtomicReference<MCSNode> tail;

    public MCSLock() {
        this.tail = new AtomicReference<>(null);
    }

    public void lock() {
        // 获取当前线程的节点
        MCSNode node = this.current.get();
        if (node == null) {
            node = new MCSNode();
            this.current.set(node);
        }
        node.locked = true;
        // 获取尾节点，并将当前线程当前先创建的节点更新为尾部节点，则相当于添加到链表尾部
        MCSNode prev = this.tail.getAndSet(node);
        if (prev != null) {
            // 将当前节点作为之前节点下一个节点
            prev.next = node;
            while (node.locked) {

            }
        }
    }

    public void unlock() {
        MCSNode node = this.current.get();
        // 第一种：这个node是最后一个即tail = node
        if (node.next == null) {
            // 这时候我们将tail更新为null，
            if (tail.compareAndSet(node,null)) {
                // 直接返回，表示当前线程一直
                System.out.println(node.locked);
                return;
            }
            // 如果当前更新失败，即回收最后一个节点的时候，又有新的线程更新了tail,继续等一会儿
            // 等一会儿是因为有可能刚更新当前线程节点为尾节点，但是还没有和之前的节点，即现在待删除的建立前后关系
            while (node.next == null) {

            }

        }
        if (node.next != null) {
            node.next.locked = false;
            // 释放next指针,方便GC,不然保持着引用，GC不会来回收
            node.next = null;

        }
    }
}
