package com.travelassistant.di

import com.travelassistant.data.repository.FlightResultRepository
import com.travelassistant.data.repository.FlightSearchRepository
import com.travelassistant.data.repository.NotificationRepository
import com.travelassistant.data.repository.SavedSearchRepository
import com.travelassistant.data.repository.impl.FlightResultRepositoryImpl
import com.travelassistant.data.repository.impl.FlightSearchRepositoryImpl
import com.travelassistant.data.repository.impl.NotificationRepositoryImpl
import com.travelassistant.data.repository.impl.SavedSearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Repositories are usually scoped to the application lifecycle
abstract class RepositoryModule {


    // YENİ BIND METODU: SavedSearchRepository için
    @Binds
    @Singleton
    abstract fun bindSavedSearchRepository(
        savedSearchRepositoryImpl: SavedSearchRepositoryImpl
    ): SavedSearchRepository

    @Binds
    @Singleton // Ensures a single instance of NotificationRepositoryImpl is provided
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindFlightSearchRepository(
        flightSearchRepositoryImpl: FlightSearchRepositoryImpl
    ): FlightSearchRepository

    @Binds
    @Singleton
    abstract fun bindFlightResultRepository(
        flightResultRepositoryImpl: FlightResultRepositoryImpl
    ): FlightResultRepository

    // Add other repository bindings here if needed

    // Bind PreferencesRepository if not already done
    // @Binds
    // @Singleton
    // abstract fun bindPreferencesRepository(
    //     preferencesRepositoryImpl: PreferencesRepositoryImpl
    // ): PreferencesRepository
}