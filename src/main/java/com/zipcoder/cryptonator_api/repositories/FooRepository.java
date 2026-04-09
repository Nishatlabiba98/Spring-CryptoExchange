package com.zipcoder.cryptonator_api.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.zipcoder.cryptonator_api.domain.Foo;

@Repository
public interface FooRepository extends CrudRepository<Foo, Long> {
    Optional<Foo> findByBaseAndTarget(String base, String target);
}