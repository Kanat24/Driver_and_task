package com.example.carsdetailsmicroservice.integration;

import com.example.carsdetailsmicroservice.controller.DetailController;
import com.example.carsdetailsmicroservice.controller.utils.ControllerUtils;
import com.example.carsdetailsmicroservice.dto.detail.create.DetailCreateRequestDto;
import com.example.carsdetailsmicroservice.dto.detail.get.DetailGetResponseDto;
import com.example.carsdetailsmicroservice.dto.detail.update.DetailUpdateRequestDto;
import com.example.carsdetailsmicroservice.dto.message.MessageDto;
import com.example.carsdetailsmicroservice.repository.DetailRepository;
import com.example.carsdetailsmicroservice.service.DetailService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DetailController.class)
public class DetailControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ControllerUtils controllerUtils;
  @MockBean
  private DetailService detailService;
  @MockBean
  private DetailRepository detailRepository;

  @Test
  public void testCreateDetail() throws Exception {
    DetailCreateRequestDto detailRequestDto = new DetailCreateRequestDto();
    detailRequestDto.setSerialNumber(TestConstants.SERIAL_NUMBER);
    detailRequestDto.setPrice(BigDecimal.valueOf(20));
    given(controllerUtils.createResponseEntityOk(anyString())).willReturn(ResponseEntity.ok(new MessageDto(TestConstants.DETAIL_CREATED_MESSAGE)));
    mockMvc.perform(post("/details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(detailRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(TestConstants.DETAIL_CREATED_MESSAGE));
  }

  @Test
  public void testDeleteDetail() throws Exception {
    given(controllerUtils.createResponseEntityOk(anyString())).willReturn(ResponseEntity.ok(new MessageDto(TestConstants.DETAIL_DELETED_MESSAGE)));
    mockMvc.perform(MockMvcRequestBuilders.delete("/details/{serialNumber}", TestConstants.SERIAL_NUMBER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(TestConstants.DETAIL_DELETED_MESSAGE));
  }

  @Test
  public void testUpdateDetail() throws Exception {
    DetailUpdateRequestDto updatedDetail = new DetailUpdateRequestDto();
    updatedDetail.setSerialNumber(TestConstants.SERIAL_NUMBER);
    updatedDetail.setPrice(BigDecimal.valueOf(20));
    given(controllerUtils.createResponseEntityOk(anyString())).willReturn(ResponseEntity.ok(new MessageDto(TestConstants.DETAIL_UPDATED_MESSAGE)));
    mockMvc.perform(patch("/details/{serialNumber}", TestConstants.SERIAL_NUMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedDetail)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(TestConstants.DETAIL_UPDATED_MESSAGE));
    }


  @Test
  public void testSearchDetailsBySerialNumber() throws Exception {
    List<DetailGetResponseDto> details = new ArrayList<>();
    details.add(new DetailGetResponseDto(TestConstants.SERIAL_NUMBER, new BigDecimal("10.0")));
    Page<DetailGetResponseDto> detailPage = new PageImpl<>(details);
    given(detailService.searchDetailsBySerialNumber(TestConstants.SERIAL_NUMBER, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_ID)).willReturn(detailPage);
    mockMvc.perform(get("/details/search")
                    .param("serialNumber", TestConstants.SERIAL_NUMBER)
                    .param("page", String.valueOf(TestConstants.PAGE))
                    .param("size", String.valueOf(TestConstants.SIZE))
                    .param("sortBy", TestConstants.SORT_BY_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(details.size())))
            .andExpect(jsonPath("$.content[0].serialNumber").value(details.get(0).getSerialNumber()))
            .andExpect(jsonPath("$.content[0].price").value(details.get(0).getPrice()));
  }

  @Test
  public void testSearchDetailsByPrice() throws Exception {
    BigDecimal price = new BigDecimal("10.0");
    List<DetailGetResponseDto> details = new ArrayList<>();
    details.add(new DetailGetResponseDto(TestConstants.SERIAL_NUMBER, new BigDecimal("10.0")));
    Page<DetailGetResponseDto> detailPage = new PageImpl<>(details);

    given(detailService.searchDetailsByPrice(price, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_ID)).willReturn(detailPage);

    mockMvc.perform(get("/details/search")
                    .param("price", price.toString())
                    .param("page", String.valueOf(TestConstants.PAGE))
                    .param("size", String.valueOf(TestConstants.SIZE))
                    .param("sortBy", TestConstants.SORT_BY_ID))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(details.size())))
            .andExpect(jsonPath("$.content[0].serialNumber").value(details.get(0).getSerialNumber()))
            .andExpect(jsonPath("$.content[0].price").value(details.get(0).getPrice().toString()));
  }

  @Test
  public void testGetDetailBySerialNumber() throws Exception {
    DetailGetResponseDto detail = new DetailGetResponseDto(TestConstants.SERIAL_NUMBER, new BigDecimal("10.0"));
    given(detailService.findDetailBySerialNumber(TestConstants.SERIAL_NUMBER)).willReturn(Optional.of(detail));
    mockMvc.perform(get("/details/{serialNumber}", TestConstants.SERIAL_NUMBER))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.serialNumber").value(detail.getSerialNumber()))
            .andExpect(jsonPath("$.price").value(detail.getPrice().toString()));
  }
}