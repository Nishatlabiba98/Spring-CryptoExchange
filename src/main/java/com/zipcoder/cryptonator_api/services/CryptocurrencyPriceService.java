package com.zipcoder.cryptonator_api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.zipcoder.cryptonator_api.domain.CryptocurrencyPrice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Locale;

@Service
public class CryptocurrencyPriceService {
    private final RestTemplate restTemplate;
    private final String priceApiBaseUrl;

    public CryptocurrencyPriceService(
            RestTemplate restTemplate,
            @Value("${crypto.price-api.base-url:https://min-api.cryptocompare.com/data/price}") String priceApiBaseUrl) {
        this.restTemplate = restTemplate;
        this.priceApiBaseUrl = priceApiBaseUrl;
    }

    public CryptocurrencyPrice fetchPrice(String base, String quote) {
        String normalizedBase = normalize(base);
        String normalizedQuote = normalize(quote);
        String requestUrl =
                String.format("%s?fsym=%s&tsyms=%s", priceApiBaseUrl, normalizedBase, normalizedQuote);

        try {
            JsonNode response = restTemplate.getForObject(requestUrl, JsonNode.class);
            if (response == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY, "Price API returned an empty response");
            }

            JsonNode amount = response.get(normalizedQuote);
            if (amount == null || amount.isNull()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_GATEWAY, "Price API returned an unexpected response");
            }

            return new CryptocurrencyPrice(
                    normalizedBase,
                    normalizedQuote,
                    amount.decimalValue());
        } catch (RestClientException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    String.format(
                            "Unable to fetch price quote from CryptoCompare (%s)",
                            exception.getClass().getSimpleName()),
                    exception);
        }
    }

    private String normalize(String symbol) {
        return symbol.trim().toUpperCase(Locale.ROOT);
    }
}
