package com.example.carsdetailsmicroservice.controller;

import com.example.carsdetailsmicroservice.controller.utils.ControllerUtils;
import com.example.carsdetailsmicroservice.dto.detail.create.DetailCreateRequestDto;
import com.example.carsdetailsmicroservice.dto.detail.get.DetailGetResponseDto;
import com.example.carsdetailsmicroservice.dto.detail.update.DetailUpdateRequestDto;
import com.example.carsdetailsmicroservice.dto.message.MessageDto;
import com.example.carsdetailsmicroservice.entity.Detail;
import com.example.carsdetailsmicroservice.mapper.DetailMapper;
import com.example.carsdetailsmicroservice.service.DetailService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import java.math.BigDecimal;

import jakarta.validation.Valid;
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

@OpenAPIDefinition(
        info = @Info(
                title = "Rik Masters Test",
                version = "1.0.0",
                description = "Service for working with machine details"
        ),
        servers = @Server(url = "http://localhost:8081")
)
@RestController
@RequestMapping("/details")
public class DetailController {
  private final DetailService detailService;
  private final ControllerUtils controllerUtils;

  @Autowired
  public DetailController(DetailService detailService, ControllerUtils controllerUtils) {
    this.detailService = detailService;
    this.controllerUtils = controllerUtils;
  }

  @Operation(
          summary = "Create detail",
          description = "This endpoint allows to create detail",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Detail created",
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
  public ResponseEntity<MessageDto> createDetail(@Valid @RequestBody DetailCreateRequestDto detailRequestDto) {
    Detail detail = DetailMapper.INSTANCE.toDetail(detailRequestDto);
    detailService.createDetail(detail);
    return controllerUtils.createResponseEntityOk("create.detail.message");
  }

  @Operation(
          summary = "Get detail by serial number or price, with page, filter and sorting",
          description = "This endpoint allows to get detail by parameter with page, filter and sorting",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Details successfully found",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = DetailGetResponseDto.class)
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
  @GetMapping("/search")
  public ResponseEntity<Page<DetailGetResponseDto>> searchDetails(
          @RequestParam(required = false) String serialNumber,
          @RequestParam(required = false) BigDecimal price,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "id") String sortBy
  ) {
    if (serialNumber != null) {
      Page<DetailGetResponseDto> details = detailService
              .searchDetailsBySerialNumber(serialNumber, page, size, sortBy);
      return ResponseEntity.ok(details);
    } else if (price != null) {
      Page<DetailGetResponseDto> details = detailService
              .searchDetailsByPrice(price, page, size, sortBy);
      return ResponseEntity.ok(details);
    } else {
      Page<DetailGetResponseDto> details = detailService.getAllDetails(page, size, sortBy);
      return ResponseEntity.ok(details);
    }
  }

  @Operation(
          summary = "Get detail by serial number",
          description = "This endpoint allows to get detail by serial number",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Detail successfully found",
                          content = @Content(
                                  mediaType = MediaType.APPLICATION_JSON_VALUE,
                                  schema = @Schema(implementation = DetailGetResponseDto.class)
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
  @GetMapping("/{serialNumber}")
  public ResponseEntity<DetailGetResponseDto> getDetailBySerialNumber(@PathVariable String serialNumber) {
    return ResponseEntity.status(HttpStatus.OK)
           .body(detailService.findDetailBySerialNumber(serialNumber).get());
  }

  @Operation(
          summary = "Delete detail by serial number",
          description = "This endpoint allows to delete detail by serial number",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Detail successfully deleted",
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
  @DeleteMapping("/{serialNumber}")
  public ResponseEntity<MessageDto> deleteDetail(@PathVariable String serialNumber) {
    detailService.deleteDetail(serialNumber);
    return controllerUtils.createResponseEntityOk("delete.detail.message");
  }

  @Operation(
          summary = "Update detail by serial number",
          description = "This endpoint allows to update detail by serial number",
          responses = {
              @ApiResponse(
                          responseCode = "200",
                          description = "Detail successfully update",
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
  @PatchMapping("/{serialNumber}")
  public ResponseEntity<MessageDto> updateDetail(@PathVariable String serialNumber,
                                                 @Valid @RequestBody DetailUpdateRequestDto updatedDetail) {
    detailService.changeDetail(serialNumber, updatedDetail);
    return controllerUtils.createResponseEntityOk("update.detail.message");
  }
}