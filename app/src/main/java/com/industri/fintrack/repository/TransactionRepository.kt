package com.industri.fintrack.repository

import com.industri.fintrack.database.TransactionDao
import com.industri.fintrack.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    val totalBalance: Flow<Double?> = transactionDao.getTotalBalance()

    suspend fun insert(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun delete(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }
}
