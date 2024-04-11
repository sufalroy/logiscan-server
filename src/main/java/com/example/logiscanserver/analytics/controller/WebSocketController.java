package com.example.logiscanserver.analytics.controller;

import com.example.logiscanserver.analytics.entity.ObjectCount;
import com.example.logiscanserver.analytics.repository.ObjectCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private ObjectCountRepository objectCountRepository;

    @MessageMapping("/object-count")
    @SendTo("/topic/object-count")
    public ObjectCount broadcastObjectCount() {
        return objectCountRepository.findFirstByOrderByTimestampDesc();
    }
}
