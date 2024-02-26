package com.example.personalexpencetrackerapp.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.personalexpencetrackerapp.roomDatabase.AppDatabase
import com.example.personalexpencetrackerapp.roomDatabase.ExpencesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "expencesDatabase")
            .build()
    }

    @Provides
    fun provideMovieDao(database: AppDatabase): ExpencesDao {
        return database.expencesDai()
    }
}

