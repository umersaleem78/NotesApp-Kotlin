package com.mus.mynotes.dal.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mus.mynotes.constants.AppConstants
import com.mus.mynotes.dal.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, AppDatabase::class.java, AppConstants.DATABASE_NAME)
            .allowMainThreadQueries().fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideNotesDao(db: AppDatabase) = db.notesDao()

    @Singleton
    @Provides
    fun provideAddTodoDao(db: AppDatabase) = db.todoDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context): SharedPreferences =
        app.getSharedPreferences(AppConstants.PREFERENCES, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()
}