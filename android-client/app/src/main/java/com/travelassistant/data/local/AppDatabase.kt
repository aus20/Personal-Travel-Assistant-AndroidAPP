package com.travelassistant.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.travelassistant.data.local.dao.AppPreferencesDao
import com.travelassistant.data.local.dao.CachedFlightResultDao
import com.travelassistant.data.local.dao.LocalNotificationDao
import com.travelassistant.data.local.dao.SavedFlightSearchDao
import com.travelassistant.data.local.entity.AppPreferencesEntity
import com.travelassistant.data.local.entity.CachedFlightResultEntity
import com.travelassistant.data.local.entity.LocalNotificationEntity
import com.travelassistant.data.local.entity.SavedFlightSearchEntity
import com.travelassistant.data.local.util.Converters

@Database(
    entities = [
        SavedFlightSearchEntity::class,
        CachedFlightResultEntity::class,
        AppPreferencesEntity::class,
        LocalNotificationEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedFlightSearchDao(): SavedFlightSearchDao
    abstract fun cachedFlightResultDao(): CachedFlightResultDao
    abstract fun appPreferencesDao(): AppPreferencesDao
    abstract fun localNotificationDao(): LocalNotificationDao
} 