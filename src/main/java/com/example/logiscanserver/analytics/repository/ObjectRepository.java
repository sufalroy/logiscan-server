package com.example.logiscanserver.analytics.repository;

import com.example.logiscanserver.analytics.entity.Object;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObjectRepository extends JpaRepository<Object, Long> {

    Optional<Object> findObjectByName(final String name);
}
