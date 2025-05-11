package com.example.currency.scheduler;

import com.example.currency.service.ExchangeRateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledService {

  private final ExchangeRateService exchangeRateService;

  public ScheduledService(ExchangeRateService exchangeRateService) {
    this.exchangeRateService = exchangeRateService;
  }

  // Fetch exchange rate from OpenExchangeRates every 6 hours
  @Scheduled(cron = "0 0 */6 * * *")
  public void fetchRatesPeriodically() {
    exchangeRateService.fetchAndSaveRates();
  }
}
