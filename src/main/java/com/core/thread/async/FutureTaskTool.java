package com.core.thread.async;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureTaskTool {
    public static void main(String[] args) {
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new Task());
        Thread thread = new Thread(futureTask);
        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 调用isDone()判断任务是否结束
        if (futureTask.isDone()) {
            System.out.println("Task is not done!");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int result = 0;
        try {
            // 5. 调用get()方法获取任务结果,如果任务没有执行完成则阻塞等待
            result = futureTask.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("result is " + result);
    }
    static class Task implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("线程["+Thread.currentThread().getName()+"] 正在运行......");
            int result = 0;
            for (int i = 0; i < 100; ++i) {
                result += 1;
            }
            Thread.sleep(3000);
            return result;
        }
    }
}
