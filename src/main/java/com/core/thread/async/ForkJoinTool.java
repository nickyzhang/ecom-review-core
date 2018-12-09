package com.core.thread.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTool {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CountTask task = new CountTask(1,10);
        Future<Integer> result = forkJoinPool.submit(task);
        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    static class CountTask extends RecursiveTask<Integer> {
        // 阈值
        private static final int THRESHOLD = 2;
        private int start;
        private int end;
        public CountTask(int start, int end) {
            super();
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int sum = 0;
            boolean canCompute = (end - start) < THRESHOLD;

            if (canCompute) {
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
            } else {
                int middle = (start + end) / 2;
                CountTask ltask = new CountTask(start,middle);
                CountTask rtask = new CountTask(middle+1,end);
                ltask.fork();
                rtask.fork();
                // 等待子任务执行完，并得到结果
                int lresult = ltask.join();
                int rresult = rtask.join();

                sum = lresult + rresult;
            }

            return sum;
        }
    }
}
