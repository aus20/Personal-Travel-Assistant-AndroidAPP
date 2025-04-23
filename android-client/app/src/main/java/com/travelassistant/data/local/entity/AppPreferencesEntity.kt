package com.travelassistant.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "app_preferences",
    indices = [
        Index("lastSyncTimestamp")
    ]
)
data class AppPreferencesEntity(
    @PrimaryKey
    val userId: String,
    val notificationEnabled: Boolean,
    val priceAlertThreshold: Double,
    val preferredCurrency: String,
    val lastSyncTimestamp: Long,
    val updatedAt: Long? = null,
    val language: String
) 