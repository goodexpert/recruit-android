package nz.co.test.transactions.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import nz.co.test.transactions.repositories.TransactionRepository
import nz.co.test.transactions.services.Transaction
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    private val transactions = MutableLiveData<List<Transaction>>()

    fun getTransactions(): LiveData<List<Transaction>> = transactions

    suspend fun fetchTransactions() {
        repository.retrieveTransactions()
            .onEach { transactions.value = it }
            .launchIn(viewModelScope)
    }
}