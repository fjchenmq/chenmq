package com.cmq.demo.event;

/**
 * Created by Administrator on 2019/3/31.
 */
public class TestEvent {

    public static MySource source = new MySource();

    public static void main(String[] args) {
        source.addStateChangeListener(new StateChangeListener());
        source.addStateChangeToOneListener(new StateChangeToOneListener());
        Thread thread = new Thread(() -> {
            source.changeFlag();
            source.changeFlag();
        });
        thread.start();
        // System.out.println(thread.getId());
        //System.out.println(Thread.currentThread().getId());
    }
}
