package nz.co.test.transactions.services

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import nz.co.test.transactions.converters.BigDecimalAdapter
import nz.co.test.transactions.converters.OffsetDateTimeAdapter
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import org.hamcrest.CoreMatchers.`is` as matches

class TransactionsServiceTest {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    private val jsonString = """
      [
        {"id":1,"transactionDate":"2021-08-31T15:47:10","summary":"Hackett, Stamm and Kuhn","debit":9379.55,"credit":0},
        {"id":2,"transactionDate":"2022-02-17T10:44:35","summary":"Hettinger, Wilkinson and Kshlerin","debit":3461.35,"credit":0},
        {"id":3,"transactionDate":"2021-02-21T08:19:12","summary":"McKenzie, Bins and Macejkovic","debit":0,"credit":1415.74}
      ]
    """.trimIndent()

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(BigDecimalAdapter)
            .add(OffsetDateTimeAdapter)
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Test
    fun `retrieveTransactions fetch transaction list from url`() = runBlocking {
        val server = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("").toString())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        server.enqueue(MockResponse().setBody(jsonString))

        val service = retrofit.create(TransactionsService::class.java)

        val transactions = service.retrieveTransactions()
        assertThat(transactions, matches(arrayOf(
            Transaction(1, LocalDateTime.parse("2021-08-31T15:47:10", formatter).atOffset(ZoneOffset.UTC), "Hackett, Stamm and Kuhn", BigDecimal(9379.55), BigDecimal(0.0)),
            Transaction(2, LocalDateTime.parse("2022-02-17T10:44:35", formatter).atOffset(ZoneOffset.UTC), "Hettinger, Wilkinson and Kshlerin", BigDecimal(3461.35), BigDecimal(0.0)),
            Transaction(3, LocalDateTime.parse("2021-02-21T08:19:12", formatter).atOffset(ZoneOffset.UTC), "McKenzie, Bins and Macejkovic", BigDecimal(0.0), BigDecimal(1415.74))
        )))
        server.shutdown()
    }
}