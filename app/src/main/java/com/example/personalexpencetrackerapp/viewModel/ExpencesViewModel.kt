package com.example.personalexpencetrackerapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.personalexpencetrackerapp.repository.ExpencesRepository
import com.example.personalexpencetrackerapp.roomDatabase.ExpencesEntitiy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class ExpencesViewModel @Inject constructor(private val repository: ExpencesRepository) :
    ViewModel() {

    fun getAllExpences(): Flow<List<ExpencesEntitiy>> {
        return repository.getAllExpences()
    }

    suspend fun insertExpence(expense: ExpencesEntitiy) {
        repository.insertExpence(expense)
    }

   suspend fun updateExpence(expense: ExpencesEntitiy) {
        repository.updateExpence(expense)
    }

    fun getExpencesByFilter(filterType: String, filterValue: String): LiveData<List<ExpencesEntitiy>> {
        return liveData {
            emit(repository.getExpencesByFilter(filterType, filterValue))
        }
    }
}
