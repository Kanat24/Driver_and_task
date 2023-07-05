package com.example.drivesbillsmicroservice.controller;

import com.example.drivesbillsmicroservice.controller.utils.ControllerUtils;
import com.example.drivesbillsmicroservice.dto.driver.create.DriverCreateRequestDto;
import com.example.drivesbillsmicroservice.dto.driver.get.DriverGetResponseDto;
import com.example.drivesbillsmicroservice.dto.driver.update.DriverUpdateRequestDto;
import com.example.drivesbillsmicroservice.dto.message.MessageDto;
import com.example.drivesbillsmicroservice.events.CarPurchaseEvent;
import com.example.drivesbillsmicroservice.service.DriverService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RestController
@RequestMapping("/drivers")
public class DriverController {
  private final DriverService driverService;
  private final ControllerUtils controllerUtils;

  @Autowired
  public DriverController(DriverService driverService, ControllerUtils controllerUtils) {
    this.driverService = driverService;
    this.controllerUtils = controllerUtils;
  }

  @Operation(
      summary = "Endpoint for create driver",
      description = "This endpoint allows to create driver",
      responses = {
        @ApiResponse(
                     responseCode = "200",
                     description = "Created",
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
  @PostMapping("/")
  public ResponseEntity<MessageDto> registerDriver(
          @Valid @RequestBody DriverCreateRequestDto driverCreateRequestDto) {
    driverService.registerDriver(driverCreateRequestDto);
    return controllerUtils.createResponseEntityOk("create.driver.message");
  }


  @Operation(
          summary = "Endpoint for get driver by passport",
          description = "This endpoint allows to get method for driver",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Get driver",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = DriverGetResponseDto.class)
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
  @GetMapping("/{passport}")
  public ResponseEntity<DriverGetResponseDto> findDriverByPassport(@PathVariable String passport) {
    Optional<DriverGetResponseDto> driver = driverService.findDriverByPassport(passport);
    return ResponseEntity.ok(driver.get());
  }

  @Operation(
          summary = "Endpoint for delete driver by passport",
          description = "This endpoint allows to delete method for driver",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Deleted",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = MessageDto.class)
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
  @DeleteMapping("/{passport}")
  public ResponseEntity<MessageDto> deleteDriverByPassport(@PathVariable String passport) {
    driverService.deleteDriver(passport);
    return controllerUtils.createResponseEntityOk("delete.driver.message");
  }

  @Operation(
          summary = "Endpoint for update driver by passport",
          description = "This endpoint allows to update method for driver",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Deleted",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = MessageDto.class)
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
  @PatchMapping("/{passport}")
  public ResponseEntity<MessageDto> updateDriver(@PathVariable String passport,
                                                 @Valid @RequestBody DriverUpdateRequestDto updatedDriver) {
    driverService.updateDriver(passport, updatedDriver);
    return controllerUtils.createResponseEntityOk("update.driver.message");
  }

  @GetMapping
  public ResponseEntity<Page<DriverGetResponseDto>> getAllDrivers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "firstName") String sortBy) {
    Page<DriverGetResponseDto> drivers = driverService.getAllDrivers(page, size, sortBy);
    return ResponseEntity.ok(drivers);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<DriverGetResponseDto>> searchDrivers(
          @RequestParam(required = false) String firstName,
          @RequestParam(required = false) String lastName,
          @RequestParam(required = false) String passport,
          @RequestParam(required = false) Integer experience,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "firstName") String sortBy) {
    Page<DriverGetResponseDto> drivers;
    if (firstName != null) {
      drivers = driverService.searchDriversByFirstName(firstName, page, size, sortBy);
    } else if (lastName != null) {
      drivers = driverService.searchDriversByLastName(lastName, page, size, sortBy);
    } else if (passport != null) {
      drivers = driverService.searchDriversByPassport(passport, page, size, sortBy);
    } else if (experience != null) {
      drivers = driverService.searchDriversByExperience(experience, page, size, sortBy);
    } else {
      drivers = driverService.getAllDrivers(page, size, sortBy);
    }
    return ResponseEntity.ok(drivers);
  }

  @PostMapping("/buy-car")
  public String processCarPurchaseRequest(@RequestBody CarPurchaseEvent carPurchaseEvent)
          throws JsonProcessingException {
    return driverService.sendCarPurchaseEvent(carPurchaseEvent);
  }
}