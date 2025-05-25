package com.travelassistant.di

import android.content.Context
import androidx.room.Room
import com.travelassistant.data.local.AppDatabase
import com.travelassistant.data.local.dao.AppPreferencesDao
import com.travelassistant.data.local.dao.CachedFlightResultDao
import com.travelassistant.data.local.dao.LocalNotificationDao
import com.travelassistant.data.local.dao.SavedFlightSearchDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "travel_assistant_db" // Your database name
        ).build()
    }

    @Provides
    @Singleton // DAOs are usually singletons tied to the DB instance
    fun provideSavedFlightSearchDao(appDatabase: AppDatabase): SavedFlightSearchDao {
        return appDatabase.savedFlightSearchDao()
    }

    @Provides
    @Singleton
    fun provideCachedFlightResultDao(appDatabase: AppDatabase): CachedFlightResultDao {
        return appDatabase.cachedFlightResultDao()
    }

    @Provides
    @Singleton
    fun provideAppPreferencesDao(appDatabase: AppDatabase): AppPreferencesDao {
        // Assuming you'll also need AppPreferencesDao at some point
        return appDatabase.appPreferencesDao()
    }

    @Provides
    @Singleton
    fun provideLocalNotificationDao(appDatabase: AppDatabase): LocalNotificationDao {
        return appDatabase.localNotificationDao()
    }
}