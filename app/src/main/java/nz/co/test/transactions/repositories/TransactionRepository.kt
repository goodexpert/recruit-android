package nz.co.test.transactions.repositories

import kotlinx.coroutines.flow.Flow
import nz.co.test.transactions.services.Transaction

interface TransactionRepository {
    suspend fun retrieveTransactions(): Flow<List<Transaction>>
}
