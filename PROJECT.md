# Personal Travel Assistant

## Project Overview
Personal Travel Assistant is an Android application designed to automate flight tracking and price monitoring. The application helps users find the best flight deals by continuously monitoring flight prices and sending notifications when suitable options are found.

## System Architecture

### Android Client (Kotlin)
- **Presentation Layer** (Jetpack Compose)
  - MVVM Architecture
  - Material Design UI components
  - Real-time price updates
  - Background notification system

- **Domain Layer**
  - Use cases for flight search and tracking
  - Repository interfaces
  - Business logic models

- **Data Layer**
  - Room Database (Local Storage)
  - Retrofit (API Communication)
  - WorkManager (Background Tasks)

### Backend Server (Spring Boot)
- RESTful API endpoints
- PostgreSQL database
- External API integrations (Skyscanner, Amadeus)
- JWT Authentication

## Key Features
1. **Flight Search**
   - Real-time flight data
   - Customizable search criteria
   - Price comparison

2. **Price Tracking**
   - Background monitoring
   - Price history tracking
   - Automated notifications

3. **User Preferences**
   - Saved searches
   - Notification settings
   - Search history

## Database Architecture

### Local Database (Room - SQLite) - In Progress
- **Purpose**: Offline access and caching
- **Tables**:
  - Saved Flight Searches
  - Cached Flight Results
  - App Preferences
  - Local Notifications
- **Status**: Implementation pending

### Remote Database (PostgreSQL) - Completed
- **Purpose**: Persistent storage and multi-device sync
- **Tables**:
  - User Accounts
  - Saved Searches
  - Search History
  - Notification Logs
- **Status**: ✅ Implemented

## Design Patterns
1. **Repository Pattern**
   - Data source abstraction
   - Clean separation of concerns

2. **Singleton Pattern**
   - Database instances
   - API clients

3. **Observer Pattern**
   - Price tracking updates
   - Notification system

4. **Service Layer Pattern**
   - Business logic separation
   - API handling

5. **DTO Pattern**
   - API communication
   - Data transformation

## Current Status (Week 6)
- System architecture documentation completed
- Database implementation pending
- Static UI development pending

## Next Steps
1. Room Database Implementation
   - Set up Room database
   - Implement DAOs and entities
   - Create database migrations
   - Implement sync mechanism with PostgreSQL

2. Static UI Development
   - Create UI components
   - Implement Material Design theme
   - Develop screen layouts

## Security Considerations
- JWT authentication
- Secure API communication
- Encrypted user data
- Environment variable management

## Dependencies
- Kotlin
- Jetpack Compose
- Room Database
- Retrofit
- WorkManager
- Spring Boot
- PostgreSQL
- JWT 