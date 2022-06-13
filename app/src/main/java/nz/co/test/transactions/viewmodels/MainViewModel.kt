package nz.co.test.transactions.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import nz.co.test.transactions.model.MainUiState
import nz.co.test.transactions.repositories.TransactionRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    var uiState by mutableStateOf(MainUiState())
        private set

    private fun setState(reduce: MainUiState.() -> MainUiState) {
        val newState = uiState.reduce()
        uiState = newState
    }

    suspend fun fetchTransactions() {
        repository.retrieveTransactions()
            .onEach { setState { copy(data = it) } }
            .onStart { setState { copy(loading = true) } }
            .onCompletion { setState { copy(loading = false) } }
            .launchIn(viewModelScope)
    }
}