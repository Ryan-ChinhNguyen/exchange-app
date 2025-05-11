package com.example.currency.service;

import com.example.currency.entity.Currency;
import com.example.currency.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExchangeRateServiceTest {

  @Autowired
  private ExchangeRateService exchangeRateService;

  @Autowired
  private CurrencyRepository currencyRepository;

  @BeforeEach
  void setUp() {
    // Before each test, clear the data in the Currency table to avoid interference between tests
    currencyRepository.deleteAll();
  }

  @Test
  void testAddCurrency() {
    String currencyCode = "USD";

    Currency addedCurrency = exchangeRateService.addCurrency(currencyCode);

    // Assert
    assertNotNull(addedCurrency, "Currency should not be null");
    assertEquals(currencyCode.toUpperCase(), addedCurrency.getCode(), "Currency code should match the input code");

    // Check if the Currency has been saved to the database
    List<Currency> allCurrencies = currencyRepository.findAll();
    assertEquals(1, allCurrencies.size(), "There should be one currency in the repository");
    assertEquals(currencyCode.toUpperCase(), allCurrencies.get(0).getCode(), "The currency code in the database should match the added one");
  }

  @Test
  void testGetAllCurrencies() {
    exchangeRateService.addCurrency("USD");
    exchangeRateService.addCurrency("EUR");

    List<Currency> currencies = exchangeRateService.getAllCurrencies();

    // Assert
    assertNotNull(currencies, "The list of currencies should not be null");
    assertEquals(2, currencies.size(), "There should be two currencies in the repository");
    assertTrue(currencies.stream().anyMatch(c -> c.getCode().equals("USD")), "USD currency should be present");
    assertTrue(currencies.stream().anyMatch(c -> c.getCode().equals("EUR")), "EUR currency should be present");
  }
}
