package com.ecommerce.catalog.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ThreadConfig {

    public final static int EXECUTOR_POOL_SIZE      = 5;
    public final static int EXECUTOR_QUEUE_SIZE     = 25;
    public final static int SCHEDULER_POOL_SIZE     = 5;

    @Bean
    public TaskExecutor executors() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(EXECUTOR_POOL_SIZE);
        executor.setMaxPoolSize( EXECUTOR_POOL_SIZE * 2 );
        executor.setQueueCapacity( EXECUTOR_QUEUE_SIZE );
        executor.setThreadNamePrefix( "executor_thread" );
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskScheduler scheduler(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize( SCHEDULER_POOL_SIZE );
        scheduler.setThreadNamePrefix( "scheduler_thread" );
        return scheduler;
    }

}
