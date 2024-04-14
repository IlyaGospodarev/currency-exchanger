package com.gambl.demo.scheduler;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail exchangeRateUpdateJobDetail() {
        return JobBuilder.newJob(ExchangeRateJob.class)
                .withIdentity("exchangeRateUpdateJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger exchangeRateUpdateJobTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInHours(1)
                .repeatForever();

        return TriggerBuilder.newTrigger()
                .forJob(exchangeRateUpdateJobDetail())
                .withIdentity("exchangeRateUpdateTrigger")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
