package com.core.thread.base;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class ThreadTools {
    public static void main(String[] args) {
        FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
            public Integer call() throws Exception {
                int i = 0;
                for (;i < 10;i++) {
                    System.out.println(Thread.currentThread().getName()+" 的循环变量i值："+i);
                }
                return i;
            }
        });

        Thread T1 = new Thread(task);
        T1.start();

        try {
            System.out.println("结果=> "+task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
