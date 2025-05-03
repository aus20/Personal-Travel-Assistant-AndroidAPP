# Database Implementation Guide

## Database Overview

### PostgreSQL (Backend) - Completed
The backend database handles persistent storage and multi-device synchronization.

**Tables**:
1. **Users**
   - Primary user information
   - Authentication details
   - Preferences

2. **Saved Searches**
   - User's saved flight searches
   - Search criteria
   - Creation/update timestamps

3. **Search History**
   - Past flight searches
   - Search results
   - Timestamps

4. **Notification Logs**
   - Sent notifications
   - Notification status
   - User interactions

### Room (Android) - To Be Implemented
The local database handles offline access and caching.

**Tables**:
1. **SavedFlightSearchEntity**
   ```kotlin
   @Entity(tableName = "saved_flight_searches")
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
   ```

2. **CachedFlightResultEntity**
   ```kotlin
   @Entity(tableName = "cached_flight_results")
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
       val expiresAt: Long
   )
   ```

3. **AppPreferencesEntity**
   ```kotlin
   @Entity(tableName = "app_preferences")
   data class AppPreferencesEntity(
       @PrimaryKey
       val userId: String,
       val notificationEnabled: Boolean,
       val priceAlertThreshold: Double,
       val preferredCurrency: String,
       val lastSyncTimestamp: Long
   )
   ```

4. **LocalNotificationEntity**
   ```kotlin
   @Entity(tableName = "local_notifications")
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
   ```

## Implementation Roadmap

### Phase 1: Database Setup
1. **Dependencies**
   - [x] Add Room dependencies to `build.gradle`
   - [x] Add Kotlin coroutines support
   - [x] Add Room testing dependencies

2. **Database Configuration**
   - [x] Create `AppDatabase` class
   - [x] Configure database versioning
   - [x] Set up migration strategies

### Phase 2: Entity Implementation
1. **Base Entities**
   - [x] Create `SavedFlightSearchEntity`
   - [x] Create `CachedFlightResultEntity`
   - [x] Create `AppPreferencesEntity`
   - [x] Create `LocalNotificationEntity`

2. **Relationships**
   - [x] Define entity relationships
   - [x] Create junction tables if needed
   - [x] Set up foreign key constraints

### Phase 3: Repository Implementation
- [x] Create repository interfaces
- [x] Implement FlightSearchRepository
- [x] Implement FlightResultRepository
- [x] Implement PreferencesRepository
- [x] Implement NotificationRepository

### Phase 4: DAO Implementation
1. **Data Access Objects**
   - [x] Create `SavedFlightSearchDao`
   - [x] Create `CachedFlightResultDao`
   - [x] Create `AppPreferencesDao`
   - [x] Create `LocalNotificationDao`

2. **Query Methods**
   - [ ] Implement CRUD operations
   - [ ] Add complex queries
   - [ ] Set up transaction methods

### Phase 5: Sync Mechanism
1. **Sync Service**
   - [x] Create `DatabaseSyncService`
   - [x] Implement sync strategies
   - [x] Handle conflicts

2. **Background Sync**
   - [x] Set up WorkManager for sync
   - [x] Configure sync intervals
   - [x] Handle sync failures

### Phase 6: Testing
1. **Unit Tests**
   - [ ] Test DAO operations
   - [ ] Test repository methods
   - [ ] Test sync mechanism

2. **Integration Tests**
   - [ ] Test database migrations
   - [ ] Test sync with PostgreSQL
   - [ ] Test offline functionality

## Project Structure Alignment

```
com.travelassistant.data.local/
├── dao/
│   ├── SavedFlightSearchDao.kt
│   ├── CachedFlightResultDao.kt
│   ├── AppPreferencesDao.kt
│   └── LocalNotificationDao.kt
├── entity/
│   ├── SavedFlightSearchEntity.kt
│   ├── CachedFlightResultEntity.kt
│   ├── AppPreferencesEntity.kt
│   └── LocalNotificationEntity.kt
└── AppDatabase.kt
```

## Next Steps
1. Review and approve the entity designs
2. Begin Phase 1 implementation
3. Set up testing environment
4. Create initial database version

## Notes
- All timestamps are stored as Long (milliseconds since epoch)
- UUIDs are used for primary keys to match PostgreSQL
- Foreign keys maintain referential integrity
- Sync timestamps track last synchronization
- Cache expiration is handled for flight results 