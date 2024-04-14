package com.example.logiscanserver.analytics.service;

import com.example.logiscanserver.analytics.entity.ObjectCount;
import com.example.logiscanserver.analytics.repository.ObjectCountRepository;
import com.example.logiscanserver.analytics.repository.ObjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class ObjectCountService {

    @Autowired
    private ObjectRepository objectRepository;

    @Autowired
    private ObjectCountRepository objectCountRepository;

    @Autowired
    SimpMessagingTemplate template;

    @RabbitListener(queues = "object-count-queue")
    public void receiveObjectCount(String message) {
        try {
            log.info("Received message: {}", message);
            JSONObject jsonObject = new JSONObject(message);

            var name = jsonObject.getString("name");
            var count = jsonObject.getInt("count");
            var time = Instant.ofEpochMilli(jsonObject.getLong("timestamp"));

            var object = objectRepository.findObjectByName(name)
                    .orElseThrow(() -> new RuntimeException("Object with name '" + name + "' not found in the repository."));

            var objectCount = ObjectCount.builder()
                    .object(object)
                    .count(count)
                    .timestamp(time)
                    .build();

            var persistedObjectCount = objectCountRepository.save(objectCount);

            template.convertAndSend("/topic/object-count", persistedObjectCount);
        } catch (JSONException e) {
            log.error("Error parsing JSON message: {}", e.getMessage());
        } catch (RuntimeException e) {
            log.error("Error processing message: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred while processing message: {}", e.getMessage());
        }
    }
}
