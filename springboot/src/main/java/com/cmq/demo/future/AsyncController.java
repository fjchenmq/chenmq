package com.cmq.demo.future;

/**
 * Created by Administrator on 2019/1/11.
 */

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsyncController {
    public static ExecutorService getExecutorService() {
        return executorService;
    }

    //线程池
    private static ExecutorService executorService;

    //保存异步计算的Future
    private  FutureContext<String> context;

    public static ThreadPoolExecutor getPool() {
        return pool;
    }

    private static ThreadPoolExecutor pool;
    /**
     * 线程池初始化方法
     *
     * corePoolSize 核心线程池大小----10
     * maximumPoolSize 最大线程池大小----30
     * keepAliveTime 线程池中超过corePoolSize数目的空闲线程最大存活时间----30+单位TimeUnit
     * TimeUnit keepAliveTime时间单位----TimeUnit.MINUTES
     * workQueue 阻塞队列----new ArrayBlockingQueue<Runnable>(10)====10容量的阻塞队列
     * threadFactory 新建线程工厂----new CustomThreadFactory()====定制的线程工厂
     * rejectedExecutionHandler 当提交任务数超过maxmumPoolSize+workQueue之和时,
     * 							即当提交第41个任务时(前面线程都没有执行完,此测试方法中用sleep(100)),
     * 						          任务会交给RejectedExecutionHandler来处理
     *
     * 线程池不允许使用 Executors 去创建，而是通过 ThreadPoolExecutor 的方式，这样
     的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
     说明： Executors 返回的线程池对象的弊端如下：
     1） FixedThreadPool 和 SingleThreadPool :
     允许的请求队列长度为 Integer.MAX_VALUE ，可能会堆积大量的请求，从而导致 OOM 。
     2） CachedThreadPool 和 ScheduledThreadPool :
     允许的创建线程数量为 Integer.MAX_VALUE ，可能会创建大量的线程，从而导致 OOM 。
     */
    public void init() {
        pool = new ThreadPoolExecutor(
            10,
            100,
            30,
            TimeUnit.MINUTES,
            new ArrayBlockingQueue<Runnable>(10));
    }

    public AsyncController() {
        //创建一个固定大小的线程池
       // this.executorService = Executors.newFixedThreadPool(100);
        this.context = new FutureContext<>();
        this.init();
    }

    public static void main(String[] args) {
        //启动异步计算
        AsyncController controller = new AsyncController();
        controller.startAsycCompution();

        //启动异步计算结果输出线程，该线程扫描异步计算Futrue的状态，如果已经完成，则输出异步计算结果
        CollectResult output = new CollectResult();
        output.setFutureContext(controller.getFutureContext());
        Thread resultThread = new Thread(output);
        resultThread.start();
    }

    public FutureContext<String> getFutureContext() {
        return this.context;
    }

    public void startAsycCompution() {
        /**
         * 开启100个异步计算，每个异步计算线程随机sleep几秒来模拟计算耗时
         */
        final Random random = new Random();

        for (int i = 0; i < 100; i++) {
            //异步计算
            Future<String> future = this.pool.submit(
                /**
                 * new Callable<String>(){

                @Override public String call() throws Exception {
                int randomInt = random.nextInt(10);
                Thread.sleep(randomInt * 1000);
                return "" + randomInt;
                }

                }
                 */
                () -> {
                    int randomInt = random.nextInt(10);
                    System.out.println(randomInt);
                    Thread.sleep(100);
                    return "" + randomInt;
                });

            //每个异步计算的结果 存放在context中
            this.context.addFuture(future);
        }
    }

}
