package com.acimage.image.service.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class DhashTaskPoolConfig {
        public static final String DHASH_TASK_POOL="dhashTask";

        @Bean(name = DHASH_TASK_POOL)
        public Executor imageDhashExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            //核心线程池大小
            executor.setCorePoolSize(4);
            //最大线程数
            executor.setMaxPoolSize(10);
            //队列容量
            executor.setQueueCapacity(100);
            //活跃时间
            executor.setKeepAliveSeconds(60);
            //线程名字前缀
            executor.setThreadNamePrefix("dhash-task");
            // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
            executor.setWaitForTasksToCompleteOnShutdown(true);
//            // 线程池对拒绝任务的处理策略,当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
//            executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            executor.initialize();
            return executor;
        }

}
