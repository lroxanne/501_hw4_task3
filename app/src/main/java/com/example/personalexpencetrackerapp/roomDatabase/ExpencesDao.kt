package com.example.personalexpencetrackerapp.roomDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpencesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveExpences(expence: ExpencesEntitiy)

    @Query("SELECT * FROM expencesTable ORDER BY date DESC")
    fun getAllExpences(): Flow<List<ExpencesEntitiy>>

    @Update
    suspend fun updateExpense(expence: ExpencesEntitiy)

    @Query("SELECT * FROM expencesTable WHERE (:filterType = 'category' AND category = :filterValue) OR (:filterType = 'date' AND date = :filterValue)")
    suspend fun getExpencesByFilter(filterType: String, filterValue: String): List<ExpencesEntitiy>


}