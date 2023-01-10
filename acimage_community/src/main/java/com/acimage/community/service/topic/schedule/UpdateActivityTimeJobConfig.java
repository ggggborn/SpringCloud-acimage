package com.acimage.community.service.topic.schedule;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateActivityTimeJobConfig {

    private static String cron ="0 */5 * * * ?";

    @Bean
    public JobDetail updateActivityTimeJobDetail() {
        JobDetail jobDetail = JobBuilder.newJob(UpdateActivityTimeJob.class)
                .withIdentity("updateActivityTime", "topicGroup")
                .storeDurably()
                .build();
        return jobDetail;
    }

    @Bean
    public Trigger updateActivityTimeJobTrigger() {
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(updateActivityTimeJobDetail())
                .withIdentity("updateActivityTimeTrigger", "topicTrigger")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        return trigger;
    }
}

