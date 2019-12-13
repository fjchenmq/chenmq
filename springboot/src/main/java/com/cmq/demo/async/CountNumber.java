package com.cmq.demo.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2019/2/13.
 */
@Service
public class CountNumber {
    @Async
    public void PrintNumber() {
        for (int i = 1; i < 10; i++) {
            System.out.println("i = " + i);
            try {
                TimeUnit.MICROSECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
