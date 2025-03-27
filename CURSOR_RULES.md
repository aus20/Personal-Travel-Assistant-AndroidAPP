# Cursor Development Rules

## Code Style Guidelines

### Kotlin Style Guide
1. **Naming Conventions**
   - Classes: PascalCase
   - Functions: camelCase
   - Variables: camelCase
   - Constants: SCREAMING_SNAKE_CASE
   - File names: PascalCase.kt

2. **Function Guidelines**
   - Single responsibility
   - Maximum 30 lines per function
   - Descriptive names
   - Suspend functions for coroutines

3. **Class Structure**
   ```kotlin
   class MyClass {
       // Constants
       // Properties
       // Init blocks
       // Lifecycle methods
       // Public methods
       // Private methods
   }
   ```

### XML Layout Guidelines
1. **ID Naming**
   - Format: `<layout>_<component>_<description>`
   - Example: `search_button_submit`

2. **Resource Naming**
   - Drawables: `ic_*` for icons, `bg_*` for backgrounds
   - Colors: descriptive names like `primary_blue`
   - Dimensions: purpose-based names like `text_size_title`

## Git Workflow

### Branch Naming
- Feature branches: `feature/description`
- Bug fixes: `fix/description`
- Releases: `release/version`

### Commit Messages
```
[Component] Brief description

Detailed explanation if needed
```

Example:
```
[Database] Add flight search entity

- Created FlightSearchEntity
- Added Room annotations
- Implemented DAO methods
```

### Pull Request Guidelines
1. Keep PRs focused and small
2. Include tests when applicable
3. Update documentation
4. Link related issues

## Project Structure Rules

### Package Organization
```
com.travelassistant/
├── data/
│   ├── local/
│   ├── remote/
│   └── repository/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
└── presentation/
    ├── screens/
    ├── components/
    └── theme/
```

### Feature Organization
- Each feature in its own package
- Shared components in common packages
- Resources organized by feature when possible

## Documentation Standards

### Code Documentation
1. **Class Documentation**
   ```kotlin
   /**
    * Purpose of the class
    *
    * @property propertyName description
    */
   ```

2. **Function Documentation**
   ```kotlin
   /**
    * What the function does
    *
    * @param paramName description
    * @return description
    * @throws ExceptionType when/why
    */
   ```

### Architecture Documentation
- Update PROJECT.md for architectural changes
- Document design decisions in DEVELOPMENT_NOTES.md
- Keep diagrams up to date

## Testing Requirements

### Unit Tests
- Required for:
  - Repository implementations
  - Use cases
  - ViewModels
  - Utility functions

### UI Tests
- Critical user flows
- Component behavior
- Screen navigation

### Test Naming Convention
```kotlin
class ClassNameTest {
    fun `when condition then expected result`() {
        // test implementation
    }
}
```

## Database Guidelines

### Room
1. **Entity Guidelines**
   - Clear table names
   - Proper indices
   - Documented relationships

2. **DAO Guidelines**
   - Single responsibility
   - Suspending functions
   - Clear method names

### PostgreSQL
1. **Table Guidelines**
   - Proper constraints
   - Indexed columns
   - Documented schemas

2. **Query Guidelines**
   - Optimized queries
   - Proper joins
   - Error handling

## Error Handling

### Exception Handling
1. Use sealed classes for error states
2. Meaningful error messages
3. Proper error propagation

### Logging
1. Use timber for logging
2. Different log levels
3. Meaningful log messages

## Performance Guidelines

### UI Performance
1. Avoid expensive operations on main thread
2. Optimize layouts
3. Use proper view recycling

### Background Tasks
1. Use WorkManager constraints
2. Battery-efficient operations
3. Proper scheduling

## Security Guidelines

1. **API Security**
   - HTTPS only
   - Token management
   - Request signing

2. **Data Security**
   - Encrypt sensitive data
   - Secure shared preferences
   - Clear user data on logout 