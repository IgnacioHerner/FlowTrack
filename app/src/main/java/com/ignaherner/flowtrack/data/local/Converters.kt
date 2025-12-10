package com.ignaherner.flowtrack.data.local

import androidx.room.TypeConverter
import com.ignaherner.flowtrack.domain.model.TransactionCategory
import com.ignaherner.flowtrack.domain.model.TransactionType

class Converters {

    // TransactionType <-> String
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String {
        return type.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }

    @TypeConverter
    fun fromTransactionCategory(category: TransactionCategory): String {
        return category.name
    }

     @TypeConverter
     fun toTransactionCategory(value: String): TransactionCategory {
         return TransactionCategory.valueOf(value)
     }
}