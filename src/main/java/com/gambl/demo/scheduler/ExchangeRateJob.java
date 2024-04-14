package com.gambl.demo.scheduler;

import com.gambl.demo.service.CurrencyService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateJob implements Job {
    private final CurrencyService currencyService;

    @Autowired
    public ExchangeRateJob(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        currencyService.fetchAndStoreExchangeRates();
    }
}
