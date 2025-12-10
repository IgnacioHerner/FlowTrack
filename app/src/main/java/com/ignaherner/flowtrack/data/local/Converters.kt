package com.ignaherner.flowtrack.data.local

import androidx.room.TypeConverter
import com.ignaherner.flowtrack.domain.model.TransactionType

class Converters {

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String {
        return type.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }
}