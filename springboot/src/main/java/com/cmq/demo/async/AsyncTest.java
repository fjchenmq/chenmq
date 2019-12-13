package com.cmq.demo.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2019/2/13.
 */
@Service
/**
 * @Async为异步注解，放到方法上，表示调用该方法的线程与此方法异步执行，需要配合@EnableAsync注解使用
 * 主线程获取CountNumber对象后，一方面继续向下执行，执行for循环语句
 * ，另一方面获取对象后，执行对象中的PrintNumber方法。因此PrintNumber方法是与主线程是异步执行的。
 */
@EnableAsync
public class AsyncTest {
    @Autowired
    private CountNumber countNumber;

    public void TestAsync() {

        countNumber.PrintNumber();
        for (int i = 1; i < 10; i++) {
            try {
                TimeUnit.MICROSECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("------------------");
        }
    }
}
