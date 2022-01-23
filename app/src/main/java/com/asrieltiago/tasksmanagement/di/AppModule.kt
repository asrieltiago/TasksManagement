package com.asrieltiago.tasksmanagement.di

import android.app.Application
import androidx.room.Room
import com.asrieltiago.tasksmanagement.data.TaskDatabase
import com.asrieltiago.tasksmanagement.data.local.TaskRepository
import com.asrieltiago.tasksmanagement.data.local.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            "task_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: TaskDatabase): TaskRepository {
        return TaskRepositoryImpl(db.dao)
    }
}