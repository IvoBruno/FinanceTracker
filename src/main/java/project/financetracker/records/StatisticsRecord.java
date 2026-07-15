package project.financetracker.records;

import java.math.BigDecimal;

public record StatisticsRecord(
        Long quantidade,
        BigDecimal soma,
        BigDecimal media,
        BigDecimal minimo,
        BigDecimal maximo
) {
}
