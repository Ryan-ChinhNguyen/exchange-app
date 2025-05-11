package com.example.currency.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ExchangeRateLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String baseCurrency;
    private String targetCurrency;
    private Double rate;
    private LocalDateTime timestamp;

    public ExchangeRateLog() {}
    public ExchangeRateLog(String base, String target, Double rate, LocalDateTime timestamp) {
        this.baseCurrency = base;
        this.targetCurrency = target;
        this.rate = rate;
        this.timestamp = timestamp;
    }

    public Long getId() { return id; }
    public String getBaseCurrency() { return baseCurrency; }
    public String getTargetCurrency() { return targetCurrency; }
    public Double getRate() { return rate; }
    public LocalDateTime getTimestamp() { return timestamp; }
}