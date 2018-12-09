package com.core.mock;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import sun.misc.Unsafe;


public class AbstractSynchronizer extends AbstractOwnableSynchronizer implements java.io.Serializable {
    private static final long serialVersionUID = 7373984972572414691L;

    protected AbstractSynchronizer() { }

    public static final class Node {

        // 标记表示共享锁模式下处于等待状态的节点
        static final Node SHARED = new Node();

        // 标记表示独占锁模式下处于等待状态的节点
        static final Node EXCLUSIVE = null;

        // 等待状态：表示当前的线程被取消(超时或中断)
        static final int CANCELLED = 1;

        // 等待状态: 表示当前节点的后继节点包含的线程需要运行
        static final int SIGNAL = -1;

        // 等待状态: 表明该线程被处于条件队列
        static final int CONDITION = -2;

        // 等待状态：表示可以传播共享给其他节点
        static final int PROPAGATE = -3;

        // 等待状态
        volatile int waitStatus;

        public volatile Node prev; // 前驱节点

        public volatile Node next; // 后继节点

        public volatile Thread thread; // 队列中线程，正持有锁或者希望持有锁

        // 条件队列中只能是独占锁，所以使用
        public Node nextWaiter;

        // 判断是不是共享锁
        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        // 返回前驱节点
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {    // Used to establish initial head or SHARED marker
        }
        // 构造节点对象
        Node(Thread thread, Node mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }


    // 链表(等待队列)头结点，也可以表示为当前持有锁的线程
    public transient volatile Node head;

    public transient volatile Node tail;

    // 锁的状态,即同步状态
    private volatile int state;

    protected final int getState() {
        return state;
    }

    protected final void setState(int newState) {
        state = newState;
    }

    // 通过比较是不是期望的值，然后是的话进行更新状态
    protected final boolean compareAndSetState(int expect, int update) {
        // See below for intrinsics setup to support this
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    // 自旋超时的阀值
    static final long spinForTimeoutThreshold = 1000L;


    private Node enq(final Node node) {
        for (;;) {
            Node t = tail; // 找到队尾节点
            if (t == null) { // 如果还没有实例化，则实例化
                // 如果head节点还没有实例化则实例化
                if (compareAndSetHead(new Node())) {
                    tail = head; // 这时候头尾指针指向的是同一个区域
                    System.out.println("首尾节点是否相等=> "+(tail == head));
                    System.out.println("初始化: HEAD=> "+head+" TAIL=> "+tail);
                }
            } else { // 如果尾节点已经实例化
                node.prev = t; // 将要添加的节点的前驱指向尾节点
                // 如果对尾节点没有变，直接更新队尾节点为新加入的node的节点
                // 即新的节点作为尾节点
                if (compareAndSetTail(t, node)) {
                    t.next = node;// 之前尾节点的next指针指向当前尾节点node
                    return t; // 返回之前的尾节点
                }
            }
        }
    }

    // 根据指定的模式，为线程创建一个入队节点
    private Node addWaiter(Node mode) {
        // 创建新的独占锁线程节点
        Node node = new Node(Thread.currentThread(), mode);
        Node pred = tail; // 尾部节点作为前驱
        if (pred != null) {// 设置尾部节点和新节点之间的关系
            node.prev = pred; // 
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);// 如果前驱为空，则通过enq方式放入AQS队列
        return node; // 返回新创建的节点
    }

    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }

