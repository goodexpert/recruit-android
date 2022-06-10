package nz.co.test.transactions.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nz.co.test.transactions.services.Transaction
import nz.co.test.transactions.services.TransactionsService
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val service: TransactionsService
) : TransactionRepository {

    override suspend fun retrieveTransactions(): Flow<List<Transaction>> = flow {
        emit(service.retrieveTransactions().toList())
    }
}