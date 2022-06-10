package nz.co.test.transactions.repositories

import nz.co.test.transactions.services.Transaction
import nz.co.test.transactions.services.TransactionsService
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class MockTransactionService : TransactionsService {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    override suspend fun retrieveTransactions() = arrayOf(
        Transaction(
            1,
            LocalDateTime.parse("2021-08-31T15:47:10", formatter).atOffset(ZoneOffset.UTC),
            "Hackett, Stamm and Kuhn",
            BigDecimal(9379.55),
            BigDecimal(0.0)
        ),
        Transaction(
            2,
            LocalDateTime.parse("2022-02-17T10:44:35", formatter).atOffset(ZoneOffset.UTC),
            "Hettinger, Wilkinson and Kshlerin",
            BigDecimal(3461.35),
            BigDecimal(0.0)
        ),
        Transaction(
            3,
            LocalDateTime.parse("2021-02-21T08:19:12", formatter).atOffset(ZoneOffset.UTC),
            "McKenzie, Bins and Macejkovic",
            BigDecimal(0.0),
            BigDecimal(1415.74)
        )
    )
}