package com.ignaherner.flowtrack.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ignaherner.flowtrack.data.local.FlowTrackDatabase
import com.ignaherner.flowtrack.data.repository.inmemory.InMemoryTransactionRepository
import com.ignaherner.flowtrack.data.repository.room.RoomTransactionRepository
import com.ignaherner.flowtrack.domain.repository.TransactionRepository

class MainViewModelFactory(
    private val appContext: Context
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create (modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {

            // 1) Obtenemos la instancia de la DB
            val database = FlowTrackDatabase.getInstance(appContext)

            // 2) Obtenemos el DAO
            val transactionDao = database.transactionDao()

            // 3) Creamos el repositorio usando Room
            val repository: TransactionRepository = RoomTransactionRepository(transactionDao)

            // 4) Creamos el ViewModel con ese repositorio
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}