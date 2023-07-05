package com.example.carsdetailsmicroservice.kafka;

import com.example.carsdetailsmicroservice.events.CarPurchaseEvent;
import com.example.carsdetailsmicroservice.events.DetailAddEvent;
import com.example.carsdetailsmicroservice.service.CarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
/**
 * This class represents a consumer that listens to Kafka topics and processes messages.
 */
@Slf4j
@Component
public class Consumer {
  private final ObjectMapper objectMapper;
  private final CarService carService;

  @Autowired
  public Consumer(ObjectMapper objectMapper, CarService carService) {
    this.objectMapper = objectMapper;
    this.carService = carService;
  }

  @KafkaListener(topics = "topic.driver.buy.car")
  public void processCarPurchaseEvent(String message) throws JsonProcessingException {
    log.info("message consumed {}", message);
    CarPurchaseEvent carPurchaseEvent = objectMapper.readValue(message, CarPurchaseEvent.class);
    carService.byuCar(carPurchaseEvent);
  }

  @Transactional
  @KafkaListener(topics = "topic.add.detail")
  public void processDetailAddEvent(String message) throws JsonProcessingException {
    log.info("message consumed {}", message);
    DetailAddEvent detailAddEvent = objectMapper.readValue(message, DetailAddEvent.class);
    carService.updateCarDetail(detailAddEvent.getLicensePlate(), detailAddEvent.getSerialNumber());
  }
}