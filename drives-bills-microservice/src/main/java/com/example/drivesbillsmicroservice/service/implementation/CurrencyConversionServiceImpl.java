package com.example.drivesbillsmicroservice.service.implementation;

import com.example.drivesbillsmicroservice.enums.Currency;
import com.example.drivesbillsmicroservice.service.CurrencyConversionService;
import org.springframework.stereotype.Service;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

  public double convert(double amount, Currency fromCurrency, Currency toCurrency) {
    if (fromCurrency == Currency.RED && toCurrency == Currency.BLUE) {
      return amount * 2.5 / 0.6;
    } else if (fromCurrency == Currency.GREEN && toCurrency == Currency.RED) {
      return amount / 2.5;
    } else if (fromCurrency == Currency.RED && toCurrency == Currency.GREEN) {
      return amount * 2.5;
    } else if (fromCurrency == Currency.GREEN && toCurrency == Currency.BLUE) {
      return amount / 0.6;
    } else if (fromCurrency == Currency.BLUE && toCurrency == Currency.RED) {
      return amount * 0.6 / 2.5;
    } else if (fromCurrency == Currency.BLUE && toCurrency == Currency.GREEN) {
      return amount * 0.6;
    }
    return amount;
  }
}