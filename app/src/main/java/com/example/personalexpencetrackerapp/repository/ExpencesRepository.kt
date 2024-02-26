package com.example.personalexpencetrackerapp.repository

import com.example.personalexpencetrackerapp.roomDatabase.ExpencesDao
import com.example.personalexpencetrackerapp.roomDatabase.ExpencesEntitiy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ExpencesRepository @Inject constructor(private val expencesDao: ExpencesDao) {

    suspend fun insertExpence(expense: ExpencesEntitiy) {
        expencesDao.saveExpences(expense)
    }

    fun getAllExpences(): Flow<List<ExpencesEntitiy>> {
        return expencesDao.getAllExpences()
    }

    suspend fun updateExpence(expense: ExpencesEntitiy) {
        expencesDao.updateExpense(expense)
    }

    suspend fun getExpencesByFilter(filterType: String, filterValue: String): List<ExpencesEntitiy> {
        return expencesDao.getExpencesByFilter(filterType, filterValue)
    }

}


