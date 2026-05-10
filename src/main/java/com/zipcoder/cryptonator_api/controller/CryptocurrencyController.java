package com.zipcoder.cryptonator_api.controller;

import com.zipcoder.cryptonator_api.domain.CryptocurrencyPrice;
import com.zipcoder.cryptonator_api.services.CryptocurrencyPriceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cryptocurrencies")
public class CryptocurrencyController {
    private final CryptocurrencyPriceService cryptocurrencyPriceService;

    public CryptocurrencyController(CryptocurrencyPriceService cryptocurrencyPriceService) {
        this.cryptocurrencyPriceService = cryptocurrencyPriceService;
    }

    @GetMapping("/{base}/{quote}")
    public CryptocurrencyPrice getPrice(@PathVariable String base, @PathVariable String quote) {
        return cryptocurrencyPriceService.fetchPrice(base, quote);
    }
}
