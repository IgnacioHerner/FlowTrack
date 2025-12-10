package com.ignaherner.flowtrack.data.repository.room

import com.ignaherner.flowtrack.data.local.TransactionDao
import com.ignaherner.flowtrack.data.local.TransactionEntity
import com.ignaherner.flowtrack.domain.model.Transaction
import com.ignaherner.flowtrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomTransactionRepository(
    private val transactionDao: TransactionDao
) : TransactionRepository{

    override val transactionsFlow : Flow<List<Transaction>> =
        transactionDao.getAllTransactionsFlow()
            .map { entities ->
                entities.map { it.toDomain() }
            }

    override suspend fun addTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(
            transaction.toEntity()
        )
    }
}

// Mappers entre capa data (Entity) y capa domain (Model)
private fun TransactionEntity.toDomain() : Transaction =
    Transaction(
        id = this.id,
        title = this.title,
        amount = this.amount,
        type = this.type,
        timestamp = this.timestamp
    )

private fun Transaction.toEntity() : TransactionEntity =
    TransactionEntity(
        id = 0L,
        title = this.title,
        amount = this.amount,
        type = this.type,
        timestamp = this.timestamp
    )