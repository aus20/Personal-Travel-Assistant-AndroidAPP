package com.travelassistant.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.travelassistant.data.local.AppDatabase
import com.travelassistant.data.local.entity.AppPreferencesEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import org.junit.Assert.*


@RunWith(AndroidJUnit4::class)
class PreferencesDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: AppPreferencesDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.appPreferencesDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrievePreferences() = runBlocking {
        // Given
        val preferences = createTestPreferences()

        // When
        dao.insertPreferences(preferences)
        val retrieved = dao.getPreferencesByUserId(preferences.userId)

        // Then
        assertNotNull(retrieved)
        assertEquals(preferences.userId, retrieved!!.userId)
        assertEquals(preferences.userId, retrieved!!.userId)
        assertEquals(preferences.preferredCurrency, retrieved.preferredCurrency)
        assertEquals(preferences.language, retrieved.language)
        assertEquals(preferences.notificationEnabled, retrieved.notificationEnabled)
    }

    @Test
    fun updatePreferences() = runBlocking {
        // Given
        val preferences = createTestPreferences()
        dao.insertPreferences(preferences)
        
        // When
        val updatedPreferences = preferences.copy(
            preferredCurrency = "EUR",
            language = "es",
            notificationEnabled = false
        )
        dao.updatePreferences(updatedPreferences)
        val retrieved = dao.getPreferencesByUserId(preferences.userId)

        // Then
        assertNotNull(retrieved)
        assertEquals("EUR", retrieved!!.preferredCurrency)
        assertEquals("es", retrieved.language)
        assertEquals(false, retrieved.notificationEnabled)
    }

    @Test
    fun deletePreferences() = runBlocking {
        // Given
        val preferences = createTestPreferences()
        dao.insertPreferences(preferences)

        // When
        dao.deletePreferences(preferences)

        // Then
        val retrieved = dao.getPreferencesByUserId(preferences.userId)
        assertNull(retrieved)
    }

    @Test
    fun getPreferencesByUserId() = runBlocking {
        // Given
        val userId = "testUser"
        val preferences1 = createTestPreferences(userId = userId)
        val preferences2 = createTestPreferences(userId = userId)
        val otherPreferences = createTestPreferences(userId = "otherUser")
        
        dao.insertPreferences(preferences1)
        dao.insertPreferences(preferences2)
        dao.insertPreferences(otherPreferences)

        // When
        val userPreferences = dao.getPreferencesByUserIdFlow(userId).first()

        // Then
        assertEquals(2, userPreferences.size)
        assertTrue(userPreferences.any { it!!.userId == preferences1.userId })
        assertTrue(userPreferences.any { it!!.userId == preferences2.userId })
    }

    @Test
    fun getPreferencesNeedingSync() = runBlocking {
        // Given
        val currentTime = System.currentTimeMillis()
        val needsSyncPreferences = createTestPreferences(
            lastSyncTimestamp = currentTime - 1000,
            updatedAt = currentTime
        )
        val syncedPreferences = createTestPreferences(
            lastSyncTimestamp = currentTime,
            updatedAt = currentTime
        )
        
        dao.insertPreferences(needsSyncPreferences)
        dao.insertPreferences(syncedPreferences)

        // When
        val preferencesNeedingSync = dao.getPreferencesNeedingSync(currentTime)

        // Then
        assertEquals(1, preferencesNeedingSync.size)
        assertEquals(needsSyncPreferences.userId, preferencesNeedingSync[0].userId)
    }

    private fun createTestPreferences(
        userId: String = "testUser",
        preferredCurrency: String = "USD",
        language: String = "en",
        notificationEnabled: Boolean = true,
        lastSyncTimestamp: Long = System.currentTimeMillis(),
        updatedAt: Long = System.currentTimeMillis(),
        priceAlertThreshold: Double = 20.0
    ) = AppPreferencesEntity(
        userId = userId,
        preferredCurrency = preferredCurrency,
        language = language,
        notificationEnabled = notificationEnabled,
        lastSyncTimestamp = lastSyncTimestamp,
        updatedAt = updatedAt,
        priceAlertThreshold = priceAlertThreshold
    )
} 