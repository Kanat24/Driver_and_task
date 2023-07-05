package com.example.drivesbillsmicroservice.service.implementation;

import com.example.drivesbillsmicroservice.entity.Account;
import com.example.drivesbillsmicroservice.enums.Currency;
import com.example.drivesbillsmicroservice.exceptions.ErrorCode;
import com.example.drivesbillsmicroservice.exceptions.account.AccountNotFoundException;
import com.example.drivesbillsmicroservice.exceptions.account.InsufficientBalanceException;
import com.example.drivesbillsmicroservice.repository.AccountRepository;
import com.example.drivesbillsmicroservice.service.AccountService;
import com.example.drivesbillsmicroservice.service.CurrencyConversionService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImplementation implements AccountService {
  private final AccountRepository accountRepository;
  private final CurrencyConversionService currencyConversionService;

  @Autowired
  public AccountServiceImplementation(AccountRepository accountRepository,
                                      CurrencyConversionService currencyConversionService) {
    this.accountRepository = accountRepository;
    this.currencyConversionService = currencyConversionService;
  }

  public void credit(Long accountId, double amount, Currency currency) {
    Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

    switch (currency) {
      case RED:
        account.setRedDollar(account.getRedDollar() + amount);
        break;
      case GREEN:
        account.setGreenDollar(account.getGreenDollar() + amount);
        break;
      case BLUE:
        account.setBlueDollar(account.getBlueDollar() + amount);
        break;
    }

    accountRepository.save(account);
  }

  public void debit(Long accountId, double amount, Currency currency) {
    Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

    switch (currency) {
      case RED:
        if (account.getRedDollar() - amount < 0) {
          throw new InsufficientBalanceException(ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);
        }
        account.setRedDollar(account.getRedDollar() - amount);
        break;
      case GREEN:
        if (account.getGreenDollar() - amount < 0) {
          throw new InsufficientBalanceException(ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);
        }
        account.setGreenDollar(account.getGreenDollar() - amount);
        break;
      case BLUE:
        if (account.getBlueDollar() - amount < 0) {
          throw new InsufficientBalanceException(ErrorCode.ACCOUNT_INSUFFICIENT_BALANCE);
        }
        account.setBlueDollar(account.getBlueDollar() - amount);
        break;
    }
    accountRepository.save(account);
  }

  public BigDecimal getBalance(Long accountId, Currency currency) {
    Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
    double redBalance = account.getRedDollar();
    double greenBalance = account.getGreenDollar();
    double blueBalance = account.getBlueDollar();
    double redToCurrency = currencyConversionService.convert(1.0, Currency.RED, currency);
    double greenToCurrency = currencyConversionService.convert(1.0, Currency.GREEN, currency);
    double blueToCurrency = currencyConversionService.convert(1.0, Currency.BLUE, currency);
    BigDecimal balance = BigDecimal.valueOf(redBalance)
                .multiply(BigDecimal.valueOf(redToCurrency))
                .add(BigDecimal.valueOf(greenBalance).multiply(BigDecimal.valueOf(greenToCurrency)))
                .add(BigDecimal.valueOf(blueBalance).multiply(BigDecimal.valueOf(blueToCurrency)));

    return balance.setScale(2, RoundingMode.HALF_UP); // Округляем до двух знаков после запятой
  }
}