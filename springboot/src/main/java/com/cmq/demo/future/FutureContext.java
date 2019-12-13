package com.cmq.demo.future;

/**
 * Created by Administrator on 2019/1/11.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class FutureContext<T> {
    private List<Future<T>> futureList = new ArrayList<>();

    public  void addFuture(Future<T> future){
        this.futureList.add(future);
    }

    public List<Future<T>> getFutureList(){
        return this.futureList;
    }
}
