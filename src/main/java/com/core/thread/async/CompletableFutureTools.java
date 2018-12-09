package com.core.thread.async;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CompletableFutureTools {
    public static void main(String[] args) {
        CompletableFuture<Long> futureNum= getAsyncNum();
        //do anything you want, 当前线程不被阻塞
        System.out.println(111);
        // 线程任务完成的话，执行回调函数，不阻塞后续操作
        futureNum.thenApply(new Function<Long, Object>() {
            @Override
            public Object apply(Long aLong) {
                return 1;
            }

            @Override
            public <V> Function<Long, V> andThen(Function<? super Object, ? extends V> after) {
                return null;
            }
        });
        System.out.println(222);
    }
    public static CompletableFuture<Long> getAsyncNum() {
        CompletableFuture<Long> futureNum = new CompletableFuture<>();
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            futureNum.complete(10000L);
        }).start();
        return futureNum;
    }



}
