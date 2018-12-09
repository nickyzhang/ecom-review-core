package com.core.collection;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class ConcurrentLinkQueue<E> extends AbstractQueue<E> implements Queue<E>, java.io.Serializable {

    private transient volatile Node<E> head;
    private transient volatile Node<E> tail;

    public ConcurrentLinkQueue() {
        this.head = this.tail = new Node<E>(null);
    }

    public ConcurrentLinkQueue(Collection<? extends E> c) {
        Node<E> h = null,t = null;
        for (E e : c) {
            Node<E> node = new Node<E>(e);
            if (h == null)
                h = t = node;
            else {
                t = node;
            }

        }

        if (h == null)
            h = t = new Node<E>(null);
        this.head = h;
        this.tail = t;
    }

    void checkNotNull(E e) {
        if (e == null)
            throw new NullPointerException();
    }
    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean offer(E e) {
        checkNotNull(e);
        Node<E> node = new Node<E>(e);
        for (Node<E> t = tail, p = t;;) {
            Node<E> q = p.next;
            if (q == null) {
                if (p.casNext(null, node)) {
                    // 如果设置尾节点后继成功，我们还需要将tail指针指向新的节点
                    return true;
                }
            }
        }
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    private boolean casTail(Node<E> cmp, Node<E> val) {
        return UNSAFE.compareAndSwapObject(this, tailOffset, cmp, val);
    }

    private boolean casHead(Node<E> cmp, Node<E> val) {
        return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
    }
    private static final sun.misc.Unsafe UNSAFE;
    private static final long headOffset;
    private static final long tailOffset;
    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = ConcurrentLinkQueue.class;
            headOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("head"));
            tailOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("tail"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    static class Node<E> {
        volatile E item;
        volatile Node<E> next;

        public Node(E item) {
            this.item = item;
        }

        boolean casItem(E src, E dest) {
            return UNSAFE.compareAndSwapObject(this,itemOffset,src,dest);
        }

        void lazySetNext(Node<E> val) {
            UNSAFE.putOrderedObject(this, nextOffset, val);
        }

        boolean casNext(Node<E> src, Node<E> dest) {
            return UNSAFE.compareAndSwapObject(this,nextOffset,src,dest);
        }

        private static final sun.misc.Unsafe UNSAFE;
        private static final long itemOffset;
        private static final long nextOffset;

        static {
            try {
                // 获取Unsafe实例对象
                UNSAFE = sun.misc.Unsafe.getUnsafe();
                // 获取要操作的class对象
                Class<?> clazz = Node.class;
                // 获取属性的偏移位置
                itemOffset = UNSAFE.objectFieldOffset(clazz.getDeclaredField("item"));
                nextOffset = UNSAFE.objectFieldOffset(clazz.getDeclaredField("next"));
            } catch (NoSuchFieldException e) {
                throw new Error(e);
            }
        }
    }
}
