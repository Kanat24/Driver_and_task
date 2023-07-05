package com.example.drivesbillsmicroservice.kafka;

import com.example.drivesbillsmicroservice.events.CarPurchaseEvent;
import com.example.drivesbillsmicroservice.events.DetailAddEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Represents a Kafka producer component.
 * Sends messages to Kafka topics "driverTopic" and "driverTopic1".
 */
@Slf4j
@Component
public class Producer {
  @Value("${topic.name}")
  private String buyCarTopic;
  @Value("${topic.name-1}")
  private String addDetailTopic;

  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public Producer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public String sendMessageCarPurchase(CarPurchaseEvent carPurchaseEvent)
          throws JsonProcessingException {
    String purchaseEventAsMessage = objectMapper.writeValueAsString(carPurchaseEvent);
    kafkaTemplate.send(buyCarTopic, purchaseEventAsMessage);
    log.info("Driver request produced {}", purchaseEventAsMessage);
    return "message sent";
  }

  public String sendMessageDetailAdd(DetailAddEvent detailAddEvent) throws JsonProcessingException {
    String detailAddEventAsMessage = objectMapper.writeValueAsString(detailAddEvent);
    kafkaTemplate.send(addDetailTopic, detailAddEventAsMessage);
    log.info("Driver request produced {}", detailAddEventAsMessage);
    return "message sent";
  }
}