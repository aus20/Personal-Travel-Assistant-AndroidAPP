package com.travelassistant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "cached_flight_results",
    foreignKeys = [
        ForeignKey(
            entity = SavedFlightSearchEntity::class,
            parentColumns = ["id"],
            childColumns = ["searchId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("searchId"),
        Index("expiresAt")
    ]
)
data class CachedFlightResultEntity(
    @PrimaryKey
    val id: String,  // UUID from server
    val searchId: String,  // Foreign key to SavedFlightSearchEntity
    val airline: String,
    val flightNumber: String,
    val departureTime: Long,
    val arrivalTime: Long,
    val price: Double,
    val stops: Int,
    val cachedAt: Long,
    val expiresAt: Long,
    val previousPrice: Double? = null
) 