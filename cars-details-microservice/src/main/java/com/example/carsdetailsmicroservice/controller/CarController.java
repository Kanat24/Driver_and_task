package com.example.carsdetailsmicroservice.controller;

import com.example.carsdetailsmicroservice.controller.utils.ControllerUtils;
import com.example.carsdetailsmicroservice.dto.car.create.CarCreateRequestDto;
import com.example.carsdetailsmicroservice.dto.car.get.CarGetResponseDto;
import com.example.carsdetailsmicroservice.dto.car.update.CarUpdateRequestDto;
import com.example.carsdetailsmicroservice.dto.message.MessageDto;
import com.example.carsdetailsmicroservice.events.DetailAddEvent;
import com.example.carsdetailsmicroservice.service.CarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@OpenAPIDefinition(
        info = @Info(
                title = "Rik Masters Test",
                version = "1.0.0",
                description = "Service for working with cars"
        ),
        servers = @Server(url = "http://localhost:8081")
)
@RestController
@RequestMapping("/cars")
public class CarController {
  private final CarService carService;
  private final ControllerUtils controllerUtils;

  @Autowired
  public CarController(CarService carService, ControllerUtils controllerUtils) {
    this.carService = carService;
    this.controllerUtils = controllerUtils;
  }

  @Operation(
      summary = "Create car",
      description = "This endpoint allows to create car",
      responses = {
        @ApiResponse(
                     responseCode = "200",
                     description = "Car created",
                     content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = MessageDto.class)
                          )
                  ),
        @ApiResponse(
                     responseCode = "409",
                     description = "Already exists",
                     content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = MessageDto.class)
                          )
                  )
          }
  )
  @PostMapping
  public ResponseEntity<MessageDto> createCar(@Valid @RequestBody CarCreateRequestDto carRequestDto) {
    carService.registerCar(carRequestDto);
    return controllerUtils.createResponseEntityOk("create.car.message");
  }

  @Operation(
          summary = "Get cars",
          description = "This endpoint allows to get all cars",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "status OK",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = CarGetResponseDto.class)
                          )
              )
          }
  )
  @GetMapping
  public ResponseEntity<Page<CarGetResponseDto>> getAllCars(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "vin") String sortBy
  ) {
    Page<CarGetResponseDto> cars = carService.getAllCars(page, size, sortBy);
    return ResponseEntity.ok(cars);
  }

  @Operation(
          summary = "Get car by manufacturer, model or year, with page, filter and sorting",
          description = "This endpoint allows to get car by parameter with page, filter "
                  + "and sorting",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Car successfully found",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = CarGetResponseDto.class)
                          )
                  ),
              @ApiResponse(
                          responseCode = "404",
                          description = "Not found",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = MessageDto.class)
                          )
                  )
          }
  )
  @GetMapping("/search")
  public ResponseEntity<Page<CarGetResponseDto>> searchCars(
          @RequestParam(required = false) String manufacturer,
          @RequestParam(required = false) String model,
          @RequestParam(required = false) Integer year,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "vin") String sortBy
  ) {
    Page<CarGetResponseDto> cars;
    if (manufacturer != null) {
      cars = carService.searchCarsByManufacturer(manufacturer, page, size, sortBy);
    } else if (model != null) {
      cars = carService.searchCarsByModel(model, page, size, sortBy);
    } else if (year != null) {
      cars = carService.searchCarsByYear(year, page, size, sortBy);
    } else {
      cars = carService.getAllCars(page, size, sortBy);
    }
    return ResponseEntity.ok(cars);
  }

  @Operation(
          summary = "Get car by vin",
          description = "This endpoint allows to get car by vin",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Car successfully found",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = CarGetResponseDto.class)
                          )
                  ),
              @ApiResponse(
                          responseCode = "401",
                          description = "Not found",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = MessageDto.class)
                          )
                  )
          }
  )
  @GetMapping("/{vin}")
  public ResponseEntity<CarGetResponseDto> getCarByVin(@PathVariable String vin) {
    return ResponseEntity.status(HttpStatus.OK)
           .body(carService.findCarByVin(vin).get());
  }

  @Operation(
          summary = "Delete detail by car by vin",
          description = "This endpoint allows to delete car by vin",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Car successfully deleted",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = MessageDto.class)
                          )
                  ),
              @ApiResponse(
                          responseCode = "404",
                          description = "Not found",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = MessageDto.class)
                          )
                  )
          }
  )
  @DeleteMapping("/{vin}")
  public ResponseEntity<MessageDto> deleteCar(@PathVariable String vin) {
    carService.deleteCar(vin);
    return controllerUtils.createResponseEntityOk("delete.car.message");
  }

  @Operation(
          summary = "Update car by vin",
          description = "This endpoint allows to update car by vin",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Car successfully update",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = MessageDto.class)
                          )
                  ),
              @ApiResponse(
                          responseCode = "404",
                          description = "Not found",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = MessageDto.class)
                          )
                  )
          }
  )
  @PatchMapping("/{vin}")
  public ResponseEntity<MessageDto> updateCar(@PathVariable String vin,
                                              @Valid @RequestBody CarUpdateRequestDto updatedCar) {
    carService.updateCar(vin, updatedCar);
    return controllerUtils.createResponseEntityOk("update.car.message");
  }

  @Operation(
          summary = "Add detail to car",
          description = "This endpoint allows to add detail to car",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Car successfully update",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = MessageDto.class)
                          )
                  ),
              @ApiResponse(
                          responseCode = "404",
                          description = "Not found",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = MessageDto.class)
                          )
                  )
          }
  )
  @PostMapping("/add-detail")
  public String addDetailToCar(@RequestBody DetailAddEvent detailAddEvent)
          throws JsonProcessingException {
    log.info("Received request to add detail to car");
    return carService.addDetail(detailAddEvent);
  }
}