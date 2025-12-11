package com.ignaherner.flowtrack.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ignaherner.flowtrack.domain.model.Transaction
import com.ignaherner.flowtrack.domain.model.TransactionCategory
import com.ignaherner.flowtrack.domain.model.TransactionType
import com.ignaherner.flowtrack.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar


//Estado de resumen que usaremos para la parte de arriba de la pantalla
data class SummaryUiState(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance: Double = 0.0
)

class MainViewModel(
    private val repository: TransactionRepository
) : ViewModel(){

    // Todas las transacciones que vienen del repo
    private val allTransaction: StateFlow<List<Transaction>> =
        repository.transactionsFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val _selectedPeriod = MutableStateFlow(PeriodFilter.ALL)
    val selectedPeriod: StateFlow<PeriodFilter> = _selectedPeriod.asStateFlow()

    // Flujo de movimientos que viene del repositorio
    // La UI va a observar esto
    val transaction: StateFlow<List<Transaction>> =
        combine(allTransaction, selectedPeriod) { list , period ->
            filterByPeriod(list, period)
        }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
        )


    // Resumen calculado en base a la lista de movimientos
    val summary: StateFlow<SummaryUiState> =
        transaction
            .map { list ->
                val income = list
                    .filter { it.type == TransactionType.INCOME }
                    .sumOf { it.amount }
                val expense = list
                    .filter { it.type == TransactionType.EXPENSE }
                    .sumOf { it.amount }

                SummaryUiState(
                    totalIncome = income,
                    totalExpense = expense,
                    balance = income - expense
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = SummaryUiState()
            )

    // Funcion publica que usara la UI para agregar un movimiento
    fun addTransaction(
        title: String,
        amount: Double,
        type: TransactionType,
        category: TransactionCategory,
        note: String?
    ) {
        // Creamos el modelo de dominio
        val transaction = Transaction (
            title = title,
            amount = amount,
            type = type,
            category = category,
            note = note
        )

        // Llamamos al repositorio dentro de una coroutine
        viewModelScope.launch {
            repository.addTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    // Cambiar periodo
    fun setPeriod(period: PeriodFilter) {
        _selectedPeriod.value = period
    }

    // ----------- Helpers privados ---------

    private fun filterByPeriod(
        list: List<Transaction>,
        period: PeriodFilter
    ): List<Transaction> {
        if (period == PeriodFilter.ALL) return list

        val now = System.currentTimeMillis()
        val cal = Calendar.getInstance()

        val startTimeMillis = when (period) {
            PeriodFilter.TODAY -> {
                // Hoy: desde las 00:00 de hoy
                cal.timeInMillis = now
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                cal.timeInMillis
            }

            PeriodFilter.WEEK -> {
                // Semana: Desde el primer dia de esta semana
                cal.timeInMillis = now
                cal.set(Calendar.HOUR_OF_DAY,0)
                cal.set(Calendar.MINUTE,0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND,0)

                cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                cal.timeInMillis
            }
            PeriodFilter.MONTH -> {
                // Mes: desde el día 1 del mes actual
                cal.timeInMillis = now
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.timeInMillis
            }
            PeriodFilter.ALL -> 0L // ya está manejado arriba, pero por si acaso
        }

        return list.filter { it.timestamp >= startTimeMillis }
    }
}