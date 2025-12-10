package com.ignaherner.flowtrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ignaherner.flowtrack.domain.model.TransactionCategory
import com.ignaherner.flowtrack.domain.model.TransactionType

// Entidad Room que representa la tabla en la DB
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val amount: Double,
    val type: TransactionType, // Lo manejamos con TypeConverter
    val category: TransactionCategory,
    val note: String?,
    val timestamp: Long
)
