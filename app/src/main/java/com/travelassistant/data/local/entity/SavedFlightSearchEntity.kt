package com.travelassistant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import kotlinx.serialization.Serializable

@Entity(
    tableName = "saved_flight_searches",
    indices = [
        Index("userId"),
        Index("lastSyncedAt")
    ]
)

data class SavedFlightSearchEntity(
    @PrimaryKey
    val id: String,  // UUID from server
    val userId: Long,
    val origin: String,
    val destination: String,
    val departureDate: String,
    val returnDate: String?,
    val maxPrice: Double,
    val passengers: Int,
    val maxStops: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val lastSyncedAt: Long
)



@Serializable
data class SearchDetails(
    val userId: String,
    val origin: String,
    val destination: String,
    val departureDate: String,
    val returnDate: String?,
    val maxPrice: Double,
    val passengers: Int,
)