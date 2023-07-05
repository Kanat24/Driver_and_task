package com.example.drivesbillsmicroservice.service;

import com.example.drivesbillsmicroservice.enums.Currency;

/**
 * Service interface for converting amounts between different currencies.
 */
public interface CurrencyConversionService {

  /**
     * Converts the specified amount from one currency to another.
     *
     * @param amount the amount to convert
     * @param fromCurrency the source currency
     * @param toCurrency the target currency
     * @return the converted amount
     */
  double convert(double amount, Currency fromCurrency, Currency toCurrency);
}