package com.zipcoder.cryptonator_api.services;

import com.zipcoder.cryptonator_api.domain.CryptocurrencyPrice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class CryptocurrencyPriceServiceTest {
    private RestTemplate restTemplate;
    private MockRestServiceServer server;
    private CryptocurrencyPriceService service;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        server = MockRestServiceServer.createServer(restTemplate);
        service = new CryptocurrencyPriceService(restTemplate, "https://min-api.cryptocompare.com/data/price");
    }

    @Test
    void fetchPriceUsesConfiguredPriceEndpoint() {
        server.expect(requestTo("https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"USD\":64321.12}", MediaType.APPLICATION_JSON));

        CryptocurrencyPrice price = service.fetchPrice("btc", "usd");

        server.verify();
        assertEquals("BTC", price.getBase());
        assertEquals("USD", price.getQuote());
        assertEquals(new BigDecimal("64321.12"), price.getAmount());
    }

    @Test
    void fetchPriceRejectsUnexpectedResponseBody() {
        server.expect(requestTo("https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"data\":{}}", MediaType.APPLICATION_JSON));

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> service.fetchPrice("btc", "usd"));

        server.verify();
        assertEquals(502, exception.getStatus().value());
    }
}
