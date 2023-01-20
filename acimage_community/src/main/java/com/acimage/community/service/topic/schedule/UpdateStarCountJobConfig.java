package com.acimage.community.service.topic.schedule;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateStarCountJobConfig {
    //3分钟
    private String cron ="0 */3 * * * ?";

    @Bean
    public JobDetail updateStarCountJobDetail() {
        JobDetail jobDetail = JobBuilder.newJob(UpdateStarCountJob.class)
                .withIdentity("updateStarCount", "topicGroup")
                .storeDurably()
                .build();
        return jobDetail;
    }

    @Bean
    public Trigger updateStarCountJobTrigger() {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(updateStarCountJobDetail())
                .withIdentity("updateStarCountTrigger", "topicTrigger")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        return trigger;
    }
}

