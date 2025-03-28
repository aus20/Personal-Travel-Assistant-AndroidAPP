package com.travelassistant.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.travelassistant.data.local.AppDatabase
import com.travelassistant.data.local.entity.SavedFlightSearchEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class SavedFlightSearchDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: SavedFlightSearchDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.savedFlightSearchDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveSearch() = runBlocking {
        // Given
        val search = createTestSearch()

        // When
        dao.insertSearch(search)
        val retrieved = dao.getSearchById(search.id)

        // Then
        assertNotNull(retrieved)
        assertEquals(search.id, retrieved.id)
        assertEquals(search.origin, retrieved.origin)
        assertEquals(search.destination, retrieved.destination)
        assertEquals(search.maxPrice, retrieved.maxPrice)
        assertEquals(search.preferredAirlines, retrieved.preferredAirlines)
    }

    @Test
    fun updateSearch() = runBlocking {
        // Given
        val search = createTestSearch()
        dao.insertSearch(search)
        
        // When
        val updatedSearch = search.copy(maxPrice = 2000.0)
        dao.updateSearch(updatedSearch)
        val retrieved = dao.getSearchById(search.id)

        // Then
        assertNotNull(retrieved)
        assertEquals(2000.0, retrieved.maxPrice)
    }

    @Test
    fun deleteSearch() = runBlocking {
        // Given
        val search = createTestSearch()
        dao.insertSearch(search)

        // When
        dao.deleteSearch(search)

        // Then
        val retrieved = dao.getSearchById(search.id)
        assertNull(retrieved)
    }

    @Test
    fun getSearchesByUserId() = runBlocking {
        // Given
        val userId = "testUser"
        val search1 = createTestSearch(userId = userId)
        val search2 = createTestSearch(userId = userId)
        val otherSearch = createTestSearch(userId = "otherUser")
        
        dao.insertSearch(search1)
        dao.insertSearch(search2)
        dao.insertSearch(otherSearch)

        // When
        val searches = dao.getSearchesByUserId(userId).first()

        // Then
        assertEquals(2, searches.size)
        assertTrue(searches.all { it.userId == userId })
    }

    @Test
    fun getActiveSearches() = runBlocking {
        // Given
        val currentTime = System.currentTimeMillis()
        val activeSearch = createTestSearch(departureDate = currentTime + 86400000) // Tomorrow
        val expiredSearch = createTestSearch(departureDate = currentTime - 86400000) // Yesterday
        
        dao.insertSearch(activeSearch)
        dao.insertSearch(expiredSearch)

        // When
        val searches = dao.getActiveSearches(activeSearch.userId, currentTime).first()

        // Then
        assertEquals(1, searches.size)
        assertEquals(activeSearch.id, searches[0].id)
    }

    @Test
    fun getUpcomingSearches() = runBlocking {
        // Given
        val currentTime = System.currentTimeMillis()
        val upcomingSearch = createTestSearch(
            departureDate = currentTime + 86400000,
            returnDate = currentTime + 172800000 // 2 days later
        )
        val expiredSearch = createTestSearch(
            departureDate = currentTime - 86400000,
            returnDate = currentTime
        )
        
        dao.insertSearch(upcomingSearch)
        dao.insertSearch(expiredSearch)

        // When
        val searches = dao.getUpcomingSearches(upcomingSearch.userId, currentTime).first()

        // Then
        assertEquals(1, searches.size)
        assertEquals(upcomingSearch.id, searches[0].id)
    }

    @Test
    fun deleteExpiredSearches() = runBlocking {
        // Given
        val currentTime = System.currentTimeMillis()
        val expiredSearch = createTestSearch(departureDate = currentTime - 86400000)
        val activeSearch = createTestSearch(departureDate = currentTime + 86400000)
        
        dao.insertSearch(expiredSearch)
        dao.insertSearch(activeSearch)

        // When
        dao.deleteExpiredSearches(currentTime)

        // Then
        val retrievedExpired = dao.getSearchById(expiredSearch.id)
        val retrievedActive = dao.getSearchById(activeSearch.id)
        
        assertNull(retrievedExpired)
        assertNotNull(retrievedActive)
    }

    private fun createTestSearch(
        id: String = UUID.randomUUID().toString(),
        userId: String = "testUser",
        origin: String = "IST",
        destination: String = "LHR",
        departureDate: Long = System.currentTimeMillis(),
        returnDate: Long? = null,
        maxPrice: Double = 1000.0,
        preferredAirlines: List<String> = listOf("TK"),
        maxStops: Int = 1
    ): SavedFlightSearchEntity {
        return SavedFlightSearchEntity(
            id = id,
            userId = userId,
            origin = origin,
            destination = destination,
            departureDate = departureDate,
            returnDate = returnDate,
            maxPrice = maxPrice,
            preferredAirlines = preferredAirlines,
            maxStops = maxStops,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
            lastSyncedAt = System.currentTimeMillis()
        )
    }
} 