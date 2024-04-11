package com.example.logiscanserver.analytics.repository;

import com.example.logiscanserver.analytics.entity.ObjectCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectCountRepository extends JpaRepository<ObjectCount, Long> {

    ObjectCount findFirstByOrderByTimestampDesc();
}
