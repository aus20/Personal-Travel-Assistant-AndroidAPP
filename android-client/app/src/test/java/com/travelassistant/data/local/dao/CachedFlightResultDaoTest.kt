package com.travelassistant.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.travelassistant.data.local.AppDatabase
import com.travelassistant.data.local.entity.CachedFlightResultEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import org.junit.Assert.*


@RunWith(AndroidJUnit4::class)
class CachedFlightResultDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: CachedFlightResultDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.cachedFlightResultDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveFlightResult() = runBlocking {
        // Given
        val result = createTestFlightResult()

        // When
        dao.insertFlightResult(result)
        val retrieved = dao.getFlightResultById(result.id)

        // Then
        assertNotNull(retrieved)
        assertEquals(result.id, retrieved!!.id)
        assertEquals(result.searchId, retrieved.searchId)
        assertEquals(result.airline, retrieved.airline)
        assertEquals(result.flightNumber, retrieved.flightNumber)
        assertEquals(result.price, retrieved.price)
    }

    @Test
    fun updateFlightResult() = runBlocking {
        // Given
        val result = createTestFlightResult()
        dao.insertFlightResult(result)
        
        // When
        val updatedResult = result.copy(price = 1500.0)
        dao.updateFlightResult(updatedResult)
        val retrieved = dao.getFlightResultById(result.id)

        // Then
        assertNotNull(retrieved)
        assertEquals(1500.0, retrieved!!.price)
    }

    @Test
    fun deleteFlightResult() = runBlocking {
        // Given
        val result = createTestFlightResult()
        dao.insertFlightResult(result)

        // When
        dao.deleteFlightResult(result)

        // Then
        val retrieved = dao.getFlightResultById(result.id)
        assertNull(retrieved)
    }

    @Test
    fun getFlightResultsBySearchId() = runBlocking {
        // Given
        val searchId = "testSearch"
        val result1 = createTestFlightResult(searchId = searchId)
        val result2 = createTestFlightResult(searchId = searchId)
        val otherResult = createTestFlightResult(searchId = "otherSearch")
        
        dao.insertFlightResult(result1)
        dao.insertFlightResult(result2)
        dao.insertFlightResult(otherResult)

        // When
        val results = dao.getFlightResultsBySearchId(searchId).first()

        // Then
        assertEquals(2, results.size)
        assertTrue(results.any { it.id == result1.id })
        assertTrue(results.any { it.id == result2.id })
    }

    @Test
    fun getExpiredResults() = runBlocking {
        // Given
        val currentTime = System.currentTimeMillis()
        val expiredResult = createTestFlightResult(expiresAt = currentTime - 1000)
        val validResult = createTestFlightResult(expiresAt = currentTime + 1000)
        
        dao.insertFlightResult(expiredResult)
        dao.insertFlightResult(validResult)

        // When
        val expiredResults = dao.getExpiredResults(currentTime)

        // Then
        assertEquals(1, expiredResults.size)
        assertEquals(expiredResult.id, expiredResults[0].id)
    }

    @Test
    fun getMatchingFlights() = runBlocking {
        // Given
        val searchId = "testSearch"
        val matchingResult1 = createTestFlightResult(searchId = searchId, price = 1000.0, stops = 1)
        val matchingResult2 = createTestFlightResult(searchId = searchId, price = 1500.0, stops = 0)
        val nonMatchingResult = createTestFlightResult(searchId = searchId, price = 2000.0, stops = 2)
        
        dao.insertFlightResult(matchingResult1)
        dao.insertFlightResult(matchingResult2)
        dao.insertFlightResult(nonMatchingResult)

        // When
        val matchingResults = dao.getMatchingFlights(searchId, 1800.0, 1).first()

        // Then
        assertEquals(2, matchingResults.size)
        assertTrue(matchingResults.any { it.id == matchingResult1.id })
        assertTrue(matchingResults.any { it.id == matchingResult2.id })
    }

    @Test
    fun getFlightsInTimeRange() = runBlocking {
        // Given
        val searchId = "testSearch"
        val startTime = System.currentTimeMillis()
        val endTime = startTime + 86400000 // 24 hours later
        
        val inRangeResult = createTestFlightResult(
            searchId = searchId,
            departureTime = startTime + 43200000 // 12 hours later
        )
        val outOfRangeResult = createTestFlightResult(
            searchId = searchId,
            departureTime = endTime + 1000
        )
        
        dao.insertFlightResult(inRangeResult)
        dao.insertFlightResult(outOfRangeResult)

        // When
        val inRangeResults = dao.getFlightsInTimeRange(searchId, startTime, endTime).first()

        // Then
        assertEquals(1, inRangeResults.size)
        assertEquals(inRangeResult.id, inRangeResults[0].id)
    }

    @Test
    fun getPriceDrops() = runBlocking {
        // Given
        val searchId = "testSearch"
        val priceDropResult = createTestFlightResult(
            searchId = searchId,
            price = 1000.0,
            previousPrice = 1500.0
        )
        val noPriceDropResult = createTestFlightResult(
            searchId = searchId,
            price = 1500.0,
            previousPrice = 1000.0
        )
        
        dao.insertFlightResult(priceDropResult)
        dao.insertFlightResult(noPriceDropResult)

        // When
        val priceDropResults = dao.getPriceDrops(searchId, 1200.0, 1400.0).first()

        // Then
        assertEquals(1, priceDropResults.size)
        assertEquals(priceDropResult.id, priceDropResults[0].id)
    }

    @Test
    fun deleteExpiredResults() = runBlocking {
        // Given
        val currentTime = System.currentTimeMillis()
        val expiredResult = createTestFlightResult(expiresAt = currentTime - 1000)
        val validResult = createTestFlightResult(expiresAt = currentTime + 1000)
        
        dao.insertFlightResult(expiredResult)
        dao.insertFlightResult(validResult)

        // When
        dao.deleteExpiredResults(currentTime)

        // Then
        val remainingResults = dao.getFlightResultsBySearchId(expiredResult.searchId).first()
        assertEquals(1, remainingResults.size)
        assertEquals(validResult.id, remainingResults[0].id)
    }

    @Test
    fun deleteResultsBySearchId() = runBlocking {
        // Given
        val searchId = "testSearch"
        val result1 = createTestFlightResult(searchId = searchId)
        val result2 = createTestFlightResult(searchId = searchId)
        val otherResult = createTestFlightResult(searchId = "otherSearch")
        
        dao.insertFlightResult(result1)
        dao.insertFlightResult(result2)
        dao.insertFlightResult(otherResult)

        // When
        dao.deleteResultsBySearchId(searchId)

        // Then
        val remainingResults = dao.getFlightResultsBySearchId(searchId).first()
        assertTrue(remainingResults.isEmpty())
        
        val otherResults = dao.getFlightResultsBySearchId("otherSearch").first()
        assertEquals(1, otherResults.size)
    }

    private fun createTestFlightResult(
        id: String = UUID.randomUUID().toString(),
        searchId: String = "testSearch",
        airline: String = "Test Airline",
        flightNumber: String = "TA123",
        departureTime: Long = System.currentTimeMillis(),
        arrivalTime: Long = System.currentTimeMillis() + 3600000,
        price: Double = 1000.0,
        stops: Int = 1,
        cachedAt: Long = System.currentTimeMillis(),
        expiresAt: Long = System.currentTimeMillis() + 86400000,
        previousPrice: Double? = null
    ) = CachedFlightResultEntity(
        id = id,
        searchId = searchId,
        airline = airline,
        flightNumber = flightNumber,
        departureTime = departureTime,
        arrivalTime = arrivalTime,
        price = price,
        stops = stops,
        cachedAt = cachedAt,
        expiresAt = expiresAt,
        previousPrice = previousPrice
    )
} 