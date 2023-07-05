package com.example.carsdetailsmicroservice.service.implementation;

import com.example.carsdetailsmicroservice.dto.detail.get.DetailGetResponseDto;
import com.example.carsdetailsmicroservice.dto.detail.update.DetailUpdateRequestDto;
import com.example.carsdetailsmicroservice.entity.Detail;
import com.example.carsdetailsmicroservice.exceptions.detail.DetailAlreadyExistException;
import com.example.carsdetailsmicroservice.exceptions.detail.DetailNotFoundException;
import com.example.carsdetailsmicroservice.repository.DetailRepository;
import com.example.carsdetailsmicroservice.service.DetailService;
import com.example.carsdetailsmicroservice.utils.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.eq;

class DetailServiceImplementationTest {
  @Mock
  private DetailRepository detailRepository;
  @Mock
  private DetailService detailService;
  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    detailService = new DetailServiceImplementation(detailRepository);
  }

  @Test
  void createDetail_CreatesDetail_WhenDetailDoesNotExist() {
    Detail detail = createDetail(TestConstants.SERIAL_NUMBER, BigDecimal.valueOf(100.00));
    when(detailRepository.existsBySerialNumber(detail.getSerialNumber())).thenReturn(false);
    when(detailRepository.save(any(Detail.class))).thenAnswer(invocation -> invocation.getArgument(0));
    detailService.createDetail(detail);
    verify(detailRepository, times(1)).save(detail);
  }

  @Test
  void createDetail_ThrowsDetailAlreadyExistException_WhenDetailExists() {
    Detail detail = createDetail(TestConstants.SERIAL_NUMBER, BigDecimal.valueOf(100.00));
    when(detailRepository.existsBySerialNumber(detail.getSerialNumber())).thenReturn(true);
    assertThrows(DetailAlreadyExistException.class, () -> detailService.createDetail(detail));
  }

  @Test
  void getAllDetails_ReturnsAllDetails() {
    List<Detail> detailList = createDetailList();
    when(detailRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(detailList));
    Page<DetailGetResponseDto> result = detailService.getAllDetails(TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_SERIAL_NUMBER);
    assertEquals(detailList.size(), result.getContent().size());
  }

  @Test
  void findDetailBySerialNumber_ReturnsDetailDto_WhenDetailExists() {
    Detail detail = createDetail(TestConstants.SERIAL_NUMBER, BigDecimal.valueOf(100.00));
    Optional<Detail> optionalDetail = Optional.of(detail);
    when(detailRepository.findBySerialNumber(TestConstants.SERIAL_NUMBER)).thenReturn(optionalDetail);
    Optional<DetailGetResponseDto> result = detailService.findDetailBySerialNumber(TestConstants.SERIAL_NUMBER);
    assertTrue(result.isPresent());
    assertEquals(detail.getSerialNumber(), result.get().getSerialNumber());
    assertEquals(detail.getPrice(), result.get().getPrice());
  }

  @Test
  void findDetailBySerialNumber_ThrowsDetailNotFoundException_WhenDetailDoesNotExist() {
    when(detailRepository.findBySerialNumber(TestConstants.SERIAL_NUMBER)).thenReturn(Optional.empty());
    assertThrows(DetailNotFoundException.class, () -> detailService.findDetailBySerialNumber(TestConstants.SERIAL_NUMBER));
  }

  @Test
  void searchDetailsBySerialNumber_ReturnsMatchingDetails() {
    List<Detail> detailList = createDetailList();
    Pageable pageable = PageRequest.of(TestConstants.PAGE, TestConstants.SIZE, Sort.by(TestConstants.SORT_BY_SERIAL_NUMBER));
    Page<Detail> expectedPage = new PageImpl<>(detailList, pageable, detailList.size());
    when(detailRepository.findBySerialNumberContaining(eq(TestConstants.SERIAL_NUMBER), any(Pageable.class)))
                .thenReturn(expectedPage);
    Page<DetailGetResponseDto> result = detailService.searchDetailsBySerialNumber(TestConstants.SERIAL_NUMBER, 0, 10, "serialNumber");
      assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
      assertEquals(expectedPage.getNumber(), result.getNumber());
      assertEquals(detailList.size(), result.getContent().size());
  }

  @Test
  void searchDetailsByPrice_ReturnsMatchingDetails() {
    BigDecimal price = BigDecimal.valueOf(100.00);
    List<Detail> detailList = createDetailList();
    Pageable pageable = PageRequest.of(TestConstants.PAGE, TestConstants.SIZE, Sort.by(TestConstants.SORT_BY_SERIAL_NUMBER));
    Page<Detail> expectedPage = new PageImpl<>(detailList, pageable, detailList.size());
    when(detailRepository.findByPrice(eq(price), any(Pageable.class)))
                .thenReturn(expectedPage);
    Page<DetailGetResponseDto> result = detailService.searchDetailsByPrice(price, TestConstants.PAGE, TestConstants.SIZE, TestConstants.SORT_BY_SERIAL_NUMBER);
    assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
    assertEquals(expectedPage.getNumber(), result.getNumber());
    assertEquals(detailList.size(), result.getContent().size());
  }

  @Test
  void deleteDetail_DeletesDetail_WhenDetailExists() {
    when(detailRepository.existsBySerialNumber(TestConstants.SERIAL_NUMBER)).thenReturn(true);
    detailService.deleteDetail(TestConstants.SERIAL_NUMBER);
    verify(detailRepository, times(1)).deleteBySerialNumber(TestConstants.SERIAL_NUMBER);
  }

  @Test
  void deleteDetail_ThrowsDetailNotFoundException_WhenDetailDoesNotExist() {
    when(detailRepository.existsBySerialNumber(TestConstants.SERIAL_NUMBER)).thenReturn(false);
    assertThrows(DetailNotFoundException.class, () -> detailService.deleteDetail(TestConstants.SERIAL_NUMBER));
  }

  @Test
  void changeDetail_UpdatesDetail_WhenDetailExists() {
    Detail existingDetail = createDetail(TestConstants.SERIAL_NUMBER, BigDecimal.valueOf(100.00));
    DetailUpdateRequestDto updatedDetail = createUpdatedDetail(TestConstants.SERIAL_NUMBER_2, BigDecimal.valueOf(200.00));
    when(detailRepository.existsBySerialNumber(TestConstants.SERIAL_NUMBER)).thenReturn(true);
    when(detailRepository.findBySerialNumber(TestConstants.SERIAL_NUMBER)).thenReturn(Optional.of(existingDetail));
    when(detailRepository.save(any(Detail.class))).thenAnswer(invocation -> invocation.getArgument(0));
    Detail result = detailService.changeDetail(TestConstants.SERIAL_NUMBER, updatedDetail);
    assertEquals(updatedDetail.getSerialNumber(), result.getSerialNumber());
    assertEquals(updatedDetail.getPrice(), result.getPrice());
  }

  @Test
  void changeDetail_ThrowsDetailNotFoundException_WhenDetailDoesNotExist() {
    DetailUpdateRequestDto updatedDetail = createUpdatedDetail(TestConstants.SERIAL_NUMBER_2, BigDecimal.valueOf(200.00));
    when(detailRepository.existsBySerialNumber(TestConstants.SERIAL_NUMBER)).thenReturn(false);
    assertThrows(DetailNotFoundException.class, () -> detailService.changeDetail(TestConstants.SERIAL_NUMBER, updatedDetail));
  }

  private Detail createDetail(String serialNumber, BigDecimal price) {
    Detail detail = new Detail();
    detail.setSerialNumber(serialNumber);
    detail.setPrice(price);
    return detail;
  }

  private List<Detail> createDetailList() {
    List<Detail> detailList = new ArrayList<>();
    detailList.add(createDetail(TestConstants.SERIAL_NUMBER, BigDecimal.valueOf(100.00)));
    detailList.add(createDetail(TestConstants.SERIAL_NUMBER, BigDecimal.valueOf(200.00)));
    return detailList;
  }

  private DetailUpdateRequestDto createUpdatedDetail(String serialNumber, BigDecimal price) {
    DetailUpdateRequestDto detailDto = new DetailUpdateRequestDto();
    detailDto.setSerialNumber(serialNumber);
    detailDto.setPrice(price);
    return detailDto;
  }
}