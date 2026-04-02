package com.zipcoder.cryptonator_api.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Foo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String base;
    private String target;
    private Double price;
    private Double volume;
    private Double change;
    private LocalDateTime lastUpdated;

    public Foo() {}

    public Foo(String base, String target, Double price, Double volume, Double change) {
        this.base        = base.toLowerCase();
        this.target      = target.toLowerCase();
        this.price       = price;
        this.volume      = volume;
        this.change      = change;
        this.lastUpdated = LocalDateTime.now();
    }

    public Long getId()                                    { return id; }
    public void setId(Long id)                             { this.id = id; }
    public String getBase()                                { return base; }
    public void setBase(String base)                       { this.base = base.toLowerCase(); }
    public String getTarget()                              { return target; }
    public void setTarget(String target)                   { this.target = target.toLowerCase(); }
    public Double getPrice()                               { return price; }
    public void setPrice(Double price)                     { this.price = price; }
    public Double getVolume()                              { return volume; }
    public void setVolume(Double volume)                   { this.volume = volume; }
    public Double getChange()                              { return change; }
    public void setChange(Double change)                   { this.change = change; }
    public LocalDateTime getLastUpdated()                  { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated)  { this.lastUpdated = lastUpdated; }
}