    // 唤醒挂起的线程，一般是头结点，也就是现在轮到当前线程持有锁，所以需要唤醒线程
    private void unparkSuccessor(Node node) {
        // 获取节head点等待状态
        int ws = node.waitStatus;
        // 状态小于0，比如是SIGNAL,则将node的等待状态修改为0
        if (ws < 0)
            // 将当前节点状态置为0
            compareAndSetWaitStatus(node, ws, 0);

        // 获取头结点下一个节点，如果不为空则恢复线程; 如果没有或者下一个节点取消了等待
        // 则从队列中从后往前找非取消等待线程，找到然后将其唤醒
        Node s = node.next;
        // 如果后继线程取消了等待或者后继线程
        if (s == null || s.waitStatus > 0) {
            s = null; // 将后继节点置空
            // 从后往前遍历节点，找到一个状态小于0的节点(SINGAL,CONDITION,PROPAGATE)
            // 然后将其置为我们要找的后继(恢复线程)
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
        // 恢复后继节点线程
        if (s != null)
            LockSupport.unpark(s.thread);
    }
    // 释放共享锁
    private void doReleaseShared() {
        for (;;) {
            Node h = head; // 找到头结点
            if (h != null && h != tail) {
                int ws = h.waitStatus; // 获取头结点等待状态
                // 如果状态是SIGNAL,则等待状态置为0，然后恢复头结点后继节点线程
                if (ws == Node.SIGNAL) {
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                        continue;            // loop to recheck cases
                    unparkSuccessor(h);
                }
                // 如果是处于取消状态且更新失败，则继续
                else if (ws == 0 &&
                        !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                    continue;                // loop on failed CAS
            }
            if (h == head)                   // loop if head changed
                break;
        }
    }


    // 设置队列的头，如果后继节点以共享模式处于等待
    private void setHeadAndPropagate(Node node, int propagate) {
        // 记录检查前的头
        Node h = head; // Record old head for check below
        setHead(node); // 设置头结点
        // 
        if (propagate > 0 || h == null || h.waitStatus < 0 ||
                (h = head) == null || h.waitStatus < 0) {
            Node s = node.next; // 满足上述条件，获取后继节点，如果是共享的，则释放锁 
            if (s == null || s.isShared())
                doReleaseShared();
        }
    }

    // 取消尝试获取锁，比如发生某种异常或者超时等需要取消等待，即取消自旋
    private void cancelAcquire(Node node) {
        if (node == null) return;
        // 将node的thread属性置为null
        node.thread = null;

        // 获取node的前驱节点
        Node pred = node.prev;
        // 一直往前找到是处于取消状态的前驱节点
        while (pred.waitStatus > 0)
            node.prev = pred = pred.prev;

        // predNext is the apparent node to unsplice. CASes below will
        // fail if not, in which case, we lost race vs another cancel
        // or signal, so no further action is necessary.
        // 获取新前驱节点的下一个节点
        Node predNext = pred.next;

        // Can use unconditional write instead of CAS here.
        // After this atomic step, other Nodes can skip past us.
        // Before, we are free of interference from other threads.
        node.waitStatus = Node.CANCELLED;

        // 如果node是处于队尾，则直接将node的前驱置为队尾
        if (node == tail && compareAndSetTail(node, pred)) {
            // 如果成功，则将新的队尾节点的后继节点置为空，因为队尾已经不应该有后继节点了
            compareAndSetNext(pred, predNext, null);
        } else {
            // If successor needs signal, try to set pred's next-link
            // so it will get one. Otherwise wake it up to propagate.
            // node如果不是队尾节点或者node是队尾但是在在更新尾节点时候失败了
            int ws;
            // node前驱不是head节点 且 前驱的状态是SINGAL或者是其他非取消状态的，也置状态为SINGAL,并且前驱线程不为空
            if (pred != head &&
                    ((ws = pred.waitStatus) == Node.SIGNAL ||
                            (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
                    pred.thread != null) {
                Node next = node.next;
                if (next != null && next.waitStatus <= 0)
                    compareAndSetNext(pred, predNext, next);
            } else {
                unparkSuccessor(node); // 恢复线程
            }

            node.next = node; // help GC
        }
    }

    // 在获取锁失败的时候是否应该挂起线程
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;// 获取前驱状态 
        if (ws == Node.SIGNAL)
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park.
             */
            return true;
        if (ws > 0) {// 如果前驱是处于取消状态
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             */
            do {
                node.prev = pred = pred.prev; // 一直寻找状态不是处于取消状态的节点
            } while (pred.waitStatus > 0);
            pred.next = node; // 
        } else { // 如果处于非取消状态，则更新为SINGAL状态

            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }

    // 中断当前线程
    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    // 挂起线程，检查是否中断
    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }

    //为处于队列中的线程获取独占的不可中断的模式，用于条件等待方式
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    System.out.println("node"+node +" >>p: "+p+" head>>"+head);
                    setHead(node);
                    p.next = null;
                    failed = false;
                    return interrupted;
                }

                if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    // 以独占中断方式获取锁
    private void doAcquireInterruptibly(int arg)
            throws InterruptedException {
        // 以独占模式创建节点
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (;;) {
                // 获取前驱，如果前驱是head且可以获取锁，则将当前设为head
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return;
                }
                // 如果要求挂起锁在获取失败后，则抛出中断异常
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            // 没有获取成功，需要取消对锁的获取
            if (failed)
                cancelAcquire(node);
        }
    }

    // 指定超时时间获取锁，原理差不多只是有一个时间限制，不能无限制等待获取锁
    private boolean doAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L)
            return false;
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return true;
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    return false;
                if (shouldParkAfterFailedAcquire(p, node) &&
                        nanosTimeout > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    // 以共享无中断模式获取锁
    private void doAcquireShared(int arg) {
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        if (interrupted)
                            selfInterrupt();
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }


    private void doAcquireSharedInterruptibly(int arg)
            throws InterruptedException {
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    // 带有超时时间的获取共享锁
    private boolean doAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (nanosTimeout <= 0L)
            return false;
        final long deadline = System.nanoTime() + nanosTimeout;
        final Node node = addWaiter(Node.SHARED);
        boolean failed = true;
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        failed = false;
                        return true;
                    }
                }
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    return false;
                if (shouldParkAfterFailedAcquire(p, node) &&
                        nanosTimeout > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }

