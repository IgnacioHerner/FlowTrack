package com.ignaherner.flowtrack.data.repository.inmemory

import com.ignaherner.flowtrack.domain.model.Transaction
import com.ignaherner.flowtrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Implementacion simple en memoria
// Esta clase NO sabe nada de la UI
class InMemoryTransactionRepository : TransactionRepository {

    // Estado interno con la lista de movimientos.
    // Por ahora lo mantenemos en memoria (se pierde al cerrar la app)
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())

    // Exponemos el Flow como solo lectura
    override val transactionsFlow: Flow<List<Transaction>> = _transactions.asStateFlow()

    override suspend fun addTransaction(transaction: Transaction) {
        // Agregamos el nuevo movimiento a la lista actual
        val currentList = _transactions.value
        _transactions.value = currentList + transaction
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        TODO("Not yet implemented")
    }
}