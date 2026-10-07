package com.industri.fintrack.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.industri.fintrack.database.AppDatabase
import com.industri.fintrack.model.TransactionEntity
import com.industri.fintrack.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository
    val allTransactions: LiveData<List<TransactionEntity>>
    val currentBalance: LiveData<Double?>

    init {
        val dao = AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(dao)
        allTransactions = repository.allTransactions.asLiveData()
        currentBalance = repository.totalBalance.asLiveData()
    }

    fun addTransaction(title: String, amount: Double, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val newTransaction = TransactionEntity(title = title, amount = amount, transactionType = type)
            repository.insert(newTransaction)
        }
    }
    
    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(transaction)
        }
    }
}
