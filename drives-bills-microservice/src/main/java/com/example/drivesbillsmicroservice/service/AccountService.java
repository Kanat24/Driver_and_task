package com.example.drivesbillsmicroservice.service;

import com.example.drivesbillsmicroservice.enums.Currency;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;


/**
 * Service interface for managing accounts and performing account-related operations.
 */
@Service
public interface AccountService {

  /**
     * Credits the specified amount in the given currency to the account with the provided ID.
     *
     * @param accountId the ID of the account to credit
     * @param amount the amount to credit
     * @param currency the currency of the amount
     */
  void credit(Long accountId, double amount, Currency currency);

  /**
     * Debits the specified amount in the given currency from the account with the provided ID.
     *
     * @param accountId the ID of the account to debit
     * @param amount the amount to debit
     * @param currency the currency of the amount
     */
  void debit(Long accountId, double amount, Currency currency);

  /**
     * Retrieves the balance of the account with the provided ID in the given currency.
     *
     * @param accountId the ID of the account
     * @param currency the currency of the balance
     * @return the balance of the account in the specified currency
     */
  BigDecimal getBalance(Long accountId, Currency currency);
}