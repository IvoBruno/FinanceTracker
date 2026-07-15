package project.financetracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import project.financetracker.records.TransactionRecord;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() throws Exception {
        // Reset controller state before each test
        this.mockMvc.perform(delete("/api/transactions"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCreateTransactionWhenDataIsValid() throws Exception {
        TransactionRecord record = new TransactionRecord(
                new BigDecimal("100.50"),
                LocalDateTime.of(2026, 7, 15, 11, 0, 0)
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction created"));
    }

    @Test
    public void shouldReturnBadRequestWhenAmountIsNegative() throws Exception {
        TransactionRecord record = new TransactionRecord(
                new BigDecimal("-10.00"),
                LocalDateTime.of(2026, 7, 15, 11, 0, 0)
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenAmountIsZero() throws Exception {
        TransactionRecord record = new TransactionRecord(
                BigDecimal.ZERO,
                LocalDateTime.of(2026, 7, 15, 11, 0, 0)
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenAmountIsNull() throws Exception {
        TransactionRecord record = new TransactionRecord(
                null,
                LocalDateTime.of(2026, 7, 15, 11, 0, 0)
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenDateTimeIsNull() throws Exception {
        TransactionRecord record = new TransactionRecord(
                new BigDecimal("50.00"),
                null
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnNotFoundWhenNoTransactionsExist() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnStatisticsWhenTransactionsExist() throws Exception {
        // Add first transaction
        TransactionRecord record1 = new TransactionRecord(
                new BigDecimal("10.00"),
                LocalDateTime.of(2026, 7, 15, 10, 0, 0)
        );
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record1)))
                .andExpect(status().isOk());

        // Add second transaction
        TransactionRecord record2 = new TransactionRecord(
                new BigDecimal("20.00"),
                LocalDateTime.of(2026, 7, 15, 11, 0, 0)
        );
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record2)))
                .andExpect(status().isOk());

        // Retrieve statistics
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantidade", is(2)))
                .andExpect(jsonPath("$.soma", is(30.0)))
                .andExpect(jsonPath("$.media", is(15.0)))
                .andExpect(jsonPath("$.minimo", is(10.0)))
                .andExpect(jsonPath("$.maximo", is(20.0)));
    }

    @Test
    public void shouldClearTransactionsWhenDeleted() throws Exception {
        // Add a transaction
        TransactionRecord record = new TransactionRecord(
                new BigDecimal("50.00"),
                LocalDateTime.of(2026, 7, 15, 11, 0, 0)
        );
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk());

        // Delete transactions
        mockMvc.perform(delete("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction list has been deleted"));

        // Get transactions should now return NotFound
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isNotFound());
    }
}
