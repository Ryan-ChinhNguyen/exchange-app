package com.example.currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CurrencyExchangeApplication {
    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(CurrencyExchangeApplication.class);
        ConfigurableApplicationContext context = app.run(args);

        String dbUrl = context.getEnvironment().getProperty("spring.datasource.url");
        System.out.println("===> DB URL from config: " + dbUrl);
    }
}