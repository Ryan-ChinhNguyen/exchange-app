package com.example.currency.service;

import com.example.currency.entity.Currency;
import com.example.currency.entity.ExchangeRateLog;
import com.example.currency.repository.CurrencyRepository;
import com.example.currency.repository.ExchangeRateLogRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExchangeRateService {

    private final CurrencyRepository currencyRepo;
    private final ExchangeRateLogRepository logRepo;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${exchange.api-url}")
    private String apiUrl;

    @Value("${exchange.app-icxdzd}")
    private String appId;

    private final Map<String, Map<String, Double>> inMemoryRates = new ConcurrentHashMap<>();

    public ExchangeRateService(CurrencyRepository currencyRepo, ExchangeRateLogRepository logRepo) {
        this.currencyRepo = currencyRepo;
        this.logRepo = logRepo;
    }

    @Scheduled(fixedRate = 3600000)
    public void fetchRates() {
        for (Currency currency : currencyRepo.findAll()) {
            String url = apiUrl + "?app_id=" + appId + "&base=" + currency.getCode();
            try {
                Map<?, ?> response = restTemplate.getForObject(url, Map.class);
                Map<String, Double> rates = (Map<String, Double>) response.get("rates");
                inMemoryRates.put(currency.getCode(), rates);
                LocalDateTime now = LocalDateTime.now();
                rates.forEach((target, rate) -> {
                    logRepo.save(new ExchangeRateLog(currency.getCode(), target, rate, now));
                });
            } catch (Exception e) {
                System.out.println("Failed to fetch rates for: " + currency.getCode());
            }
        }
    }

    public Currency addCurrency(String code) {
        return currencyRepo.save(new Currency(code.toUpperCase()));
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepo.findAll();
    }

    public Map<String, Double> getRates(String baseCurrency) {
        return inMemoryRates.getOrDefault(baseCurrency.toUpperCase(), Collections.emptyMap());
    }
}