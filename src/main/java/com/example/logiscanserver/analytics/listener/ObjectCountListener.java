package com.example.logiscanserver.analytics.listener;

import com.example.logiscanserver.analytics.entity.ObjectCount;
import com.example.logiscanserver.analytics.repository.ObjectCountRepository;
import com.example.logiscanserver.analytics.repository.ObjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class ObjectCountListener {

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private ObjectCountRepository objectCountRepository;

    @RabbitListener(queues = "object-count-queue")
    public void receiveObjectCount(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            var object = objectRepository.findObjectByName(jsonObject.getString("name"))
                    .orElseThrow(() -> new RuntimeException("Object not found"));

            var objectCount = ObjectCount.builder()
                    .object(object)
                    .count(jsonObject.getInt("count"))
                    .timestamp(Instant.ofEpochMilli(jsonObject.getLong("timestamp")))
                    .build();

            objectCountRepository.save(objectCount);

            log.info("ObjectCount persisted: {}", objectCount);
        } catch (Exception e) {
            log.error("Error deserializing message: {}", e.getMessage());
        }
    }
}