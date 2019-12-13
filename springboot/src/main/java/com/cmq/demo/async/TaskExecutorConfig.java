package com.cmq.demo.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
//不加这个类启动会报：spring异步执行报异常No qualifying bean of type 'org.springframework.core.task.TaskExecutor' available

@Configuration
@ComponentScan("com.cmq.demo.async")
@EnableAsync //开启异步任务支持   如果Application已经开启，则这个可以省略
public class TaskExecutorConfig implements AsyncConfigurer {
    // 此配置可以解决Could not find default TaskExecutor bean的debug异常
    @Override
    public Executor getAsyncExecutor() {// 实现AsyncConfigurer接口并重写getAsyncExecutor方法，并返回一个ThreadPoolTaskExecutor，这样我们就获得了一个基于线程池TaskExecutor
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(80);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
