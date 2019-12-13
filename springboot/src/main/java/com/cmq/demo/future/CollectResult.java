package com.cmq.demo.future;

import java.util.List;
import java.util.concurrent.Future;

public class CollectResult implements Runnable {


    private FutureContext<String> context;

    public void setFutureContext(FutureContext<String> context) {
        this.context = context;
    }

    @Override
    public void run() {
        System.out.println("start to collect result:");
        List<Future<String>> list = this.context.getFutureList();
        int total = 0;
        //结果收集合并
        for (Future<String> future : list) {
            total += this.outputResultFromFuture(future);
        }
        System.out.println("finish to collect result.");
        System.out.println("total:" + total);
        //关闭线程池
      AsyncController.getPool().shutdown();
    }

    private int outputResultFromFuture(Future<String> future) {
        try {
            //while (true)////好像不需要 get自动会等待结果返回
            {
                //if(future.isDone() && !future.isCancelled()){//好像不需要 get就是在done之后返回
                return Integer.parseInt(future.get());
                // }else{
                //   Thread.sleep(1000);
                //  }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
        return -1000;
    }

}
