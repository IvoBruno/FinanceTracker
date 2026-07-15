package project.financetracker.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.financetracker.records.StatisticsRecord;
import project.financetracker.records.TransactionRecord;
import java.math.BigDecimal;
import java.util.DoubleSummaryStatistics;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

   Queue<TransactionRecord> transactions = new ConcurrentLinkedQueue<>();
   @PostMapping
   public ResponseEntity<String> createTransaction(@Valid @RequestBody TransactionRecord dto) {
      transactions.add(dto);
      return ResponseEntity.ok("Transaction created");
   }

   @DeleteMapping
   public ResponseEntity<String> deleteTransaction() {
      transactions.clear();
      return ResponseEntity.ok("Transaction list has been deleted");
   }


   @GetMapping
   public ResponseEntity<StatisticsRecord> getTransactions() {
      if (transactions.isEmpty()) {
         return ResponseEntity.notFound().build();
      }
      DoubleSummaryStatistics statistics =
              transactions.
              stream().
              mapToDouble(transactionRecord -> transactionRecord.amount().doubleValue()).
              summaryStatistics();
      return ResponseEntity.ok(new StatisticsRecord(
              statistics.getCount(),
              BigDecimal.valueOf(statistics.getSum()),
              BigDecimal.valueOf(statistics.getAverage()),
              BigDecimal.valueOf(statistics.getMin()),
              BigDecimal.valueOf(statistics.getMax())
      ));
   }
}
