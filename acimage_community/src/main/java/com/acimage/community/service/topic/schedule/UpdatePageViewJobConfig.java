package com.acimage.community.service.topic.schedule;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdatePageViewJobConfig {

    private String cron ="0 */2 * * * ?";

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
                .startNow()
                .build();
        return trigger;
    }
}

