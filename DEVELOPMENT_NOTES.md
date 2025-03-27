# Development Notes

## Project Timeline & Progress

### Current Week: 6
**Status**: System Architecture Phase Complete

### Completed Work Packages
1. **WP1**: Project Planning & Proposal Preparation (Week 1-3)
   - ✅ Project idea finalized
   - ✅ Advisor approval obtained
   - ✅ Project proposal submitted

2. **WP2**: System & Database Design (Week 3-4)
   - ✅ System architecture documentation
   - ⏳ Database implementation (In Progress)

### Current Sprint
**Focus**: Room Database Implementation & Static UI Development

#### Active Tasks
1. Room Database Implementation
   - [ ] Room Database Setup
     - [ ] Entity creation
     - [ ] DAO interfaces
     - [ ] Database migrations
     - [ ] Sync mechanism with PostgreSQL
   - [x] PostgreSQL Setup (Completed)
     - [x] Table creation
     - [x] Entity mappings
     - [x] Repository implementations

2. Static UI Development
   - [ ] Material Design theme setup
   - [ ] Basic UI components
   - [ ] Screen layouts

### Upcoming Work Packages
3. **WP3**: User Interface Development (Week 4-5)
4. **WP4**: API Integration & Backend (Week 6-7)
5. **WP5**: UI-Backend Integration (Week 8-9)
6. **WP6**: Flight Search Implementation (Week 9-10)
7. **WP7**: Background Flight Monitoring (Week 11)
8. **WP8**: Notification Functionality (Week 11-12)
9. **WP9**: Performance Optimization (Week 12)
10. **WP10**: Testing & Debugging (Week 13)
11. **WP11**: Finalizing & Publishing (Week 14)

## Development Decisions

### Architecture Decisions
1. **Clean Architecture**
   - Separation of concerns
   - Independent layers
   - Easy to test and maintain

2. **MVVM Pattern**
   - Better state management
   - Improved testability
   - Lifecycle awareness

3. **Dual Database Strategy**
   - Room for offline capability (In Progress)
   - PostgreSQL for persistence (Completed)
   - Sync mechanism between both (To be implemented)

### Technical Decisions
1. **UI Framework**: Jetpack Compose
   - Modern UI toolkit
   - Kotlin-first approach
   - Better performance

2. **Background Processing**: WorkManager
   - Reliable background tasks
   - Battery-efficient
   - Handles system constraints

3. **API Communication**: Retrofit
   - Type-safe HTTP client
   - Easy to implement
   - Good performance

## Known Issues
- None reported yet

## Future Considerations
1. **Scalability**
   - Monitor database performance
   - Optimize background tasks
   - Cache management

2. **Features**
   - Price history analytics (optional)
   - Multiple flight combinations
   - Advanced filtering options

## Meeting Notes
_(To be updated with each team meeting)_

### Week 6 Status Meeting
- System architecture documentation completed
- Database implementation starting
- UI development planning phase 