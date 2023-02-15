package com.acimage.community.schedule;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdatePageViewJobConfig {

    @Value("${cron.page-view-job}")
    private String cron ;

    @Bean
    public JobDetail updatePageViewJobDetail() {
        JobDetail jobDetail = JobBuilder.newJob(UpdatePageViewJob.class)
                .withIdentity("updatePageView", "topicGroup")
                .storeDurably()
                .build();
        return jobDetail;
    }

    @Bean
    public Trigger updatePageViewJobTrigger() {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(updatePageViewJobDetail())
                .withIdentity("updatePageViewTrigger", "topicTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .startNow()
                .build();
        return trigger;
    }
}

