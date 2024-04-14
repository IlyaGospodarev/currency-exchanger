package com.gambl.demo.service;

import com.gambl.demo.domain.entity.CurrencyEntity;
import com.gambl.demo.domain.model.CurrencyData;
import com.gambl.demo.domain.response.FreeCurrencyResponse;
import com.gambl.demo.repository.CurrencyRepository;
import com.gambl.demo.scheduler.ExchangeRateJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CurrencyServiceIt {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ExchangeRateJob exchangeRateJob;

    @Autowired
    private CurrencyService currencyService;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres").withTag("latest"))
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setUp() {
        currencyRepository.deleteAll();
        currencyRepository.save(new CurrencyEntity("USD", "USD", BigDecimal.ONE));
        currencyService.setCurrenciesMap(new HashMap<>() {{
            put("USD", List.of(new CurrencyData("USD", BigDecimal.ONE)));
        }});
    }

    @Test
    void testFetchAndStoreExchangeRates() throws JobExecutionException {
        FreeCurrencyResponse mockResponse = new FreeCurrencyResponse();
        Map<String, BigDecimal> mockData = new HashMap<>();
        mockData.put("EUR", BigDecimal.valueOf(0.85));
        mockData.put("GBP", BigDecimal.valueOf(0.75));
        mockResponse.setData(mockData);

        when(restTemplate.getForObject(any(String.class), eq(FreeCurrencyResponse.class)))
                .thenReturn(mockResponse);

        exchangeRateJob.execute(null);
        Set<String> allCurrencies = currencyService.getAllCurrencies();

        assertEquals(1, allCurrencies.size());
        assertTrue(allCurrencies.contains("USD"));

        List<CurrencyData> exchangeRates = currencyService.getExchangeRates("USD");
        assertEquals(2, exchangeRates.size());
        List<String> list = exchangeRates.stream().map(CurrencyData::currencyCode).toList();
        assertTrue(list.containsAll(List.of("EUR", "GBP")));
    }
}
