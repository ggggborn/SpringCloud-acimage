package com.acimage.community.service.topic.schedule;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateCommentCountJobConfig {

    private String cron ="0 */3 * * * ?";

    @Bean
    public JobDetail updateCommentCountJobDetail() {
        JobDetail jobDetail = JobBuilder.newJob(UpdateCommentCountJob.class)
                .withIdentity("updateCommentCount", "topicGroup")
                .storeDurably()
                .build();
        return jobDetail;
    }

    @Bean
    public Trigger updateCommentCountJobTrigger() {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(updateCommentCountJobDetail())
                .withIdentity("updateCommentCountTrigger", "topicTrigger")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        return trigger;
    }
}

