package com.example.drivesbillsmicroservice.kafka;

import com.example.drivesbillsmicroservice.enums.Currency;
import com.example.drivesbillsmicroservice.events.DetailAddEvent;
import com.example.drivesbillsmicroservice.service.AccountService;
import com.example.drivesbillsmicroservice.service.DriverService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Represents a Kafka consumer component.
 * Consumes messages from the "topic.car" Kafka topic.
 */
@Slf4j
@Component
public class Consumer {
  private final ObjectMapper objectMapper;
  private final AccountService accountService;
  private final DriverService driverService;

  @Autowired
  public Consumer(ObjectMapper objectMapper,
                  AccountService accountService,
                  DriverService driverService) {
    this.objectMapper = objectMapper;
    this.accountService = accountService;
    this.driverService = driverService;
  }

  @KafkaListener(topics = "topic.car")
  public void consumeMessageCarTopic(String message) throws JsonProcessingException {
    log.info("message consumed {}", message);
    DetailAddEvent detailAddEvent = objectMapper.readValue(message, DetailAddEvent.class);
    accountService.debit(detailAddEvent.getDriverId(),
                         detailAddEvent.getPrice().doubleValue(),
                         Currency.valueOf(detailAddEvent.getCurrency()));
    driverService.sendSuccessfulPaymentEvent(detailAddEvent);
  }
}