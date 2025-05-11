package com.example.currency.service;

import com.example.currency.entity.Currency;
import com.example.currency.entity.ExchangeRateLog;
import com.example.currency.repository.CurrencyRepository;
import com.example.currency.repository.ExchangeRateLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExchangeRateService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    private final CurrencyRepository currencyRepo;
    private final ExchangeRateLogRepository logRepo;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${exchange.api-url}")
    private String apiUrl;

    @Value("${exchange.app-id}")
    private String appId;

    private final Map<String, Map<String, Double>> inMemoryRates = new ConcurrentHashMap<>();

    public ExchangeRateService(CurrencyRepository currencyRepo, ExchangeRateLogRepository logRepo) {
        this.currencyRepo = currencyRepo;
        this.logRepo = logRepo;
    }

    @Scheduled(fixedRate = 3600000)
    public void fetchAndSaveRates() {
        List<Currency> currencies = currencyRepo.findAll();
        logger.info("Starting exchange rate fetch for {} currencies", currencies.size());

        for (Currency currency : currencyRepo.findAll()) {
            String base = currency.getCode().toUpperCase();
            String url = apiUrl + "?app_id=" + appId + "&base=" + base;

            try {
                Map<?, ?> response = restTemplate.getForObject(url, Map.class);
                if (response == null || !response.containsKey("rates")) {
                    logger.warn("No 'rates' in response for base: {}", base);
                    continue;
                }

                Map<String, Double> rates = (Map<String, Double>) response.get("rates");
                inMemoryRates.put(base, rates);

                LocalDateTime now = LocalDateTime.now();
                for (Map.Entry<String, Double> entry : rates.entrySet()) {
                    logRepo.save(new ExchangeRateLog(base, entry.getKey(), entry.getValue(), now));
                }

                logger.info("Updated rates for base: {}", base);
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