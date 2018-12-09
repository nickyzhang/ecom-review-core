
package com.core.thread.lock;

public class SynchronizeTool {
    int count;
    public void inc(){
        synchronized (this) {
            count++;
            System.out.println(count);
            doInc();

        }
    }

    public void doInc(){
        synchronized (this) {
           count++;
            System.out.println(count);
        }
    }

    public static void main(String[] args) {
        SynchronizeTool client = new SynchronizeTool();
        client.inc();
    }
}
