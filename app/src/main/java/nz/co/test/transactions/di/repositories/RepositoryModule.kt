package nz.co.test.transactions.di.repositories

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import nz.co.test.transactions.repositories.TransactionRepository
import nz.co.test.transactions.repositories.TransactionRepositoryImpl
import nz.co.test.transactions.services.TransactionsService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesTransactionRepository(transactionsService: TransactionsService): TransactionRepository =
        TransactionRepositoryImpl(transactionsService)
}