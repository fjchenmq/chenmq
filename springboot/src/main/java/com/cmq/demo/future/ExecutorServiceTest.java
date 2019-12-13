package com.cmq.demo.future;

/**
 * Created by Administrator on 2019/1/11.
 */

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceTest {
    static ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    //线程池
    private static ExecutorService executorService;

    public ExecutorServiceTest() {
        //创建一个固定大小的线程池
        this.executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 10000; i++) {
            queue.add(i);
        }
    }

    public static void main(String[] args) {
        //启动异步计算
        ExecutorServiceTest test = new ExecutorServiceTest();

        for (int i = 0; i < 1000; i++) {

            executorService.submit(() -> {//创建一个任务
                while (!queue.isEmpty()) {
                    System.out.println(queue.poll() + ":" + Thread.currentThread());
                }
            });
        }
        if (queue.isEmpty()) {
            executorService.shutdown();
        }
    }

}
