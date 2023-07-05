package com.example.drivesbillsmicroservice.service.implementation;

import com.example.drivesbillsmicroservice.enums.Currency;
import com.example.drivesbillsmicroservice.service.CurrencyConversionService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ConversionServiceTest {

  @Autowired
  private CurrencyConversionService currencyConversionService;

  @ParameterizedTest
  @CsvSource({
            "RED, GREEN, 100.0, 250.0",
            "RED, GREEN, 200.0, 500.0",
            "GREEN, RED, 100.0, 40.0",
            "GREEN, RED, 200.0, 80.0",
            "GREEN, BLUE, 100.0, 166.6667",
            "GREEN, BLUE, 200.0, 333.3333",
            "BLUE, GREEN, 100.0, 60.0",
            "BLUE, GREEN, 200.0, 120.0",
            "RED, BLUE, 100.0, 416.6667",
            "RED, BLUE, 200.0, 833.3333",
            "BLUE, RED, 100.0, 24.0",
            "BLUE, RED, 200.0, 48.0"
  })
  public void testCurrencyConversion(Currency sourceCurrency,
                                       Currency targetCurrency,
                                       double amount,
                                       double expectedAmount) {
    double delta = 0.001;
    double actualAmount = currencyConversionService.convert(amount, sourceCurrency, targetCurrency);
    assertEquals(expectedAmount, actualAmount, delta);
  }
}