# Framework Samples Compose

A comprehensive Android sample project demonstrating various Android framework components using Jetpack Compose. This project serves as a learning resource and reference implementation for different Android framework features.

## Features

The project includes examples for the following Android framework components:

- **Coroutines**: Demonstrates Kotlin coroutines usage in Android
- **Broadcast Receivers**: Examples of system and custom broadcast receivers
- **Notifications**: Implementation of Android notifications with channels and actions
- **Persistence**: Data storage examples using Room, SharedPreferences, and DataStore
- **Services**: Examples of standard and bound services
- **WorkManager**: Background work scheduling and execution
- **Location**: Location services and updates
- **Web Services**: Network calls and API integration
- **Fragments**: Fragment usage with Compose
- **Themes in Compose**: Some samples on how to implement custom themes, change them at runtime, etc.

## Project Structure

```
app/src/main/java/com/antonioleiva/frameworksamples/
├── ui/
│   ├── screens/           # Feature-specific screens
│   ├── components/        # Reusable UI components
│   └── navigation/        # Navigation setup
├── model/                # Data models and entities
└── persistence/          # Data persistence implementations
```

## Technology Stack

- **UI Framework**: Jetpack Compose
- **Language**: Kotlin
- **Architecture**: MVVM
- **Dependency Management**: Gradle Version Catalog
- **Build System**: Gradle with Kotlin DSL

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17 or newer
- Android SDK 34 or newer

### Setup

1. Clone the repository
2. Open the project in Android Studio
3. Sync the project with Gradle files
4. Run the app on an emulator or physical device

## Project Guidelines

### Navigation

- Uses `composable<T>` for route definitions
- Direct navigation using `navController.navigate(it.destination)`
- Each feature has its own navigation graph

### UI Components

- All screens use the `Screen` component wrapper
- Sample lists use the `SamplesScreen` component
- Consistent spacing and padding across the app

### State Management

- Uses `remember` and `mutableStateOf` for local state
- Business logic is kept separate from composables
- Initial data loading in `remember` when necessary

### Dependencies

- Dependencies are managed through `libs.versions.toml`
- Use `implementation(libs.dependency.name)` to add new dependencies