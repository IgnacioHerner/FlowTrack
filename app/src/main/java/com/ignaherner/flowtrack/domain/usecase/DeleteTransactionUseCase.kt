package com.ignaherner.flowtrack.domain.usecase

import com.ignaherner.flowtrack.domain.model.Transaction
import com.ignaherner.flowtrack.domain.repository.TransactionRepository


/**
 * Caso de uso para eliminar una transaccion
 */
class DeleteTransactionUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.deleteTransaction(transaction)
    }
}