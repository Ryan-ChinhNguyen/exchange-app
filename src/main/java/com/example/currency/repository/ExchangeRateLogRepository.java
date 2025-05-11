package com.example.currency.repository;

import com.example.currency.entity.ExchangeRateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExchangeRateLogRepository extends JpaRepository<ExchangeRateLog, Long> {
    List<ExchangeRateLog> findByBaseCurrency(String baseCurrency);
}