package com.ignaherner.flowtrack.domain.usecase

import com.ignaherner.flowtrack.domain.model.Transaction
import com.ignaherner.flowtrack.domain.repository.TransactionRepository

/**
 * Caso de uso para actualizar una transaccion existente
 */
class UpdateTransactionUseCase(
    private val repository: TransactionRepository
){
    suspend operator fun invoke(transaction: Transaction) {
        repository.updateTransaction(transaction)
    }
}