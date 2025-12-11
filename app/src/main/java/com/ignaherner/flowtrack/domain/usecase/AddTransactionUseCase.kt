package com.ignaherner.flowtrack.domain.usecase

import com.ignaherner.flowtrack.domain.model.Transaction
import com.ignaherner.flowtrack.domain.repository.TransactionRepository

/**
 * Caso de uso para gregar una nueva transaccion
 * Encapsula la logica de agregado (y validaciones futuras)
 */

class AddTransactionUseCase(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke (transaction: Transaction) {
        // Aca mas adelant4e metemos validaciones de dominio
        repository.addTransaction(transaction)
    }
}