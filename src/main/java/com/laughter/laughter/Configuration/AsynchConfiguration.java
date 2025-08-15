package com.laughter.laughter.Configuration;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class AsynchConfiguration implements AsyncConfigurer{
    @Override
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("EmailAsync-");
        return executor;
    }

    @Override 
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler(){
        return (ex,method,params)->{
            System.err.println("Asynch Error in method "+method.getName());
        };
    }

}
