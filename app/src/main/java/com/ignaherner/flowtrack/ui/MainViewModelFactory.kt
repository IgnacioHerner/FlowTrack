package com.ignaherner.flowtrack.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ignaherner.flowtrack.data.repository.inmemory.InMemoryTransactionRepository

class MainViewModelFactory(
    private val repository: InMemoryTransactionRepository = InMemoryTransactionRepository()
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create (modelClass: Class<T>) : T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}