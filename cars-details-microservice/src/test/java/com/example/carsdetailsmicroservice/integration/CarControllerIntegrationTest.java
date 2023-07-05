package com.example.carsdetailsmicroservice.integration;

import com.example.carsdetailsmicroservice.controller.CarController;
import com.example.carsdetailsmicroservice.controller.utils.ControllerUtils;
import com.example.carsdetailsmicroservice.dto.car.create.CarCreateRequestDto;
import com.example.carsdetailsmicroservice.dto.car.get.CarGetResponseDto;
import com.example.carsdetailsmicroservice.dto.car.update.CarUpdateRequestDto;
import com.example.carsdetailsmicroservice.dto.message.MessageDto;
import com.example.carsdetailsmicroservice.entity.Detail;
import com.example.carsdetailsmicroservice.repository.CarRepository;
import com.example.carsdetailsmicroservice.service.CarService;
import com.example.carsdetailsmicroservice.utils.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
public class CarControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ControllerUtils controllerUtils;
  @MockBean
  private CarService carService;
  @MockBean
  private CarRepository carRepository;

  @Test
  public void testCreateCar() throws Exception {
    CarCreateRequestDto carRequestDto = new CarCreateRequestDto();
    Detail detail = new Detail(TestConstants.ID, TestConstants.SERIAL_NUMBER, new BigDecimal(10));
    Set<Detail> details = new HashSet<>();
    details.add(detail);
    carRequestDto.setModel(TestConstants.MODEL);
    carRequestDto.setVin(TestConstants.VIN_1);
    carRequestDto.setLicensePlate(TestConstants.LICENSE_PLATE_1);
    carRequestDto.setDetails(details);
    given(controllerUtils.createResponseEntityOk(anyString())).willReturn(ResponseEntity.ok(new MessageDto(TestConstants.CAR_CREATED_MESSAGE)));
    mockMvc.perform(post("/cars")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(carRequestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(TestConstants.CAR_CREATED_MESSAGE));
  }

  @Test
  public void testDeleteCar() throws Exception {
    given(controllerUtils.createResponseEntityOk(anyString())).willReturn(ResponseEntity.ok(new MessageDto(TestConstants.CAR_DELETED_MESSAGE)));
    mockMvc.perform(delete("/cars/{vin}", TestConstants.VIN_1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(TestConstants.CAR_DELETED_MESSAGE));
  }

  @Test
  public void testUpdateCar() throws Exception {
    CarUpdateRequestDto updatedCar = new CarUpdateRequestDto();
    updatedCar.setModel(TestConstants.MODEL);
    updatedCar.setVin(TestConstants.VIN_1);
    given(controllerUtils.createResponseEntityOk(anyString())).willReturn(ResponseEntity.ok(new MessageDto(TestConstants.CAR_UPDATED_MESSAGE)));
    mockMvc.perform(MockMvcRequestBuilders.patch("/cars/{vin}", TestConstants.VIN_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedCar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(TestConstants.CAR_UPDATED_MESSAGE));
  }

  @Test
  public void testSearchCarsByManufacturer() throws Exception {
    List<CarGetResponseDto> cars = new ArrayList<>();
    cars.add(new CarGetResponseDto(TestConstants.VIN_1, TestConstants.LICENSE_PLATE_1, TestConstants.MANUFACTURER, TestConstants.MODEL, TestConstants.YEAR, TestConstants.ID));
    Page<CarGetResponseDto> carPage = new PageImpl<>(cars);
    given(carService.searchCarsByManufacturer(TestConstants.MANUFACTURER, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_VIN)).willReturn(carPage);
    mockMvc.perform(get("/cars/search")
                    .param("manufacturer", TestConstants.MANUFACTURER)
                    .param("page", String.valueOf(TestConstants.PAGE))
                    .param("size", String.valueOf(TestConstants.SIZE))
                    .param("sortBy", TestConstants.SORT_BY_VIN))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(cars.size())))
            .andExpect(jsonPath("$.content[0].vin").value(cars.get(0).getVin()))
            .andExpect(jsonPath("$.content[0].manufacturer").value(cars.get(0).getManufacturer()))
            .andExpect(jsonPath("$.content[0].model").value(cars.get(0).getModel()))
            .andExpect(jsonPath("$.content[0].year").value(cars.get(0).getYear()));
  }

  @Test
  public void testSearchCarsByModel() throws Exception {
    List<CarGetResponseDto> cars = new ArrayList<>();
    cars.add(new CarGetResponseDto(TestConstants.VIN_1, TestConstants.LICENSE_PLATE_1, TestConstants.MANUFACTURER, TestConstants.MODEL, TestConstants.YEAR, TestConstants.ID));
    Page<CarGetResponseDto> carPage = new PageImpl<>(cars);
    given(carService.searchCarsByModel(TestConstants.MODEL, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_VIN)).willReturn(carPage);
    mockMvc.perform(get("/cars/search")
                    .param("model", TestConstants.MODEL)
                    .param("page", String.valueOf(TestConstants.PAGE))
                    .param("size", String.valueOf(TestConstants.SIZE))
                    .param("sortBy", TestConstants.SORT_BY_VIN))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(cars.size())))
            .andExpect(jsonPath("$.content[0].vin").value(cars.get(0).getVin()))
            .andExpect(jsonPath("$.content[0].manufacturer").value(cars.get(0).getManufacturer()))
            .andExpect(jsonPath("$.content[0].model").value(cars.get(0).getModel()))
            .andExpect(jsonPath("$.content[0].year").value(cars.get(0).getYear()));
  }

  @Test
  public void testSearchCarsByYear() throws Exception {
    List<CarGetResponseDto> cars = new ArrayList<>();
    cars.add(new CarGetResponseDto(TestConstants.VIN_1, TestConstants.LICENSE_PLATE_1, TestConstants.MANUFACTURER, TestConstants.MODEL, TestConstants.YEAR, TestConstants.ID));
    Page<CarGetResponseDto> carPage = new PageImpl<>(cars);
    given(carService.searchCarsByYear(TestConstants.YEAR, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_VIN)).willReturn(carPage);
    mockMvc.perform(get("/cars/search")
                    .param("year", String.valueOf(TestConstants.YEAR))
                    .param("page", String.valueOf(TestConstants.PAGE))
                    .param("size", String.valueOf(TestConstants.SIZE))
                    .param("sortBy", TestConstants.SORT_BY_VIN))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(cars.size())))
            .andExpect(jsonPath("$.content[0].vin").value(cars.get(0).getVin()))
            .andExpect(jsonPath("$.content[0].manufacturer").value(cars.get(0).getManufacturer()))
            .andExpect(jsonPath("$.content[0].model").value(cars.get(0).getModel()))
            .andExpect(jsonPath("$.content[0].year").value(cars.get(0).getYear()));
  }

  @Test
  public void testGetCarByVin() throws Exception {
    CarGetResponseDto car = new CarGetResponseDto(TestConstants.VIN_1, TestConstants.LICENSE_PLATE_1, TestConstants.MANUFACTURER, TestConstants.MODEL, TestConstants.YEAR, TestConstants.ID);
    given(carService.findCarByVin(TestConstants.VIN_1)).willReturn(Optional.of(car));
    mockMvc.perform(get("/cars/{vin}", TestConstants.VIN_1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.vin").value(car.getVin()))
            .andExpect(jsonPath("$.manufacturer").value(car.getManufacturer()))
            .andExpect(jsonPath("$.model").value(car.getModel()))
            .andExpect(jsonPath("$.year").value(car.getYear()));
  }

  @Test
  public void testGetAllCars() throws Exception {
    List<CarGetResponseDto> cars = new ArrayList<>();
    cars.add(new CarGetResponseDto(TestConstants.VIN_1, TestConstants.LICENSE_PLATE_1, TestConstants.MANUFACTURER, TestConstants.MODEL, TestConstants.YEAR, TestConstants.ID));
    Page<CarGetResponseDto> carPage = new PageImpl<>(cars);
    given(carService.getAllCars(TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_VIN)).willReturn(carPage);
    mockMvc.perform(get("/cars")
                    .param("page", String.valueOf(TestConstants.PAGE))
                    .param("size", String.valueOf(TestConstants.SIZE))
                    .param("sortBy", TestConstants.SORT_BY_VIN))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(cars.size())))
            .andExpect(jsonPath("$.content[0].vin").value(cars.get(0).getVin()))
            .andExpect(jsonPath("$.content[0].manufacturer").value(cars.get(0).getManufacturer()))
            .andExpect(jsonPath("$.content[0].model").value(cars.get(0).getModel()))
            .andExpect(jsonPath("$.content[0].year").value(cars.get(0).getYear()));
  }
}