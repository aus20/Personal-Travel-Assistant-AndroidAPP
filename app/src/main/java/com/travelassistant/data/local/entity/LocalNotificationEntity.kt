package com.travelassistant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "local_notifications",
    foreignKeys = [
        ForeignKey(
            entity = SavedFlightSearchEntity::class,
            parentColumns = ["id"],
            childColumns = ["searchId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CachedFlightResultEntity::class,
            parentColumns = ["id"],
            childColumns = ["flightId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("userId"),
        Index("searchId"),
        Index("flightId"),
        Index("createdAt"),
        Index("readAt")
    ]
)
data class LocalNotificationEntity(
    @PrimaryKey
    val id: String,  // UUID from server
    val userId: String,
    val searchId: String,  // Foreign key to SavedFlightSearchEntity
    val flightId: String,  // Foreign key to CachedFlightResultEntity
    val title: String,
    val message: String,
    val createdAt: Long,
    val readAt: Long?
) 