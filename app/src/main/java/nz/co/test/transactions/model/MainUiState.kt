package nz.co.test.transactions.model

import nz.co.test.transactions.services.Transaction

data class MainUiState(
    val data: List<Transaction> = listOf(),
    val loading: Boolean = false
)
