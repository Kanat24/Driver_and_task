package com.example.carsdetailsmicroservice.kafka;

import com.example.carsdetailsmicroservice.events.DetailAddEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
/**
 * This class represents a producer that sends messages to a Kafka topic.
 */

@Slf4j
@Component
public class Producer {

  @Value("${topic.name}")
  private String driverTopic;

  private final ObjectMapper objectMapper;
  private final KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  public Producer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
  }

  public String sendDetailAddEventMessage(DetailAddEvent detailAddEvent)
          throws JsonProcessingException {
    String detailEventAsMessage = objectMapper.writeValueAsString(detailAddEvent);
    kafkaTemplate.send(driverTopic, detailEventAsMessage);
    log.info("Driver request produced {}", detailEventAsMessage);
    return "message sent";
  }
}