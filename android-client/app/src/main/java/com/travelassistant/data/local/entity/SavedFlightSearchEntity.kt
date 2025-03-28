package com.travelassistant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

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
    val userId: String,
    val origin: String,
    val destination: String,
    val departureDate: Long,
    val returnDate: Long?,
    val maxPrice: Double,
    val preferredAirlines: List<String>,
    val maxStops: Int,
    val createdAt: Long,
    val updatedAt: Long,
    val lastSyncedAt: Long
) 