# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Android debug build
./gradlew :composeApp:assembleDebug

# Android release build
./gradlew :composeApp:assembleRelease

# iOS — open iosApp/iosApp.xcodeproj in Xcode and run from there
```

Gradle configuration caching is enabled (`org.gradle.configuration-cache=true`).

## Architecture

This is a **Kotlin Multiplatform** (KMP) project targeting **Android** and **iOS**, using **Compose Multiplatform** for shared UI.

### Layer structure (Clean Architecture)

```
commonMain/
  data/         — Room DAOs, entities, database, repository impl
  domain/       — repository interfaces + use cases
  di/           — Koin DI modules (shared + expect/actual platform)
  presentation/ — Compose screens, ViewModels, theme, routes, utilities
```

**Key pattern:** Each screen (Home, Editor, Detail) has:
- A `ViewModel` extending `androidx.lifecycle.ViewModel` exposing `StateFlow<UiState<T>>`
- A `@Composable` screen function receiving `topAppBarState` callback, `navController`, and an `id` parameter (except Home)
- Routes defined as `@Serializable` data classes/objects under `presentation/routes/`

### Navigation

Type-safe navigation using `kotlinx-serialization` with `NavHost`/`composable<Route>`. Routes live in `presentation/routes/`:
- `Home` (object) — start destination
- `Editor(id: Int)` — id=0 means new note; id>0 means edit existing
- `Detail(id: Int)` — read-only note view with edit/delete actions

### Top app bar

Managed via `TopAppBarState` data class passed upward from screens to the shared `Scaffold` in `App.kt`. Screens emit their desired bar state via a callback pattern (`topAppBarState: (TopAppBarState) -> Unit`).

### DI (Koin)

- `di/KoinInit.kt` — `initKoin()` entry point, combines `platformModule()` + `appModule`
- `di/AppModule.kt` (common) — declares database, DAO, repository, use cases, and ViewModels
- Platform modules (`androidMain`, `iosMain`) provide the Room `Builder` since database instantiation differs per platform
- ViewModels are injected via `koinViewModel<VM>()` in composables

### Database (Room)

Single table `notepad_table` with fields: `id`, `title`, `notes`, `date`, `color`. The entity color is stored as a `Long` ARGB value. Uses `BundledSQLiteDriver` and `Dispatchers.IO` for queries. Platform-specific DB file paths:
- Android: `notes.db` in app data directory
- iOS: `my_room.db` in NSDocumentDirectory

### UiState

Sealed class with four variants: `Loading`, `Success<T>`, `Error(message)`, `Empty`. Used consistently across all ViewModels to represent screen state.

## Key dependencies

| Library | Purpose |
|---------|---------|
| Compose Multiplatform 1.9.0 | Shared UI |
| Room 2.8.3 + KSP | Local database |
| Koin 4.1.1 | Dependency injection |
| Navigation Compose 2.9.1 | Type-safe navigation |
| kotlinx-datetime 0.7.1 | Date formatting |
| kotlinx-serialization | Route serialization |