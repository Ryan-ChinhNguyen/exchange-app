package com.example.currency.controller;

import com.example.currency.entity.Currency;
import com.example.currency.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final ExchangeRateService service;

    public CurrencyController(ExchangeRateService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        return ResponseEntity.ok(service.getAllCurrencies());
    }

    @GetMapping("/{code}/rates")
    public ResponseEntity<Map<String, Double>> getRates(@PathVariable String code) {
        return ResponseEntity.ok(service.getRates(code));
    }

    @PostMapping
    public ResponseEntity<Currency> addCurrency(@RequestParam String code) {
        return ResponseEntity.ok(service.addCurrency(code));
    }
}