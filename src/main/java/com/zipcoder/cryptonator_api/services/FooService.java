package com.zipcoder.cryptonator_api.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.zipcoder.cryptonator_api.domain.Foo;
import com.zipcoder.cryptonator_api.repositories.FooRepository;

@Service
public class FooService {

    // CoinGecko free API — no key required
    // base = coin id (e.g. "bitcoin", "ethereum", "litecoin")
    // target = currency (e.g. "usd", "eur", "btc")
    private static final String API_URL =
        "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=%s&include_24hr_change=true&include_24hr_vol=true";

    private final FooRepository fooRepository;
    private final RestTemplate restTemplate;

    @Inject
    public FooService(FooRepository fooRepository, RestTemplate restTemplate) {
        this.fooRepository = fooRepository;
        this.restTemplate  = restTemplate;
    }

    public List<Foo> getAll() {
        List<Foo> result = new ArrayList<>();
        fooRepository.findAll().forEach(result::add);
        return result;
    }

    public Optional<Foo> getByPair(String base, String target) {
        return fooRepository.findByBaseAndTarget(base.toLowerCase(), target.toLowerCase());
    }

    public Foo getById(Long id) {
        return fooRepository.findOne(id);
    }

    public void deleteById(Long id) {
        fooRepository.delete(id);
    }

    public Foo fetchAndSave(String base, String target) throws JSONException {
        String url      = String.format(API_URL, base.toLowerCase(), target.toLowerCase());
        String response = restTemplate.getForObject(url, String.class);

        // Response looks like: {"bitcoin":{"usd":67000,"usd_24h_vol":123456,"usd_24h_change":1.5}}
        JSONObject root       = new JSONObject(response);
        JSONObject coinData   = root.getJSONObject(base.toLowerCase());

        double price  = coinData.optDouble(target.toLowerCase(), 0);
        double volume = coinData.optDouble(target.toLowerCase() + "_24h_vol", 0);
        double change = coinData.optDouble(target.toLowerCase() + "_24h_change", 0);

        Foo foo = fooRepository
                .findByBaseAndTarget(base.toLowerCase(), target.toLowerCase())
                .orElse(new Foo(base, target, price, volume, change));

        foo.setPrice(price);
        foo.setVolume(volume);
        foo.setChange(change);
        foo.setLastUpdated(LocalDateTime.now());

        return fooRepository.save(foo);
    }

    @Scheduled(fixedRate = 300000)
    public void refreshAll() {
        getAll().forEach(foo -> {
            try {
                fetchAndSave(foo.getBase(), foo.getTarget());
            } catch (Exception e) {
                System.err.println("Refresh failed: " + foo.getBase() + "-" + foo.getTarget());
            }
        });
    }
}