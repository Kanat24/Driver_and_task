package com.example.drivesbillsmicroservice.integration.mvc;

import com.example.drivesbillsmicroservice.controller.DriverController;
import com.example.drivesbillsmicroservice.controller.utils.ControllerUtils;
import com.example.drivesbillsmicroservice.dto.driver.create.DriverCreateRequestDto;
import com.example.drivesbillsmicroservice.dto.driver.get.DriverGetResponseDto;
import com.example.drivesbillsmicroservice.dto.driver.update.DriverUpdateRequestDto;
import com.example.drivesbillsmicroservice.dto.message.MessageDto;
import com.example.drivesbillsmicroservice.enums.LicenseCategory;
import com.example.drivesbillsmicroservice.repository.DriverRepository;
import com.example.drivesbillsmicroservice.service.DriverService;
import com.example.drivesbillsmicroservice.utils.TestConstants;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DriverController.class)
public class DriverControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ControllerUtils controllerUtils;
  @MockBean
  private DriverService driverService;
  @MockBean
  DriverRepository driverRepository;

  @Test
  public void testRegisterDriver() throws Exception {
    DriverCreateRequestDto driverCreateRequestDto = new DriverCreateRequestDto();
    driverCreateRequestDto.setPassport(TestConstants.PASSPORT);
    driverCreateRequestDto.setLastName(TestConstants.LAST_NAME);
    driverCreateRequestDto.setFirstName(TestConstants.FIRST_NAME);
    given(controllerUtils.createResponseEntityOk(anyString())).willReturn(ResponseEntity.ok(new MessageDto(TestConstants.DRIVER_CREATED_MESSAGE)));
    mockMvc.perform(post("/drivers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(driverCreateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(TestConstants.DRIVER_CREATED_MESSAGE));
  }

  @Test
  public void testDeleteDriverByPassport() throws Exception {
    given(controllerUtils.createResponseEntityOk(anyString())).willReturn(ResponseEntity.ok(new MessageDto(TestConstants.DRIVER_DELETED_MESSAGE)));
    mockMvc.perform(delete("/drivers/{passport}", TestConstants.PASSPORT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(TestConstants.DRIVER_DELETED_MESSAGE));
  }

  @Test
  public void testUpdateDriver() throws Exception {
    DriverUpdateRequestDto updatedDriver = new DriverUpdateRequestDto();
    updatedDriver.setFirstName(TestConstants.FIRST_NAME);
    updatedDriver.setLastName(TestConstants.LAST_NAME);
    given(controllerUtils.createResponseEntityOk(anyString())).willReturn(ResponseEntity.ok(new MessageDto(TestConstants.DRIVER_UPDATED_MESSAGE)));
    mockMvc.perform(patch("/drivers/{passport}", TestConstants.PASSPORT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedDriver)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(TestConstants.DRIVER_UPDATED_MESSAGE));
  }

  @Test
  public void testGetAllDrivers() throws Exception {
    List<DriverGetResponseDto> drivers = new ArrayList<>();
    drivers.add(new DriverGetResponseDto(TestConstants.FIRST_NAME, TestConstants.LAST_NAME, TestConstants.PASSPORT, LicenseCategory.B, LocalDate.now(), 2));
    drivers.add(new DriverGetResponseDto(TestConstants.FIRST_NAME, TestConstants.LAST_NAME,TestConstants.PASSPORT, LicenseCategory.B, LocalDate.now(), 2));
    Page<DriverGetResponseDto> driverPage = new PageImpl<>(drivers);
    given(driverService.getAllDrivers(TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_FIRST_NAME)).willReturn(driverPage);
    mockMvc.perform(get("/drivers")
                    .param("page", String.valueOf(TestConstants.PAGE))
                    .param("size", String.valueOf(TestConstants.SIZE))
                    .param("sortBy", TestConstants.SORT_BY_FIRST_NAME))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(drivers.size())))
            .andExpect(jsonPath("$.content[0].firstName").value(drivers.get(0).getFirstName()))
            .andExpect(jsonPath("$.content[0].lastName").value(drivers.get(0).getLastName()))
            .andExpect(jsonPath("$.content[1].firstName").value(drivers.get(1).getFirstName()))
            .andExpect(jsonPath("$.content[1].lastName").value(drivers.get(1).getLastName()));
  }

  @Test
  public void testFindDriverByPassport() throws Exception {
    DriverGetResponseDto driver = new DriverGetResponseDto(TestConstants.FIRST_NAME, TestConstants.LAST_NAME,TestConstants.PASSPORT, LicenseCategory.B, LocalDate.now(), 2);
    given(driverService.findDriverByPassport(TestConstants.PASSPORT)).willReturn(Optional.of(driver));
    mockMvc.perform(get("/drivers/{passport}", TestConstants.PASSPORT))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(driver.getFirstName()))
            .andExpect(jsonPath("$.lastName").value(driver.getLastName()));
  }

  @Test
  public void testGetAllDrivers1() throws Exception {
    List<DriverGetResponseDto> drivers = new ArrayList<>();
    drivers.add(new DriverGetResponseDto(TestConstants.FIRST_NAME, TestConstants.LAST_NAME,TestConstants.PASSPORT, LicenseCategory.B, LocalDate.now(), 2));
    drivers.add(new DriverGetResponseDto(TestConstants.FIRST_NAME, TestConstants.LAST_NAME,TestConstants.PASSPORT, LicenseCategory.B, LocalDate.now(), 2));
    Page<DriverGetResponseDto> driverPage = new PageImpl<>(drivers);
    given(driverService.getAllDrivers(TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_FIRST_NAME)).willReturn(driverPage);
    mockMvc.perform(get("/drivers")
                    .param("page", String.valueOf(TestConstants.PAGE))
                    .param("size", String.valueOf(TestConstants.SIZE))
                    .param("sortBy", TestConstants.SORT_BY_FIRST_NAME))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(drivers.size())))
            .andExpect(jsonPath("$.content[0].firstName").value(drivers.get(0).getFirstName()))
            .andExpect(jsonPath("$.content[0].lastName").value(drivers.get(0).getLastName()))
            .andExpect(jsonPath("$.content[1].firstName").value(drivers.get(1).getFirstName()))
            .andExpect(jsonPath("$.content[1].lastName").value(drivers.get(1).getLastName()));
  }

  @Test
  public void testSearchDriversByFirstName() throws Exception {
    List<DriverGetResponseDto> drivers = new ArrayList<>();
    drivers.add(new DriverGetResponseDto(TestConstants.FIRST_NAME, TestConstants.LAST_NAME,TestConstants.PASSPORT, LicenseCategory.B, LocalDate.now(), 2));
    Page<DriverGetResponseDto> driverPage = new PageImpl<>(drivers);
    given(driverService.searchDriversByFirstName(TestConstants.FIRST_NAME, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_FIRST_NAME)).willReturn(driverPage);
    mockMvc.perform(get("/drivers/search")
                    .param("firstName", TestConstants.FIRST_NAME)
                    .param("page", String.valueOf(TestConstants.PAGE))
                    .param("size", String.valueOf(TestConstants.SIZE))
                    .param("sortBy", TestConstants.SORT_BY_FIRST_NAME))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(drivers.size())))
            .andExpect(jsonPath("$.content[0].firstName").value(drivers.get(0).getFirstName()))
            .andExpect(jsonPath("$.content[0].lastName").value(drivers.get(0).getLastName()));
  }

  @Test
  public void testSearchDriversByLastName() throws Exception {
    List<DriverGetResponseDto> drivers = new ArrayList<>();
    drivers.add(new DriverGetResponseDto(TestConstants.FIRST_NAME, TestConstants.LAST_NAME,TestConstants.PASSPORT, LicenseCategory.B, LocalDate.now(), 2));
    Page<DriverGetResponseDto> driverPage = new PageImpl<>(drivers);
    given(driverService.searchDriversByLastName(TestConstants.LAST_NAME, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_FIRST_NAME)).willReturn(driverPage);
    mockMvc.perform(get("/drivers/search")
                    .param("lastName", TestConstants.LAST_NAME)
                    .param("page", String.valueOf(TestConstants.PAGE))
                    .param("size", String.valueOf(TestConstants.SIZE))
                    .param("sortBy", TestConstants.SORT_BY_FIRST_NAME))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(drivers.size())))
            .andExpect(jsonPath("$.content[0].firstName").value(drivers.get(0).getFirstName()))
            .andExpect(jsonPath("$.content[0].lastName").value(drivers.get(0).getLastName()));
  }

  @Test
  public void testSearchDriversByPassport() throws Exception {
    List<DriverGetResponseDto> drivers = new ArrayList<>();
    drivers.add(new DriverGetResponseDto(TestConstants.FIRST_NAME, TestConstants.LAST_NAME,TestConstants.PASSPORT, LicenseCategory.B, LocalDate.now(), 2));
    Page<DriverGetResponseDto> driverPage = new PageImpl<>(drivers);
    given(driverService.searchDriversByPassport(TestConstants.PASSPORT, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_FIRST_NAME)).willReturn(driverPage);
    mockMvc.perform(get("/drivers/search")
                    .param("passport", TestConstants.PASSPORT)
                    .param("page", String.valueOf(TestConstants.PAGE))
                    .param("size", String.valueOf(TestConstants.SIZE))
                    .param("sortBy", TestConstants.SORT_BY_FIRST_NAME))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(drivers.size())))
            .andExpect(jsonPath("$.content[0].firstName").value(drivers.get(0).getFirstName()))
            .andExpect(jsonPath("$.content[0].lastName").value(drivers.get(0).getLastName()));
  }

  @Test
  public void testSearchDriversByExperience() throws Exception {
    List<DriverGetResponseDto> drivers = new ArrayList<>();
    drivers.add(new DriverGetResponseDto(TestConstants.FIRST_NAME, TestConstants.LAST_NAME,TestConstants.PASSPORT, LicenseCategory.B, LocalDate.now(), 2));
    Page<DriverGetResponseDto> driverPage = new PageImpl<>(drivers);
    given(driverService.searchDriversByExperience(TestConstants.EXPERIENCE, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_FIRST_NAME)).willReturn(driverPage);
    mockMvc.perform(get("/drivers/search")
                    .param("experience", String.valueOf(TestConstants.EXPERIENCE))
                    .param("page", String.valueOf(TestConstants.PAGE))
                    .param("size", String.valueOf(TestConstants.SIZE))
                    .param("sortBy", TestConstants.SORT_BY_FIRST_NAME))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(drivers.size())))
            .andExpect(jsonPath("$.content[0].firstName").value(drivers.get(0).getFirstName()))
            .andExpect(jsonPath("$.content[0].lastName").value(drivers.get(0).getLastName()));
  }
}