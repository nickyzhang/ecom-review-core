package com.core.thread.lock;


import com.core.mock.AbstractSynchronizer;
import com.core.mock.XLock;

public class LockTool {
    int num = 0;
    XLock lock = new XLock(true);
    public void save() {
        lock.lock();
        num++;
        System.out.println(Thread.currentThread().getName()+ ": "+num);
        AbstractSynchronizer.Node head = lock.sync.head;

        for (AbstractSynchronizer.Node node = head; node != null; node = node.next) {
            if (node == head) {
                System.out.println(Thread.currentThread().getName()+" HEAD=> "+node);
            } else if (node.next == null) {
                System.out.println(Thread.currentThread().getName()+" TAIL=> "+node);
            } else {
                System.out.println(Thread.currentThread().getName()+" Next=> "+node);
            }
        }

        lock.unlock();
    }

    public static void main(String[] args) {
        LockTool tool = new LockTool();
        int i = 0;
        while (i < 2) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    tool.save();
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            });
            t.start();
            i++;
        }
    }
}
