package nz.co.test.transactions.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.withContext
import nz.co.test.transactions.extensions.getOrAwaitValue
import nz.co.test.transactions.rules.MainCoroutineRule
import nz.co.test.transactions.repositories.MockTransactionService
import nz.co.test.transactions.repositories.TransactionRepository
import nz.co.test.transactions.repositories.TransactionRepositoryImpl
import nz.co.test.transactions.services.Transaction
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import org.hamcrest.CoreMatchers.`is` as matches

@ExperimentalCoroutinesApi
class MainViewModelTest {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    private lateinit var repository: TransactionRepository

    private lateinit var viewModel: MainViewModel

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        repository = TransactionRepositoryImpl(MockTransactionService())

        viewModel = MainViewModel(repository)
    }

    @Test
    fun `fetchTransactions fetch transaction list`() = runBlockingTest{
        viewModel.fetchTransactions()

        withContext(Dispatchers.Main) {
            val transactions = viewModel.getTransactions().getOrAwaitValue()

            assertThat(transactions, matches(listOf(
                Transaction(1, LocalDateTime.parse("2021-08-31T15:47:10", formatter).atOffset(ZoneOffset.UTC), "Hackett, Stamm and Kuhn", BigDecimal(9379.55), BigDecimal(0.0)),
                Transaction(2, LocalDateTime.parse("2022-02-17T10:44:35", formatter).atOffset(ZoneOffset.UTC), "Hettinger, Wilkinson and Kshlerin", BigDecimal(3461.35), BigDecimal(0.0)),
                Transaction(3, LocalDateTime.parse("2021-02-21T08:19:12", formatter).atOffset(ZoneOffset.UTC), "McKenzie, Bins and Macejkovic", BigDecimal(0.0), BigDecimal(1415.74))
            )))
        }
    }
}