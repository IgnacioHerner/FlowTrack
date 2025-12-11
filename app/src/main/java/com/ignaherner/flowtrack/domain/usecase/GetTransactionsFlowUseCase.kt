package com.ignaherner.flowtrack.domain.usecase

import com.ignaherner.flowtrack.domain.model.Transaction
import com.ignaherner.flowtrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obtener el flujo de transacciones
 * En el futuro prodia aceptar filtros u opciones
 */
class GetTransactionsFlowUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> {
        return repository.transactionsFlow
    }
}