package com.ignaherner.flowtrack.domain.repository

import com.ignaherner.flowtrack.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    //Flujo con la lista completa de movimientos.
    val transactionsFlow: Flow<List<Transaction>>

    // Agrega un nuevo movimiento
    suspend fun addTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)
}