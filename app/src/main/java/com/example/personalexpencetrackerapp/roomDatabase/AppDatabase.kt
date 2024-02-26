package com.example.personalexpencetrackerapp.roomDatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExpencesEntitiy::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expencesDai(): ExpencesDao
}