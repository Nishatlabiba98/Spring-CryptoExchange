package com.zipcoder.cryptonator_api.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zipcoder.cryptonator_api.domain.Foo;
import com.zipcoder.cryptonator_api.services.FooService;

@RestController
@RequestMapping("/api/crypto")
public class FooController {

    private final FooService fooService;

    @Inject
    public FooController(FooService fooService) {
        this.fooService = fooService;
    }

    @GetMapping
    public ResponseEntity<List<Foo>> getAll() {
        return ResponseEntity.ok(fooService.getAll());
    }

    @GetMapping("/{base}/{target}")
    public ResponseEntity<Foo> getByPair(@PathVariable String base,
                                          @PathVariable String target) {
        return fooService.getByPair(base, target)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{base}/{target}")
    public ResponseEntity<?> track(@PathVariable String base,
                                    @PathVariable String target) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(fooService.fetchAndSave(base, target));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }

    @PutMapping("/{base}/{target}")
    public ResponseEntity<?> refresh(@PathVariable String base,
                                      @PathVariable String target) {
        try {
            return ResponseEntity.ok(fooService.fetchAndSave(base, target));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (fooService.getById(id) == null) return ResponseEntity.notFound().build();
        fooService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}