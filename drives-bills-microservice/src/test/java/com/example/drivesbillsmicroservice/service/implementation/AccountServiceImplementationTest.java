package com.example.drivesbillsmicroservice.service.implementation;

import com.example.drivesbillsmicroservice.entity.Account;
import com.example.drivesbillsmicroservice.enums.Currency;
import com.example.drivesbillsmicroservice.exceptions.account.InsufficientBalanceException;
import com.example.drivesbillsmicroservice.repository.AccountRepository;
import com.example.drivesbillsmicroservice.service.CurrencyConversionService;
import com.example.drivesbillsmicroservice.utils.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

class AccountServiceImplementationTest {
  private AccountServiceImplementation accountService;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private CurrencyConversionService currencyConversionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    accountService = new AccountServiceImplementation(accountRepository, currencyConversionService);
  }

  @Test
  void credit_IncreasesAccountBalanceForSpecifiedCurrency() {
    Currency currency = Currency.RED;
    Account account = createAccount();
    when(accountRepository.findById(TestConstants.ID)).thenReturn(Optional.of(account));
    accountService.credit(TestConstants.ID, TestConstants.AMOUNT, currency);
    switch (currency) {
      case RED:
        assertEquals(TestConstants.AMOUNT, account.getRedDollar(), TestConstants.DELTA);
        break;
      case GREEN:
        assertEquals(TestConstants.AMOUNT, account.getGreenDollar(), TestConstants.DELTA);
        break;
      case BLUE:
        assertEquals(TestConstants.AMOUNT, account.getBlueDollar(), TestConstants.DELTA);
      break;
    }
      verify(accountRepository, times(1)).findById(TestConstants.ID);
    verify(accountRepository, times(1)).save(account);
  }

  @Test
  void debit_DecreasesAccountBalanceForSpecifiedCurrency() {
    double amount = 50.0;
    Currency currency = Currency.GREEN;
    Account account = createAccount();
    account.setGreenDollar(100.0);
    when(accountRepository.findById(TestConstants.ID)).thenReturn(Optional.of(account));
    accountService.debit(TestConstants.ID, amount, currency);
    switch (currency) {
      case RED:
        assertEquals(0.0, account.getRedDollar(), TestConstants.DELTA);
        assertEquals(100.0, account.getGreenDollar(), TestConstants.DELTA);
        assertEquals(0.0, account.getBlueDollar(), TestConstants.DELTA);
        break;
      case GREEN:
         assertEquals(50.0, account.getGreenDollar(), TestConstants.DELTA);
         break;
      case BLUE:
          assertEquals(0.0, account.getBlueDollar(), TestConstants.DELTA);
          assertEquals(100.0, account.getGreenDollar(), TestConstants.DELTA);
          break;
    }
    verify(accountRepository, times(1)).findById(TestConstants.ID);
    verify(accountRepository, times(1)).save(account);
  }

  @Test
  void debit_ThrowsInsufficientBalanceException_WhenAccountBalanceIsLessThanDebitAmount() {
    Currency currency = Currency.BLUE;
    Account account = createAccount();
    account.setBlueDollar(100.0);
    when(accountRepository.findById(TestConstants.ID)).thenReturn(Optional.of(account));
    assertThrows(InsufficientBalanceException.class, () -> accountService.debit(TestConstants.ID, TestConstants.AMOUNT, currency));
    verify(accountRepository, times(1)).findById(TestConstants.ID);
    verify(accountRepository, never()).save(account);
  }

  @Test
  void getBalance_ReturnsCorrectBalanceForSpecifiedCurrency() {
    Currency currency = Currency.GREEN;
    Account account = createAccount();
    account.setRedDollar(100.0);
    account.setGreenDollar(200.0);
    account.setBlueDollar(300.0);
    double redToCurrencyRate = 2.0;
    double greenToCurrencyRate = 1.5;
    double blueToCurrencyRate = 0.8;
    when(accountRepository.findById(TestConstants.ID)).thenReturn(Optional.of(account));
    when(currencyConversionService.convert(1.0, Currency.RED, currency)).thenReturn(redToCurrencyRate);
    when(currencyConversionService.convert(1.0, Currency.GREEN, currency)).thenReturn(greenToCurrencyRate);
    when(currencyConversionService.convert(1.0, Currency.BLUE, currency)).thenReturn(blueToCurrencyRate);
    BigDecimal expectedBalance = BigDecimal.valueOf(account.getRedDollar())
                .multiply(BigDecimal.valueOf(redToCurrencyRate))
                .add(BigDecimal.valueOf(account.getGreenDollar()).multiply(BigDecimal.valueOf(greenToCurrencyRate)))
                .add(BigDecimal.valueOf(account.getBlueDollar()).multiply(BigDecimal.valueOf(blueToCurrencyRate)));
    BigDecimal balance = accountService.getBalance(TestConstants.ID, currency);
    assertEquals(expectedBalance.setScale(2, BigDecimal.ROUND_HALF_UP), balance);
    verify(accountRepository, times(1)).findById(TestConstants.ID);
    verify(currencyConversionService, times(1)).convert(1.0, Currency.RED, currency);
    verify(currencyConversionService, times(1)).convert(1.0, Currency.GREEN, currency);
    verify(currencyConversionService, times(1)).convert(1.0, Currency.BLUE, currency);
  }

  private Account createAccount() {
    Account account = new Account();
    account.setId(TestConstants.ID);
    account.setRedDollar(0.0);
    account.setGreenDollar(0.0);
    account.setBlueDollar(0.0);
    return account;
  }
}