    // 尝试获取独占锁，总是抛出异常，如果此方法报告失败，acquireQueued
    // 可能会对线程排队，如果它还没有排队，直到它被其他线程释放信号为止
    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    // 释放锁，抛出异常，具体的交给子类实现
    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

    // 试图获取共享锁,具体交给子类实现，父类总是返回不支持异常
    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }

    // 试图释放共享锁,具体交给子类实现，父类总是返回不支持异常
    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }

    // 检查线程是否获取了独占锁，未获取独占锁调用 signal 方法是不允许的
    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    // 独占模式获取锁，忽略中断(抢占锁)
    public final void acquire(int arg) {
        // 如果尝试获取锁没有成功，则让线程处于等待队列
        if (!tryAcquire(arg) &&
                acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }

    public final void acquireInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (!tryAcquire(arg))
            doAcquireInterruptibly(arg);
    }


    public final boolean tryAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquire(arg) ||
                doAcquireNanos(arg, nanosTimeout);
    }


    // 释放锁，如果尝试释放成功，则把头结点对应的线程恢复
    public final boolean release(int arg) {
        if (tryRelease(arg)) { // 尝试释放锁,如果释放成功
            Node h = head;
            // 头结点如果不为空,且还被其他线程持有，这时候需要唤醒
            if (h != null && h.waitStatus != 0)
                // 恢复线程
                unparkSuccessor(h);
            return true;
        }
        return false;
    }

    // 获取共享锁
    public final void acquireShared(int arg) {
        if (tryAcquireShared(arg) < 0)
            doAcquireShared(arg);
    }

    public final void acquireSharedInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (tryAcquireShared(arg) < 0)
            doAcquireSharedInterruptibly(arg);
    }

    public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquireShared(arg) >= 0 ||
                doAcquireSharedNanos(arg, nanosTimeout);
    }

    public final boolean releaseShared(int arg) {
        if (tryReleaseShared(arg)) {
            doReleaseShared();
            return true;
        }
        return false;
    }

    // 是否有线程等待获取锁
    public final boolean hasQueuedThreads() {
        return head != tail;
    }

    // 是不是有线程曾经想获取同步器
    public final boolean hasContended() {
        return head != null;
    }

    // 返回等待队列中第一个线程
    public final Thread getFirstQueuedThread() {

        return (head == tail) ? null : fullGetFirstQueuedThread();
    }

    private Thread fullGetFirstQueuedThread() {
        Node h, s;
        Thread st;
        if (((h = head) != null && (s = h.next) != null &&
                s.prev == head && (st = s.thread) != null) ||
                ((h = head) != null && (s = h.next) != null &&
                        s.prev == head && (st = s.thread) != null))
            return st;

        Node t = tail;
        Thread firstThread = null;
        while (t != null && t != head) {
            Thread tt = t.thread;
            if (tt != null)
                firstThread = tt;
            t = t.prev;
        }
        return firstThread;
    }

    // 指定的线程是否在等待队列中
    public final boolean isQueued(Thread thread) {
        if (thread == null)
            throw new NullPointerException();
        for (Node p = tail; p != null; p = p.prev)
            if (p.thread == thread)
                return true;
        return false;
    }

    // 如果等待队列的第一个线程是以独占模式等待，则返回true
    final boolean apparentlyFirstQueuedIsExclusive() {
        Node h, s;
        return (h = head) != null &&
                (s = h.next)  != null &&
                !s.isShared()         &&
                s.thread != null;
    }

    // 如果锁没有被占用，但因为是公平锁,所以按照先进先出的顺序，看看前面有没有其他线程等待获取锁
    public final boolean hasQueuedPredecessors() {
        Node t = tail;
        Node h = head;
        Node s;
        // 获取头结点后继节点，有可能为空，有可能不为空
        // 如果后继结点线程不是当前想要获取说的线程，则表示还有其他线程在等待获取锁
        return h != t &&
                ((s = h.next) == null || s.thread != Thread.currentThread());
    }

    // 获取等待队列的长度
    public final int getQueueLength() {
        int n = 0;
        for (Node p = tail; p != null; p = p.prev) {
            if (p.thread != null)
                ++n;
        }
        return n;
    }

    // 获取处于等待队列中获取锁的所有线程
    public final Collection<Thread> getQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            Thread t = p.thread;
            if (t != null)
                list.add(t);
        }
        return list;
    }

    // 获取处于等待队列中获取锁的所有独占模式线程
    public final Collection<Thread> getExclusiveQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            if (!p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }

    // 获取处于等待队列中获取锁的所有共享模式线程
    public final Collection<Thread> getSharedQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            if (p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }

    public String toString() {
        int s = getState();
        String q  = hasQueuedThreads() ? "non" : "";
        return super.toString() +
                "[State = " + s + ", " + q + "empty queue]";
    }

    // 用于判断节点是否已经转移到阻塞队列了
    final boolean isOnSyncQueue(Node node) {
        // 节点在同步队列上时，其状态可能为 0、SIGNAL、PROPAGATE 和 CANCELLED 其中之一
        // 而且在同步队列的时候他的前驱一定不是null
        if (node.waitStatus == Node.CONDITION || node.prev == null)
            return false;
        // 如果节点的后继节点不为空,表示肯定在同步队列，因为条件队列的后继是nextWaiter来指向的
        if (node.next != null) // If has successor, it must be on queue
            return true;
        // 如果当前节点确实没有后继节点，则从队尾往前寻找
        return findNodeFromTail(node);
    }

    // 如果节点已经进入阻塞同步队列，则从尾节点向前查找节点
    private boolean findNodeFromTail(Node node) {
        Node t = tail;
        for (;;) {
            if (t == node)
                return true;
            if (t == null)
                return false;
            t = t.prev;
        }
    }

    // 将节点从条件队列(condition queue)传输到同步队列(sync queue)
    final boolean transferForSignal(Node node) {
        // 如果将节点的等待状态由 CONDITION 设为 0 失败，则表明节点被取消
        // 因为 transferForSignal 中不存在线程竞争的问题，所以下面的 CAS 
        // 失败的唯一原因是节点的等待状态为 CANCELLED。
        if (!compareAndSetWaitStatus(node, Node.CONDITION, 0))
            return false;

        // 将条件队列第一个节点加入同步队列末尾,同时返回前驱节点
        Node p = enq(node);
        int ws = p.waitStatus;
        // 前驱节点等待状态处于取消，那么就该此时应唤醒 node 对应的线程去获取同步状态。
        // 此时如果 ws < 0,就该让前驱的状态置为SINGAL,这样p在释放同步状态后才会唤醒node对应的线程
        if (ws > 0 || !compareAndSetWaitStatus(p, ws, Node.SIGNAL))
            LockSupport.unpark(node.thread);
        return true;
    }

    // 是判断中断发生的时机，分为两种：
    // 1. 中断在节点被转移到同步队列前发生，此时返回 true
    // 2. 中断在节点被转移到同步队列期间或之后发生，此时返回 false
    final boolean transferAfterCancelledWait(Node node) {
        // 当前线程如果在条件队列，则置为线程等待状态置为0
        if (compareAndSetWaitStatus(node, Node.CONDITION, 0)) {
            enq(node); // 将这个线程节点从条件队列转移到同步队列末尾
            return true; // 表示节点被转移到同步队列前发生中断
        }
        // 如果这个节点还没有出现在同步队列，则让出CPU执行权
        while (!isOnSyncQueue(node))
            Thread.yield();
        // 中断在节点被转移到同步队列期间或之后发生，此时返回 false
        return false;
    }

    // 完全释放新加入节点队列的线程所对应的同步阻塞队列中该线程对应的节点的锁
    // 并且保存这个锁的状态,重新竞争锁时会用到
    final int fullyRelease(Node node) {
        boolean failed = true;
        try {
            // 获取锁状态
            int savedState = getState();
            // 释放锁,如果成功则返回；否则抛出异常，并且该线程节点处于取消状态
            if (release(savedState)) {
                failed = false;
                return savedState;
            } else {
                throw new IllegalMonitorStateException();
            }
        } finally {
            if (failed)
                node.waitStatus = Node.CANCELLED;
        }
    }

    // 查询是否指定条件对象使用同步器作为锁
    public final boolean owns(ConditionObject condition) {
        return condition.isOwnedBy(this);
    }

    // 判断基于指定的条件处于等待的线程是否和同步器有关联
    public final boolean hasWaiters(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.hasWaiters();
    }

    // 返回基于指定条件和同步器有关联的等待线程数量
    public final int getWaitQueueLength(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitQueueLength();
    }

    // 获取和同步器相关的等待线程
    public final Collection<Thread> getWaitingThreads(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitingThreads();
    }

    public class ConditionObject implements Condition, java.io.Serializable {
        private static final long serialVersionUID = 1173984872572414699L;
        // 条件队列第一个等待节点
        private transient Node firstWaiter;

        // 条件队列最后一个等待节点
        private transient Node lastWaiter;

        public ConditionObject() { }

        // 添加新的等待者到队列
        private Node addConditionWaiter() {
            // 声明最后一个等待者
            Node t = lastWaiter;
            // 如果条件队列的最后一个节点取消了，将其清除出去
            if (t != null && t.waitStatus != Node.CONDITION) {
                // 遍历整个条件队列链表，然后会将已取消的所有节点清除出队列
                unlinkCancelledWaiters();
                t = lastWaiter;
            }
            // 创建新的条件节点
            Node node = new Node(Thread.currentThread(), Node.CONDITION);
            // 如果条件节点为空，则这个就是条件队列第一个节点
            if (t == null)
                firstWaiter = node;
                // 如果条件节点不为空，则这个就是最后后尾节点的下一个节点
            else
                t.nextWaiter = node;
            lastWaiter = node; // 并且新节点更新为最后新的尾节点
            return node;
        }


        // 删除和传输节点直到遇到非取消状态或者null命中
        private void doSignal(Node first) {
            do {
                // 队列中第一个为空则最后一个也为空
                if ( (firstWaiter = first.nextWaiter) == null)
                    lastWaiter = null;
                first.nextWaiter = null;
            } while (!transferForSignal(first) && // 将第一个从条件队列转移到同步队列
                    (first = firstWaiter) != null);
        }

        // 删除和转移所有条件队列的的节点到同步队列
        private void doSignalAll(Node first) {
            lastWaiter = firstWaiter = null;
            do {
                Node next = first.nextWaiter;
                first.nextWaiter = null;
                transferForSignal(first);
                first = next;
            } while (first != null);
        }


        // 该方法用于清除队列中已经取消等待的节点，比如 await 的时候如果发生了取消操作
        private void unlinkCancelledWaiters() {
            Node t = firstWaiter;
            Node trail = null;
            while (t != null) {
                Node next = t.nextWaiter;
                if (t.waitStatus != Node.CONDITION) {
                    t.nextWaiter = null;
                    if (trail == null)
                        firstWaiter = next;
                    else
                        trail.nextWaiter = next;
                    if (next == null)
                        lastWaiter = trail;
                }
                else
                    trail = t;
                t = next;
            }
        }

        // 如果不是持有独占锁，抛出异常；如果不是则唤醒
        public final void signal() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            // 获取条件队列第一个节点，然后往同步队列转移
            Node first = firstWaiter;
            if (first != null)
                doSignal(first);
        }

        // 唤醒所有其他线程，将条件队列中的头结点转移到同步队列中
        public final void signalAll() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            Node first = firstWaiter;
            if (first != null)
                doSignalAll(first);
        }

        // 等待直到被唤醒或者中断
        public final void awaitUninterruptibly() {
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean interrupted = false;
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                if (Thread.interrupted())
                    interrupted = true;
            }
            if (acquireQueued(node, savedState) || interrupted)
                selfInterrupt();
        }

        // REINTERRUPT:  中断在 node 转移到同步队列“期间”或“之后”发生，此时表明有线程正在调用 
        // singal/singalAll 转移节点。在该种中断模式下，再次设置线程的中断状态。
        // 向后传递中断标志，由后续代码去处理中断
        private static final int REINTERRUPT =  1;
        // THROW_IE:中断在 Node 转移到同步队列之前发生，需要当前线程自行将 node 转移到同步队列中，
        // 并在随后抛出 InterruptedException 异常
        private static final int THROW_IE    = -1;

        // 在唤醒其他线程之前，检查是否中断
        // 检测中断模式，这里有两种中断模式，如下：
        // THROW_IE:中断在 Node 转移到同步队列之前发生，需要当前线程自行将 node 转移到同步队列中，并在随后抛出 InterruptedException 异常
        // REINTERRUPT:  中断在 node 转移到同步队列“期间”或“之后”发生，此时表明有线程正在调用 
        // singal/singalAll 转移节点。在该种中断模式下，再次设置线程的中断状态。
        // 向后传递中断标志，由后续代码去处理中断
        private int checkInterruptWhileWaiting(Node node) {
            return Thread.interrupted() ?
                    (transferAfterCancelledWait(node) ? THROW_IE : REINTERRUPT) :
                    0;
        }

        // 根据中断模式处理中断 
        private void reportInterruptAfterWait(int interruptMode)
                throws InterruptedException {
            if (interruptMode == THROW_IE)
                throw new InterruptedException();
            else if (interruptMode == REINTERRUPT)
                selfInterrupt();
        }


        public final void await() throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            // 条件队列中添加新的线程节点
            Node node = addConditionWaiter();
            // 保存并完全释放同步状态，保存下来的同步状态在重新竞争锁时会用到
            // 相当于从同步队列转移到条件队列
            int savedState = fullyRelease(node);
            int interruptMode = 0; // 中断模式 
            // 这个节点如果没有在同步队列，就表示在条件队列，则一直挂起线程，直到被唤醒或者当前等待线程被中断
            // 在等待时候检查中断状态，如果不是0，就表示有中断发生，跳出循环
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                // 在等待时候检查中断状态，如果不是0，就表示有中断发生，跳出循环
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            // 将这个节点如果转移到了同步队列且中断发生在转移之后
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT; // 代表 await返回的时候，需要重新设置中断状态
            if (node.nextWaiter != null)  // 清理等待状态非 CONDITION 的节点
                unlinkCancelledWaiters();
            if (interruptMode != 0) // 根据中断模式处理中断
                reportInterruptAfterWait(interruptMode);
        }

        public final long awaitNanos(long nanosTimeout)
                throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            final long deadline = System.nanoTime() + nanosTimeout;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    transferAfterCancelledWait(node);
                    break;
                }
                if (nanosTimeout >= spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                nanosTimeout = deadline - System.nanoTime();
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return deadline - System.nanoTime();
        }

        public final boolean awaitUntil(Date deadline)
                throws InterruptedException {
            long abstime = deadline.getTime();
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (System.currentTimeMillis() > abstime) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                LockSupport.parkUntil(this, abstime);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }

        public final boolean await(long time, TimeUnit unit)
                throws InterruptedException {
            long nanosTimeout = unit.toNanos(time);
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            final long deadline = System.nanoTime() + nanosTimeout;
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                if (nanosTimeout >= spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                nanosTimeout = deadline - System.nanoTime();
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }

        //  support for instrumentation

        final boolean isOwnedBy(AbstractSynchronizer sync) {
            return sync == AbstractSynchronizer.this;
        }

        protected final boolean hasWaiters() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION)
                    return true;
            }
            return false;
        }

        protected final int getWaitQueueLength() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            int n = 0;
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION)
                    ++n;
            }
            return n;
        }

        protected final Collection<Thread> getWaitingThreads() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            ArrayList<Thread> list = new ArrayList<Thread>();
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION) {
                    Thread t = w.thread;
                    if (t != null)
                        list.add(t);
                }
            }
            return list;
        }
    }


    static Unsafe getUnsafe() {
        Class<?> unsafeClass = Unsafe.class;
        try {
            for (Field f : unsafeClass.getDeclaredFields()) {
                if ("theUnsafe".equals(f.getName())) {
                    f.setAccessible(true);
                    return (Unsafe) f.get(null);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static final Unsafe unsafe = getUnsafe();
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset
                    (AbstractSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                    (AbstractSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (AbstractSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("next"));

        } catch (Exception ex) { throw new Error(ex); }
    }

    // CAS方式设置head节点，当前对象的headOffset属性为null的时候，才进行更新值
    private final boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    // 通过CAS方式设置tail字段，如果tailOffset和期望值不一样，则更新尾节点
    private final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    private static final boolean compareAndSetWaitStatus(Node node,
                                                         int expect,
                                                         int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset,
                expect, update);
    }

    private static final boolean compareAndSetNext(Node node,
                                                   Node expect,
                                                   Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }
}
