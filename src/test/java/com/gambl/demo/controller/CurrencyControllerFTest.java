package com.gambl.demo.controller;

import com.gambl.demo.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
@AutoConfigureMockMvc
class CurrencyControllerFTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    private static final String BASE_CURRENCY = "USD";

    @Test
    void testGetAllCurrencies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/currencies"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetExchangeRates() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/exchange-rates")
                        .param("baseCurrency", BASE_CURRENCY))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        verify(currencyService).getExchangeRates(BASE_CURRENCY);
    }

    @Test
    void testAddCurrency() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"baseCurrencyCode\":\"%s\"}".formatted(BASE_CURRENCY)))
                .andExpect(status().isCreated());

        verify(currencyService).addCurrency(BASE_CURRENCY);
    }
}
