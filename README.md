# Feed Flow

An Android application built with Jetpack Compose that demonstrates user authentication, data retrieval from a remote API like JSONPlaceholder, offline support using Room database, search functionality, favorites management, and dark mode toggle.

## Features
- **User Authentication**:
  - Registration screen with email, password, and confirm password fields.
  - Input validation: email format, password length (min 6 characters), and password match.
  - Login screen with email and password fields.
  - Users stored in Room database; authentication uses local data (no real backend).
  - Auto-login if a user is already logged in (stored in SharedPreferences).
  - Logout button on the posts screen.

- **Posts Display**:
  - Loads posts from JSONPlaceholder API.
  - Displays posts with title and body using lazy loading and Paging 3 for efficient scrolling.
  - Handles loading, error, and empty states.

- **Search Functionality**:
  - Search bar on the posts screen to filter posts by title or body.
  - Search works on cached data in Room database for offline support.

- **Favorites**:
  - Users can mark/unmark posts as favorites with a toggle button.
  - Favorites stored in Room database.
  - Separate favorites screen to view marked posts.

- **Offline Support**:
  - Posts cached in Room database on app start.
  - All features (viewing posts, search, favorites) work offline using cached data.

- **Dark Mode**:
  - Toggle button on posts screen to switch between light and dark themes.
  - Theme preference persisted using DataStore.
  - Supports system dark mode by default.

- **Architecture & Best Practices**:
  - MVVM pattern with ViewModels for business logic.
  - Coroutines and Flow for asynchronous operations.
  - Dependency Injection with Hilt.
  - Clean, modular code with separation of concerns.

## Setup & Build Instructions
1. Clone the repository:
   ```
   git clone https://github.com/toriqultonu/FeedFlow.git
   ```
2. Open the project in Android Studio (version Arctic Fox or later recommended).
3. Sync the Gradle files (Android Studio will prompt you, or use File > Sync Project with Gradle Files).
4. Build and run the app on an emulator or physical device (minimum SDK 24 - Android 7.0).

## App Architecture and Libraries Used
- **Architecture**: MVVM with Clean Architecture principles (data, domain, ui layers).
- **UI**: Jetpack Compose for declarative UI.
- **Navigation**: Compose Navigation for screen routing.
- **Data**: Room for local database (posts, users, favorites); Gson for JSON parsing; Paging 3 for lazy loading.
- **State Management**: Coroutines, Flow for reactive data streams.
- **Dependency Injection**: Hilt/Dagger for DI.
- **Persistence**: DataStore for theme preferences; SharedPreferences for logged-in user.
- **Other Libraries**:
  - androidx.compose.material3 (UI components and themes).
  - androidx.room (local database).
  - androidx.paging (pagination).
  - com.google.dagger:hilt-android (DI).
  - com.google.code.gson:gson (JSON parsing).
  - kotlinx.coroutines (async operations).

## Assumptions or Limitations
- **Authentication**: Local only (no real backend or encryption for passwords). Users are stored in Room; multiple users can register, but only one is "logged in" at a time via SharedPreferences.
- **Data Source**: Posts loaded from local `posts.json` file on app start (clears and repopulates database to simulate API refresh). No real API calls; assumes JSON structure matches PostEntity.
- **Offline Support**: Fully supported via Room cache. Search and favorites work on cached data only.
- **Dark Mode**: Persisted via DataStore; toggle overrides system setting.
- **No Images**: Posts do not include images (as per JSONPlaceholder structure).
- **Tests**: Unit/UI tests implemented for brevity.
- **Platform**: Tested on Android 7.0+; dynamic colors require Android 12+.

## Demo Uses

https://github.com/user-attachments/assets/6a2ea1ec-8f61-47fa-9fb2-f3cdf6affa77



For any issues or contributions, feel free to open a pull request or issue!
