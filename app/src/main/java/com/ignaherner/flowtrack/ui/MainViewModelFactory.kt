package com.ignaherner.flowtrack.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ignaherner.flowtrack.data.local.FlowTrackDatabase
import com.ignaherner.flowtrack.data.repository.inmemory.InMemoryTransactionRepository
import com.ignaherner.flowtrack.data.repository.room.RoomTransactionRepository
import com.ignaherner.flowtrack.domain.repository.TransactionRepository
import com.ignaherner.flowtrack.domain.usecase.AddTransactionUseCase
import com.ignaherner.flowtrack.domain.usecase.DeleteTransactionUseCase
import com.ignaherner.flowtrack.domain.usecase.GetTransactionsFlowUseCase
import com.ignaherner.flowtrack.domain.usecase.UpdateTransactionUseCase

class MainViewModelFactory(
    private val appContext: Context
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create (modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {

            // 1) BD + DAO + Repository
            val database = FlowTrackDatabase.getInstance(appContext)
            val transactionDao = database.transactionDao()
            val repository: TransactionRepository = RoomTransactionRepository(transactionDao)

            // 2) Use cases
            val getTransactionsFlowUseCase = GetTransactionsFlowUseCase(repository)
            val addTransactionUseCase = AddTransactionUseCase(repository)
            val updateTransactionUseCase = UpdateTransactionUseCase(repository)
            val deteleTransactionUseCase = DeleteTransactionUseCase(repository)

            // 3) ViewModel
            return MainViewModel(
                getTransactionsFlowUseCase = getTransactionsFlowUseCase,
                addTransactionUseCase = addTransactionUseCase,
                updateTransactionUseCase = updateTransactionUseCase,
                deleteTransactionUseCase = deteleTransactionUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}