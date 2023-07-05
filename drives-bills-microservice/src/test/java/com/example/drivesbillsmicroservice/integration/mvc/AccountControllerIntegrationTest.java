package com.example.drivesbillsmicroservice.integration.mvc;

import com.example.drivesbillsmicroservice.controller.AccountController;
import com.example.drivesbillsmicroservice.controller.utils.ControllerUtils;
import com.example.drivesbillsmicroservice.dto.message.MessageDto;
import com.example.drivesbillsmicroservice.enums.Currency;
import com.example.drivesbillsmicroservice.service.AccountService;
import com.example.drivesbillsmicroservice.utils.TestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ControllerUtils controllerUtils;
  @MockBean
  private AccountService accountService;

  @Test
  public void testCredit() throws Exception {
    Currency currency = Currency.RED;
    given(controllerUtils.createResponseEntityOk(anyString())).willReturn(ResponseEntity.ok(new MessageDto(TestConstants.MONEY_CREDITED)));
    mockMvc.perform(post("/accounts/{accountId}/credit", TestConstants.ID)
                        .param("amount", String.valueOf(TestConstants.AMOUNT))
                        .param("currency", currency.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(TestConstants.MONEY_CREDITED));
  }

  @Test
  public void testDebit() throws Exception {
    Currency currency = Currency.RED;
    given(controllerUtils.createResponseEntityOk(anyString())).willReturn(ResponseEntity.ok(new MessageDto(TestConstants.MONEY_DEBITED)));
    mockMvc.perform(post("/accounts/{accountId}/debit", TestConstants.ID)
                        .param("amount", String.valueOf(TestConstants.AMOUNT))
                        .param("currency", currency.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(TestConstants.MONEY_DEBITED));
  }

  @Test
  public void testGetBalance() throws Exception {
    Currency currency = Currency.RED;
    BigDecimal balance = BigDecimal.valueOf(1000.0);
    given(accountService.getBalance(TestConstants.ID, currency)).willReturn(balance);
    mockMvc.perform(get("/accounts/{accountId}/balance", TestConstants.ID)
                        .param("currency", currency.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(balance.toString()));
    }
}