package com.ignaherner.flowtrack.domain.model

// Tipo de movimiento: Ingreso o gasto
enum class TransactionType {
    INCOME, // Ingreso
    EXPENSE, // Gasto
}

data class Transaction (
    val id: Long = System.currentTimeMillis(), // ID simple por ahora
    val title: String,                         // Descripcion: "Sueldo", "Alquiler", Etc
    val amount: Double,                        // Monto positivio. el tipo indica si es ingreso o gasto
    val type: TransactionType,                 // INCOME O EXPENSE
    val category: TransactionCategory,
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis()  // Momento del movimiento en millis
)