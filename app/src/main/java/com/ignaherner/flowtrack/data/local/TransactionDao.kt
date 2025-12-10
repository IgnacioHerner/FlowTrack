package com.ignaherner.flowtrack.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionEntity>>

    @Insert
    suspend fun insertTransaction(entity: TransactionEntity)

    // Mas adelante:
    // @Delete, @Update, filtros, etc
}