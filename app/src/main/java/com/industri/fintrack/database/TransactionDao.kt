package com.industri.fintrack.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.industri.fintrack.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions_table ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>
    
    @Query("SELECT SUM(CASE WHEN transaction_type = 'INCOME' THEN amount ELSE -amount END) FROM transactions_table")
    fun getTotalBalance(): Flow<Double?>
}
